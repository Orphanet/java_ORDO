/**
 * 
 */
package org.semanticweb.owlapi.rio;

import java.util.concurrent.atomic.AtomicInteger;

import org.coode.owlapi.rdfxml.parser.AnonymousNodeChecker;
import org.coode.owlapi.rdfxml.parser.OWLRDFConsumer;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author Peter Ansell p_ansell@yahoo.com
 * 
 */
public class RioOWLRDFConsumerAdapter extends OWLRDFConsumer implements RDFHandler
{
    private final Logger logger = LoggerFactory.getLogger(RioOWLRDFConsumerAdapter.class);
    
    private AtomicInteger statementCount = new AtomicInteger(0);
    
    public RioOWLRDFConsumerAdapter(final OWLOntology ontology, final AnonymousNodeChecker checker,
            final OWLOntologyLoaderConfiguration configuration)
    {
        super(ontology, checker, configuration);
    }
    
    public RioOWLRDFConsumerAdapter(final OWLOntologyManager owlOntologyManager, final OWLOntology ontology,
            final AnonymousNodeChecker checker, final OWLOntologyLoaderConfiguration configuration)
    {
        this(ontology, checker, configuration);
    }
    
    @Override
    public void endRDF() throws RDFHandlerException
    {
        this.logger.debug("Parsed {} statements", this.statementCount.toString());
        
        try
        {
            this.endModel();
        }
        catch(final SAXException e)
        {
            throw new RDFHandlerException(e);
        }
    }
    
    @Override
    public void handleComment(final String comment) throws RDFHandlerException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void handleNamespace(final String prefix, final String uri) throws RDFHandlerException
    {
        this.getOntologyFormat().setPrefix(prefix + ":", uri);
    }
    
    @Override
    public void handleStatement(final Statement st) throws RDFHandlerException
    {
        this.statementCount.incrementAndGet();
        
        this.logger.trace("st{}={}", this.statementCount.get(), st);
        
        String subjectString;
        String objectString;
        
        if(st.getSubject() instanceof BNode)
        {
            subjectString = st.getSubject().stringValue();
            
            // it is not mandatory for BNode.stringValue() to return a string prefixed with the
            // turtle blank node syntax, so we check here to make sure
            // if(!(subjectString.startsWith("_:genid")))
            // {
            subjectString = "#genid-nodeid-" + subjectString;
            // }
        }
        else
        {
            subjectString = st.getSubject().stringValue();
        }
        
        if(st.getObject() instanceof BNode)
        {
            objectString = st.getObject().stringValue();
            
            // it is not mandatory for BNode.stringValue() to return a string prefixed with the
            // turtle blank node syntax, so we check here to make sure
            // if(!(objectString.startsWith("_:genid")))
            // {
            objectString = "#genid-nodeid-" + objectString;
            // }
        }
        else
        {
            objectString = st.getObject().stringValue();
        }
        
        try
        {
            if(st.getObject() instanceof Resource)
            {
                this.logger.trace("statement with resource value");
                this.statementWithResourceValue(subjectString, st.getPredicate().stringValue(), objectString);
            }
            else
            {
                final Literal literalObject = (Literal)st.getObject();
                String literalDatatype = null;
                final String literalLanguage = literalObject.getLanguage();
                
                if(literalObject.getDatatype() != null)
                {
                    literalDatatype = literalObject.getDatatype().stringValue();
                }
                
                this.logger.trace("statement with literal value");
                this.statementWithLiteralValue(subjectString, st.getPredicate().stringValue(), objectString,
                        literalLanguage, literalDatatype);
            }
        }
        catch(final SAXException e)
        {
            throw new RDFHandlerException(e);
        }
    }
    
    @Override
    public void startRDF() throws RDFHandlerException
    {
        this.statementCount = new AtomicInteger(0);
        
        try
        {
            this.startModel("");
        }
        catch(final SAXException e)
        {
            throw new RDFHandlerException(e);
        }
    }
    
}
