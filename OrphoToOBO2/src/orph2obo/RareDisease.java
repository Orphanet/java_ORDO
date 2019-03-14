package orph2obo;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.rdf.rdfxml.renderer.XMLWriterPreferences;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.model.OWLLiteral;

import orph2obo.ExternalReference;
import orph2obo.Prevalence;
import orph2obo.Conf;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;
/**
 * Class representing the information in a RareDisease XML stanza. See the code for the class
 * OrphaXRefXMLParser/OrphaGenesXMLParser/OrphaEpidemiologyParser/OrphadataClassificationXMLParser
 *  to get an idea of how objects of this class are constructed.
 */
public class RareDisease {
    
    /** The name of the disease */
    private String name=null;
    /**Disorder id- the internal id for orphanet*/
    private String orphID = null;
    /** The orphanet ID for this rare disease*/
    private String orphanum=null;
    /** The orphanet short def for this rare disease*/
    private String def=null;
    /** The reference for this rare disease*/
    private String reference=null;
    /** Prevalences */
    private ArrayList<Prevalence> prevalences;
    
    /** Obsolete Flag */
    private boolean obsolete; 
    private boolean movedTo;
    /** Obsolete from disorder */
    private String obsoleteFrom;
    
    /**AgeofOnset for a disease*/
    private ArrayList<String> ageOfOnset = null;
    /**AgeOfDeath for a disease*/
    private ArrayList<String> ageOfDeath = null;
    /**gene typing status */
    private String geneTypeStatus=null;
    /**disease typig validity*/
    private String disTypeValidity=null;
    /**Count of exrefs for genes*/
    //this is necessary to get the division for the number of xrefs a gene is associated with
    private ArrayList<String> count;
    /**Count of synonyms for genes*/
    //this is necessary to get the division for the number of synonyms of a gene
    private ArrayList<String> genSynCount;
    //this is necessary to get the division for the number of synonyms of a gene
    private ArrayList<String> genLocusCount;
    /**list of gene symbol*/
    private ArrayList<String> symbol;
    /**Mode of Inheritance*/
    private ArrayList<String> inheritance;
   /**Synonym for disease*/
    private ArrayList<String> synonym;
    /** each disorder has one expert link for this disease - used as URI */
    private String expert_link=null;
    /** List of Sources and references for external ref for the disease*/
    private ArrayList<String> sourceList;
    private ArrayList<String> refList;
    private ArrayList<String> refValidList; // UPDATE SD validation status
    private ArrayList<String> ICDRelList; // UPDATE SD validation status
    /** List of names of genes for this disease */
    private ArrayList<String> genelists;
    /**list of gene orphanumbers**/
    private ArrayList<String> geneNum;
    /**list of gene xref sources */
    private ArrayList<String> gsources;
    /**list of gene xref references*/
    private ArrayList<String> grefs;
    
    /** List of external references count for genes. */
    private ArrayList<ExternalReference> xref_list;
    /** Parents (ids) */
    private ArrayList<String> isa_list;
    //private boolean flag = true;
    /**Type of gene disease association Orphanumber*/
     private ArrayList<String> geneType;;
     /**type of gene disease association name*/
     private ArrayList<String> geneTypeName;
     /**Set the disease-type */
     private String diseaseType = null;
     /**set the Orphanumber for age of Onset*/
     private ArrayList<String> onsetNum = null;
     /**set the Orphanumber for age of Death*/
     private String deathNum = null;
     /**set the Orphanumber for inheritance*/
     private ArrayList<String>  inheritNum;
     /**set the Orphanumber for Prevalence data*/
     private String prevNum = null;
     /** set the synonyms for the genes */
     private ArrayList<String> geneSyn;
     /** set the locus for the genes */
     private ArrayList<String> geneLocus;
     /**list of geneType orphanumbers**/
     private ArrayList<String> geneTypNum;
     /**list of geneTyp name**/
     private ArrayList<String> geneTyp;
     
     static private OWLVariables owlvar = null;
     static private Set<String> rareOrpho = new HashSet<String> ();
     
     // SD MULTI LNG TEST 
     private static Map<String, String> labels = new HashMap<String, String>();
     private static Map<String, String> definitions = new HashMap<String, String>();
     private static String lang = "fr";
     private static String version = "2.7";
     
     //variables that need to be set for OWL API
    /** private OWLDataFactory factory;
    private PrefixManager pm;
     private OWLOntologyManager manager;
     private OWLOntology ontology;
     private IRI ontologyIRI;*/

    public String get_orphanum() { return this.orphanum; }
    public void setOrphanum(String num) { this.orphanum = num; }
    public void setID (String id) { this.orphID = id; }
    public void setDef(String def) { this.def = def; }
    public String getDef() { return this.def; }

    public void setExpertLink(String link) { this.expert_link = link; 
    //System.out.println(expert_link_list); 
    }
    public String getExpertLink(){ return this.expert_link; }
    

    public void setName(String s) { this.name = s;}
    public String getName() { return this.name; }

    public void setObsolete(){this.obsolete=true;}
    public boolean isObsolete(){return this.obsolete;}
    
    public void setMovedTo(){this.movedTo=true;}
    public boolean isMovedTo(){return this.movedTo;}
    
    public void setObsoleteFrom(String id){this.obsoleteFrom = id;}
    public String getObsoleteFrom(){return this.obsoleteFrom;}
    
    public void  setReference(String ref) { this.reference = ref; }
    public String getReference() { return this.reference ; }
    
    public void setAgeOfOnset (String onset){ this.ageOfOnset.add(onset);}
    public void setAgeOfDeath (String death) { this.ageOfDeath.add(death);}
    
    public void setSymbol (String symb){ this.symbol.add(symb);}
    
    
    public void setInheritance ( String inherit){
    	this.inheritance.add(inherit);
    	}
    
	
    public void setSynonym (String syn){
		this.synonym.add(syn);
	}

    public void add_genes(String genes) {
	//System.out.println("This is the name of the gene recieved" + genes);
	    this.genelists.add(genes);
    }

    public void add_geneNum (String gnum){
    	this.geneNum.add(gnum);
    }
    
    public void addExternalReference(ExternalReference xr) {
	this. xref_list.add(xr);
    }

    public void addSource (String sr){
    	this.sourceList.add(sr);
    }
    
    public void addRef(String re){
    	this.refList.add(re);
    }    
    
    //UPDATE SD validation status
    public void addValidRef(String re){ 
    	this.refValidList.add(re);
    }
    public void addICDRel(String re){ 
    	this.ICDRelList.add(re);
    }
    
    public void addPreval(Prevalence re){
    	this.prevalences.add(re);
    }
    
    public void setExRefCount(String erc){
    	//System.out.println("erc" + erc);
    	this.count.add(erc);
    }
    
    public void setGeneSource( String gs){
    	this.gsources.add(gs);
    	//System.out.println(gsources.isEmpty());
    }
    
    public void setGeneRefs(String gr){
    	this.grefs.add(gr);
    }
    
    public void add_is_a_link(String parnt) { if(! this.isa_list.contains(parnt)){ this.isa_list.add(parnt);} }

    public void setGeneType(String geneType){
    	this.geneType.add(geneType);
    	//System.out.println("gene type is added to the list" + geneType);
    }
    
    public String getdiseaseType(){
    	return this.diseaseType;
    }
    
public void setTypeOrph(String diseaseType) {
		this.diseaseType = diseaseType;
		
	}
public void setOnsetNum(String onsetNum) {
	this.onsetNum.add(onsetNum);
	
	}
public void setDeathNum(String deathNum) {
	this.deathNum = deathNum;
}
public void setInheritNum(String inheritNum) {
	//System.out.println("Inheritance number is " + inheritNum);
	this.inheritNum.add(inheritNum);
}
	public void setPrevNum(String prevNum) {
	this.prevNum = prevNum;
	
}
	
	//====================== GESTION GENE =================================//
	public void setgeneTypeName(String geneTypeName) {
		//System.out.println("the gene typing recieved is :" + geneTypeName);
		this.geneTypeName.add(geneTypeName);
	}
	
	//============ Gestion des synonymes ==========//
	public void setGeneSyn(String geneSyn) {
		this.geneSyn.add(geneSyn);
		
	}
	public void setGeneSynCount(String genSynCount) {
		this.genSynCount.add(genSynCount);
	}
	//==============================================
	
	//============= Gestion des locus =============/
	public void setGeneLocus(String geneLocus) {
		this.geneLocus.add(geneLocus);
		
	}
	public void setGeneLocusCount(String genLocusCount) {
		this.genLocusCount.add(genLocusCount);
	}
	//================================================
	
	//============= Gestion des types  =============//
	public void setGeneTyp(String geneTyp) {
		this.geneTyp.add(geneTyp);
		
	}
	public void setGeneTypNum(String geneTypNum) {
		this.geneTypNum.add(geneTypNum);
	}
	//================================================
	
	//============= Gestion des associations gene/Disorder  =============//
	public void setGeneTypeStatus(String geneTypeStatus) {
		
		this.geneTypeStatus = geneTypeStatus;
	}
	//====================================================================//

	public void setTypeValidity(String disTypeValid) {
		
		this.disTypeValidity = disTypeValid;
	}
	



    public RareDisease() {
	//this.expert_link_list = new ArrayList<String> ();
    //System.out.println("initialising all the array lists");
	this.genelists = new ArrayList<String> ();
	this.xref_list = new  ArrayList<ExternalReference> ();
	this.isa_list = new ArrayList<String> ();
	this.inheritance = new ArrayList<String>();
	this.synonym = new ArrayList<String> ();
	this.sourceList = new ArrayList<String> ();
	this.refList = new ArrayList<String>();
	this.refValidList = new ArrayList<String>(); // UPDATE SD validation status
	this.ICDRelList = new ArrayList<String>(); // UPDATE SD  ICDRelList
	this.symbol = new ArrayList<String>();
	this.geneNum = new ArrayList<String>();
	this.gsources = new ArrayList<String>();
	this.grefs = new ArrayList<String> ();
	this.count= new ArrayList<String>();
	this.geneType = new ArrayList<String>();//type d'association gene/maladie
	this.geneTypeName = new ArrayList<String>();//nom de l'association gene/maladie
	this.inheritNum = new ArrayList<String>();
	this.geneSyn = new ArrayList<String>();
	this.genSynCount = new ArrayList<String> ();
	this.prevalences = new ArrayList<Prevalence> ();
	this.geneLocus = new ArrayList<String>();
	this.genLocusCount = new ArrayList<String> ();
	this.geneTyp = new ArrayList<String>();//type de gene
	this.geneTypNum = new ArrayList<String> ();//orphanNum du type de gene
	this.ageOfDeath = new ArrayList<String> ();
	this.ageOfOnset = new ArrayList<String> ();
	this.onsetNum = new ArrayList<String> ();
	//System.out.println(this.synonym.toString());
    }
    
    //variables that need to be set for OWL API
    /** private OWLDataFactory factory;
    private PrefixManager pm;
     private OWLOntologyManager manager;
     private OWLOntology ontology;
     private IRI ontologyIRI;
     * @throws OWLOntologyCreationException 
     * @throws OWLOntologyStorageException */
    
    public void extractModule() throws OWLOntologyCreationException, OWLOntologyStorageException{
    	writeToOWLFile();
    	OrphadataClassificationXMLParser parser = new OrphadataClassificationXMLParser();
    	rareOrpho = parser.getRareGeneOrpha();
    	if(rareOrpho.contains(this.orphanum)){
	    	OWLClass rareDisorder = owlvar.getFactory().getOWLClass(this.orphanum, owlvar.getPrefixmanager());
			OWLAnnotation labelRare = owlvar.getFactory().getOWLAnnotation(
			owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.name,getLang()));
			OWLDeclarationAxiom declarationClass = owlvar.getFactory().getOWLDeclarationAxiom(rareDisorder);
			OWLAxiom labelling = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), labelRare);
			owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntologymod(), declarationClass));
			owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntologymod(), labelling));
			if(!isa_list.isEmpty()){
				for ( int i= 0; i<isa_list.size(); i++){
				
					OWLClass superClass = owlvar.getFactory().getOWLClass(this.isa_list.get(i), owlvar.getPrefixmanager());
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntologymod(), owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder, superClass)));
				}
			}
			
	
			//def for the disease if any
			 if (this.def != null) {
				PrefixManager pm2 = new DefaultPrefixManager("http://www.ebi.ac.uk/efo/");
				OWLAnnotation definition = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
						.getOWLAnnotationProperty(
								"definition", pm2),
								owlvar.getFactory().getOWLLiteral(this.def,getLang()));
				OWLAxiom define = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), definition);
				OWLAnnotation definitionCitation = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
						.getOWLAnnotationProperty(
								"definition_citation",
								pm2),owlvar.getFactory().getOWLLiteral("orphanet",getLang()));
				OWLAxiom defineCite = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), definitionCitation);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntologymod(), define));
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntologymod(), defineCite));
			}
			//synonyms for the disease
			 if (! this.synonym.isEmpty()){
				for( int i=0; i< this.synonym.size(); i++){
					PrefixManager pm2 = new DefaultPrefixManager("http://www.ebi.ac.uk/efo/");
					OWLAnnotation alternativeTerm = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
							.getOWLAnnotationProperty(
									"alternative_term",
									pm2), owlvar.getFactory().getOWLLiteral(this.synonym.get(i),getLang()));
					OWLAxiom synonym = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), alternativeTerm);
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntologymod(),synonym));
				}
			}
			
			//xrefs for the disease
			 if (!this.sourceList.isEmpty()){
				for (int i=0; i<this.sourceList.size(); i++){
					
					//PrefixManager pm2 = new DefaultPrefixManager("http://www.geneontology.org/formats/oboInOwl/");
					PrefixManager oboInOwl = new DefaultPrefixManager("http://www.geneontology.org/formats/oboInOwl#");
					PrefixManager obo  = new DefaultPrefixManager("http://purl.obolibrary.org/obo/");
					
					OWLAnnotation database_cross_reference = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
							.getOWLAnnotationProperty(
									"hasDbXref",
									oboInOwl), owlvar.getFactory().getOWLLiteral(this.sourceList.get(i) + ":" + this.refList.get(i)));
					OWLAxiom xref = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), database_cross_reference);
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntologymod(), xref));
					//UPDATE SD mapping relation
					Set<OWLAnnotation> ref = new HashSet<OWLAnnotation>();
					ref.add(database_cross_reference);
										
					if(!this.refValidList.get(i).equals("")){
						
						OWLAnnotation curationAssertion = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("ECO_0000218",obo),owlvar.getFactory().getOWLLiteral(this.refValidList.get(i),getLang()));
						Set<OWLAnnotation> owlAnnoCur = new HashSet<OWLAnnotation>();

						owlAnnoCur.add(curationAssertion);
						if(!this.ICDRelList.get(i).equals("")){
							
							OWLAnnotation icd10Rel = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("ECO_0000218",obo),owlvar.getFactory().getOWLLiteral(this.ICDRelList.get(i),getLang()),owlAnnoCur);
							owlAnnoCur = new HashSet<OWLAnnotation>();
							owlAnnoCur.add(icd10Rel);
							
						}
						OWLAxiom xref2 = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), database_cross_reference,owlAnnoCur);
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), xref2));
						
					}
					// FIN UPDATE
		
				}
			}
		}		 			
	}
    
    // 
    public void readDefinitionFile() throws FileNotFoundException{
    	String lang = getLang();
        Scanner scan = new Scanner(new File(Conf.defPath+"DEF_ORDO_"+lang+".txt"));

        while(scan.hasNext()){
            String curLine = scan.nextLine();
            String[] splitted = curLine.split("\t");
            String iri = splitted[0].trim();
            String lab = splitted[1].trim();
            String def = splitted[2].trim();
            
            if(lab.equals("NA")){
            	System.out.println("NO LABEL FOR CONCEPT :"+iri);
            }else{
            	setConceptLabel(iri,lab);
            }
            
            if(def.equals("NA") || def.equals("")){
            	System.out.println("NO DEFINITION FOR CONCEPT :"+iri);            	
            }else{
            	setConceptDefinition(iri,def);
            }
            
        }
        
        scan.close();
    }
    

    public void writeToOWLFile() throws OWLOntologyCreationException{
    	if(this.owlvar != null){
    		return;
    		}
    	
    	// 
    	setLang(ORDOVariables.lang);
    	setVersion(ORDOVariables.version);
    	try {
			readDefinitionFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("owl variable is initiated");
    	this.owlvar = new OWLVariables();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        owlvar.setManager(manager);
        IRI ontologyIRI = IRI.create("http://www.orpha.net/ontology/ORDO_"+getLang()+"_"+getVersion()+".owl");
        owlvar.setOntologyIRI(ontologyIRI);
        OWLOntology ontology = manager.createOntology(ontologyIRI);
        owlvar.setOntology(ontology);

    	System.out.println("Created ontology1: " + ontology);
    	
    	 // UPDATE SD Absolute IRI
    	
    	IRI versionIRI = IRI.create("http://www.orpha.net/version"+getVersion());
    	OWLOntologyID newOntologyID = new OWLOntologyID(ontologyIRI,versionIRI);
    	SetOntologyID setOntologyID = new SetOntologyID(ontology, newOntologyID);
    	manager.applyChange(setOntologyID);
    	
    	PrefixManager pm = new DefaultPrefixManager("http://www.orpha.net/ORDO/Orphanet_");
    	owlvar.setPrefixmanager(pm);
    	OWLDataFactory factory = manager.getOWLDataFactory();
    	owlvar.setFactory(factory);
    	
    	// UPDATE SD ADD DOCTYPE
    	XMLWriterPreferences.getInstance().setUseNamespaceEntities(true);
    	
    	PrefixManager obo = new DefaultPrefixManager("http://purl.obolibrary.org/obo/");
    	// UPDATE SD add Version 
    	PrefixManager oboInOwl = new DefaultPrefixManager("http://www.geneontology.org/formats/oboInOwl#");
    	PrefixManager owl = new DefaultPrefixManager("http://www.w3.org/2002/07/owl#");
		OWLAnnotation version = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("versionInfo", owl),
				factory.getOWLLiteral(getVersion()));
		manager.applyChange(new AddOntologyAnnotation(ontology, version));
		
        /* **** UPDATE SD   ajout cr�ateur **** */
		PrefixManager dct = new DefaultPrefixManager("http://purl.org/dc/terms/");
    	PrefixManager dc  = new DefaultPrefixManager("http://purl.org/dc/elements/1.1/");
    	PrefixManager cc  = new DefaultPrefixManager(ORDOVariables.licensePrefixIRI);
    	
    	ArrayList<String> list = ORDOVariables.creatorList;
    	
    	for (final String name: list){
			OWLAnnotation creator = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("creator", dc),
					factory.getOWLLiteral(name));
			manager.applyChange(new AddOntologyAnnotation(ontology, creator));
    	}
    	
    	/* *** UPDATE SD AJOUT DATES *** */ 
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	Date date = new Date();
    	
    	OWLAnnotation dateGeneration = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("modified", dct),
    			factory.getOWLLiteral(dateFormat.format(date), OWL2Datatype.XSD_DATE_TIME));
    	
		manager.applyChange(new AddOntologyAnnotation(ontology, dateGeneration));

		OWLAnnotation dateStart      = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("created", dct),
				factory.getOWLLiteral(ORDOVariables.dateOfCreation, OWL2Datatype.XSD_DATE_TIME));
		
		manager.applyChange(new AddOntologyAnnotation(ontology, dateStart));
		/* **** UPDATE SD  FIN ajout cr�ateur **** */
		
		/* ***** UPDATE SD Ajout licence  **** */
		IRI licenseIRI = IRI.create(ORDOVariables.licensePrefixIRI + ORDOVariables.licenseClassIRI );
		OWLAnnotation licence      = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("license", dct),licenseIRI);
		
		manager.applyChange(new AddOntologyAnnotation(ontology, licence));
		
		
		OWLClass licenceClass = factory.getOWLClass(ORDOVariables.licenseClassIRI,cc);manager.applyChange(new AddOntologyAnnotation(ontology, licence));
		OWLDeclarationAxiom licenceDec = factory.getOWLDeclarationAxiom(licenceClass);
    	manager.applyChange(new AddAxiom(ontology, licenceDec));
    	
       	OWLAnnotation licenceLabel = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(ORDOVariables.licenceLabel));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(licenceClass.getIRI(), licenceLabel)));
    	
		for(int annotIndex = 0;annotIndex < ORDOVariables.legalTerms.length ;annotIndex++){
			OWLAnnotation legalTerm = factory.getOWLAnnotation(
					factory.getOWLAnnotationProperty(ORDOVariables.legalTerms[annotIndex][0],cc), IRI.create(ORDOVariables.legalTerms[annotIndex][1]));
			
			manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(licenceClass.getIRI(), legalTerm)));
		}
		
		
		
		
    	
		/* ***** FIN UPDATE SD Ajout licence  **** */
		
    	OWLClass phenome = factory.getOWLClass("C001",pm);
    	OWLAnnotation phenomelabel = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(getConceptLabel("C001"),getLang()));
    	OWLClass groupPhenome = factory.getOWLClass("377794",pm);
    	OWLAnnotation label = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(getConceptLabel("377794"),getLang()));
    	OWLClass disease = factory.getOWLClass("377788",pm);
    	OWLAnnotation label1 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(getConceptLabel("377788"),getLang()));
    	OWLClass clinicalSub = factory.getOWLClass("377796", pm);
    	OWLAnnotation label2 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(getConceptLabel("377796"),getLang()));
    	OWLClass malformSyn = factory.getOWLClass("377789", pm);
    	OWLAnnotation label3 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(getConceptLabel("377789"),getLang()));
    	OWLClass etioSub = factory.getOWLClass("377795", pm);
    	OWLAnnotation label4 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(getConceptLabel("377795"),getLang()));
    	OWLClass morphAna = factory.getOWLClass("377791", pm);
    	OWLAnnotation label5 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(getConceptLabel("377791"),getLang()));
    	OWLClass clinicalSyn = factory.getOWLClass("377792", pm);
    	OWLAnnotation label6 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(getConceptLabel("377792"),getLang()));
    	OWLClass partClinSitu = factory.getOWLClass("377793", pm);
    	OWLAnnotation label7 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(getConceptLabel("377793"),getLang()));
    	OWLClass histoPathoSub = factory.getOWLClass("377797", pm);
    	OWLAnnotation label9 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(getConceptLabel("377797"),getLang()));
    	OWLClass bioAna = factory.getOWLClass("377790", pm);
    	OWLAnnotation label0 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral(getConceptLabel("377790"),getLang()));
    	
    	//=======Gene=====
    	OWLClass gene = factory.getOWLClass("C010", pm);
       	OWLAnnotation genelabel = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("C010"),getLang()));
    	OWLClass geneTypProtProd = factory.getOWLClass("410298", pm);
       	OWLAnnotation geneTypProtProdLabel = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("410298"),getLang()));
    	OWLClass geneTypDisAssLoc = factory.getOWLClass("410297", pm);
       	OWLAnnotation geneTypDisAssLocLabel = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("410297"),getLang()));
    	OWLClass geneTypNonCodingRNA = factory.getOWLClass("410299", pm);
       	OWLAnnotation geneTypNonCodingRNALabel = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("410299"),getLang()));
       	//=================
       	
       	OWLClass inheritance = factory.getOWLClass("C005", pm);
       	OWLAnnotation inheritancelabel = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("C005"),getLang()));
       	// OBSOLETE_CLASS
       	OWLClass obsoleteClass = factory.getOWLClass("ObsoleteClass", new DefaultPrefixManager("http://www.orpha.net/ORDO/"));  // /_\ � revoir, le lien d'origine n'existe plus
       	OWLAnnotation obsoleteLabel = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("ObsoleteClass"),getLang()));
       	
    	//OWLClass autoRecess = factory.getOWLClass("108933", pm);
    	//OWLAnnotation autoRLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("autosomal recessive"));
    	//OWLClass autoDom = factory.getOWLClass("108932", pm);
    	//OWLAnnotation autoDomLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("autosomal dominant"));
       	//OWLClass mitoInher = factory.getOWLClass("409933", pm);
    	//OWLAnnotation mitoInherLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("mitochondriale"));
    	//OWLClass xRecess = factory.getOWLClass("108934", pm);
    	//OWLAnnotation xRecessLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("x linked recessive"));
    	//OWLClass xDom = factory.getOWLClass("108935", pm);
    	//OWLAnnotation xDomLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("x linked dominant"));
    	//OWLClass sporadic = factory.getOWLClass("108938", pm);
    	//OWLAnnotation sporadicLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("sporadic"));
    	
    	OWLClass autoDominant = factory.getOWLClass("409929", pm);
    	OWLAnnotation autoDominantLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409929"),getLang()));
    	OWLClass autoRecessive = factory.getOWLClass("409930", pm);
    	OWLAnnotation autoRecessiveLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409930"),getLang()));
    	OWLClass multiGene = factory.getOWLClass("409931", pm);
    	OWLAnnotation multiGeneLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409931"),getLang()));
    	OWLClass xlinkedRecessive = factory.getOWLClass("409932", pm);
    	OWLAnnotation xlinkedRecessiveLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409932"),getLang()));
    	OWLClass mitho = factory.getOWLClass("409933", pm);
    	OWLAnnotation mithoLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409933"),getLang()));
    	OWLClass xlinkedDominant = factory.getOWLClass("409934", pm);
    	OWLAnnotation xlinkedDominantLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409934"),getLang()));
    	OWLClass oligenic = factory.getOWLClass("409936", pm);
    	OWLAnnotation oligenicLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409936"),getLang()));
    	OWLClass semiDom = factory.getOWLClass("409937", pm);
    	OWLAnnotation semiDomLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409937"),getLang()));
    	OWLClass yLinked = factory.getOWLClass("409938", pm);
    	OWLAnnotation yLinkedLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409938"),getLang()));
    	OWLClass unknown = factory.getOWLClass("409939", pm);
    	OWLAnnotation unknownLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409939"),getLang()));
    	OWLClass nodata = factory.getOWLClass("409940", pm);
    	OWLAnnotation noDataLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409940"),getLang()));
    	OWLClass notApplicable = factory.getOWLClass("409941", pm);
    	OWLAnnotation notApplicableLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409941"),getLang()));
    	
    	OWLClass ageofOnsetClass = factory.getOWLClass("C023", pm);
    	OWLAnnotation ageOfOnLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("C023"),getLang()));
    	// UPDATE SD ajout de geography, epidemio, prevalence, cas/familles, cas et famille
    	OWLClass geographyClass = factory.getOWLClass("C009", pm);
    	OWLAnnotation geographyLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("C009"),getLang()));
    	OWLClass epidemioClass = factory.getOWLClass("C003", pm);
    	OWLAnnotation epidemioLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("C003"),getLang()));
    	OWLClass prevClass = factory.getOWLClass("C004", pm);
    	OWLAnnotation prevLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("C004"),getLang()));
    	// ajout point preval etc
    	OWLClass pointPrevClass = factory.getOWLClass("409966", pm);
    	OWLAnnotation pointPrevLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409966"),getLang()));
    	OWLClass birthPrevClass = factory.getOWLClass("409968", pm);
    	OWLAnnotation birthPrevLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409968"),getLang()));
    	OWLClass lifePrevClass = factory.getOWLClass("409969", pm);
    	OWLAnnotation lifePrevLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409969"),getLang()));

    	OWLClass incidenceClass = factory.getOWLClass("409967", pm);
    	OWLAnnotation incidenceLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409967"),getLang()));
    	
    	OWLClass casFamClass = factory.getOWLClass("409970", pm);
    	OWLAnnotation casFamLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409970"),getLang()));
    	OWLClass casClass = factory.getOWLClass("409973", pm);
    	OWLAnnotation casLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409973"),getLang()));
    	OWLClass famClass = factory.getOWLClass("409974", pm);
    	OWLAnnotation famLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("409974"),getLang()));
    	
    	// UPDATE SD 4/01/2015
    	OWLAnnotationProperty curatorClass = factory.getOWLAnnotationProperty("ECO_0000205", obo);
    	OWLAnnotation curatorLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("ECO_0000205"),"en"));
    	OWLAnnotationProperty assertionClass = factory.getOWLAnnotationProperty("ECO_0000218", obo);
    	OWLAnnotation assertionLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("ECO_0000218"),"en"));
    	
    	OWLClass disGermMutClass = factory.getOWLClass("317343", pm);
    	OWLAnnotation disGermMutLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("317343"),getLang()));
    	OWLClass cgtClass = factory.getOWLClass("327767", pm);
    	OWLAnnotation cgtLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("327767"),getLang()));
    	OWLClass dgmgfClass = factory.getOWLClass("410296", pm);
    	OWLAnnotation dgmgfLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("410296"),getLang()));
    	OWLClass dgmlfClass = factory.getOWLClass("410295", pm);
    	OWLAnnotation dgmlfLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("410295"),getLang()));
    	OWLClass dgsClass = factory.getOWLClass("317344", pm);
    	OWLAnnotation dgsLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("317344"),getLang()));
    	OWLClass majSusClass = factory.getOWLClass("317345", pm);
    	OWLAnnotation majSusLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("317345"),getLang()));
    	OWLClass mgClass = factory.getOWLClass("317346", pm);
    	OWLAnnotation mgLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("317346"),getLang()));
    	OWLClass msmClass = factory.getOWLClass("465410", pm);
    	OWLAnnotation msmLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("465410"),getLang()));
    	OWLClass partOfFusionClass = factory.getOWLClass("317348", pm);
    	OWLAnnotation partOfFusionLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("317348"),getLang()));
    	OWLClass roleInClass = factory.getOWLClass("317349", pm);
    	OWLAnnotation roleInLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("317349"),getLang()));
    	OWLClass hasLocClass = factory.getOWLClass("C040", pm);
    	OWLAnnotation hasLocLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("C040"),getLang()));
    	OWLClass presentInClass = factory.getOWLClass("C022", pm);
    	OWLAnnotation presentInLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("C022"),getLang()));
    	OWLClass partOfClass = factory.getOWLClass("C021", pm);
    	OWLAnnotation partOfLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(getConceptLabel("C021"),getLang()));
    	
   	
    	
    	OWLDeclarationAxiom phenDec = factory.getOWLDeclarationAxiom(phenome);
    	OWLDeclarationAxiom declarationAxiom = factory.getOWLDeclarationAxiom(groupPhenome);
    	OWLDeclarationAxiom declarationAxiom1 = factory.getOWLDeclarationAxiom(disease);
    	OWLDeclarationAxiom declarationAxiom2 = factory.getOWLDeclarationAxiom(clinicalSub);
    	OWLDeclarationAxiom declarationAxiom3 = factory.getOWLDeclarationAxiom(malformSyn);
    	OWLDeclarationAxiom declarationAxiom4 = factory.getOWLDeclarationAxiom(etioSub);
    	OWLDeclarationAxiom declarationAxiom5 = factory.getOWLDeclarationAxiom(morphAna);
    	OWLDeclarationAxiom declarationAxiom6 = factory.getOWLDeclarationAxiom(clinicalSyn);
    	OWLDeclarationAxiom declarationAxiom7 = factory.getOWLDeclarationAxiom(partClinSitu);
    	OWLDeclarationAxiom declarationAxiom9 = factory.getOWLDeclarationAxiom(histoPathoSub);
    	OWLDeclarationAxiom declarationAxiom0 = factory.getOWLDeclarationAxiom(bioAna);
    	//==gene=====
    	OWLDeclarationAxiom declarationGene = factory.getOWLDeclarationAxiom(gene);
    	OWLDeclarationAxiom genType1= factory.getOWLDeclarationAxiom(geneTypProtProd);//gene with protein product
    	OWLDeclarationAxiom genType2 = factory.getOWLDeclarationAxiom(geneTypDisAssLoc);//disorder-associated locus
    	OWLDeclarationAxiom genType3 = factory.getOWLDeclarationAxiom(geneTypNonCodingRNA);//non-coding RNA
    	//===========
    	// OBSOLETE
    	OWLDeclarationAxiom obsolete = factory.getOWLDeclarationAxiom(obsoleteClass);
    	
    	OWLDeclarationAxiom inheritance0 = factory.getOWLDeclarationAxiom(inheritance);
    	OWLDeclarationAxiom inheritance1 = factory.getOWLDeclarationAxiom(semiDom);
    	//OWLDeclarationAxiom inheritance2 = factory.getOWLDeclarationAxiom(autoDom);
    	//OWLDeclarationAxiom inheritance3 = factory.getOWLDeclarationAxiom(mitoInher);
    	//OWLDeclarationAxiom inheritance4 = factory.getOWLDeclarationAxiom(multigen);
    	//OWLDeclarationAxiom inheritance5 = factory.getOWLDeclarationAxiom(xRecess);
    	//OWLDeclarationAxiom inheritance6 = factory.getOWLDeclarationAxiom(xDom);
    	//OWLDeclarationAxiom inheritance7 = factory.getOWLDeclarationAxiom(sporadic);
    	
    	OWLDeclarationAxiom inheritance8 = factory.getOWLDeclarationAxiom(autoDominant);
    	OWLDeclarationAxiom inheritance9 = factory.getOWLDeclarationAxiom(autoRecessive);
    	OWLDeclarationAxiom inheritance10 = factory.getOWLDeclarationAxiom(multiGene);
    	OWLDeclarationAxiom inheritance11 = factory.getOWLDeclarationAxiom(xlinkedRecessive);
    	OWLDeclarationAxiom inheritance12 = factory.getOWLDeclarationAxiom(xlinkedDominant);
    	OWLDeclarationAxiom inheritance13 = factory.getOWLDeclarationAxiom(oligenic);
    	OWLDeclarationAxiom inheritance14 = factory.getOWLDeclarationAxiom(yLinked);
    	OWLDeclarationAxiom inheritance15 = factory.getOWLDeclarationAxiom(unknown);
    	OWLDeclarationAxiom inheritance16 = factory.getOWLDeclarationAxiom(nodata);
    	OWLDeclarationAxiom inheritance17 = factory.getOWLDeclarationAxiom(notApplicable);
    	OWLDeclarationAxiom inheritance18 = factory.getOWLDeclarationAxiom(mitho);
    	
    	OWLDeclarationAxiom ageofOn = factory.getOWLDeclarationAxiom(ageofOnsetClass);
    	OWLDeclarationAxiom prev = factory.getOWLDeclarationAxiom(prevClass);
    	// ajout point preval etc
    	OWLDeclarationAxiom pointPrev = factory.getOWLDeclarationAxiom(pointPrevClass);
    	OWLDeclarationAxiom birthPrev = factory.getOWLDeclarationAxiom(birthPrevClass);
    	OWLDeclarationAxiom lifePrev = factory.getOWLDeclarationAxiom(lifePrevClass);
    	
    	OWLDeclarationAxiom geography = factory.getOWLDeclarationAxiom(geographyClass);
    	OWLDeclarationAxiom epidemiology = factory.getOWLDeclarationAxiom(epidemioClass);
    	OWLDeclarationAxiom casFam = factory.getOWLDeclarationAxiom(casFamClass);
    	OWLDeclarationAxiom incidence = factory.getOWLDeclarationAxiom(incidenceClass);
    	OWLDeclarationAxiom cas = factory.getOWLDeclarationAxiom(casClass);
    	OWLDeclarationAxiom fam = factory.getOWLDeclarationAxiom(famClass);
    	
    	// UPDATE SD 4/01/2015
    	OWLDeclarationAxiom curator = factory.getOWLDeclarationAxiom(curatorClass);
    	OWLDeclarationAxiom assertion = factory.getOWLDeclarationAxiom(assertionClass);
    	
    	OWLDeclarationAxiom disGermMut = factory.getOWLDeclarationAxiom(disGermMutClass);
    	OWLDeclarationAxiom cgt = factory.getOWLDeclarationAxiom(cgtClass);
    	OWLDeclarationAxiom dgmgf = factory.getOWLDeclarationAxiom(dgmgfClass);
    	OWLDeclarationAxiom dgmlf = factory.getOWLDeclarationAxiom(dgmlfClass);
    	OWLDeclarationAxiom dgs = factory.getOWLDeclarationAxiom(dgsClass);
    	OWLDeclarationAxiom majSus = factory.getOWLDeclarationAxiom(majSusClass);
    	OWLDeclarationAxiom mg = factory.getOWLDeclarationAxiom(mgClass);
    	OWLDeclarationAxiom msm = factory.getOWLDeclarationAxiom(msmClass);
    	OWLDeclarationAxiom partOfFusion = factory.getOWLDeclarationAxiom(partOfFusionClass);
    	OWLDeclarationAxiom roleIn = factory.getOWLDeclarationAxiom(roleInClass);
    	OWLDeclarationAxiom hasLoc = factory.getOWLDeclarationAxiom(hasLocClass);
    	OWLDeclarationAxiom presentIn = factory.getOWLDeclarationAxiom(presentInClass);
    	OWLDeclarationAxiom partOf = factory.getOWLDeclarationAxiom(partOfClass);
    	
    	OWLAxiom lab = factory.getOWLAnnotationAssertionAxiom(groupPhenome.getIRI(), label);
    	OWLAxiom lab1 = factory.getOWLAnnotationAssertionAxiom(disease.getIRI(), label1);
    	OWLAxiom lab2 = factory.getOWLAnnotationAssertionAxiom(clinicalSub.getIRI(), label2);
    	OWLAxiom lab3 = factory.getOWLAnnotationAssertionAxiom(malformSyn.getIRI(), label3);
    	OWLAxiom lab4 = factory.getOWLAnnotationAssertionAxiom(etioSub.getIRI(), label4);
    	OWLAxiom lab5 = factory.getOWLAnnotationAssertionAxiom(morphAna.getIRI(), label5);
    	OWLAxiom lab6 = factory.getOWLAnnotationAssertionAxiom(clinicalSyn.getIRI(), label6);
    	OWLAxiom lab7 = factory.getOWLAnnotationAssertionAxiom(partClinSitu.getIRI(), label7);
    	OWLAxiom lab9 = factory.getOWLAnnotationAssertionAxiom(histoPathoSub.getIRI(), label9);
    	OWLAxiom lab0 = factory.getOWLAnnotationAssertionAxiom(bioAna.getIRI(), label0);
    	//=====Gene=====
    	OWLAxiom labGene = factory.getOWLAnnotationAssertionAxiom(gene.getIRI(), genelabel);
    	//Annotation A FAIRE
    	//==============
    	    	
    	
    	manager.applyChange(new AddAxiom(ontology, phenDec));
       	manager.applyChange(new AddAxiom(ontology, declarationAxiom));
       	manager.applyChange(new AddAxiom(ontology, declarationAxiom1));
       	manager.applyChange(new AddAxiom(ontology, declarationAxiom2));
       	manager.applyChange(new AddAxiom(ontology, declarationAxiom3));
       	manager.applyChange(new AddAxiom(ontology, declarationAxiom4));
       	manager.applyChange(new AddAxiom(ontology, declarationAxiom5));
       	manager.applyChange(new AddAxiom(ontology, declarationAxiom6));
       	manager.applyChange(new AddAxiom(ontology, declarationAxiom7));
       	manager.applyChange(new AddAxiom(ontology, declarationAxiom9));
       	manager.applyChange(new AddAxiom(ontology, declarationAxiom0));
       	
       	//=====Gene=====
       	manager.applyChange(new AddAxiom(ontology, declarationGene));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(geneTypProtProd, gene)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(geneTypDisAssLoc, gene)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(geneTypNonCodingRNA, gene)));
       	
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(geneTypProtProd.getIRI(), geneTypProtProdLabel)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(geneTypDisAssLoc.getIRI(), geneTypDisAssLocLabel)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(geneTypNonCodingRNA.getIRI(), geneTypNonCodingRNALabel)));
      //================
       	
    	manager.applyChange(new AddAxiom(ontology, obsolete));
       	manager.applyChange(new AddAxiom(ontology, inheritance0));
       	manager.applyChange(new AddAxiom(ontology, inheritance1));
       	//manager.applyChange(new AddAxiom(ontology, inheritance2));
       	//manager.applyChange(new AddAxiom(ontology, inheritance3));
       	//manager.applyChange(new AddAxiom(ontology, inheritance4));
       	//manager.applyChange(new AddAxiom(ontology, inheritance5));
       	//manager.applyChange(new AddAxiom(ontology, inheritance6));
       	//manager.applyChange(new AddAxiom(ontology, inheritance7));

       	manager.applyChange(new AddAxiom(ontology, inheritance8));
       	manager.applyChange(new AddAxiom(ontology, inheritance9));
       	manager.applyChange(new AddAxiom(ontology, inheritance10));
       	manager.applyChange(new AddAxiom(ontology, inheritance11));
       	manager.applyChange(new AddAxiom(ontology, inheritance12));
       	manager.applyChange(new AddAxiom(ontology, inheritance13));
       	manager.applyChange(new AddAxiom(ontology, inheritance14));
       	manager.applyChange(new AddAxiom(ontology, inheritance15));
       	manager.applyChange(new AddAxiom(ontology, inheritance16));
       	manager.applyChange(new AddAxiom(ontology, inheritance17));
       	manager.applyChange(new AddAxiom(ontology, inheritance18));
       	
       	//UPDATE SD
       	manager.applyChange(new AddAxiom(ontology, curator));
       	manager.applyChange(new AddAxiom(ontology, assertion));
       	
       	manager.applyChange(new AddAxiom(ontology, lab));
       	manager.applyChange(new AddAxiom(ontology, lab1));
       	manager.applyChange(new AddAxiom(ontology, lab2));
       	manager.applyChange(new AddAxiom(ontology, lab3));
       	manager.applyChange(new AddAxiom(ontology, lab4));
       	manager.applyChange(new AddAxiom(ontology, lab5));
       	manager.applyChange(new AddAxiom(ontology, lab6));
       	manager.applyChange(new AddAxiom(ontology, lab7));
       	manager.applyChange(new AddAxiom(ontology, lab9));
       	manager.applyChange(new AddAxiom(ontology, lab0));
       	manager.applyChange(new AddAxiom(ontology, labGene));
       	manager.applyChange(new AddAxiom(ontology, ageofOn));
    	manager.applyChange(new AddAxiom(ontology, prev));
    	// ajout point preval etc
    	manager.applyChange(new AddAxiom(ontology, pointPrev));
    	manager.applyChange(new AddAxiom(ontology, birthPrev));
    	manager.applyChange(new AddAxiom(ontology, lifePrev));
    	
    	manager.applyChange(new AddAxiom(ontology, geography));
    	manager.applyChange(new AddAxiom(ontology, epidemiology));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(phenome.getIRI(), phenomelabel)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(obsoleteClass.getIRI(), obsoleteLabel)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(inheritance.getIRI(), inheritancelabel)));
       	//manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(autoDom.getIRI(), autoDomLab)));
       	//manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(mitoInher.getIRI(), mitoInherLab)));
       	//manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(multigen.getIRI(), multigenLab)));
       	//manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(xRecess.getIRI(), xRecessLab)));
       	//manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(xDom.getIRI(), xDomLab)));
       	//manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(sporadic.getIRI(), sporadicLab)));
       	
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(semiDom.getIRI(), semiDomLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(autoDominant.getIRI(), autoDominantLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(autoRecessive.getIRI(), autoRecessiveLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(multiGene.getIRI(), multiGeneLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(xlinkedRecessive.getIRI(), xlinkedRecessiveLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(xlinkedDominant.getIRI(), xlinkedDominantLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(oligenic.getIRI(), oligenicLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(yLinked.getIRI(), yLinkedLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(unknown.getIRI(), unknownLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(nodata.getIRI(), noDataLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(notApplicable.getIRI(), notApplicableLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(mitho.getIRI(), mithoLab)));
       	
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(ageofOnsetClass.getIRI(), ageOfOnLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(prevClass.getIRI(), prevLab)));
    	// ajout point preval etc
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(pointPrevClass.getIRI(), pointPrevLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(birthPrevClass.getIRI(), birthPrevLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(lifePrevClass.getIRI(), lifePrevLab)));
    	    	
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(geographyClass.getIRI(), geographyLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(epidemioClass.getIRI(), epidemioLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(casFamClass.getIRI(), casFamLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(incidenceClass.getIRI(), incidenceLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(casClass.getIRI(), casLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(famClass.getIRI(), famLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(semiDom, inheritance)));
    	//manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(autoDom, inheritance)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(mitho, inheritance)));
    	//manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(multigen, inheritance)));
    	//manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(xRecess, inheritance)));
    	//manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(xDom, inheritance)));
    	//manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(sporadic, inheritance)));
    	
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(autoDominant, inheritance)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(autoRecessive, inheritance)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(multiGene, inheritance)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(xlinkedRecessive, inheritance)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(xlinkedDominant, inheritance)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(oligenic, inheritance)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(yLinked, inheritance)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(unknown, inheritance)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(nodata, inheritance)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(notApplicable, inheritance)));
    	
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(groupPhenome, phenome)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(disease, phenome)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(clinicalSub, phenome)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(malformSyn, phenome)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(etioSub, phenome)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(morphAna, phenome)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(clinicalSyn, phenome)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(partClinSitu, phenome)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(histoPathoSub, phenome)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(bioAna, phenome)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(prevClass, epidemioClass)));
    	// ajout point preval etc
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(pointPrevClass, prevClass)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(birthPrevClass, prevClass)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(lifePrevClass, prevClass)));

    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(incidenceClass, epidemioClass)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(casFamClass, epidemioClass)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(casClass, casFamClass)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(famClass, casFamClass)));
    	
    	// UPDATE SD 4/01/2015
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(assertionClass.getIRI(), assertionLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(curatorClass.getIRI(), curatorLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(disGermMutClass.getIRI(), disGermMutLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(cgtClass.getIRI(), cgtLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(dgmgfClass.getIRI(), dgmgfLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(dgmlfClass.getIRI(), dgmlfLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(dgsClass.getIRI(), dgsLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(majSusClass.getIRI(), majSusLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(mgClass.getIRI(), mgLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(msmClass.getIRI(), msmLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(partOfFusionClass.getIRI(), partOfFusionLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(roleInClass.getIRI(), roleInLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(hasLocClass.getIRI(), hasLocLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(presentInClass.getIRI(), presentInLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(partOfClass.getIRI(), partOfLab)));
    	
    	// UPDATE SD Definitions 
		PrefixManager pm2 = new DefaultPrefixManager("http://www.ebi.ac.uk/efo/");		
		
		// obsolete_class  ObsoleteClass
		OWLAnnotation obsoleteDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("ObsoleteClass"),getLang()));
		OWLAxiom obsoleteDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(obsoleteClass.getIRI(), obsoleteDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), obsoleteDefine));
		
		// epidemio C003
		OWLAnnotation epidemioDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("C003"),getLang()));
		OWLAxiom epidemioDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(epidemioClass.getIRI(), epidemioDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), epidemioDefine));
		// geo    C009
		OWLAnnotation geographyDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("C009"),getLang()));
		OWLAxiom geographyDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(geographyClass.getIRI(), geographyDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), geographyDefine));
		// Cas/familles    409970
		OWLAnnotation casFamDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409970"),getLang()));
		OWLAxiom casFamDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(casFamClass.getIRI(), casFamDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), casFamDefine));
		// Cas          409973
		OWLAnnotation casDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409973"),getLang()));
		OWLAxiom casDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(casClass.getIRI(), casDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), casDefine));
		// Famille          409974
		OWLAnnotation famDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409974"),getLang()));
		OWLAxiom famDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(famClass.getIRI(), famDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), famDefine));

		// prevalence        C004
		OWLAnnotation prevalDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("C004"),getLang()));
		OWLAxiom prevalDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(prevClass.getIRI(), prevalDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), prevalDefine));

		// incidence        409967
		OWLAnnotation incidenceDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409967"),getLang()));
		OWLAxiom incidenceDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(incidenceClass.getIRI(), incidenceDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), incidenceDefine));
		
		// ajout point preval etc

		// point prevalence             409966
		OWLAnnotation pointPrevalDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409966"),getLang()));
		OWLAxiom pointPrevalDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(pointPrevClass.getIRI(), pointPrevalDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), pointPrevalDefine));
		
		// birth prevalence            409968
		OWLAnnotation birthPrevalDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409968"),getLang()));
		OWLAxiom birthPrevalDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(birthPrevClass.getIRI(), birthPrevalDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), birthPrevalDefine));
		
		// lifetime prevalence          409969
		OWLAnnotation lifePrevalDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409969"),getLang()));
		OWLAxiom lifePrevalDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(lifePrevClass.getIRI(), lifePrevalDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), lifePrevalDefine));
		
		
		// age of onset          C023
		OWLAnnotation ageOfOnsetDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("C023"),getLang()));
		OWLAxiom ageOfOnsetDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(ageofOnsetClass.getIRI(), ageOfOnsetDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), ageOfOnsetDefine));
		
		// inheritance         C005
		OWLAnnotation inheritanceDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("C005"),getLang()));
		OWLAxiom inheritanceDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(inheritance.getIRI(), inheritanceDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), inheritanceDefine));
		
		// Phenome C001
		OWLAnnotation phenomeDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("C001"),getLang()));
		OWLAxiom phenomeDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(phenome.getIRI(), phenomeDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), phenomeDefine));
		
		// biological anomaly 377790
		OWLAnnotation bioAnaDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("377790"),getLang()));
		OWLAxiom bioAnaDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(bioAna.getIRI(), bioAnaDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), bioAnaDefine));
		
		// clinical subtype 377796
		OWLAnnotation clinicalSubDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("377796"),getLang()));
		OWLAxiom clinicalSubDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(clinicalSub.getIRI(), clinicalSubDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), clinicalSubDefine));
		
		// clinical syndrome 377792
		OWLAnnotation clinicalSynDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("377792"),getLang()));
		OWLAxiom clinicalSynDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(clinicalSyn.getIRI(),clinicalSynDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), clinicalSynDefine));
		
		// disease 377788
		OWLAnnotation diseaseDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("377788"),getLang()));
		OWLAxiom diseaseDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(disease.getIRI(), diseaseDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), diseaseDefine));
		
		// etiological subtype 377795
		OWLAnnotation etioSubDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("377795"),getLang()));
		OWLAxiom etioSubDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(etioSub.getIRI(), etioSubDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), etioSubDefine));	
				
		// groupe of disorders 377794
		OWLAnnotation groupPhenomeDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("377794"),getLang()));
		OWLAxiom groupPhenomeDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(groupPhenome.getIRI(), groupPhenomeDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), groupPhenomeDefine));
		
		// histopathological subtype 377797
		OWLAnnotation histoPathoSubDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("377797"),getLang()));
		OWLAxiom histoPathoSubDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(histoPathoSub.getIRI(),histoPathoSubDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), histoPathoSubDefine));
		
		// malformation syndrome 377789
		OWLAnnotation malformSynDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("377789"),getLang()));
		OWLAxiom malformSynDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(malformSyn.getIRI(), malformSynDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), malformSynDefine));
		
		// particular clinical situation in a disease or syndrome 377793
		OWLAnnotation partClinSituDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("377793"),getLang()));
		OWLAxiom partClinSituDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(partClinSitu.getIRI(), partClinSituDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), partClinSituDefine));	
		
		// Morphological anomaly 377791
		OWLAnnotation morphAnaDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("377791"),getLang()));
		OWLAxiom morphAnaDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(morphAna.getIRI(), morphAnaDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), morphAnaDefine));
		
		/* *** UPDATE SD : Inheritance  *** */
		
		// autosomal recessive    409930
		OWLAnnotation autoRecDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409930"),getLang()));
		OWLAxiom autoRecDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(autoRecessive.getIRI(), autoRecDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), autoRecDefine));
		
		// autosomal dominant         409929
		OWLAnnotation autoDomDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409929"),getLang()));
		OWLAxiom autoDomDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(autoDominant.getIRI(), autoDomDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), autoDomDefine));
		
		// multigenic/multifactorial        409931
		OWLAnnotation multigenDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409931"),getLang()));
		OWLAxiom multigenDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(multiGene.getIRI(), multigenDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), multigenDefine));
		
		// x linked recessive         409932
		OWLAnnotation xlinkedRecDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409932"),getLang()));
		OWLAxiom xlinkedRecDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(xlinkedRecessive.getIRI(), xlinkedRecDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), xlinkedRecDefine));
		
		// mitochondrial        409933
		OWLAnnotation mithoDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409933"),getLang()));
		OWLAxiom mithoDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(mitho.getIRI(), mithoDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), mithoDefine));
		
		// x linked dominant       409934
		OWLAnnotation xlinkedDomDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409934"),getLang()));
		OWLAxiom xlinkedDomDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(xlinkedDominant.getIRI(), xlinkedDomDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), xlinkedDomDefine));
		
		//oligogenic          409936
		OWLAnnotation oligogenicDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409936"),getLang()));
		OWLAxiom oligogenicDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(oligenic.getIRI(), oligogenicDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), oligogenicDefine));
		
		// semi-dominant        409937
		OWLAnnotation semiDomDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409937"),getLang()));
		OWLAxiom semiDomDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(semiDom.getIRI(), semiDomDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), semiDomDefine));
		
		// y linked     409938
		OWLAnnotation yLinkedDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409938"),getLang()));
		OWLAxiom yLinkedDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(yLinked.getIRI(), yLinkedDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), yLinkedDefine));
		
		// no data available            409940
		OWLAnnotation noDataDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409940"),getLang()));
		OWLAxiom noDataDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(nodata.getIRI(), noDataDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), noDataDefine));
		
		// not applicable        409941
		OWLAnnotation notApplicDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409941"),getLang()));
		OWLAxiom notApplicDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(notApplicable.getIRI(), notApplicDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), notApplicDefine));
		
		// unknown         409939
		OWLAnnotation unknownDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("409939"),getLang()));
		OWLAxiom unknownDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(unknown.getIRI(), unknownDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), unknownDefine));
    		
		//========Gene====//
		// Type de g�ne-gene with protein product      410298
		OWLAnnotation geneTyp1Definition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("410298"),getLang()));
		OWLAxiom geneTyp1Define = owlvar.getFactory().getOWLAnnotationAssertionAxiom(geneTypProtProd.getIRI(), geneTyp1Definition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), geneTyp1Define));
		
		// Type de g�ne-disorder-associated locus         410297
		OWLAnnotation geneTyp2Definition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("410297"),getLang()));
		//OWLAxiom geneTypDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(geneTypProtProd.getIRI(), geneTyp2Definition);
		//owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), geneTypDefine));
		OWLAxiom geneTyp2Define = owlvar.getFactory().getOWLAnnotationAssertionAxiom(geneTypDisAssLoc.getIRI(), geneTyp2Definition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), geneTyp2Define));
		
		// Type de g�ne-non-coding RNA              410299
		OWLAnnotation geneTyp3Definition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("410299"),getLang()));
		OWLAxiom geneTyp3Define = owlvar.getFactory().getOWLAnnotationAssertionAxiom(geneTypNonCodingRNA.getIRI(), geneTyp3Definition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), geneTyp3Define));
		
		// genetic material          C010
		OWLAnnotation geneticMaterialDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("C010"),getLang()));
		OWLAxiom genMatDefinition = owlvar.getFactory().getOWLAnnotationAssertionAxiom(gene.getIRI(), geneticMaterialDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), genMatDefinition));
		//====================================//
		
    	// UPDATE SD 4/01/2015
    	// Disease-causing germline mutation(s) in          317343
		OWLAnnotation disGermMutDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("317343"),getLang()));
		OWLAxiom disGermMutDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(disGermMutClass.getIRI(), disGermMutDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), disGermMutDefine));
    	
		// Candidate gene tested in         327767
		OWLAnnotation cgtDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("327767"),getLang()));
		OWLAxiom cgtDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(cgtClass.getIRI(), cgtDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), cgtDefine));
		
		// Disease-causing germline mutation(s) (gain of function) in      410296
		OWLAnnotation dgmgfDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("410296"),getLang()));
		OWLAxiom dgmgfDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(dgmgfClass.getIRI(), dgmgfDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), dgmgfDefine));
		
		// Disease-causing germline mutation(s) (loss of function) in        410295
		OWLAnnotation dgmlfDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("410295"),getLang()));
		OWLAxiom dgmlfDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(dgmlfClass.getIRI(), dgmlfDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), dgmlfDefine));
		
		// Disease-causing somatic mutation(s) in          317344
		OWLAnnotation dgsDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("317344"),getLang()));
		OWLAxiom dgsDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(dgsClass.getIRI(), dgsDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), dgsDefine));
		
		// Major susceptibility factor in        317345
		OWLAnnotation majSusDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("317345"),getLang()));
		OWLAxiom majSusDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(majSusClass.getIRI(), majSusDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), majSusDefine));
		
		// Modifying germline mutation in         317346
		OWLAnnotation mgDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("317346"),getLang()));
		OWLAxiom mgDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(mgClass.getIRI(), mgDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), mgDefine));
		
		// Biomarker tested in          465410
		OWLAnnotation msmDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("465410"),getLang()));
		OWLAxiom msmDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(msmClass.getIRI(), msmDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), msmDefine));
		
		// Part of a fusion gene in         317348
		OWLAnnotation partOfFusionDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("317348"),getLang()));
		OWLAxiom partOfFusionDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(partOfFusionClass.getIRI(), partOfFusionDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), partOfFusionDefine));
		
		// Role in the phenotype of        317349
		OWLAnnotation roleInDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("317349"),getLang()));
		OWLAxiom roleInDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(roleInClass.getIRI(), roleInDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), roleInDefine));
		
		// has_chromosomal location         C040
		OWLAnnotation hasLocDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("C040"),getLang()));
		OWLAxiom hasLocDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(hasLocClass.getIRI(), hasLocDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), hasLocDefine));
		
		// present_in         C022
		OWLAnnotation presentInDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("C022"),getLang()));
		OWLAxiom presentInDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(presentInClass.getIRI(), presentInDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), presentInDefine));
		
		// part_of        C021
		OWLAnnotation partOfDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral(getConceptDefinition("C021"),getLang()));
		OWLAxiom partOfDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(partOfClass.getIRI(), partOfDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), partOfDefine));
		
		
		
    	//for the module ontology
        IRI ontologyIRImod = IRI.create("http://www.orpha.net/ontology/orphaEfoMod.owl");
        owlvar.setOntologyIRImod(ontologyIRImod);
        OWLOntology ontologymod = manager.createOntology(ontologyIRImod);
        owlvar.setOntologymod(ontologymod);
    	System.out.println("Created ontology2: " + ontologymod);
    	IRI versionIRImod = IRI.create("/version"+getVersion());
    	OWLOntologyID newOntologyIDmod = new OWLOntologyID(ontologyIRImod,versionIRImod);
    	SetOntologyID setOntologyIDmod = new SetOntologyID(ontologymod, newOntologyIDmod);
    	manager.applyChange(setOntologyIDmod);
     	
       } 
    
    private boolean checkParentDisease(){   // skip classif head
    	if (this.orphanum.contentEquals("165711") || this.orphanum.contentEquals("98050") || this.orphanum.contentEquals("93419") ||
    			this.orphanum.contentEquals("97929") || this.orphanum.contentEquals("98028") || this.orphanum.contentEquals("93890") ||
    			this.orphanum.contentEquals("97978") || this.orphanum.contentEquals("97966")|| this.orphanum.contentEquals("97935")||
    			this.orphanum.contentEquals("98053") || this.orphanum.contentEquals("96344")|| this.orphanum.contentEquals("97992") ||
    			this.orphanum.contentEquals("57146")|| this.orphanum.contentEquals("98004")|| this.orphanum.contentEquals("68416")||
    			this.orphanum.contentEquals("98047")|| this.orphanum.contentEquals("108999") || this.orphanum.contentEquals("68329")||
    			this.orphanum.contentEquals("98006")||this.orphanum.contentEquals("98026")|| this.orphanum.contentEquals("250908")||
    			this.orphanum.contentEquals("98036") ||this.orphanum.contentEquals("93626")||  this.orphanum.contentEquals("280342")||
    			this.orphanum.contentEquals("97955")|| this.orphanum.contentEquals("89826") || this.orphanum.contentEquals("97965")||
    			this.orphanum.contentEquals("97962") || this.orphanum.contentEquals("98023")||this.orphanum.contentEquals("101433") ||
    			this.orphanum.contentEquals("68367")|| this.orphanum.contentEquals("52662") || this.orphanum.contentEquals("93890") || 
    			this.orphanum.contentEquals("98053") || this.orphanum.contentEquals("138221") || this.orphanum.contentEquals("68335") || 
    			this.orphanum.contentEquals("506207") ){
    		return true;}
		return false;
    }
    
    
	public void createOWLFile(HashMap<String, RareDisease> disease_xrefs) throws OWLOntologyCreationException {
		writeToOWLFile();

		boolean flag = checkParentDisease();
		//addition of diseases as classes along with sub-class or part_of axiom 
		//remove those that are not head of classification and have no superclass
		//System.out.println("is the subclass list empty? " + isa_list.isEmpty() + "and the head flag is set to " + flag);
		//System.out.println("No a head and orphaned");
		if(this.isObsolete() || this.isMovedTo()  ){

			/** Cr�ation des obsolete */
			OWLClass rareDisorder = owlvar.getFactory().getOWLClass(this.orphanum, owlvar.getPrefixmanager());
			OWLAnnotation labelRare = owlvar.getFactory().getOWLAnnotation(
					owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.name,getLang()));
			OWLDeclarationAxiom declarationClass = owlvar.getFactory().getOWLDeclarationAxiom(rareDisorder);
			OWLAxiom labelling = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), labelRare);
			owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), declarationClass));
			owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), labelling));
			
			PrefixManager pmObso = new DefaultPrefixManager("http://www.orpha.net/ORDO/");				
			owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder, owlvar.getFactory().getOWLClass("ObsoleteClass",pmObso))));
			
			String ifMovedTo="";
			if(this.isMovedTo()){ifMovedTo=getConceptDefinition("deprecated") + " ";}
			else{ifMovedTo=getConceptDefinition("use") + " ";}
			
			String idObsoletFrom = this.getObsoleteFrom();
			if(idObsoletFrom!=null && disease_xrefs.get(idObsoletFrom)!=null){
				OWLAnnotation definition = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("reason_for_obsolescence", new DefaultPrefixManager("http://www.ebi.ac.uk/efo/")),
						owlvar.getFactory().getOWLLiteral(ifMovedTo+"http://www.orpha.net/ORDO/Orphanet_"+idObsoletFrom+" "+getConceptDefinition("with_label")+" "+disease_xrefs.get(idObsoletFrom).getName(),getLang()));
				OWLAxiom define = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), definition);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), define));
			}else if (idObsoletFrom!=null){
				System.out.println(idObsoletFrom+" : is null");
			}
		}
		else if(flag == false && this.isa_list.isEmpty()){
			//System.out.println("Not a head and orphaned : "+this.orphanum);
		}
		else{
			//System.out.println("entered the disease loop");
		//if(this.get_orphanum().equals("953")){System.err.println("FOUND 953");}
		OWLClass rareDisorder = owlvar.getFactory().getOWLClass(this.orphanum, owlvar.getPrefixmanager());
		OWLAnnotation labelRare = owlvar.getFactory().getOWLAnnotation(
		owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.name,getLang()));
		if (this.diseaseType != "108939" && this.diseaseType != null && this.diseaseType != ""){//that is if disease is no type dont include in the ontology 
			OWLDeclarationAxiom declarationClass = owlvar.getFactory().getOWLDeclarationAxiom(rareDisorder);
			OWLAxiom labelling = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), labelRare);
			owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), declarationClass));
			owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), labelling));
			//System.out.println("I am here");
			if (this.diseaseType.equalsIgnoreCase("377794")){//If disease is a group of Phenome then its a subclass relation
				//System.out.println("entered the group of phenome loop");
				OWLClass classType = owlvar.getFactory().getOWLClass(this.diseaseType, owlvar.getPrefixmanager());
				PrefixManager pm3 = new DefaultPrefixManager("http://purl.obolibrary.org/obo/");
				OWLAnnotation manualAssert = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("ECO_0000218",pm3),owlvar.getFactory().getOWLLiteral(this.disTypeValidity,getLang()));
				Set<OWLAnnotation> owlAnn = new HashSet<OWLAnnotation>();
				owlAnn.add(manualAssert);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder, classType,owlAnn)));
				//if (!isa_list.isEmpty()){
					
				for ( int i= 0; i<isa_list.size(); i++){
					
					OWLClass superClass = owlvar.getFactory().getOWLClass(this.isa_list.get(i), owlvar.getPrefixmanager());
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder, superClass)));
						}
					}
			else {	
				//System.out.println("entered the non-group of phenome loop");
				OWLClass classType = owlvar.getFactory().getOWLClass(this.diseaseType, owlvar.getPrefixmanager());
				PrefixManager pm3 = new DefaultPrefixManager("http://purl.obolibrary.org/obo/");
				//System.out.println("Name: "+this.name);// test pour voir le contenu de this
				//System.out.println("Type: "+this.disTypeValidity);// test pour voir le contenu de this
				OWLAnnotation manualAssert = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("ECO_0000218",pm3),owlvar.getFactory().getOWLLiteral(this.disTypeValidity,getLang()));
				Set<OWLAnnotation> owlAnn = new HashSet<OWLAnnotation>();
				owlAnn.add(manualAssert);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder, classType,owlAnn)));
				//if (!isa_list.isEmpty()){
					
				for ( int i= 0; i<isa_list.size(); i++){
					String currentDiseaseType =this.diseaseType;
					//System.out.println("the current object is ");
					//referring to the superclass object 
					RareDisease supClass = disease_xrefs.get(this.isa_list.get(i));					
					String supDiseaseType = supClass.getdiseaseType();
					//System.out.println("the super class disease type is " +supDiseaseType);
					//if both have the same disease type then is_a relationship
					if(supDiseaseType !=null && supDiseaseType.contentEquals(currentDiseaseType)){
						OWLClass superClass = owlvar.getFactory().getOWLClass(this.isa_list.get(i), owlvar.getPrefixmanager());
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder, superClass)));
					}else{
						PrefixManager pm2 = new DefaultPrefixManager("http://purl.obolibrary.org/obo/");
						OWLObjectProperty partOf = owlvar.getFactory().getOWLObjectProperty("BFO_0000050", pm2);
						OWLAnnotation partOfLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(getConceptLabel("C021"),getLang()));
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(partOf.getIRI(), partOfLab)));
						OWLClass superClass = owlvar.getFactory().getOWLClass(this.isa_list.get(i), owlvar.getPrefixmanager());
						OWLClassExpression partOfSuperClass = owlvar.getFactory().getOWLObjectSomeValuesFrom(partOf, superClass);
						OWLSubClassOfAxiom ax = owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder, partOfSuperClass);
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), ax));
					}
				}
			}			
		// SD add ORPHA:XXX
		OWLAnnotation orphaLab = owlvar.getFactory().getOWLAnnotation(
					owlvar.getFactory().getOWLAnnotationProperty("notation", new DefaultPrefixManager("http://www.w3.org/2004/02/skos/core#")),
					owlvar.getFactory().getOWLLiteral("ORPHA:"+this.orphanum));
		OWLAxiom orpha = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), orphaLab);

		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), orpha));
		PrefixManager pm2 = new DefaultPrefixManager("http://www.ebi.ac.uk/efo/");
		
		//System.out.println("exited the disease loop starting inheritance Inheritance size is  " + this.inheritNum.size());
		//type of inheritance for that disease
		 if (!(this.inheritNum.isEmpty())){
			//System.out.println("writing the inheritance");
			for (int i= 0; i< this.inheritNum.size(); i++){
				if (!inheritNum.get(i).contentEquals("108939") &&! inheritNum.get(i).contentEquals("108940")){//that is if inheritance is not Unknown
				OWLObjectProperty has_inheritance = owlvar.getFactory().getOWLObjectProperty("C016", owlvar.getPrefixmanager());
				OWLAnnotation hasInheritLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(getConceptLabel("C016"),getLang()));
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_inheritance.getIRI(), hasInheritLab)));
				OWLClass inheritanceType  = owlvar.getFactory().getOWLClass(this.inheritNum.get(i),owlvar.getPrefixmanager());
				OWLClassExpression hasInheritanceClass = owlvar.getFactory().getOWLObjectSomeValuesFrom(has_inheritance, inheritanceType);
				
				//SD 22/06/18
				OWLAnnotation hasInheritDef =  owlvar.getFactory().getOWLAnnotation( owlvar.getFactory().getOWLAnnotationProperty("definition", pm2),
						owlvar.getFactory().getOWLLiteral(getConceptDefinition("C016"),getLang()));
				OWLAxiom defineHasInherit = owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_inheritance.getIRI(), hasInheritDef);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), defineHasInherit));
				
				OWLSubClassOfAxiom ax = owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder,hasInheritanceClass);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), ax));
				}
			}
		}
		
		//age of onset
		 if (this.onsetNum != null){
			//System.out.println("writing the age of onset");
			 for(int i=0;i<this.ageOfOnset.size();i++){
				OWLObjectProperty has_ageOfOnset = owlvar.getFactory().getOWLObjectProperty("C017", owlvar.getPrefixmanager());
				OWLAnnotation hasageOfOnLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(getConceptLabel("C017"),getLang()));
				
				OWLAnnotation onsetDef =  owlvar.getFactory().getOWLAnnotation( owlvar.getFactory().getOWLAnnotationProperty("definition", pm2),
						owlvar.getFactory().getOWLLiteral(getConceptDefinition("C017"),getLang()));
				OWLAxiom onsetDefin = owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_ageOfOnset.getIRI(), onsetDef);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), onsetDefin));

				
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_ageOfOnset.getIRI(), hasageOfOnLab)));
				OWLClass onsetValue = owlvar.getFactory().getOWLClass(this.onsetNum.get(i),owlvar.getPrefixmanager());
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLDeclarationAxiom(onsetValue)));//asserting age class
				//set the subclass relationship
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLSubClassOfAxiom(onsetValue, owlvar.getFactory().getOWLClass("C023", owlvar.getPrefixmanager()))));
				//OWLAnnotation onsetLabel = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.ageOfOnset.get(i),"fr"));//label for the value of age of onset
				
				// SD 09/08/18 multi lang 
				OWLAnnotation onsetLabel;
				onsetLabel = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(getConceptLabel(this.onsetNum.get(i)),getLang()));//no data avaible label
				
				/*if(this.onsetNum.get(i).equals("409951")){
					onsetLabel = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral("pas de donn�e disponible pour l'�ge d'apparition","fr"));//no data avaible label
				}else{
					onsetLabel = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.ageOfOnset.get(i).toLowerCase(),"fr"));//label for the value of age of onset
				}*/
				
				OWLClassExpression hasageOfOnsetClass = owlvar.getFactory().getOWLObjectSomeValuesFrom(has_ageOfOnset, onsetValue);
				OWLSubClassOfAxiom ax = owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder,hasageOfOnsetClass);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), ax));
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(onsetValue.getIRI(), onsetLabel)));
				
				/* *** UPDATE SD : Age of onset  *** */
				// SD 09/08/18 multi lang 
				/*String def="";
				if(this.onsetNum.get(i).equals("409943")){def="Avant la naissance.";}                                           //antenatal
				else if(this.onsetNum.get(i).equals("409944")){def="De la naissance � la quatri�me semaine de vie.";}           //neonatal
				else if(this.onsetNum.get(i).equals("409945")){def="De la quatri�me semaine r�volue au 23�me mois de vie.";}    //infancy
				else if(this.onsetNum.get(i).equals("409946")){def="De 2 � 11 ans.";}                                           //childhood
				else if(this.onsetNum.get(i).equals("409947")){def="De 12 � 18 ans.";}                                          //adolescent
				else if(this.onsetNum.get(i).equals("409948")){def="De 19 � 65 ans.";}                                          //adult
				else if(this.onsetNum.get(i).equals("409949")){def="Apr�s 65 ans.";}                                            //eldery
				else if(this.onsetNum.get(i).equals("409950")){def="De la naissance � l'�ge adulte sans pic d'apparition.";}    //all age
				else if(this.onsetNum.get(i).equals("409951")){def="Aucune information n'est disponible dans la litt�rature scientifique sur l'�ge d'apparition des premi�res manifestations cliniques.";} //no data avaible
				
				OWLAnnotation onsetDefinition =  owlvar.getFactory().getOWLAnnotation( owlvar.getFactory().getOWLAnnotationProperty("definition", pm2),
						owlvar.getFactory().getOWLLiteral(def,"fr"));
						*/
				OWLAnnotation onsetDefinition =  owlvar.getFactory().getOWLAnnotation( owlvar.getFactory().getOWLAnnotationProperty("definition", pm2),
						owlvar.getFactory().getOWLLiteral(getConceptDefinition(this.onsetNum.get(i)),getLang()));
				OWLAxiom onsetDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(onsetValue.getIRI(), onsetDefinition);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), onsetDefine));

			 }
		}
		 
		
		// UPDATE SD Prevalence class, prevalence value, prevalence type and prevalence loc

		if (!this.prevalences.isEmpty()){
			//System.out.println("writing the prevelances");

			
			for(int i=0;i<this.prevalences.size();i++){
				Set<OWLClassExpression> intersec = new HashSet<OWLClassExpression> (); // set d'info sur la pr�valence en cours
				String type=this.prevalences.get(i).getType();                         // type de pr�valence
				
				if (this.prevalences.get(i).getPrevalClass()!=null && !this.prevalences.get(i).getPrevalClass().equals("") 
						&& !this.prevalences.get(i).getPrevalClass().equals("409982")){
					/* UPDATE s�lection du typage de classe */
					if (type.equals("409966") ){ type = "C025";      // prevalence point
					}else if (type.equals("409968") ){type = "C026"; // prevalence at birth
													  //this.prevalences.get(i).setTypeLab(this.prevalences.get(i).getTypeLab().toLowerCase().replaceAll("prevalence at birth","Pr�valence � la naissance"));
					}else if (type.equals("409969") ){type = "C027"; // lifetime prevalence		
					}else if (type.equals("409967") ){type = "C020"; // annual incidence
					}else if (type.equals("409973") ){type = "C024"; // cases
					}else if (type.equals("409974") ){type = "C024"; // families
					}
					
					// UPDATE SD create has_.... anonymous class
					OWLObjectProperty has_Prevalence = owlvar.getFactory().getOWLObjectProperty(type,owlvar.getPrefixmanager());
					
					if(this.prevalences.get(i).getTypeLab()!=null && !this.prevalences.get(i).getTypeLab().equals("")){
						OWLAnnotation hasPrevLab;
						/*if(type == "C020"){
							hasPrevLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral("a_une_tranche_d_"+this.prevalences.get(i).getTypeLab().toLowerCase().replaceAll(" ","_"),"fr"));
						}else{
							hasPrevLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral("a_une_tranche_de_"+this.prevalences.get(i).getTypeLab().toLowerCase().replaceAll(" ","_"),"fr"));
						}*/
						hasPrevLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(getConceptLabel(type),getLang()));
						
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_Prevalence.getIRI(), hasPrevLab)));
						
						// UPDATE SD ajout def sur les propri�t� has_...
						/*String defHasPrev="";
						if (type.equals("C025") ){      defHasPrev = "Relation entre l'entit� clinique et l'intervalle de pr�valence instantan�e.";      // prevalence point
						}else if (type.equals("C026") ){defHasPrev = "Relation entre l'entit� clinique et l'intervalle de pr�valence � la naissance.";   // prevalence at birth
						}else if (type.equals("C027") ){defHasPrev = "Relation entre l'entit� clinique et l'intervalle de pr�valence vie-enti�re.";      // lifetime prevalence		
						}else if (type.equals("C020") ){defHasPrev = "Relation entre l'entit� clinique et l'intervalle d'incidence annuelle.";           // annual incidence
						// SD 18/04/18 
						}else if (type.equals("C024") ){defHasPrev = "Relation entre l'entit� clinique et le nombre de cas/familles.";    // cases/families
						}
						*/
						OWLAnnotation hasPrevDefinition = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("definition", pm2),
								owlvar.getFactory().getOWLLiteral(getConceptDefinition(type),getLang()));
						OWLAxiom prevalTypeDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_Prevalence.getIRI(), hasPrevDefinition);
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), prevalTypeDefine));
						// FIN UPDATE
					}
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLDeclarationAxiom(has_Prevalence)));//asserting prevalence class
					
					
					
					// UPDATE SD add the type of prevalence
					OWLClass prevType = owlvar.getFactory().getOWLClass(this.prevalences.get(i).getType(),owlvar.getPrefixmanager());
					String defType="";
					// d�finition et super classe
					/*if (type.equals("C020")){
						defType="Nombre de cas nouvellement diagnostiqu�s au sein de la population pendant 1 ann�e.";
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLSubClassOfAxiom(prevType, owlvar.getFactory().getOWLClass("C003", owlvar.getPrefixmanager()))));
					}/*else{
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLSubClassOfAxiom(prevType, owlvar.getFactory().getOWLClass("C004", owlvar.getPrefixmanager()))));
						if (type.equals("C025")       ){ defType = "Le nombre d'individus atteints de la maladie dans une population donn�e � un moment donn�.";      // prevalence point
						}else if (type.equals("C026") ){ defType = "Le nombre de nourrissons n�s avec la maladie par rapport au nombre total de naissances vivantes dans une p�riode de temps donn�e."; // prevalence at birth
						}else if (type.equals("C027") ){ defType = "Le nombre d'individus dans une population qui, � un certain moment de leur vie, ont pr�sent� la maladie, par rapport au nombre total d'individus."; // lifetime prevalence						
						}
					}*/
					// UPDATE SD application d�finition
										// SD 22/06/18 test modif fonctionnement
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLDeclarationAxiom(prevType)));//asserting prevalence Type
					if(defType!=""){
						
						OWLAnnotation prevalTypeDefinition = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("definition", pm2),
								owlvar.getFactory().getOWLLiteral(getConceptDefinition("C020"),getLang()));
						OWLAxiom prevalTypeDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(prevType.getIRI(), prevalTypeDefinition);
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), prevalTypeDefine));

						// Ajout du label
						OWLAnnotation prevalTypeLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(getConceptLabel("C020"),getLang()));
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(prevType.getIRI(), prevalTypeLab)));
						
					}	
					
					// UPDATE SD add the Class of prevalence
					OWLClass prevClass = owlvar.getFactory().getOWLClass(this.prevalences.get(i).getPrevalClass(),owlvar.getPrefixmanager());
					
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLSubClassOfAxiom(prevClass, prevType)));
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLDeclarationAxiom(prevClass)));//asserting prevalence class
					
		
					OWLClassExpression hasPrevalenceClass = owlvar.getFactory().getOWLObjectSomeValuesFrom(has_Prevalence, prevClass); 
					intersec.add(hasPrevalenceClass);
					/*String def="";
					String label="";
					if(this.prevalences.get(i).getPrevalClass().equals("409975")){
						def="Intervalle de pr�valence ou incidence annuelle comprise entre 1 et 5 cas pour 10 000  au sein de la population.";
						label="1-5 / 10 000";
					}
					else if(this.prevalences.get(i).getPrevalClass().equals("409976")){
						def="Intervalle de pr�valence ou incidence annuelle comprise entre 6 et 9 cas pour 1 000 000  au sein de la population.";
						label="1-9 / 1 000 000";
					}
					else if(this.prevalences.get(i).getPrevalClass().equals("409977")){
						def="Intervalle de pr�valence ou incidence annuelle est comprise entre 1 et 9 cas pour 100 000  au sein de la population.";
						label="1-9 / 100 000";
					}
					else if(this.prevalences.get(i).getPrevalClass().equals("409978")){
						def="Intervalle de pr�valence ou incidence annuelle comprise entre 6 et 9 cas pour 10 000  au sein de la population.";
						label="6-9 / 10 000";
					}
					else if(this.prevalences.get(i).getPrevalClass().equals("409979")){
						def="Intervalle de pr�valence ou incidence annuelle inf�rieure � 1 cas pour 1 000 000  au sein de la population.";
						label="<1 / 1 000 000";
					}
					else if(this.prevalences.get(i).getPrevalClass().equals("409980")){
						def="Intervalle de pr�valence ou incidence annuelle sup�rieure � 1 cas pour 1 000 au sein de la population.";
						label=">1 / 1000";
					}
					else if(this.prevalences.get(i).getPrevalClass().equals("409981")){
						def="Absence d'information dans la litt�rature scientifique permettant de renseigner la pr�valence ou l'incidence annuelle.";
						label="Tranche_�pid�miologique_Inconnue";
					}*/
					
					//label for prevalence class
					OWLAnnotation prevalClassLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(getConceptLabel(this.prevalences.get(i).getPrevalClass()),getLang()));
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(prevClass.getIRI(), prevalClassLab)));
					
					//definition for prevalence class
					OWLAnnotation prevalClassDefinition = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("definition", pm2),
							owlvar.getFactory().getOWLLiteral(getConceptDefinition(this.prevalences.get(i).getPrevalClass()),getLang()));
					OWLAxiom prevalClassDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(prevClass.getIRI(), prevalClassDefinition);
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), prevalClassDefine));
					
					
				}
				
				type = this.prevalences.get(i).getType();
				/* UPDATE  pour g�rer les valeurs moyennes (0.0 exclu)*/
				if ( type != null && this.prevalences.get(i).getValMoy()!=null && !this.prevalences.get(i).getValMoy().equals("0.0") && !this.prevalences.get(i).getValMoy().equals("")){
					/* UPDATE s�lection du typage de Valeur moyenne */
					if (type.equals("409966") ){ type = "C028";      // prevalence point
					}else if (type.equals("409968") ){type = "C029"; // prevalence at birth
													  this.prevalences.get(i).setTypeLab(this.prevalences.get(i).getTypeLab().toLowerCase().replaceAll("prevalence at birth","birth prevalence"));
					}else if (type.equals("409969") ){type = "C030"; // lifetime prevalence				
					}else if (type.equals("409967") ){type = "C032"; // annual incidence
					}else if (type.equals("409974") ){type = "C024"; // Family
					}else if (type.equals("409973") ){type = "C024"; // Case
					}else{
						System.out.println("Type non-reconnu: "+type);
					}
					
					OWLObjectProperty has_Prevalence = owlvar.getFactory().getOWLObjectProperty(type, owlvar.getPrefixmanager());
					
					OWLAnnotation hasPrevLab;
					/*if(type=="C024"){
						hasPrevLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral("a_un_nombre_de_cas/familles","fr"));
					}else{
						hasPrevLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral("a_une_valeur_moyenne_de_"+this.prevalences.get(i).getTypeLab().toLowerCase().replaceAll(" ","_"),"fr"));
					}*/
					hasPrevLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(getConceptLabel(type),getLang()));
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_Prevalence.getIRI(), hasPrevLab)));
					OWLDataProperty prevalType = owlvar.getFactory().getOWLDataProperty(type, owlvar.getPrefixmanager());
					
					// UPDATE SD ajout def sur les propri�t� has_...
					String defAvgPrev="";
					/*if (type.equals("C028") ){      defAvgPrev = "Relation entre l'entit� clinique et la valeur moyenne de sa pr�valence instantan�e.";    // prevalence point
					}else if (type.equals("C029") ){defAvgPrev = "Relation entre l'entit� clinique et la valeur moyenne de sa pr�valence � la naissance.";    // prevalence at birth
					}else if (type.equals("C030") ){defAvgPrev = "Relation entre l'entit� clinique et la valeur moyenne de sa pr�valence vie-enti�re."; // lifetime prevalence		
					}else if (type.equals("C032") ){defAvgPrev = "Relation entre l'entit� clinique et la valeur moyenne de son incidence annuelle.";    // annual incidence
					}else if (type.equals("C024") ){defAvgPrev = "Relation entre l'entit� clinique et le nombre de cas/familles";    // cases/families
					/*}else if (type.equals("409974") ){defAvgPrev = "Nombre de familles publi�e(s) dans la litt�rature.";    // families
					}else if (type.equals("409973") ){defAvgPrev = "Nombre de cas publi�(s) dans la litt�rature.";    // cases 
					}*/
					
					
					OWLAnnotation hasAvgDefinition = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("definition", pm2),
							owlvar.getFactory().getOWLLiteral(getConceptDefinition(type),getLang()));
					OWLAxiom hasAvgDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_Prevalence.getIRI(), hasAvgDefinition);
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), hasAvgDefine));
					// FIN UPDATE
					
					OWLLiteral prevValue = owlvar.getFactory().getOWLLiteral(this.prevalences.get(i).getValMoy(), owlvar.getFactory().getFloatOWLDatatype());
					OWLDataHasValue hasPrevVal = owlvar.getFactory().getOWLDataHasValue(prevalType, prevValue);
					intersec.add(hasPrevVal);
					
					
					
					
				}
				
				/* UPDATE SD add G�o */
				if (this.prevalences.get(i).getGeo()!=null && !this.prevalences.get(i).getGeo().equals("")){
					
					OWLObjectProperty has_Geo = owlvar.getFactory().getOWLObjectProperty("C022",owlvar.getPrefixmanager());
					OWLAnnotation GeoLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(getConceptLabel("C022"),getLang()));
					
					OWLAnnotation hasGeoLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.prevalences.get(i).getGeoLab(),getLang()));
					
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_Geo.getIRI(), GeoLab)));
					OWLClass prevGeoClass = owlvar.getFactory().getOWLClass(this.prevalences.get(i).getGeo(),owlvar.getPrefixmanager());
					
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLDeclarationAxiom(prevGeoClass)));//asserting prevalence class
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLSubClassOfAxiom(prevGeoClass, owlvar.getFactory().getOWLClass("C009", owlvar.getPrefixmanager()))));
					//OWLAnnotation prevalenceLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.prevalences.get(i).getGeo()));//label for the value of prevalence
					OWLClassExpression hasPrevalenceClass = owlvar.getFactory().getOWLObjectSomeValuesFrom(has_Geo, prevGeoClass); // a transformer en hasvalue
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLAnnotationAssertionAxiom(prevGeoClass.getIRI(), hasGeoLab)));
					intersec.add(hasPrevalenceClass);
				}

				// UPDATE SD faire l'intersection des 3 contraintes pour en faire une classe anonyme.
				OWLClassExpression diseaseHasPrevVal = owlvar.getFactory().getOWLObjectIntersectionOf(intersec);
				OWLSubClassOfAxiom ax = owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder,diseaseHasPrevVal);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), ax));

				// UPDATE SD validation status for prevalence
				if(this.prevalences.get(i).getValidation()!=null){
					PrefixManager pm3 = new DefaultPrefixManager("http://purl.obolibrary.org/obo/");
					OWLAnnotation manualAssert = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("ECO_0000205",pm3),owlvar.getFactory().getOWLLiteral(this.prevalences.get(i).getValidation(),getLang()));
					Set<OWLAnnotation> owlAnn = new HashSet<OWLAnnotation>();
					owlAnn.add(manualAssert);
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder, diseaseHasPrevVal,owlAnn)));
				}
				
			}

		}

		
		//def for the disease if any
		 if (this.def != null) {
			
			OWLAnnotation definition = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
					.getOWLAnnotationProperty(
							"definition", pm2),
							owlvar.getFactory().getOWLLiteral(this.def,getLang()));
			OWLAxiom define = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), definition);
			OWLAnnotation definitionCitation = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
					.getOWLAnnotationProperty(
							"definition_citation",
							pm2),owlvar.getFactory().getOWLLiteral("orphanet",getLang()));
			OWLAxiom defineCite = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), definitionCitation);
			owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), define));
			owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), defineCite));
		}
		//synonyms for the disease
		 if (! this.synonym.isEmpty()){
			for( int i=0; i< this.synonym.size(); i++){
				//PrefixManager pm2 = new DefaultPrefixManager("http://www.ebi.ac.uk/efo/");
				OWLAnnotation alternativeTerm = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
						.getOWLAnnotationProperty(
								"alternative_term",
								pm2), owlvar.getFactory().getOWLLiteral(this.synonym.get(i),getLang()));
				OWLAxiom synonym = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), alternativeTerm);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),synonym));
			}
		}
		
		//xrefs for the disease
		 if (!this.sourceList.isEmpty()){
			for (int i=0; i<this.sourceList.size(); i++){
				PrefixManager oboInOwl = new DefaultPrefixManager("http://www.geneontology.org/formats/oboInOwl#");
				PrefixManager obo  = new DefaultPrefixManager("http://purl.obolibrary.org/obo/");
				OWLAnnotation database_cross_reference = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
						.getOWLAnnotationProperty(
								"hasDbXref",
								oboInOwl), owlvar.getFactory().getOWLLiteral(this.sourceList.get(i) + ":" + this.refList.get(i)));
				OWLAxiom xref = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), database_cross_reference);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), xref));
				//UPDATE SD mapping relation
				Set<OWLAnnotation> ref = new HashSet<OWLAnnotation>();
				ref.add(database_cross_reference);
									
				if(!this.refValidList.get(i).equals("")){
					
					OWLAnnotation curationAssertion = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("ECO_0000218",obo),owlvar.getFactory().getOWLLiteral(this.refValidList.get(i),getLang()));
					Set<OWLAnnotation> owlAnnoCur = new HashSet<OWLAnnotation>();

					owlAnnoCur.add(curationAssertion);						
					OWLAxiom xref2 = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), database_cross_reference,owlAnnoCur);
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), xref2));
				}
				// FIN UPDATE
			}
		
		}
	 
		 
		 
		//gene class and its annotations
		int start = 0;
		int start1 = 0;
		int start2 = 0;//ajout pour la gestion des locus
		
		if(! this.genelists.isEmpty()){
			//System.out.println("Genes*********: "+this.genelists.toString());
			//System.out.println("Smb********: "+this.symbol.toString());
			//System.out.println("Type*****: "+this.geneTypeName.toString());
			//System.out.println("GeneType*****: "+this.geneTyp.toString());
			//System.out.println("GeneLocus*****: "+this.geneLocus.toString());
			//System.out.println("GeneSyn*****: "+this.geneSyn.toString());
			for( int i =0; i<this.genelists.size(); i++){
				//creating gene class and adding the symbol annotation and label annotation
				OWLClass gene = owlvar.getFactory().getOWLClass(this.geneNum.get(i), owlvar.getPrefixmanager());
				OWLAnnotation geneLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(), owlvar.getFactory().getOWLLiteral(this.genelists.get(i),"en"));
				OWLDeclarationAxiom geneClass = owlvar.getFactory().getOWLDeclarationAxiom(gene);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), geneClass));
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLAnnotationAssertionAxiom(gene.getIRI(), geneLab)));
				
				//subclass axiom
				//OWLClass superClass = owlvar.getFactory().getOWLClass("C010",owlvar.getPrefixmanager());
				//owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLSubClassOfAxiom(gene, superClass)));
				
				//Symbole du g�ne
				OWLAnnotation symbol = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("#symbol", owlvar.getPrefixmanager()),owlvar.getFactory().getOWLLiteral(this.symbol.get(i)));
				OWLAxiom symb = owlvar.getFactory().getOWLAnnotationAssertionAxiom(gene.getIRI(), symbol);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), symb));
				
				//Type de g�ne (new)
				OWLClass superClass = owlvar.getFactory().getOWLClass(this.geneTypNum.get(i),owlvar.getPrefixmanager());
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLSubClassOfAxiom(gene, superClass)));
				//OWLAnnotation geneTyp = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("#genTyp", owlvar.getPrefixmanager()),owlvar.getFactory().getOWLLiteral(this.geneTyp.get(i)));
				//OWLAxiom geneType = owlvar.getFactory().getOWLAnnotationAssertionAxiom(gene.getIRI(), geneTyp);
				//owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), geneType));
				

				//LIEN GENE/MALADIE
				//adding gene typing object property along with the label for the typing
				OWLObjectProperty has_typing = owlvar.getFactory().getOWLObjectProperty(this.geneType.get(i), owlvar.getPrefixmanager());
				//OWLAnnotation typeLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(), owlvar.getFactory().getOWLLiteral(this.geneTypeName.get(i),"fr"));
				//OWLAnnotation typeLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(), owlvar.getFactory().getOWLClass(this.geneTypNum.get(i), owlvar.getPrefixmanager()));
				PrefixManager pmCur = new DefaultPrefixManager("http://purl.obolibrary.org/obo/");//pas utiliser?!
				//System.out.println("gene type status is  " + this.geneTypeStatus);
				OWLAnnotation curationAssertion = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("ECO_0000205",pmCur),owlvar.getFactory().getOWLLiteral(this.geneTypeStatus,getLang()));
				Set<OWLAnnotation> owlAnnoCur = new HashSet<OWLAnnotation>();
				owlAnnoCur.add(curationAssertion);
				OWLClass thisgeneClass = owlvar.getFactory().getOWLClass(gene.getIRI());
				OWLClassExpression hasTypingClass = owlvar.getFactory().getOWLObjectSomeValuesFrom(has_typing, rareDisorder);
				//@SuppressWarnings("unchecked")
				OWLSubClassOfAxiom ax1 = owlvar.getFactory().getOWLSubClassOfAxiom(thisgeneClass,hasTypingClass,owlAnnoCur);	
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), ax1));
				//owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLSubObjectPropertyOfAxiom(subProperty, superProperty)
				//owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_typing.getIRI(), typeLab)));
				
				//adding xref annotation for a gene 
				int cnt = Integer.parseInt(this.count.get(i));
				if(!this.gsources.isEmpty()){
					for (int j= start; j< (cnt+start); j++){
						PrefixManager obo = new DefaultPrefixManager("http://www.geneontology.org/formats/oboInOwl#");
						OWLAnnotation database_cross_reference = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
								.getOWLAnnotationProperty(
										"hasDbXref",
										obo), owlvar.getFactory().getOWLLiteral(this.gsources.get(j) + ":" + this.grefs.get(j)));
						OWLAxiom xref = owlvar.getFactory().getOWLAnnotationAssertionAxiom(gene.getIRI(), database_cross_reference);
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), xref));
						/*//UPDATE SD mapping relation
						Set<OWLAnnotation> ref = new HashSet<OWLAnnotation>();
						ref.add(database_cross_reference);
											
						if(!this.refValidList.get(i).equals("")){
							
							OWLAnnotation curationAssertion = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("ECO_0000218",obo),owlvar.getFactory().getOWLLiteral(this.refValidList.get(i)));
							Set<OWLAnnotation> owlAnnoCur = new HashSet<OWLAnnotation>();

							owlAnnoCur.add(curationAssertion);						
							OWLAxiom xref2 = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), database_cross_reference,owlAnnoCur);
							owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), xref2));
						}
						// FIN UPDATE*/
						 }
					}
					start = (cnt + start);
					
					//adding synonyms for the gene
					int counter = Integer.parseInt(this.genSynCount.get(i));
					if(!this.geneSyn.isEmpty()){
						for( int k = start1; k<(counter +start1); k++){
							//PrefixManager pm2 = new DefaultPrefixManager("http://www.ebi.ac.uk/efo/");
							OWLAnnotation alternativeTerm = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
									.getOWLAnnotationProperty(
											"alternative_term",
											pm2), owlvar.getFactory().getOWLLiteral(this.geneSyn.get(k),"en"));
							OWLAxiom synonym = owlvar.getFactory().getOWLAnnotationAssertionAxiom(gene.getIRI(), alternativeTerm);
							owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),synonym));
							//sb.append("; synonym :" + geneSyn.get(k));
						}
						
					}
				
					start1 = (counter + start1);
					
					//adding locus for the gene
					int counterLocus = Integer.parseInt(this.genLocusCount.get(i));
					OWLDataProperty has_Locus = owlvar.getFactory().getOWLDataProperty("C040", owlvar.getPrefixmanager());
					OWLAnnotation has_LocusLabel = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(getConceptLabel("C040"),getLang()));
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_Locus.getIRI(), has_LocusLabel)));
					
					
					if(!this.geneLocus.isEmpty()){

						for( int k = start2; k<(counterLocus +start2); k++){

							OWLClass thisgeneLocusClass = owlvar.getFactory().getOWLClass(gene.getIRI());
							OWLDataHasValue hasLocus = owlvar.getFactory().getOWLDataHasValue(has_Locus, owlvar.getFactory().getOWLLiteral(geneLocus.get(k)));
							OWLSubClassOfAxiom locusax1 = owlvar.getFactory().getOWLSubClassOfAxiom(thisgeneLocusClass,hasLocus);
							owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), locusax1));
							owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_Locus.getIRI(), has_LocusLabel)));
						}
						
					}
					
	
					start2 = (counterLocus + start2);
			}
		}//end of genlist
		
			}//end of if disease is null
		}//end of else loop
		
		
		
}
	
	
	
	public void saveOWLFile() throws OWLOntologyStorageException, OWLOntologyCreationException{
		writeToOWLFile();
		IRI documentIRI = IRI.create(Conf.outPath+"ORDO_"+getLang()+"_"+getVersion()+".owl");
		SimpleIRIMapper mapper = new SimpleIRIMapper(owlvar.getOntologyIRI(), documentIRI);
		owlvar.getManager().addIRIMapper(mapper);
		owlvar.getManager().saveOntology(owlvar.getOntology(), documentIRI);
		IRI documentIRImod = IRI.create(Conf.outPath+"orphaEfoMod_"+getLang()+"_"+getVersion()+".owl");
		SimpleIRIMapper mappermod = new SimpleIRIMapper(owlvar.getOntologyIRImod(), documentIRImod);
		owlvar.getManager().addIRIMapper(mappermod);
		System.out.println("saving the module");
		owlvar.getManager().saveOntology(owlvar.getOntologymod(), documentIRImod);
	}
    
    public String getDXRString(HashMap<String,RareDisease> xrefs) {
	StringBuilder sb = new StringBuilder();

	//** check if head of classification - keep them even if has no parent
	if (this.orphanum.contentEquals("165711") || this.orphanum.contentEquals("98050") || this.orphanum.contentEquals("93419") ||
			this.orphanum.contentEquals("97929") || this.orphanum.contentEquals("98028") || this.orphanum.contentEquals("93890") ||
			this.orphanum.contentEquals("97978") || this.orphanum.contentEquals("97966")|| this.orphanum.contentEquals("97935")||
			this.orphanum.contentEquals("98053") || this.orphanum.contentEquals("96344")|| this.orphanum.contentEquals("97992") ||
			this.orphanum.contentEquals("57146")|| this.orphanum.contentEquals("98004")|| this.orphanum.contentEquals("68416")||
			this.orphanum.contentEquals("98047")|| this.orphanum.contentEquals("108999") || this.orphanum.contentEquals("68329")||
			this.orphanum.contentEquals("98006")||this.orphanum.contentEquals("98026")|| this.orphanum.contentEquals("250908")||
			this.orphanum.contentEquals("98036") ||this.orphanum.contentEquals("93626")||  this.orphanum.contentEquals("280342")||
			this.orphanum.contentEquals("97955")|| this.orphanum.contentEquals("89826") || this.orphanum.contentEquals("97965")||
			this.orphanum.contentEquals("97962") || this.orphanum.contentEquals("98023")||this.orphanum.contentEquals("101433") ||
			this.orphanum.contentEquals("68367")|| this.orphanum.contentEquals("52662") || this.orphanum.contentEquals("138221") || 
			this.orphanum.contentEquals("68335") || this.orphanum.contentEquals("506207")){
			this.add_is_a_link("000");

	}
	if (this.isa_list.isEmpty()){ return sb.toString(); }

	sb.append("\n[Term]\n");
	sb.append("id:" +  this.orphanum +  "\n");
	/*This if loop makes sure that the following parent classes are not classified under Unclassified Rare diseases*/
	sb.append("name:" + this.name + "\n");
	sb.append ("DiseaseType:" +this.diseaseType +"\n");
	if(this.def != null){ sb.append("def:\"" + this.def + "\" [Orphanet]\n"); }
	//sb.append("comment:uri:http://www.orpha.net/consor/cgi-bin/OC_Exp.php?lng=en&Expert=" +  this.orphanum +  ".\n");
	//sb.append("comment:Orphanet ID- " + this.orphID + ".\n");
	if((this.prevalences.size()>0 ) || (this.ageOfOnset != null) || (this.ageOfDeath != null) || (! this.inheritance.isEmpty()) || (! this.genelists.isEmpty())){
		sb.append("comment:");
		if((this.prevalences.size()>0) || (this.ageOfOnset != null) || (this.ageOfDeath != null) || (! this.inheritance.isEmpty())){
			sb.append("Epidemiology [");

			if(this.prevalences.size()>0){ sb.append(" Prevalence- " + this.prevalences.get(0).getPrevalClass() + ";"); }
			if(this.ageOfOnset != null){ sb.append(" AgeOfOnset- "+ this.ageOfOnset + ";"); }
			if(this.ageOfDeath != null){ sb.append(" AgeOfDeath- " + this.ageOfDeath + ";"); }
			if (! this.inheritance.isEmpty()){
				sb.append("Inheritance:");
				for (int i= 0; i< this.inheritance.size(); i++){
					if(i>0){ sb.append("|"); }
					sb.append(this.inheritance.get(i)+ ";");
				}
			}
	
			//sb.append("comment: prevalence- " + this.prevalence + ";" + " AgeOfOnset- "+ this.ageOfOnset + ";"+ " AgeOfDeath- " + this.ageOfDeath + ";");
			/*if(this.prevalence != null){ sb.append("prevalence:" + this.prevalence + "\n"); }
			if(this.ageOfOnset != null){ sb.append("ageOfOnset:" + this.ageOfOnset + "\n"); }
			if(this.ageOfDeath != null){ sb.append("ageOfDeath:" + this.ageOfDeath + "\n"); }
			if (! this.inheritance.isEmpty()){
				for (int i= 0; i< this.inheritance.size(); i++){
				sb.append("Inheritance:"+ this.inheritance.get(i) + "\n");}
			}*/
			sb.append("]");
		}
		/**the problem with the expert is link due to the fault in the XML file. Need to use amp not the symbol*/
		//sb.append(" \ncomment: Expert Link -http://www.orpha.net/consor/cgi-bin/OC_Exp.php?lng=en&" + this.expert_link);
		
		/** printing out the genes comment*/
		int start = 0;
		int start1 = 0;
		if(! this.genelists.isEmpty()){
			for( int i =0; i<this.genelists.size(); i++){
				sb.append("Gene [OrphaNum:"+ this.geneNum.get(i)+ " ;  Name:" + this.genelists.get(i) + " ;  Symbol:" + this.symbol.get(i) +" ;Type:" + this.geneTypeName.get(i));
				int cnt = Integer.parseInt(this.count.get(i));
			if(!this.gsources.isEmpty()){
				for (int j= start; j< (cnt+start); j++){
					sb.append(" ; xref: " + this.gsources.get(j) +":" + this.grefs.get(j));
					 }
			}
				start = (cnt + start);
				//check from here for Synonym 
				int counter = Integer.parseInt(this.genSynCount.get(i));
				if(!this.geneSyn.isEmpty()){
					for( int k = start1; k<(counter +start1); k++){
						sb.append("; synonym :" + geneSyn.get(k));
					}
					
				}
			
				start1 = (counter + start1);
				sb.append("]");
			}
		}
		sb.append("\n");
	}
	
	
	/**Printing out the synonyms of the disease*/
	if (! this.synonym.isEmpty()){
	for( int i=0; i< this.synonym.size(); i++){
	sb.append("synonym: \""+ this.synonym.get(i).replaceAll("\"", "") +"\"  EXACT[]"+"\n" );}}
	
	/**printing out the disease xrefs*/
	if (!this.sourceList.isEmpty()){
		for (int i=0; i<this.sourceList.size(); i++){
			sb.append("xref: " + this.sourceList.get(i) + ":" + this.refList.get(i)+ "\n");
		}
		
	}
	//if (!this.isa_list.isEmpty()){
		for ( int i= 0; i<isa_list.size(); i++){
			sb.append("is_a:" + this.isa_list.get(i)+"\n" );
				}
	//}
	
	/* Classifies the floating classes under Unclasified rare diseases*/
	/*if (this.isa_list.isEmpty() && flag){
		sb.append("\nis_a:000");
	}*/
		//sb.append( "\n");
	
	return sb.toString();

    }
    
    // SD nouvelles fonctions pour miltilang
	public static String getConceptLabel(String iri) {
		if(labels.containsKey(iri)){
			return labels.get(iri);
		}else{
			System.out.println("ERROR FOR IRI : "+iri);
			return "";
		}
	}
	public static void setConceptLabel(String iri, String label) {
		RareDisease.labels.put(iri,label);
	}
	
	public static String getConceptDefinition(String iri) {
		return definitions.get(iri);
	}
	public static void setConceptDefinition(String iri, String def) {
		RareDisease.definitions.put(iri,def);
	}

	public static void setLang(String lang){
		RareDisease.lang = lang;
	}
	public static String getLang(){
		return RareDisease.lang;
	}

	public static void setVersion(String version){
		RareDisease.version = version;
	}
	public static String getVersion(){
		return RareDisease.version;
	}
	
	

}
