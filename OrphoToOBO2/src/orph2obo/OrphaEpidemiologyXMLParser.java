package orph2obo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import orph2obo.Prevalence;

/**
 * Parse the file "http://www.orphadata.org/data/xml/en_product2.xml"
 * from the Orphadata website.
 * @author peter
 * @author Samuel Demarest maj Nov2015
 */
public class OrphaEpidemiologyXMLParser extends DefaultHandler {
	private String tempVal;
	private RareDisease currentDisease;
	private HashMap<String,RareDisease> diseases;
	private boolean within_ClassOfPrevalence = false;
	private boolean within_PrevalenceGeo = false;
	private boolean within_PrevalenceType = false;
	private boolean within_AgeOfOnset = false;
	private boolean within_AgeOfDeath = false;
	private boolean within_Inheritance = false;
	private boolean within_Qualification = false; //UPDATE SD flag de PrevalenceQualification pour cas/famille
	private Prevalence prevalence;
	
	/**
	 * This method parses the Epidemiological file,
	 * and passes the required information to the
	 * rare disease class where it is stored in memory and printed 
	 * out when required.
	 * @param XMLFilePath
	 * @param dis
	 */

	public void parseDocument(String XMLFilePath,HashMap<String,RareDisease> dis ) {
		//System.out.println("Parsing: " + XMLFilePath);
		this.diseases = dis;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			sp.parse(XMLFilePath, this);

		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	//reset
    	
		tempVal = "";
		if(qName.equalsIgnoreCase("Disorder")) {
			//System.err.println("epidemio : "+uri+"; "+localName+"; "+qName); 
		    currentDisease = null; // reset.
		} else if (qName.equalsIgnoreCase("Prevalence")){
		    prevalence=new Prevalence();
		} else if (qName.equalsIgnoreCase("PrevalenceClass")){
			within_ClassOfPrevalence = true;
		} else if (qName.equalsIgnoreCase("AverageAgeOfOnset")){
			within_AgeOfOnset = true;
		} else if (qName.equalsIgnoreCase("AverageAgeOfDeath")){
			within_AgeOfDeath = true;	
		} else if (qName.equalsIgnoreCase("TypeOfInheritance")){
			within_Inheritance = true;
		} else if (qName.equalsIgnoreCase("PrevalenceType")){
			within_PrevalenceType = true;
		} else if (qName.equalsIgnoreCase("PrevalenceGeographic")){
			within_PrevalenceGeo = true;
		} else if (qName.equalsIgnoreCase("PrevalenceQualification")){ //UPDATE SD flag de PrevalenceQualification pour cas/famille
			within_Qualification = true;
		} 
		    
	}
	    
    
    

		public void characters(char[] ch, int start, int length) throws SAXException {
			tempVal = new String(ch,start,length);
			
		}

		public void endElement(String uri, String localName,
			String qName) throws SAXException {
			
			if (qName.equalsIgnoreCase("OrphaNumber") && currentDisease==null && !(within_ClassOfPrevalence) && !(within_AgeOfOnset) && !(within_AgeOfDeath) && !(within_Inheritance)) {
				//again make sure that the orphanumber does not contain F cause that means that its not a disease Orphanumber
				/**if (!tempVal.contains("F")){*/
				//System.err.println("Epidemio : Passer ici valeur de tempVal: " + tempVal);
				setOrphanum(tempVal);
				}
				else if(qName.equalsIgnoreCase("PrevalenceClass")){
					within_ClassOfPrevalence = false;
				}
				else if(qName.equalsIgnoreCase("Prevalence")){
					currentDisease.addPreval(prevalence);
				}
				else if(qName.equalsIgnoreCase("PrevalenceType")){
					within_PrevalenceType = false;
				}
				else if(qName.equalsIgnoreCase("PrevalenceGeographic")){
					within_PrevalenceGeo = false;
				}else if(qName.equalsIgnoreCase("PrevalenceQualification")){
					within_Qualification = false;
				}
				else if (qName.equalsIgnoreCase("ValMoy")){
					prevalence.setValMoy(tempVal);	
				} 				
				else if (qName.equalsIgnoreCase("Orphanumber") && within_ClassOfPrevalence){
					prevalence.setPrevalClass(tempVal);					
				} 				
				else if (qName.equalsIgnoreCase("Orphanumber") && within_PrevalenceType){
					prevalence.setType(tempVal);					
				} 
				else if (qName.equalsIgnoreCase("Orphanumber") && within_PrevalenceGeo){
					prevalence.setGeo(tempVal);
				}else if (qName.equalsIgnoreCase("Orphanumber") && within_Qualification && prevalence.getType().equals("409970")){
					//UPDATE SD si type = "Cas/familles", garder le typage "Cas" ou "Familles" à la place 
					prevalence.setType(tempVal);
				}
				else if (qName.equalsIgnoreCase("AverageAgeOfOnset")){
					within_AgeOfOnset = false;
				}
				else if (within_AgeOfOnset && qName.equalsIgnoreCase("OrphaNumber")){
					currentDisease.setOnsetNum(tempVal);
				}
				else if (qName.equalsIgnoreCase("Name") && within_PrevalenceGeo){
					prevalence.setGeoLab(tempVal);
				}
				else if (qName.equalsIgnoreCase("Name") && within_PrevalenceType){
					prevalence.setTypeLab(tempVal);
				}
				else if (qName.equalsIgnoreCase("Name") && within_AgeOfOnset){
					currentDisease.setAgeOfOnset(tempVal);
				}
				else if (qName.equalsIgnoreCase("AverageAgeOfDeath")){
					within_AgeOfDeath = false;
				}
				else if (qName.equalsIgnoreCase("Name") && within_AgeOfDeath){
					currentDisease.setAgeOfDeath(tempVal);
				}
				else if (within_AgeOfDeath && qName.equalsIgnoreCase("OrphaNumber")) {
					currentDisease.setDeathNum(tempVal);
				}
				else if ( qName.equalsIgnoreCase("TypeOfInheritance")){
					within_Inheritance = false;
				}
				else if (qName.equalsIgnoreCase("Name") &&  within_Inheritance){
					currentDisease.setInheritance (tempVal);
				}
				else if (within_Inheritance && qName.equalsIgnoreCase("OrphaNumber")){
					//System.out.println("Inheritance number " + tempVal);
					currentDisease.setInheritNum(tempVal);
				}
		}

    private void setOrphanum(String orphID) {
    	//System.out.println("CurrentDiseaseBefore=" + orphID);
		currentDisease = this.diseases.get(orphID);
		//System.out.println("CurrentDisease=" + currentDisease);
		if (currentDisease == null) {
			//System.err.println("You are here");
		    Iterator<String> it = this.diseases.keySet().iterator();
		    while (it.hasNext()) {
			String s = it.next();
			RareDisease dxr = this.diseases.get(s);
			//System.out.println(s + ":" + dxr.getName());
		    }
		    //System.err.println("Parsing Epidemiology File: Could not fetch OrphaID: " + orphID);
		    System.exit(1);
		}
	
    }


}
