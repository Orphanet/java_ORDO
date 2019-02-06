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
 *
 */
public class OrphaGenesXMLParser extends DefaultHandler {
	private String tempVal;
	private RareDisease currentDisease;
	private HashMap<String,RareDisease> diseases;
	private Gene tmpGene;
	private ExternalReference tmpExtRef;
	private boolean in_gene_node = false;
	private boolean in_gene_list = false;
	private boolean in_disordergeneassociation_list = false;
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
			} else if (qName.equalsIgnoreCase("GeneList")){//
				genes = new ArrayList<Gene>(); // reset
				in_gene_list=true;
			} else if (qName.equalsIgnoreCase("Gene")&& !in_disordergeneassociation_list){
				in_gene_node=true; //d�tection d'un nouveau g�ne
				//A FAIRE verification si gene n'existe pas deja
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
			}else if (qName.equalsIgnoreCase("DisorderGeneAssociationList")){
				in_disordergeneassociation_list=true;
			}else if (qName.equalsIgnoreCase("DisorderGeneAssociationStatus")){
				within_geneAssociationStatus =true;
			}else if (qName.equalsIgnoreCase("LocusList")){ //UPDATE-CR261115-ajout des locus
				in_locus_list=true;
				currentDisease.setGeneLocusCount(attributes.getValue("count"));
			}else if (qName.equalsIgnoreCase("GeneType")){ //UPDATE-CR261115 pour distinguer l'orphanumber du gene et du type
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
		 if (!in_gene_list  && !in_disordergeneassociation_list){ //infos sur Disorder - Orphanumber
			 //System.out.println("disease orphanummm" + tempVal);
			 currentDisease = this.diseases.get(tempVal);
		 }	
		 else if (in_gene_list && in_gene_node && !in_gene_type){ //infos sur Gene - Orphanumber
			//System.out.println("Gene Orphanum" + tempVal);
				currentDisease.add_geneNum(tempVal); 
		 }
		 else if (in_gene_list && in_gene_type && in_gene_node){//infos sur GeneType - Orphanumber
			//System.out.println("GeneType Orphanum" + tempVal);
				currentDisease.setGeneTypNum(tempVal); 
		 }
		 else if (in_disordergeneassociation_list && within_geneAssociationType){//infos sur DisorderGeneAssociationType - Orphanumber
			//System.out.println("GeneAssociationType Orphanum" + tempVal);
				currentDisease.setGeneType(tempVal); //A QUOI CA SERT?
				//currentDisease.setGeneTypeOrphaNum(tempVal); //A FAIRE
		 }
		 else if (in_disordergeneassociation_list && !within_geneAssociationType){//infos sur DisorderGeneAssociationStatus - Orphanumber
			//System.out.println("GeneAssociationStatus Orphanum" + tempVal);
				//currentDisease.setGeneStatusOrphaNum(tempVal); //Pas utiliser pour le moment
		 }
   }
   //======================================================================================================//
   
 //=============================== GESTION DES INFOS SUR GENE =============================================//
   //infos sur gene - Symbol
   else if (qName.equalsIgnoreCase("Symbol")) {
	   //System.out.println("Symbole du gene" + tempVal);
		currentDisease.setSymbol(tempVal);
	} 
  

   //infos sur gene - Name
   else if (in_gene_list && in_gene_node && !in_gene_type && qName.equalsIgnoreCase("Name")) { 
		//System.out.println("Nom du gene :" + tempVal + " and the current disease object is :" + currentDisease);
	    currentDisease.add_genes(tempVal);
   } 
   
   //infos sur les synonymes
	else if (qName.equalsIgnoreCase("Synonym")){
		//System.out.println("Current disease object is :" + currentDisease + " and synonym is : " + tempVal);
		currentDisease.setGeneSyn(tempVal);
	}
   
      //infos sur geneType - type name
   else if (in_gene_node && qName.equalsIgnoreCase("Name") && in_gene_type) { 
		//System.out.println("you are in the gene name node and the name is :" + tempVal);
		currentDisease.setGeneTyp(tempVal);//CREATION setGenType
   } 
   
   //infos sur les locus
  	else if (qName.equalsIgnoreCase("GeneLocus")){
  		//System.out.println("Current disease object is :" + currentDisease + " and locus is : " + tempVal);
  		currentDisease.setGeneLocus(tempVal);
  	}
   

   //======================================================================================================//
   
 //=============================== GESTION DES INFOS SUR ASSOCIATION GENE/DISORDER ========================//
   //infos sur DisorderGeneAssociationType - status
   else if (within_geneAssociationStatus && qName.equalsIgnoreCase("Name")){
	   //System.out.println("genAssociationStatus :" + tempVal);
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
		//System.out.println("referecence source :" + tempVal);
		currentDisease.setGeneSource(tempVal);
	} else if (within_externalReferenceElement && 
		qName.equalsIgnoreCase("Reference")){
		//System.out.println("referecence reference :" + tempVal);
	  	currentDisease.setGeneRefs(tempVal);
	} 
 //======================================================================================================//
   
   //detection balise de fermeture
	else if(qName.equalsIgnoreCase("Gene")&& !in_disordergeneassociation_list) {
	    in_gene_node = false;
	    this.genes.add(tmpGene); //detection de la fin des informations sur un g�ne
	}else if(qName.equalsIgnoreCase("GeneList")) {
	    in_gene_list = false;
    } else if (qName.equalsIgnoreCase("DisorderGeneAssociationList")){
    	in_disordergeneassociation_list = false;
	}else if (qName.equalsIgnoreCase("DisorderGeneAssociationType")){
		within_geneAssociationType = false;
	}else if (qName.equalsIgnoreCase("DisorderGeneAssociationStatus")){
		within_geneAssociationStatus =false;
	}else if (qName.equalsIgnoreCase("GeneType")){
		in_gene_type =false;
	}else if (qName.equalsIgnoreCase("LocusList")){
		in_locus_list =false;
	}
	//else if (qName.equalsIgnoreCase("ExternalReference")){
		//within_externalReferenceElement =false;
	//}
   
   
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


