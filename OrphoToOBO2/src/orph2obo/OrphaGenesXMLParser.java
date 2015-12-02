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
 * @author Celine Rousselot maj Nov2015
 * @author Samuel Demarest maj Nov2015
 */
public class OrphaGenesXMLParser extends DefaultHandler {
	private String tempVal;
	private RareDisease currentDisease;
	private HashMap<String,RareDisease> diseases;
	private Gene tmpGene;
	private ExternalReference tmpExtRef;
	private boolean in_gene_node = false;
	private boolean in_gene_list = false;
	private boolean within_externalReferenceElement = false;
	private boolean within_geneAssociationType = false;
	private ArrayList<Gene> genes;
	private boolean within_geneAssociationStatus = false;
	private boolean in_locus_list = false;//UPDATE-CR261115-ajout des locus
	private boolean in_gene_type = false;//UPDATE-CR261115 pour distinguer l'orphanumber du gene et du type

	
	
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

			//detection balise d'ouverture
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
			}else if (qName.equalsIgnoreCase("GeneList")){
				in_gene_list=true;
			}else if (qName.equalsIgnoreCase("LocusList")){ //UPDATE-CR261115-ajout des locus
				in_locus_list=true;
			}
			else if (qName.equalsIgnoreCase("GeneType")){ //UPDATE-CR261115 pour distinguer l'orphanumber du gene et du type
				in_gene_type=true;
			}
		}


		public void characters(char[] ch, int start, int length) throws SAXException {
			tempVal = new String(ch,start,length);
		}

    public void endElement(String uri, String localName, String qName) 
	throws SAXException {

    	//=============================== GESTION DES ORPHANUMBER =============================================//
   if(qName.equalsIgnoreCase("Orphanumber")){
		 if (!(in_gene_node) && !(within_geneAssociationType)){ //infos sur Disorder - Orphanumber
			 //System.out.println("disease orphanum" + tempVal);
			 currentDisease = this.diseases.get(tempVal);
		 }	
		 else if (in_gene_node && !in_gene_type && !(within_geneAssociationType)){ //infos sur Gene - Orphanumber
			//System.out.println("Gene Orphanum" + tempVal);
				currentDisease.add_geneNum(tempVal); 
		 }
		 else if (in_gene_node && in_gene_type && !(within_geneAssociationType)){//infos sur GeneType - Orphanumber
			//System.out.println("GeneType Orphanum" + tempVal);
				//currentDisease.add_geneNum(tempVal); 
		 }
		// A FAIRE Orphanumber � rajouter dans fichier en_product6.xml
		 else if (!in_gene_node && within_geneAssociationType){//infos sur DisorderGeneAssociationType - Orphanumber
			//System.out.println("GeneAssociationType Orphanum" + tempVal);
				currentDisease.setGeneType(tempVal); //A QUOI CA SERT?
				//currentDisease.setGeneTypeOrphaNum(tempVal); //A FAIRE
		 }
   }
   //======================================================================================================//
   
 //=============================== GESTION DES INFOS SUR GENE =============================================//
   //infos sur gene - Symbol
   else if (qName.equalsIgnoreCase("Symbol")) {
		currentDisease.setSymbol(tempVal);
	} 
  

 //infos sur gene - Name
   else if (in_gene_node && qName.equalsIgnoreCase("Name") && !in_gene_type) { 
		//System.out.println("you are in the gene name node and the name is :" + tempVal + " and the current disease object is :" + currentDisease);
	    currentDisease.add_genes(tempVal);
   } 
   
   //infos sur geneType - type name
   else if (in_gene_node && qName.equalsIgnoreCase("Name") && in_gene_type) { 
		System.out.println("you are in the gene name node and the name is :" + tempVal);
		//currentDisease.setGenType(tempVal);CREATION setGenType
   } 
   
   //infos sur les locus
  	else if (qName.equalsIgnoreCase("GeneLocus")){
  		System.out.println("Current disease object is :" + currentDisease + " and locus is : " + tempVal);
  		//currentDisease.setGeneLocus(tempVal);//A FAIRE!
  	}
   
   //infos sur les synonymes
	else if (qName.equalsIgnoreCase("Synonym")){
		System.out.println("Current disease object is :" + currentDisease + " and synonym is : " + tempVal);
		currentDisease.setGeneSyn(tempVal);
	}
   
   //======================================================================================================//
   
 //=============================== GESTION DES INFOS SUR ASSOCIATION GENE/DISORDER ========================//
   //infos sur DisorderGeneAssociationType - status
   else if (within_geneAssociationStatus && qName.equalsIgnoreCase("Name")){
		currentDisease.setGeneTypeStatus(tempVal);
	}
   
   //infos sur DisorderGeneAssociationType - name
   else if(within_geneAssociationType && qName.equalsIgnoreCase("Name")){
	   //System.out.println("you are in the gene name node and gentype and the name is :" + tempVal);
		currentDisease.setgeneTypeName(tempVal);
	}
 //======================================================================================================//
  
 //=============================== GESTION DES INFOS SUR REFERENCES EXTERNES ============================//

   //infos sur les external reference
	else if (qName.equalsIgnoreCase("ExternalReference")){
	    within_externalReferenceElement = false;
	    tmpGene.addExternalReference(tmpExtRef);
	} else if (within_externalReferenceElement && 
		   qName.equalsIgnoreCase("Source")){
	  
		currentDisease.setGeneSource(tempVal);
	} else if (within_externalReferenceElement && 
		   qName.equalsIgnoreCase("Reference")){
	  
		currentDisease.setGeneRefs(tempVal);
	} 
 //======================================================================================================//
   
   //detection balise de fermeture
	else if(qName.equalsIgnoreCase("Gene")) {
	    in_gene_node = false;
	    this.genes.add(tmpGene);
	}else if(qName.equalsIgnoreCase("GeneList")) {
	    in_gene_list = false;
	} else if (qName.equalsIgnoreCase("DisorderGeneAssociationType")){
		within_geneAssociationType = false;
	}else if (qName.equalsIgnoreCase("DisorderGeneAssociationStatus")){
		within_geneAssociationStatus =false;
	}else if (qName.equalsIgnoreCase("GeneType")){
		in_gene_type =false;
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


