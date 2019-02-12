package orph2obo;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is for parsing the Orphanet cross ref file, i.e., 
 * "http://www.orphadata.org/data/xml/en_product1.xml";
 * It is meant to return a HashMap with orphanumber and  Disease2XRef objects,
 * each of which represents one of the <Disease> stanzas of the
 * XML file.
 * @author peter
 *
 */
public class OrphaXRefXMLParser extends DefaultHandler {
	
    private String tempVal;
    /** A temporary stanza object used for parsing. It accumulates the values needed to construct a rare disease object. */
    private RareDisease tmpDisXref;
    private ExternalReference tmpExtRef;
    /** This variable is true if the SAX parser is currently in an external reference element. */
    private boolean within_externalReferenceElement = false;
   
    /** UPDATE SD validation/relation status **/
    /** This variable is true if the SAX parser is currently in an external reference relation element. */
    private boolean within_externalReferenceRelation = false;
    /** This variable is the temp orpha value of the relation status **/ 
    private String relationValue;
    private String relationOrpha;
    /** This variable is true if the SAX parser is currently in an ICD Relation element. */
    private boolean within_ICDRelation = false;

    private String ICDrelVal;
    private String ICDrelOrpha;
    
    /** This variable is true if the SAX parser is currently in an TextSectionType element. */
    private boolean within_DiseaseDefinition = false;
    /**this variable is true if the SAX parser is currently in the DisorderType element */
    private boolean within_diseaseType;
    /**this variable is true if the SAX parser is currently in the DisorderDisorderAssociation element */
    private boolean within_disorderDisorder;
    /** List of Rare disease objects. */
	//private ArrayList<RareDisease> classes;
    private HashMap<String,RareDisease> diseaseXRefs;
	
	
	public OrphaXRefXMLParser() {
		this.diseaseXRefs = new HashMap<String,RareDisease> ();
	}
	
	public HashMap<String,RareDisease> getXrefHashMap() {
		return this.diseaseXRefs;
	}
	

	public void parseDocument(String XMLFilePath) {
	    // System.out.println("Parsing: " + XMLFilePath);
	    
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
		//System.err.println("XREF attr : "+attributes+"; "+qName); 
		if(qName.equalsIgnoreCase("Disorder")) {
		    tmpDisXref = new RareDisease();
		    tmpDisXref.setID(attributes.getValue("id")); /*The id is the internal id used by the Orphanet - differs from orphaNum*/
		} else if (qName.equalsIgnoreCase("ExternalReference")){
		    this.tmpExtRef = new ExternalReference();
		    within_externalReferenceElement = true;
		}else if (qName.equalsIgnoreCase("TextSection")){
			    this.tmpExtRef = new ExternalReference();
			    within_DiseaseDefinition = true;
		}else if (qName.equalsIgnoreCase("DisorderType")){
			 within_diseaseType = true;
		}else if (qName.equalsIgnoreCase("DisorderDisorderAssociation")){
			 within_disorderDisorder = true;
		}else if (qName.equalsIgnoreCase("DisorderDisorderAssociationType")){
			 within_disorderDisorder = false;
		}else if(qName.equalsIgnoreCase("DisorderFlag")){
			if(attributes.getValue("id").equals("455")){
				tmpDisXref.setObsolete();
			}else if(attributes.getValue("id").equals("459")){
				tmpDisXref.setMovedTo();
			}
			
		}else if(qName.equalsIgnoreCase("DisorderMappingRelation")){
			within_externalReferenceRelation=true;
			relationValue="";
			relationOrpha="";
			
		}else if(qName.equalsIgnoreCase("DisorderMappingICDRelation")){
			within_ICDRelation = true;
			ICDrelVal="";
			ICDrelOrpha="";
		}
    }
    
    
    public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
		//System.out.println(ch);
    }
    
    /*
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     * This method extracts the orphanumber, id, the expert link,
     * Synonyms and the external references and passes them on to the
     * Rare disease class where they are all stored in memory and called upon when
     * printing out.
     */
    
    public void endElement(String uri, String localName,
			   String qName) throws SAXException {

    	if(qName.equalsIgnoreCase("Orphanumber") && within_disorderDisorder && (tmpDisXref.isMovedTo() ||tmpDisXref.isObsolete())){
			tmpDisXref.setObsoleteFrom(tempVal);
		}else if(qName.equalsIgnoreCase("Disorder")) {
		    String orphnum = tmpDisXref.get_orphanum();
		   // System.out.println("orphanum="+ orphnum + "Value="+ tmpDisXref);
		    this.diseaseXRefs.put(orphnum, tmpDisXref);
		}else if (qName.equalsIgnoreCase("Orphanumber") && !(within_diseaseType) && this.tmpDisXref.get_orphanum()==null) {
			//In the new set of sata files orphanumber is even given to the disesase type, inheritance etc which starts from F,
			//hence to make sure the Orphanumber is for a disease make sure it does not contain F
			//if (!tempVal.contains("F"))
		    tmpDisXref.setOrphanum(tempVal);
		} else if (qName.equalsIgnoreCase("Orphanumber") && within_diseaseType){
		    this.tmpDisXref.setTypeOrph(tempVal);
		}else if(qName.equalsIgnoreCase("Orphanumber") && within_externalReferenceRelation){
			relationOrpha=tempVal;
		}else if(qName.equalsIgnoreCase("Name") && within_externalReferenceRelation){
			relationValue=tempVal;
		}else if(qName.equalsIgnoreCase("Orphanumber") && within_ICDRelation){
			ICDrelOrpha=tempVal;
		}else if(qName.equalsIgnoreCase("Name") && within_ICDRelation){
			ICDrelVal=tempVal;
		}else if (qName.equalsIgnoreCase("TextSection")){
			within_DiseaseDefinition = false;
		} else if (within_DiseaseDefinition && 
				   qName.equalsIgnoreCase("Contents")){
			// add conversion escaped char to char
			tempVal=tempVal.replaceAll("\\|\\|lt\\|\\|", "<");
			tempVal=tempVal.replaceAll("\\|\\|gt\\|\\|", ">");
			this.tmpDisXref.setDef(tempVal);
			
		}else if (qName.equalsIgnoreCase("ExpertLink")) {
			//System.out.println(tempVal);
		    tmpDisXref.setExpertLink(tempVal);
		}else if (qName.equalsIgnoreCase("Name") ) {
			if(within_DiseaseDefinition == false && tmpDisXref.getName()==null){
				tmpDisXref.setName(tempVal);
			}else if(within_diseaseType){
				tmpDisXref.setTypeValidity(tempVal); //typevalidity n'existe plus, il faudra renommer la variable en diseaseType
			}
		}else if (qName.equalsIgnoreCase("Synonym")){
			
			// add conversion escaped char to char
			tempVal=tempVal.replaceAll("\\|\\|lt\\|\\|", "<");
			tempVal=tempVal.replaceAll("\\|\\|gt\\|\\|", ">");
			
			tmpDisXref.setSynonym(tempVal);
		}else if (qName.equalsIgnoreCase("ExternalReference")){
		    within_externalReferenceElement = false;
		    tmpDisXref.addExternalReference(tmpExtRef);
		} else if (within_externalReferenceElement && 
			   qName.equalsIgnoreCase("Source")){
		    this.tmpDisXref.addSource(tempVal);
		} else if (within_externalReferenceElement && 
			   qName.equalsIgnoreCase("Reference")){
		    this.tmpDisXref.addRef(tempVal);
		}else if(qName.equalsIgnoreCase("DisorderMappingRelation")){
			within_externalReferenceRelation=false;
			this.tmpDisXref.addValidRef(relationValue);
			//tmpDisXref.addValidOrphaRef(relationOrpha);
		}else if(qName.equalsIgnoreCase("DisorderMappingICDRelation")){
			within_ICDRelation=false;
			this.tmpDisXref.addICDRel(ICDrelVal);
			//tmpDisXref.addValidOrphaRef(relationOrpha);
		}else if (qName.equalsIgnoreCase("DisorderType")){
			within_diseaseType = false;
		}else if (qName.equalsIgnoreCase("DisorderDisorderAssociation")){
			 within_disorderDisorder = false;
		}/*else if (qName.equalsIgnoreCase("TypeValid")){
			this.tmpDisXref.setTypeValidity(tempVal);
		}*/
	
    }

	


}
