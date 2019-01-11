package orph2obo;

/**
 * This class forms the main class for parsing
 * Orphanet files to OBO.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import uk.ac.ebi.efo.bubastis.CompareOntologies;



public class Orph2OBO {
    /** This class downloads the ca. 20 Orpha Rare Disease XML files. */
    private OrphadataDownloader downloader;
    /**This Map stores all the orphanumber and its corresponding disease object*/
    private HashMap<String,RareDisease> disease_xrefs;
    private String rare_disease_xref_xml_file=null;
    private String epidemiology_xml_file = null;
    private String genes_XML_file  = null;
    private static String lang;
    
    
    /**
     * @param args
     * @throws OWLOntologyCreationException 
     * @throws OWLOntologyStorageException 
     */
    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
	
	    /**This is the name of the directory where the Orphanet XML files will be written to*/
	    String directory = ORDOVariables.xmlDir;
	    Orph2OBO.setLang(ORDOVariables.lang);
	    String version = ORDOVariables.version;
	    String prev_version = ORDOVariables.prevVersion;
	    directory = Orph2OBO.getLang()+"/"+directory;
	    	//String directory = "OrphadataMay";
		Orph2OBO o2o = new Orph2OBO();
		o2o.createDownloadDirectoryIfDoesntExist(directory);
		o2o.downloadOrphanetFiles(directory);//uncomment this to generate an Orphanet OWL file 
		o2o.getDiseaseXRefs();
		
		o2o.addEpidemiologyData();//uncomment this to generate an Orphanet OWL file 
		o2o.addGenesData();//uncomment this to generate an Orphanet OWL file  
		o2o.downloadDiseaseClassifications();//uncomment this to generate an Orphanet OWL file 
		/* Rare immunological diseases */
		//String immunological = "http://www.orphadata.org/data/xml/fr_product3_195.xml";
		//o2o.download_specific_disease_classification(immunological,"immunological.out");
		
		/* rare cardiac diseases */
		//String cardiac = "http://www.orphadata.org/data/xml/fr_product3_146.xml";
		//o2o.download_specific_disease_classification(cardiac,"cardiac.txt");
		o2o.printOutputToFile();//uncomment this to generate an Orphanet OWL file 
		System.out.println("Obo File written");//uncomment this to generate an Orphanet OWL file 
		System.out.println("starting to save the owl module file");
		RareDisease rd = new RareDisease();
		try {
			//Comment this out if you do not just want to extract a module 
			rd.saveOWLFile();//uncomment this to generate an Orphanet OWL file 
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			System.out.println("problem saving the owl file");
			e.printStackTrace();
		}
		System.out.println("Owl "+Orph2OBO.getLang()+" file is now saved");
		
		
		/******** UPDATE SD Add ChangeLog ********/
		
		/*FileOutputStream f;
		PrintStream defaultOut =  System.out;
		try {
			 f = new FileOutputStream("C:\\OrphoToOBO2\\bubastis_change_log_"+Orph2OBO.getLang()+"_"+version+"_"+prev_version+".txt");			 
			 System.setOut(new PrintStream(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CompareOntologies bubastis = new CompareOntologies();
		bubastis.doFindAllChanges("file:/OrphoToOBO2/ORDO_"+Orph2OBO.getLang()+"_"+prev_version+".owl","file:/OrphoToOBO2/ORDO_"+Orph2OBO.getLang()+"_"+version+".owl");
		bubastis.writeDiffAsXMLFile("C:\\OrphoToOBO2\\bubastis_change_log_"+Orph2OBO.getLang()+"_"+version+"_"+prev_version+".xml");
		System.setOut(defaultOut);
		System.out.println("Exit program after saving Change Log");
		*/
		/****************************************/
		
    }
    /*LogMap2_Matcher logmap2 = new LogMap2_Matcher(
            "http://oaei.ontologymatching.org/2012/conference/data/ekaw.owl",
            "http://oaei.ontologymatching.org/2012/conference/data/cmt.owl");

    Set<MappingObjectStr> logmap2_mappings = logmap2.getLogmap2_Mappings();*/

	/*  
    private void download_specific_disease_classification(String URI,String outname)
    {
	String XML_file = this.downloader.downloadClassificationXML(URI);
	//addClassificationData(XML_file);
	SimpleClassificationParser scp = new SimpleClassificationParser();
	
	try{
	    FileWriter fstream = new FileWriter(outname);
	    BufferedWriter out = new BufferedWriter(fstream);
	    scp.parseDocument(XML_file,disease_xrefs,out);
	    out.close();
	}catch (IOException e){
	    System.err.println("Error while printing OBO file: " + e.getMessage());
	}
	

	
    }
    */
	
	
	
    private void downloadDiseaseClassifications() {
    
	/* rare cardiac diseases */
	String geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_146.xml";
	String XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Developmental anomalies during embryogenesis */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_147.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Inborn errors of metabolism */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_150.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare gastroenterological diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_152.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* 	Rare neurological diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_181.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare abdominal surgical diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_182.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare hepatic diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_183.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* 	Rare respiratory diseases */ 
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_184.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare urogenital diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_185.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare surgical thoracic diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_186.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* 	Rare skin diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_187.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare renal diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_188.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* 	Rare eye diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_189.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare endocrine diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_193.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare haematological diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_194.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare immunological diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_195.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/*  	Rare systemic and rhumatological diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_196.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/*  	Rare odontological diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_197.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare circulatory system diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_198.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* 	Rare bone diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_199.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare otorhinolaryngological diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_200.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* h 	rare infertility disorders */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_201.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* h 	Rare tumors */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_202.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* rare infectious diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_203.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* rare intoxications */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_204.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare gynaecological and obstetric diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_205.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/*  	Rare surgical maxillo-facial diseases */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_209.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare psychiatric diseases - supress in Orphanet
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/fr_product3_211.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);*/
	/* Rare allergic disease */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_212.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* 	Teratologic disorders */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_216.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);

	/* 	childhood disorders */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_231.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* Rare cardiac malformations */
	geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_148.xml";
	XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
	addClassificationData(XML_file);
	/* rare genetic */
     geneticDiseasesClassificationURI = "http://www.orphadata.org/data/xml/"+Orph2OBO.getLang()+"_product3_156.xml";
     XML_file = this.downloader.downloadClassificationXML(geneticDiseasesClassificationURI);
    addClassificationData(XML_file);
    }
    
    /** Add the subclass relations from a classificaiton file. */
    private void addClassificationData(String XMLfile) {
	OrphadataClassificationXMLParser ocxp = new OrphadataClassificationXMLParser();
	ocxp.parseDocument(XMLfile,disease_xrefs);
	
    }
    
    /** Parse the Orphanet epidemiology file and add the information
     * to the objects.
     */
    private void addEpidemiologyData() {
    	//System.err.println("Passer dans addEpidemiologyData");
	OrphaEpidemiologyXMLParser oe = new OrphaEpidemiologyXMLParser();
	if (this.epidemiology_xml_file == null) {
	    System.err.println("Error: Rare Disease Epidemiology XML File not initialized");
	    System.exit(1);
	}
	/*the disease_xrefs is a map which was passed on by the previous method*/
	oe.parseDocument(epidemiology_xml_file, disease_xrefs);
    }
    
    /** 
     * Parse the Orphanet file with information about gene association for the RDs.
     */
    private void addGenesData() {
	OrphaGenesXMLParser ogp = new OrphaGenesXMLParser();
	if (this.genes_XML_file == null) {
	    System.err.println("Error: Rare Disease Genes XML File not initialized");
	    System.exit(1);
	}
	ogp.parseDocument(genes_XML_file, disease_xrefs);
    }
    
    /**
     * Print an OBO file containing the entire RD classification as an ontology.
     * @throws OWLOntologyCreationException 
     * @throws OWLOntologyStorageException 
     */
    private void printOutputToFile() throws OWLOntologyCreationException, OWLOntologyStorageException {
	Iterator<String> it = this.disease_xrefs.keySet().iterator();
	ArrayList<String> sorted = new ArrayList<String>();
	while (it.hasNext()) {
	    String s = it.next();
	    sorted.add(s);
	}
	Collections.sort(sorted);
	it = sorted.iterator();/** sorted the orphanumbers in the disease list*/
	try{
	   FileWriter fstream = new FileWriter("orphadata.obo");
	   BufferedWriter out = new BufferedWriter(fstream);
	   printHeader(out);
	    DiseaseXRef print = new DiseaseXRef();
	    out.write(print.toString());
	    while (it.hasNext()) {
			String id = it.next();
			RareDisease st = this.disease_xrefs.get(id); /**retrieving the disease object for every orphanumber*/
			st.createOWLFile(this.disease_xrefs); //uncomment this to write the Orphanet OWL file 
			st.extractModule();
			out.write(st.getDXRString(this.disease_xrefs) + "\n");
			Gene gen = new Gene();
			out.write(gen.toString());
	    }
	    out.close();
	}catch (IOException e){
	    System.err.println("Error while printing OBO file: " + e.getMessage());
	}
    }
    
    /**
     * Print a brief OBO ontology header.
     * @param out
     * @throws IOException
     */
    private void printHeader(BufferedWriter out) throws IOException
    {
    	//** find date
		Calendar calendar = Calendar.getInstance();
		String dat = calendar.get(Calendar.YEAR)+":"+(calendar.get(Calendar.MONTH)+1)+":"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);

	out.write("format-version: 1.4\n"+
		  "date: "+ dat +"\n" +
		  "saved-by: Drashtti\n"+
		  "default-namespace: orphadata\n\n");
	out.write("[Term]\n" +
			"id:000\n"+
			"name: Rare Disorders\n"); //** root directory
    }
    
    /**
     * Get the cross-references to the diseases using Orphanet's ontology files.
     */
    private void getDiseaseXRefs() {
	OrphaXRefXMLParser parser = new OrphaXRefXMLParser();
	if (this.rare_disease_xref_xml_file == null) {
	    System.err.println("Error: Rare Disease XRef XML File not initialized");
	    System.exit(1);
	}
	parser.parseDocument(rare_disease_xref_xml_file);
	this.disease_xrefs = parser.getXrefHashMap();
	//System.out.println(disease_xrefs.toString());
	//System.out.println(disease_xrefs.isEmpty());
	
    }
    
    /**
     * Download various ontology XML files from the Orphanet website.
     * @param directory
     */
    private void downloadOrphanetFiles(String directory) {
	this.downloader = new OrphadataDownloader(directory);
	/*Downloads the disease and its xref file*/
	rare_disease_xref_xml_file = downloader.downloadRareDiseaseXRefXML();
	/*Downloads the Epidemiological file*/
	epidemiology_xml_file = downloader.downloadEpidemiologyXML();
	/*Downloads the disease and its genes file*/
	genes_XML_file = downloader.downloadGenesXML();
    }
    
    
    /**
     * If it does not already exist, this function creates the directory indicated by the arg.
     * @param directory path of directory to be created.
     */
    private void createDownloadDirectoryIfDoesntExist(String directory) {
	try {
	    File file=new File(directory);
	    if (file.exists()) {
		System.out.println("Directory: \"" + directory + "\" exists already. ");
		return;
	    }
	    boolean success = file.mkdir();
	    if (success) {
		System.out.println("Directory: " + directory + " created for downloading Orphanet data");
	    }  
	    
	}catch (Exception e){//Catch exception if any
	    System.err.println("Error making directory: " + directory + 
					  "\n"+ e.getMessage());
	    System.exit(1);
	}
    }
    

    
    public Orph2OBO() {
	//	
    }
    
    /** Use this to produce informative error messages, like __LINE__ in C */
    public static int getLineNumber() {
	return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

	public static String getLang() {
		return lang;
	}

	public static void setLang(String lang) {
		Orph2OBO.lang = lang;
	}
    
    
	
}
