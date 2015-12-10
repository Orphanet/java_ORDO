package orph2obo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
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
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.model.OWLLiteral;

import orph2obo.ExternalReference;
import orph2obo.Prevalence;
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

    public void setExpertLink(String link) { this.expert_link = link; 
    //System.out.println(expert_link_list); 
    }
    public String getExpertLink(){ return this.expert_link; }
    

    public void setName(String s) { this.name = s;}
    public String getName() { return this.name; }

   
    
    public void  setReference(String ref) { this.reference = ref; }
    public String getReference() { return this.reference ; }
    
   /* public void  addPrevalence(String p) { this.prevalence.setPrevalClass(p); }
    public String getPrevalenceClass() { return this.prevalence.getPrevalClass(); }  
    
    public void  setPrevalenceGeo(String p) { this.prevalence.setGeo(p); }
    public String getPrevalenceGeo() { return this.prevalence.getGeo(); }  
    
    public void  setPrevalenceValMoy(String p) { this.prevalence.setValMoy(p); }
    public String getPrevalenceValMoy() {return this.prevalence.getValMoy();}*/
    
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
	this.symbol = new ArrayList<String>();
	this.geneNum = new ArrayList<String>();
	this.gsources = new ArrayList<String>();
	this.grefs = new ArrayList<String> ();
	this.count= new ArrayList<String>();
	this.geneType = new ArrayList<String>();
	this.geneTypeName = new ArrayList<String>();
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
		owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.name));
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
							owlvar.getFactory().getOWLLiteral(this.def));
			OWLAxiom define = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), definition);
			OWLAnnotation definitionCitation = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
					.getOWLAnnotationProperty(
							"definition_citation",
							pm2),owlvar.getFactory().getOWLLiteral("orphanet"));
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
								pm2), owlvar.getFactory().getOWLLiteral(this.synonym.get(i)));
				OWLAxiom synonym = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), alternativeTerm);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntologymod(),synonym));
			}
		}
		
		//xrefs for the disease
		 if (!this.sourceList.isEmpty()){
			for (int i=0; i<this.sourceList.size(); i++){
				PrefixManager pm2 = new DefaultPrefixManager("http://www.geneontology.org/formats/oboInOwl");
				OWLAnnotation database_cross_reference = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
						.getOWLAnnotationProperty(
								"#hasDbXref",
								pm2), owlvar.getFactory().getOWLLiteral(this.sourceList.get(i) + ":" + this.refList.get(i)));
				OWLAxiom xref = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), database_cross_reference);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntologymod(), xref));
				
				}
			}
    	}		 			
    }
    
    
    
    
    
    
    
    

    public void writeToOWLFile() throws OWLOntologyCreationException{
    	if(this.owlvar != null){
    		return;
    		}
    	System.out.println("owl variable is initiated");
    	this.owlvar = new OWLVariables();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        owlvar.setManager(manager);
        IRI ontologyIRI = IRI.create("http://www.orpha.net/ontology/orphanet.owl");
        owlvar.setOntologyIRI(ontologyIRI);
        OWLOntology ontology = manager.createOntology(ontologyIRI);
        owlvar.setOntology(ontology);
    	System.out.println("Created ontology1: " + ontology);
    	IRI versionIRI = IRI.create("/version1.2");
    	OWLOntologyID newOntologyID = new OWLOntologyID(ontologyIRI,versionIRI);
    	SetOntologyID setOntologyID = new SetOntologyID(ontology, newOntologyID);
    	manager.applyChange(setOntologyID);
    	PrefixManager pm = new DefaultPrefixManager("http://www.orpha.net/ORDO/Orphanet_");
    	owlvar.setPrefixmanager(pm);
    	OWLDataFactory factory = manager.getOWLDataFactory();
    	owlvar.setFactory(factory);
    	OWLClass phenome = factory.getOWLClass("C001",pm);
    	OWLAnnotation phenomelabel = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("phenome"));
    	OWLClass groupPhenome = factory.getOWLClass("377794",pm);
    	OWLAnnotation label = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("group of disorders"));
    	OWLClass disease = factory.getOWLClass("377788",pm);
    	OWLAnnotation label1 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("disease"));
    	OWLClass clinicalSub = factory.getOWLClass("377796", pm);
    	OWLAnnotation label2 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("clinical subtype"));
    	OWLClass malformSyn = factory.getOWLClass("377789", pm);
    	OWLAnnotation label3 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("malformation syndrome"));
    	OWLClass etioSub = factory.getOWLClass("377795", pm);
    	OWLAnnotation label4 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("etiological subtype"));
    	OWLClass morphAna = factory.getOWLClass("377791", pm);
    	OWLAnnotation label5 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("morphological anomaly"));
    	OWLClass clinicalSyn = factory.getOWLClass("377792", pm);
    	OWLAnnotation label6 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("clinical syndrome"));
    	OWLClass partClinSitu = factory.getOWLClass("377793", pm);
    	OWLAnnotation label7 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("particular clinical situation in a disease or syndrome"));
    	OWLClass histoPathoSub = factory.getOWLClass("377797", pm);
    	OWLAnnotation label9 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("histopathological subtype"));
    	OWLClass bioAna = factory.getOWLClass("377790", pm);
    	OWLAnnotation label0 = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("biological anomaly"));
    	OWLClass gene = factory.getOWLClass("C010", pm);
       	OWLAnnotation genelabel = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("gene"));
       	OWLClass inheritance = factory.getOWLClass("C005", pm);
       	OWLAnnotation inheritancelabel = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("inheritance"));
    	//OWLClass autoRecess = factory.getOWLClass("108933", pm);
    	//OWLAnnotation autoRLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("autosomal recessive"));
    	//OWLClass autoDom = factory.getOWLClass("108932", pm);
    	//OWLAnnotation autoDomLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("autosomal dominant"));
    	OWLClass mitoInher = factory.getOWLClass("409933", pm);
    	OWLAnnotation mitoInherLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("mitochondrial inheritance"));
    	//OWLClass xRecess = factory.getOWLClass("108934", pm);
    	//OWLAnnotation xRecessLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("x linked recessive"));
    	//OWLClass xDom = factory.getOWLClass("108935", pm);
    	//OWLAnnotation xDomLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("x linked dominant"));
    	//OWLClass sporadic = factory.getOWLClass("108938", pm);
    	//OWLAnnotation sporadicLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("sporadic"));
    	
    	OWLClass autoDominant = factory.getOWLClass("409929", pm);
    	OWLAnnotation autoDominantLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("autosomal dominant"));
    	OWLClass autoRecessive = factory.getOWLClass("409930", pm);
    	OWLAnnotation autoRecessiveLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("autosomal recessive"));
    	OWLClass multiGene = factory.getOWLClass("409931", pm);
    	OWLAnnotation multiGeneLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("multigenic/multifactorial"));
    	OWLClass xlinkedRecessive = factory.getOWLClass("409932", pm);
    	OWLAnnotation xlinkedRecessiveLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("x-linked recessive"));
    	OWLClass mitho = factory.getOWLClass("409933", pm);
    	OWLAnnotation mithoLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("mitochondrial inheritance"));
    	OWLClass xlinkedDominant = factory.getOWLClass("409934", pm);
    	OWLAnnotation xlinkedDominantLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("x-linked dominant"));
    	OWLClass oligenic = factory.getOWLClass("409936", pm);
    	OWLAnnotation oligenicLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("oligogenic"));
    	OWLClass semiDom = factory.getOWLClass("409937", pm);
    	OWLAnnotation semiDomLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("semi-dominant"));
    	OWLClass yLinked = factory.getOWLClass("409938", pm);
    	OWLAnnotation yLinkedLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("y-linked"));
    	OWLClass unknown = factory.getOWLClass("409939", pm);
    	OWLAnnotation unknownLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("unknown"));
    	OWLClass nodata = factory.getOWLClass("409940", pm);
    	OWLAnnotation noDataLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("no data avaible"));
    	OWLClass notApplicable = factory.getOWLClass("409941", pm);
    	OWLAnnotation notApplicableLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("not applicable"));
    	
    	OWLClass ageofOnsetClass = factory.getOWLClass("C023", pm);
    	OWLAnnotation ageOfOnLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("age of onset"));
    	// UPDATE SD ajout de geography, epidemio, prevalence, cas/familles, cas et famille
    	OWLClass geographyClass = factory.getOWLClass("C009", pm);
    	OWLAnnotation geographyLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("geography"));
    	OWLClass epidemioClass = factory.getOWLClass("C003", pm);
    	OWLAnnotation epidemioLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("epidemilogy"));
    	OWLClass prevClass = factory.getOWLClass("C004", pm);
    	OWLAnnotation prevLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("prevalence"));
    	OWLClass casFamClass = factory.getOWLClass("409970", pm);
    	OWLAnnotation casFamLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("cases/families"));
    	OWLClass casClass = factory.getOWLClass("409973", pm);
    	OWLAnnotation casLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("Case"));
    	OWLClass famClass = factory.getOWLClass("409974", pm);
    	OWLAnnotation famLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral("Family"));
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
    	OWLDeclarationAxiom declarationGene = factory.getOWLDeclarationAxiom(gene);
    	OWLDeclarationAxiom inheritance0 = factory.getOWLDeclarationAxiom(inheritance);
    	OWLDeclarationAxiom inheritance1 = factory.getOWLDeclarationAxiom(semiDom);
    	//OWLDeclarationAxiom inheritance2 = factory.getOWLDeclarationAxiom(autoDom);
    	OWLDeclarationAxiom inheritance3 = factory.getOWLDeclarationAxiom(mitoInher);
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
    	OWLDeclarationAxiom geography = factory.getOWLDeclarationAxiom(geographyClass);
    	OWLDeclarationAxiom epidemiology = factory.getOWLDeclarationAxiom(epidemioClass);
    	OWLDeclarationAxiom casFam = factory.getOWLDeclarationAxiom(casFamClass);
    	OWLDeclarationAxiom cas = factory.getOWLDeclarationAxiom(casClass);
    	OWLDeclarationAxiom fam = factory.getOWLDeclarationAxiom(famClass);
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
    	OWLAxiom labGene = factory.getOWLAnnotationAssertionAxiom(gene.getIRI(), genelabel);
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
       	manager.applyChange(new AddAxiom(ontology, declarationGene));
       	manager.applyChange(new AddAxiom(ontology, inheritance0));
       	manager.applyChange(new AddAxiom(ontology, inheritance1));
       	//manager.applyChange(new AddAxiom(ontology, inheritance2));
       	manager.applyChange(new AddAxiom(ontology, inheritance3));
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
    	manager.applyChange(new AddAxiom(ontology, geography));
    	manager.applyChange(new AddAxiom(ontology, epidemiology));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(phenome.getIRI(), phenomelabel)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(inheritance.getIRI(), inheritancelabel)));
       	//manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(autoDom.getIRI(), autoDomLab)));
       	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(mitoInher.getIRI(), mitoInherLab)));
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
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(geographyClass.getIRI(), geographyLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(epidemioClass.getIRI(), epidemioLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(casFamClass.getIRI(), casFamLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(casClass.getIRI(), casLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLAnnotationAssertionAxiom(famClass.getIRI(), famLab)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(semiDom, inheritance)));
    	//manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(autoDom, inheritance)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(mitoInher, inheritance)));
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
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(casFamClass, epidemioClass)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(casClass, casFamClass)));
    	manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(famClass, casFamClass)));
    	
    	// UPDATE SD Definitions 
		PrefixManager pm2 = new DefaultPrefixManager("http://www.ebi.ac.uk/efo/");
		// epidemio
		OWLAnnotation epidemioDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Refers to the concepts describing the distribution of cases of a disorder in a given population at a given time."));
		OWLAxiom epidemioDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(epidemioClass.getIRI(), epidemioDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), epidemioDefine));
		// geo
		OWLAnnotation geographyDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Refers to a country, a continent, or the entire world."));
		OWLAxiom geographyDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(geographyClass.getIRI(), geographyDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), geographyDefine));
		// Cas/familles
		OWLAnnotation casFamDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("The number of cases or families with the disorder published in the literature."));
		OWLAxiom casFamDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(casFamClass.getIRI(), casFamDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), casFamDefine));
		// Cas
		OWLAnnotation casDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("The number of all cases with the disorder published in the literature."));
		OWLAxiom casDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(casClass.getIRI(), casDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), casDefine));
		// Famille
		OWLAnnotation famDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("The number of all families with the disorder published in the literature."));
		OWLAxiom famDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(famClass.getIRI(), famDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), famDefine));
		
		// prevalence
		OWLAnnotation prevalDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Refers to the concepts describing the distribution of all current cases (new and old cases) of a disorder in a given population at a given time."));
		OWLAxiom prevalDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(prevClass.getIRI(), prevalDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), prevalDefine));
		
		// age of onset
		OWLAnnotation ageOfOnsetDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Age of onset of clinical manifestations related to a disorder."));
		OWLAxiom ageOfOnsetDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(ageofOnsetClass.getIRI(), ageOfOnsetDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), ageOfOnsetDefine));
		
		// inheritance
		OWLAnnotation inheritanceDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("The way a genetic disorder can be spread to one or more members of a family."));
		OWLAxiom inheritanceDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(inheritance.getIRI(), inheritanceDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), inheritanceDefine));
		
		// Phenome
		OWLAnnotation phenomeDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("A set of phenotypes expressed at the cell, tissue, organ or organism level. It describes the \"physical totality of all traits of an organism or of one of its subsystems\" (Mahner and Kary, 1997)."));
		OWLAxiom phenomeDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(phenome.getIRI(), phenomeDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), phenomeDefine));
		
		// biological anormaly
		OWLAnnotation bioAnaDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("An alteration of the normal values of biological products. Example :  hypertransferrinemia."));
		OWLAxiom bioAnaDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(bioAna.getIRI(), bioAnaDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), bioAnaDefine));
		
		// clinical subtype
		OWLAnnotation clinicalSubDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("A subset of a disorder defined by a distinct clinical presentation."));
		OWLAxiom clinicalSubDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(clinicalSub.getIRI(), clinicalSubDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), clinicalSubDefine));
		
		// clinical syndrome
		OWLAnnotation clinicalSynDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("A set of manifestations resulting from the alteration of a physiological state and that can be present in several diseases. Examples :  nephrotic syndrome, hepatic failure."));
		OWLAxiom clinicalSynDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(clinicalSyn.getIRI(),clinicalSynDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), clinicalSynDefine));
		
		// disease
		OWLAnnotation diseaseDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("An alteration of health status resulting from a physiopathological mechanism, and having a homogeneous clinical presentation and evolution and homogeneous therapeutic possibilities. Excludes developmental anomalies."));
		OWLAxiom diseaseDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(disease.getIRI(), diseaseDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), diseaseDefine));
		
		// etiological subtype
		OWLAnnotation etioSubDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("A subset of a disorder defined by its cause, and clinically indistinguishable from other etiological subtypes."));
		OWLAxiom etioSubDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(etioSub.getIRI(), etioSubDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), etioSubDefine));	
				
		// groupe of disorders
		OWLAnnotation groupPhenomeDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("A collection of different types of phenomes, sharing a given characteristic and therefore being classified together."));
		OWLAxiom groupPhenomeDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(groupPhenome.getIRI(), groupPhenomeDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), groupPhenomeDefine));
		
		// histopathological subtype
		OWLAnnotation histoPathoSubDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("A subset of a disorder defined on the basis of its histological aspect."));
		OWLAxiom histoPathoSubDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(histoPathoSub.getIRI(),histoPathoSubDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), histoPathoSubDefine));
		
		// malformation syndrome
		OWLAnnotation malformSynDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("A set of morphological anomalies resulting from a developmental anomaly involving more than one morphogenetic field, regardless of the cause.  Includes sequences and associations."));
		OWLAxiom malformSynDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(malformSyn.getIRI(), malformSynDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), malformSynDefine));
		
		// particular clinical situation in a disease or syndrome
		OWLAnnotation partClinSituDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("A set of manifestations presenting as a subset of a disorder under particular circumstances."));
		OWLAxiom partClinSituDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(partClinSitu.getIRI(), partClinSituDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), partClinSituDefine));	
		
		// Morphological anomaly
		OWLAnnotation morphAnaDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("A set of anomalies resulting from a developmental anomaly involving only one morphogenetic field. Includes isolated anomalies and anatomical variants."));
		OWLAxiom morphAnaDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(morphAna.getIRI(), morphAnaDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), morphAnaDefine));
		
		/* *** UPDATE SD : Inheritance  *** */
		// autosomal recessive
		OWLAnnotation autoRecDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Describes a disorder in which two mutated alleles located on one of the 22 autosomes (non-sex chromosomes) are necessary to express the phenotype and which carries a 25% risk of being passed on to offspring."));
		OWLAxiom autoRecDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(autoRecessive.getIRI(), autoRecDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), autoRecDefine));
		// autosomal dominant
		OWLAnnotation autoDomDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Describes a disorder in which only one mutated allele located on one of the 22 autosomes (non-sex chromosomes) is sufficient to express the phenotype and which carries a 50% risk of being passed on to offspring."));
		OWLAxiom autoDomDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(autoDominant.getIRI(), autoDomDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), autoDomDefine));
		// multigenic/multifactorial
		OWLAnnotation multigenDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Describes a disorder in which the combination of one or more genes and/or environmental factors contributes to the expression of the phenotype."));
		OWLAxiom multigenDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(multiGene.getIRI(), multigenDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), multigenDefine));
		// x linked recessive
		OWLAnnotation xlinkedRecDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Describes a disorder in which two mutated alleles located on the X chromosome are necessary to express the phenotype, and which carries a risk of inheritance that differs between males and females. The phenotype is expressed in hemizygous males (having only one X chromosome) and homozygous females."));
		OWLAxiom xlinkedRecDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(xlinkedRecessive.getIRI(), xlinkedRecDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), xlinkedRecDefine));
		// mitochondrial
		OWLAnnotation mithoDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Describes a disorder in which a mutation in one of the mitochondrial genes is necessary to express the phenotype and that is always maternally inherited."));
		OWLAxiom mithoDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(mitho.getIRI(), mithoDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), mithoDefine));
		// x linked dominant
		OWLAnnotation xlinkedDomDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Describes a disorder in which only one mutated alleles located on the X chromosome is sufficient to express the phenotype, and which carries a risk of inheritance that differs between males and females. The phenotype is  more consistently and severely expressed in hemizygous males (having only one X chromosome) than in heterozygous females."));
		OWLAxiom xlinkedDomDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(xlinkedDominant.getIRI(), xlinkedDomDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), xlinkedDomDefine));
		//oligogenic
		OWLAnnotation oligogenicDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Describes a disorder in which the combination of mutated alleles of two or more genes are sufficient to express the phenotype.."));
		OWLAxiom oligogenicDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(oligenic.getIRI(), oligogenicDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), oligogenicDefine));
		// semi-dominant
		OWLAnnotation semiDomDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Describes a disorder in which only one mutated allele located on one of the 22 autosomes (non-sex chromosomes) is sufficient to express the phenotype, the phenotype of the homozyguous individual being more severe, when the two alleles are mutated."));
		OWLAxiom semiDomDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(semiDom.getIRI(), semiDomDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), semiDomDefine));
		// y linked
		OWLAnnotation yLinkedDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Describes a disorder in which only one mutated allele located on the Y chromosome is sufficient to express the phenotype. There is only male-to-male transmission."));
		OWLAxiom yLinkedDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(yLinked.getIRI(), yLinkedDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), yLinkedDefine));
		// no data avaible
		OWLAnnotation noDataDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("This value is used when no data is available in the scientific literature about the inheritability of the disorder."));
		OWLAxiom noDataDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(nodata.getIRI(), noDataDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), noDataDefine));
		// not applicable
		OWLAnnotation notApplicDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Describes a disorder that is not inherited."));
		OWLAxiom notApplicDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(notApplicable.getIRI(), notApplicDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), notApplicDefine));
		// unknown
		OWLAnnotation unknownDefinition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("definition", pm2),
				factory.getOWLLiteral("Describes an inherited disorder with unknown mode of inheritance."));
		OWLAxiom unknownDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(unknown.getIRI(), unknownDefinition);
		owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), unknownDefine));
    	
    	//for the module ontology
        IRI ontologyIRImod = IRI.create("http://www.orpha.net/ontology/orphaEfoMod.owl");
        owlvar.setOntologyIRImod(ontologyIRImod);
        OWLOntology ontologymod = manager.createOntology(ontologyIRImod);
        owlvar.setOntologymod(ontologymod);
    	System.out.println("Created ontology2: " + ontologymod);
    	IRI versionIRImod = IRI.create("/version1.2");
    	OWLOntologyID newOntologyIDmod = new OWLOntologyID(ontologyIRImod,versionIRImod);
    	SetOntologyID setOntologyIDmod = new SetOntologyID(ontologymod, newOntologyIDmod);
    	manager.applyChange(setOntologyIDmod);
    	
    	
    	
       } 
    
    private boolean checkParentDisease(){
    	if (this.orphanum.contentEquals("165711") || this.orphanum.contentEquals("98050") || this.orphanum.contentEquals("93419") ||
    			this.orphanum.contentEquals("97929") || this.orphanum.contentEquals("98028") || this.orphanum.contentEquals("93890") ||
    			this.orphanum.contentEquals("97978") || this.orphanum.contentEquals("97966")|| this.orphanum.contentEquals("97935")||
    			this.orphanum.contentEquals("98053") || this.orphanum.contentEquals("96344")|| this.orphanum.contentEquals("97992") ||
    			this.orphanum.contentEquals("57146")|| this.orphanum.contentEquals("98004")|| this.orphanum.contentEquals("68416")||
    			this.orphanum.contentEquals("98047")|| this.orphanum.contentEquals("108999") || this.orphanum.contentEquals("68329")||
    			this.orphanum.contentEquals("98006")||this.orphanum.contentEquals("98026")|| this.orphanum.contentEquals("250908")||
    			this.orphanum.contentEquals("98036") ||this.orphanum.contentEquals("93626")||
    			this.orphanum.contentEquals("97955")|| this.orphanum.contentEquals("89826") || this.orphanum.contentEquals("97965")||
    			this.orphanum.contentEquals("97962") || this.orphanum.contentEquals("98023")||this.orphanum.contentEquals("101433") ||
    			this.orphanum.contentEquals("68367")|| this.orphanum.contentEquals("52662") || this.orphanum.contentEquals("93890") || this.orphanum.contentEquals("98053")){
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
		if(flag == false && this.isa_list.isEmpty()){
			System.out.println("Not a head and orphaned");
		}
		else{
			//System.out.println("entered the disease loop");
		OWLClass rareDisorder = owlvar.getFactory().getOWLClass(this.orphanum, owlvar.getPrefixmanager());
		OWLAnnotation labelRare = owlvar.getFactory().getOWLAnnotation(
		owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.name));
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
				OWLAnnotation manualAssert = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("ECO_0000218",pm3),owlvar.getFactory().getOWLLiteral(this.disTypeValidity));
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
				System.out.println("Name: "+this.name);// test pour voir le contenu de this
				System.out.println("Type: "+this.disTypeValidity);// test pour voir le contenu de this
				OWLAnnotation manualAssert = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("ECO_0000218",pm3),owlvar.getFactory().getOWLLiteral(this.disTypeValidity));
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
						OWLAnnotation partOfLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral("part_of"));
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(partOf.getIRI(), partOfLab)));
						OWLClass superClass = owlvar.getFactory().getOWLClass(this.isa_list.get(i), owlvar.getPrefixmanager());
						OWLClassExpression partOfSuperClass = owlvar.getFactory().getOWLObjectSomeValuesFrom(partOf, superClass);
						OWLSubClassOfAxiom ax = owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder, partOfSuperClass);
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), ax));
					}
				}
			}			
			
		PrefixManager pm2 = new DefaultPrefixManager("http://www.ebi.ac.uk/efo/");
		
		//System.out.println("exited the disease loop starting inheritance Inheritance size is  " + this.inheritNum.size());
		//type of inheritance for that disease
		 if (!(this.inheritNum.isEmpty())){
			System.out.println("writing the inheritance");
			for (int i= 0; i< this.inheritNum.size(); i++){
				if (!inheritNum.get(i).contentEquals("108939") &&! inheritNum.get(i).contentEquals("108940")){//that is if inheritance is not Unknown
				OWLObjectProperty has_inheritance = owlvar.getFactory().getOWLObjectProperty("C016", owlvar.getPrefixmanager());
				OWLAnnotation hasInheritLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral("has_inheritance"));
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_inheritance.getIRI(), hasInheritLab)));
				OWLClass inheritanceType  = owlvar.getFactory().getOWLClass(this.inheritNum.get(i),owlvar.getPrefixmanager());
				OWLClassExpression hasInheritanceClass = owlvar.getFactory().getOWLObjectSomeValuesFrom(has_inheritance, inheritanceType);
				OWLSubClassOfAxiom ax = owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder,hasInheritanceClass);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), ax));
				}
			}
		}
		
		//age of onset
		 if (this.onsetNum != null){
			 for(int i=0;i<this.ageOfOnset.size();i++){
				System.out.println("writing the age of onset");
				OWLObjectProperty has_ageOfOnset = owlvar.getFactory().getOWLObjectProperty("C017", owlvar.getPrefixmanager());
				OWLAnnotation hasageOfOnLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral("has_AgeOfOnset"));
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_ageOfOnset.getIRI(), hasageOfOnLab)));
				OWLClass onsetValue = owlvar.getFactory().getOWLClass(this.onsetNum.get(i),owlvar.getPrefixmanager());
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLDeclarationAxiom(onsetValue)));//asserting age class
				//set the subclass relationship
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLSubClassOfAxiom(onsetValue, owlvar.getFactory().getOWLClass("C023", owlvar.getPrefixmanager()))));
				OWLAnnotation onsetLabel = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.ageOfOnset.get(i)));//label for the value of age of onset
				OWLClassExpression hasageOfOnsetClass = owlvar.getFactory().getOWLObjectSomeValuesFrom(has_ageOfOnset, onsetValue);
				OWLSubClassOfAxiom ax = owlvar.getFactory().getOWLSubClassOfAxiom(rareDisorder,hasageOfOnsetClass);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), ax));
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(onsetValue.getIRI(), onsetLabel)));
				
				/* *** UPDATE SD : Age of onset  *** */
				String def="";
				if(this.onsetNum.get(i).equals("409943")){def="Before delivery";}                                            //antenatal
				else if(this.onsetNum.get(i).equals("409944")){def="From delivery to 4 weeks of life.";}                     //neonatal
				else if(this.onsetNum.get(i).equals("409945")){def="From 4 weeks to 23 months of life.";}                    //infancy
				else if(this.onsetNum.get(i).equals("409946")){def="From 2 to 11 years of life.";}                           //childhood
				else if(this.onsetNum.get(i).equals("409947")){def="From 12 to 18 years of life.";}                          //adolescent
				else if(this.onsetNum.get(i).equals("409948")){def="From 19 to 65 years of life.";}                          //adult
				else if(this.onsetNum.get(i).equals("409949")){def="After 65 years of life.";}                               //eldery
				else if(this.onsetNum.get(i).equals("409950")){def="From delivery to adulthood without any peak of onset.";} //all age
				else if(this.onsetNum.get(i).equals("409951")){def="This value is used when there is no data in the scientific literature to allow for a specific age of onset to be assigned to a disorder.";} //no data avaible
				
				OWLAnnotation onsetDefinition =  owlvar.getFactory().getOWLAnnotation( owlvar.getFactory().getOWLAnnotationProperty("definition", pm2),
						owlvar.getFactory().getOWLLiteral(def));
				OWLAxiom onsetDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(onsetValue.getIRI(), onsetDefinition);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), onsetDefine));

			 }
		}
		 
		
		// UPDATE SD Prevalence class, prevalence value, prevalence type and prevalence loc

		if (!this.prevalences.isEmpty()){
			System.out.println("writing the prevelances");

			
			for(int i=0;i<this.prevalences.size();i++){
				Set<OWLClassExpression> intersec = new HashSet<OWLClassExpression> (); // set d'info sur la prévalence en cours
				String type=this.prevalences.get(i).getType();                         // type de prévalence
				
				if (this.prevalences.get(i).getPrevalClass()!=null && !this.prevalences.get(i).getPrevalClass().equals("") 
						&& !this.prevalences.get(i).getPrevalClass().equals("409981")){
					/* UPDATE sélection du typage de classe */
					if (type.equals("409966") ){ type = "C025";      // prevalence point
					}else if (type.equals("409968") ){type = "C026"; // prevalence at birth
					}else if (type.equals("409969") ){type = "C027"; // lifetime prevalence		
					}else if (type.equals("409967") ){type = "C020"; // annual incidence
					}
					
					// UPDATE SD create has_.... anonymous class
					OWLObjectProperty has_Prevalence = owlvar.getFactory().getOWLObjectProperty(type,owlvar.getPrefixmanager());
					
					if(this.prevalences.get(i).getTypeLab()!=null && !this.prevalences.get(i).getTypeLab().equals("")){
						OWLAnnotation hasPrevLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral("has_"+this.prevalences.get(i).getTypeLab().toLowerCase().replaceAll(" ","_")+"_range"));
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_Prevalence.getIRI(), hasPrevLab)));
					}
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLDeclarationAxiom(has_Prevalence)));//asserting prevalence class
					
					
					
					// UPDATE SD add the type of prevalence
					OWLClass prevType = owlvar.getFactory().getOWLClass(this.prevalences.get(i).getType(),owlvar.getPrefixmanager());
					String defType="";
					// définition et super classe
					if (type.equals("C020")){
						defType="The number of new cases that arise in a population in a given time period (year).";
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLSubClassOfAxiom(prevType, owlvar.getFactory().getOWLClass("C003", owlvar.getPrefixmanager()))));
					}else{
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLSubClassOfAxiom(prevType, owlvar.getFactory().getOWLClass("C004", owlvar.getPrefixmanager()))));
						if (type.equals("C025")       ){ defType = "The number of all the existing cases of a disorder in a given population at a specific point in time.";      // prevalence point
						}else if (type.equals("C026") ){ defType = "The number of infants born with the disorder compared to the total number of live births in a given time period."; // prevalence at birth
						}else if (type.equals("C027") ){ defType = "The number of individuals in a statistical population that at some point in their lives have experienced a disorder, compared to the total number of individuals."; // lifetime prevalence						
						}
					}
					// UPDATE SD application définition
					OWLAnnotation prevalTypeDefinition = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("definition", pm2),
							owlvar.getFactory().getOWLLiteral(defType));	
					OWLAxiom prevalTypeDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(prevType.getIRI(), prevalTypeDefinition);
										
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLDeclarationAxiom(prevType)));//asserting prevalence Type
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), prevalTypeDefine));
					// Ajout du label
					OWLAnnotation prevalTypeLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.prevalences.get(i).getTypeLab()));
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(prevType.getIRI(), prevalTypeLab)));
					
					// UPDATE SD add the Class of prevalence
					OWLClass prevClass = owlvar.getFactory().getOWLClass(this.prevalences.get(i).getPrevalClass(),owlvar.getPrefixmanager());
					
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLSubClassOfAxiom(prevClass, prevType)));
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLDeclarationAxiom(prevClass)));//asserting prevalence class
					
					


					
					
					OWLClassExpression hasPrevalenceClass = owlvar.getFactory().getOWLObjectSomeValuesFrom(has_Prevalence, prevClass); 
					intersec.add(hasPrevalenceClass);
					String def="";
					String label="";
					if(this.prevalences.get(i).getPrevalClass().equals("409975")){
						def="Prevalence or incidence figures ranging from 1 to 5 individuals per ten thousand inhabitants.";
						label="1-5 / 10 000";
					}
					else if(this.prevalences.get(i).getPrevalClass().equals("409976")){
						def="Prevalence or incidence figures ranging from 1 to 9 individuals per 1 million inhabitants.";
						label="1-9 / 1 000 000";
					}
					else if(this.prevalences.get(i).getPrevalClass().equals("409977")){
						def="Prevalence or incidence figures ranging from 1 to 9 individuals per 1 hundred thousand inhabitants.";
						label="1-9 / 100 000";
					}
					else if(this.prevalences.get(i).getPrevalClass().equals("409978")){
						def="Prevalence or incidence figures ranging from 6 to 9 individuals per ten thousand inhabitants.";
						label="6-9 / 10 000";
					}
					else if(this.prevalences.get(i).getPrevalClass().equals("409979")){
						def="Prevalence or incidence figures are inferior to 1 individual per 1 million inhabitants.";
						label="<1 / 1 000 000";
					}
					else if(this.prevalences.get(i).getPrevalClass().equals("409980")){
						def="Prevalence or incidence figures are greater than 1 individual per 1 thousand inhabitants.";
						label=">1 / 1000";
					}
					
					//label for prevalence class
					OWLAnnotation prevalClassLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(label));
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(prevClass.getIRI(), prevalClassLab)));
					
					//definition for prevalence class
					OWLAnnotation prevalClassDefinition = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("definition", pm2),
							owlvar.getFactory().getOWLLiteral(def));
					OWLAxiom prevalClassDefine = owlvar.getFactory().getOWLAnnotationAssertionAxiom(prevClass.getIRI(), prevalClassDefinition);
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), prevalClassDefine));
					
					
				}
				
				type = this.prevalences.get(i).getType();
				/* UPDATE  pour gérer les valeurs moyennes*/
				
				if (this.prevalences.get(i).getValMoy()!=null && !this.prevalences.get(i).getValMoy().equals("")){
					/* UPDATE sélection du typage de Valeur moyenne */
					if (type.equals("409966") ){ type = "C028";      // prevalence point
					}else if (type.equals("409968") ){type = "C029"; // prevalence at birth
					}else if (type.equals("409969") ){type = "C030"; // lifetime prevalence				
					}else if (type.equals("409967") ){type = "C032"; // annual incidence
					}else if (type.equals("409974") ){type = "C024"; // Family
					}else if (type.equals("409973") ){type = "C024"; // Case
					}
					OWLObjectProperty has_Prevalence = owlvar.getFactory().getOWLObjectProperty(type, owlvar.getPrefixmanager());
					OWLAnnotation hasPrevLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral("has_"+this.prevalences.get(i).getTypeLab().toLowerCase().replaceAll(" ","_")+"_average_value"));
					
					owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_Prevalence.getIRI(), hasPrevLab)));
					OWLDataProperty prevalType = owlvar.getFactory().getOWLDataProperty(type, owlvar.getPrefixmanager());
			
					OWLLiteral prevValue = owlvar.getFactory().getOWLLiteral(this.prevalences.get(i).getValMoy());
					OWLDataHasValue hasPrevVal = owlvar.getFactory().getOWLDataHasValue(prevalType, prevValue);
					intersec.add(hasPrevVal);
					
					
					
					
				}
				
				/* UPDATE SD add Géo */
				if (this.prevalences.get(i).getGeo()!=null && !this.prevalences.get(i).getGeo().equals("")){
					
					OWLObjectProperty has_Geo = owlvar.getFactory().getOWLObjectProperty("C022",owlvar.getPrefixmanager());
					OWLAnnotation GeoLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral("present_in"));
					
					OWLAnnotation hasGeoLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(),owlvar.getFactory().getOWLLiteral(this.prevalences.get(i).getGeoLab()));
					
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
				
				
				

			}
		

			
			
		}
		
		

		
		
		
		
		
		//def for the disease if any
		 if (this.def != null) {
			
			OWLAnnotation definition = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
					.getOWLAnnotationProperty(
							"definition", pm2),
							owlvar.getFactory().getOWLLiteral(this.def));
			OWLAxiom define = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), definition);
			OWLAnnotation definitionCitation = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
					.getOWLAnnotationProperty(
							"definition_citation",
							pm2),owlvar.getFactory().getOWLLiteral("orphanet"));
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
								pm2), owlvar.getFactory().getOWLLiteral(this.synonym.get(i)));
				OWLAxiom synonym = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), alternativeTerm);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),synonym));
			}
		}
		
		//xrefs for the disease
		 if (!this.sourceList.isEmpty()){
			for (int i=0; i<this.sourceList.size(); i++){
				//PrefixManager pm2 = new DefaultPrefixManager("http://www.geneontology.org/formats/oboInOwl");
				OWLAnnotation database_cross_reference = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
						.getOWLAnnotationProperty(
								"#hasDbXref",
								pm2), owlvar.getFactory().getOWLLiteral(this.sourceList.get(i) + ":" + this.refList.get(i)));
				OWLAxiom xref = owlvar.getFactory().getOWLAnnotationAssertionAxiom(rareDisorder.getIRI(), database_cross_reference);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), xref));
				
				}
			}
		//gene class and its annotations
		int start = 0;
		int start1 = 0;
		int start2 = 0;//ajout pour la gestion des locus
		
		if(! this.genelists.isEmpty()){
			//System.out.println("Genes: "+this.genelists.toString());
			//System.out.println("Smb: "+this.symbol.toString());
			//System.out.println("Type: "+this.geneTypeName.toString());
			for( int i =0; i<this.genelists.size(); i++){
				//creating gene class and adding the symbol annotation and label annotation
				OWLClass gene = owlvar.getFactory().getOWLClass(this.geneNum.get(i), owlvar.getPrefixmanager());
				OWLAnnotation geneLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(), owlvar.getFactory().getOWLLiteral(this.genelists.get(i)));
				OWLDeclarationAxiom geneClass = owlvar.getFactory().getOWLDeclarationAxiom(gene);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), geneClass));
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLAnnotationAssertionAxiom(gene.getIRI(), geneLab)));
				//subclass axiom
				OWLClass superClass = owlvar.getFactory().getOWLClass("C010",owlvar.getPrefixmanager());
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLSubClassOfAxiom(gene, superClass)));
				OWLAnnotation symbol = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("#symbol", owlvar.getPrefixmanager()),owlvar.getFactory().getOWLLiteral(this.symbol.get(i)));
				OWLAxiom symb = owlvar.getFactory().getOWLAnnotationAssertionAxiom(gene.getIRI(), symbol);
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), symb));
				
				//adding gene typing object property along with the label for the typing
				OWLObjectProperty has_typing = owlvar.getFactory().getOWLObjectProperty(this.geneType.get(i), owlvar.getPrefixmanager());
				OWLAnnotation typeLab = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getRDFSLabel(), owlvar.getFactory().getOWLLiteral(this.geneTypeName.get(i)));
				PrefixManager pmCur = new DefaultPrefixManager("http://purl.obolibrary.org/obo/");
				//System.out.println("gene type status is  " + this.geneTypeStatus);
				OWLAnnotation curationAssertion = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory().getOWLAnnotationProperty("ECO_0000205",pmCur),owlvar.getFactory().getOWLLiteral(this.geneTypeStatus));
				Set<OWLAnnotation> owlAnnoCur = new HashSet<OWLAnnotation>();
				owlAnnoCur.add(curationAssertion);
				OWLClass thisgeneClass = owlvar.getFactory().getOWLClass(gene.getIRI());
				OWLClassExpression hasTypingClass = owlvar.getFactory().getOWLObjectSomeValuesFrom(has_typing, rareDisorder);
				//@SuppressWarnings("unchecked")
				OWLSubClassOfAxiom ax1 = owlvar.getFactory().getOWLSubClassOfAxiom(thisgeneClass,hasTypingClass,owlAnnoCur);	
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), ax1));
				//owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLSubObjectPropertyOfAxiom(subProperty, superProperty)
				owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), owlvar.getFactory().getOWLAnnotationAssertionAxiom(has_typing.getIRI(), typeLab)));
				
				//adding xref annotation for a gene 
				int cnt = Integer.parseInt(this.count.get(i));
				if(!this.gsources.isEmpty()){
					for (int j= start; j< (cnt+start); j++){
						//PrefixManager pm2 = new DefaultPrefixManager("http://www.geneontology.org/formats/oboInOwl");
						OWLAnnotation database_cross_reference = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
								.getOWLAnnotationProperty(
										"#hasDbXref",
										pm2), owlvar.getFactory().getOWLLiteral(this.gsources.get(j) + ":" + this.grefs.get(j)));
						OWLAxiom xref = owlvar.getFactory().getOWLAnnotationAssertionAxiom(gene.getIRI(), database_cross_reference);
						owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(), xref));
						
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
											pm2), owlvar.getFactory().getOWLLiteral(this.geneSyn.get(k)));
							OWLAxiom synonym = owlvar.getFactory().getOWLAnnotationAssertionAxiom(gene.getIRI(), alternativeTerm);
							owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),synonym));
							//sb.append("; synonym :" + geneSyn.get(k));
						}
						
					}
				
					start1 = (counter + start1);
					
					//adding locus for the gene
					int counterLocus = Integer.parseInt(this.genLocusCount.get(i));
					if(!this.geneLocus.isEmpty()){
						for( int k = start2; k<(counterLocus +start2); k++){
							//PrefixManager pm2 = new DefaultPrefixManager("http://www.ebi.ac.uk/efo/");//QUOI METTRE comme Prefix?
							OWLAnnotation alternativeTerm = owlvar.getFactory().getOWLAnnotation(owlvar.getFactory()
									.getOWLAnnotationProperty(
											"alternative_term",
											pm2), owlvar.getFactory().getOWLLiteral(this.geneLocus.get(k)));
							OWLAxiom locus = owlvar.getFactory().getOWLAnnotationAssertionAxiom(gene.getIRI(), alternativeTerm);
							owlvar.getManager().applyChange(new AddAxiom(owlvar.getOntology(),locus));
							//sb.append("; locus :" + geneLocus.get(k));
						}
						
					}
				
					start2 = (counterLocus + start2);
			}
		}
		
			}//end of if disease is null
		}//end of else loop
		
		
		
}
	
	
	
	public void saveOWLFile() throws OWLOntologyStorageException, OWLOntologyCreationException{
		writeToOWLFile();
		IRI documentIRI = IRI.create("file:/OrphoToOBO2/orphadata.owl");
		SimpleIRIMapper mapper = new SimpleIRIMapper(owlvar.getOntologyIRI(), documentIRI);
		owlvar.getManager().addIRIMapper(mapper);
		owlvar.getManager().saveOntology(owlvar.getOntology(), documentIRI);
		IRI documentIRImod = IRI.create("file:/OrphoToOBO2/orphaEfoMod.owl");
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
			this.orphanum.contentEquals("98036") ||this.orphanum.contentEquals("93626")||
			this.orphanum.contentEquals("97955")|| this.orphanum.contentEquals("89826") || this.orphanum.contentEquals("97965")||
			this.orphanum.contentEquals("97962") || this.orphanum.contentEquals("98023")||this.orphanum.contentEquals("101433") ||
			this.orphanum.contentEquals("68367")|| this.orphanum.contentEquals("52662")){
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
	
	
	
	

}
