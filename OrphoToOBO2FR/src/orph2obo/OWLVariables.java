package orph2obo;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;



 
 
public class OWLVariables {
	
	//variables that need to be set for OWL API
		private OWLDataFactory factory;
		private PrefixManager pm;
		private OWLOntologyManager manager;
		private OWLOntology ontology;
		private IRI ontologyIRI;
		private OWLOntology ontologymod;
		private IRI ontologyIRImod;
		
		/**
		 * @return the factory
		 */
		public OWLDataFactory getFactory() {
			return factory;
		}
		/**
		 * @param factory the factory to set
		 */
		public void setFactory(OWLDataFactory factory) {
			this.factory = factory;
		}
		/**
		 * @return the pm
		 */
		public PrefixManager getPrefixmanager() {
			return pm;
		}
		/**
		 * @param pm the pm to set
		 */
		public void setPrefixmanager(PrefixManager pm) {
			this.pm = pm;
		}
		/**
		 * @return the manager
		 */
		public OWLOntologyManager getManager() {
			return manager;
		}
		/**
		 * @param manager the manager to set
		 */
		public void setManager(OWLOntologyManager manager) {
			this.manager = manager;
		}
		/**
		 * @return the ontology
		 */
		public OWLOntology getOntology() {
			return ontology;
		}
		/**
		 * @param ontology the ontology to set
		 */
		public void setOntology(OWLOntology ontology) {
			this.ontology = ontology;
		}
		/**
		 * @return the ontologyIRI
		 */
		public IRI getOntologyIRI() {
			return ontologyIRI;
		}
		/**
		 * @param ontologyIRI the ontologyIRI to set
		 */
		public void setOntologyIRI(IRI ontologyIRI) {
			this.ontologyIRI = ontologyIRI;
		}
		
		
		
		public void setOntologyIRImod(IRI ontologyIRImod) {
			this.ontologyIRImod = ontologyIRImod;
			
		}
		/**
		 * @return the ontologymod
		 */
		public OWLOntology getOntologymod() {
			return ontologymod;
		}
		/**
		 * @param ontologymod the ontologymod to set
		 */
		public void setOntologymod(OWLOntology ontologymod) {
			this.ontologymod = ontologymod;
		}
		/**
		 * @return the ontologyIRImod
		 */
		public IRI getOntologyIRImod() {
			return ontologyIRImod;
		}
		
		
		

}
