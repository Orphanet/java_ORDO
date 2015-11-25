package orph2obo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parse 	http://www.orphadata.org/data/xml/en_product6.xml 	
 * i.e., diseases and their associated genes.
 * @author peter
 *
 */
public class OrphaGenesXMLParser extends DefaultHandler {
	private String tempVal;
	private RareDisease currentDisease;
	private HashMap<String,RareDisease> diseases;
	private Gene tmpGene;
	private ExternalReference tmpExtRef;
	private boolean in_gene_node = false;
	private boolean within_externalReferenceElement = false;
	private boolean within_geneAssociationType = false;
	private ArrayList<Gene> genes;
	private boolean within_geneAssociationStatus = false;
	
	
	
	public OrphaGenesXMLParser () {
		
	}
	
	
	/**
	 * This method parses the genes and xrefs xml
	 * file and passes the gene Orphanum, name, symbol 
	 * and xrefs to the rare disease class.
	 * @param XMLFilePath
	 * @param dis
	 */
	
	public void parseDocument(String XMLFilePath,HashMap<String,RareDisease> dis ) {
		System.out.println("Parsing: " + XMLFilePath);
		this.diseases = dis;
		System.out.println("Set up disease = size = " + diseases.size());
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
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
			//reset
			tempVal = "";
			System.err.println("uri : "+uri+"; "+localName+"; "+qName); 
			if(qName.equalsIgnoreCase("Disorder")) {
				currentDisease = null; // reset
			} else if (qName.equalsIgnoreCase("DisorderGeneAssociationList")){
				genes = new ArrayList<Gene>(); // reset
			} else if (qName.equalsIgnoreCase("Gene")){
				in_gene_node=true;
				tmpGene = new Gene();
			} else if (qName.equalsIgnoreCase("ExternalReference")){
				this.tmpExtRef = new ExternalReference();
				within_externalReferenceElement = true;
			}else if (qName.equalsIgnoreCase("ExternalReferenceList")){
				currentDisease.setExRefCount(attributes.getValue("count"));
			}else if (qName.equalsIgnoreCase("DisorderGeneAssociationType")){
				within_geneAssociationType = true;
			}else if (qName.equalsIgnoreCase("SynonymList")){
				currentDisease.setGeneSynCount(attributes.getValue("count"));
			}else if (qName.equalsIgnoreCase("DisorderGeneAssociationStatus")){
				within_geneAssociationStatus =true;
			}
		}


		public void characters(char[] ch, int start, int length) throws SAXException {
			tempVal = new String(ch,start,length);
		}

    public void endElement(String uri, String localName, String qName) 
	throws SAXException {
   if (qName.equalsIgnoreCase("Orphanumber") && in_gene_node){
		System.out.println(tempVal);
		currentDisease.add_geneNum(tempVal);    		
   }else if(qName.equalsIgnoreCase("Orphanumber") && within_geneAssociationType){
		currentDisease.setGeneType(tempVal);
    }else if(qName.equalsIgnoreCase("Orphanumber")){
    		 if (!(in_gene_node) && !(within_geneAssociationType)){
    	currentDisease = this.diseases.get(tempVal);}	//setting the gene-disease orphanumber
    } else if (qName.equalsIgnoreCase("Symbol")) {
		currentDisease.setSymbol(tempVal);
	} else if (in_gene_node && qName.equalsIgnoreCase("Name")) {
		//System.out.println("you are in the gene name node and the name is :" + tempVal + " and the current disease object is :" + currentDisease);
	    currentDisease.add_genes(tempVal);
	   // System.out.println("the gene name has been added to the list");
	} else if(within_geneAssociationType && qName.equalsIgnoreCase("Name")){
		//System.out.println("Current disease object is :" + currentDisease + " and gene association type is : " + tempVal);
		currentDisease.setgeneTypeName(tempVal);
		
	}else if (within_geneAssociationStatus && qName.equalsIgnoreCase("Name")){
		currentDisease.setGeneTypeStatus(tempVal);
	}
	else if (qName.equalsIgnoreCase("ExternalReference")){
	    within_externalReferenceElement = false;
	    tmpGene.addExternalReference(tmpExtRef);
	} else if (within_externalReferenceElement && 
		   qName.equalsIgnoreCase("Source")){
	  
		currentDisease.setGeneSource(tempVal);
	} else if (within_externalReferenceElement && 
		   qName.equalsIgnoreCase("Reference")){
	  
		currentDisease.setGeneRefs(tempVal);
	} else if(qName.equalsIgnoreCase("Gene")) {
	    in_gene_node = false;
	    
	    this.genes.add(tmpGene);
	} else if (qName.equalsIgnoreCase("DisorderGeneAssociationType")){
		within_geneAssociationType = false;
	}else if (qName.equalsIgnoreCase("Synonym")){
		//System.out.println("Current disease object is :" + currentDisease + " and synonym is : " + tempVal);
		currentDisease.setGeneSyn(tempVal);
	}else if (qName.equalsIgnoreCase("DisorderGeneAssociationStatus")){
		within_geneAssociationStatus =false;
		
	}
   /**else if (qName.equalsIgnoreCase("Disorder")) {
	    RareDisease dxr = this.diseases.get(disease_orphanum);
	    if (dxr == null) {
		System.err.println("Could not retrieve disease object for \"" + currentDisease
				   + "\" while parsing Genes XML file");
		System.exit(1);
	    }
	  //  dxr.add_genes(this.genes);
	}*/	
    }
}


