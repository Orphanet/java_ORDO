/**
 * 
 */
package org.semanticweb.owlapi.rio.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.semanticweb.owlapi.io.RDFLiteral;
import org.semanticweb.owlapi.io.RDFResourceIRI;
import org.semanticweb.owlapi.io.RDFTriple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Peter Ansell p_ansell@yahoo.com
 * 
 */
public class RioUtils
{
    private static final Logger log = LoggerFactory.getLogger(RioUtils.class);
    
    /**
     * Create a Statement with an empty context.
     * 
     * @param triple
     * @return
     */
    public static Statement tripleAsStatement(final RDFTriple triple)
    {
        Collection<Statement> statements = RioUtils.tripleAsStatements(triple);
        
        if(!statements.isEmpty())
        {
            return statements.iterator().next();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Create a Statement with the given context.
     * 
     * @param triple
     * @param context
     *            If context is not null, it is used to create a context statement
     * @return
     */
    public static Collection<Statement> tripleAsStatements(final RDFTriple triple, final Resource... contexts)
    {
        final ValueFactoryImpl vf = ValueFactoryImpl.getInstance();
        
        Resource subject;
        URI predicate;
        Value object;
        
        if(triple.getSubject() instanceof RDFResourceIRI)
        {
            try
            {
                subject = vf.createURI(triple.getSubject().getIRI().toString());
            }
            catch(IllegalArgumentException iae)
            {
                log.error("Subject URI was invalid: {}", triple);
                return Collections.emptyList();
            }
        }
        else
        {
            // FIXME: When blank nodes are no longer represented as IRIs internally, need to fix
            // this
            if(triple.getSubject().getIRI().toString().startsWith("_:"))
            {
                subject = vf.createBNode(triple.getSubject().getIRI().toString().substring(2));
            }
            else
            {
                subject = vf.createBNode(triple.getSubject().getIRI().toString());
            }
        }
        
        predicate = vf.createURI(triple.getPredicate().getIRI().toString());
        
        if(triple.getObject() instanceof RDFResourceIRI)
        {
            try
            {
                object = vf.createURI(triple.getObject().getIRI().toString());
            }
            catch(IllegalArgumentException iae)
            {
                log.error("Object URI was invalid: {}", triple);
                return Collections.emptyList();
            }
        }
        else if(triple.getObject() instanceof RDFLiteral)
        {
            final RDFLiteral literalObject = (RDFLiteral)triple.getObject();
            if(literalObject.hasDatatype())
            {
                object =
                        vf.createLiteral(literalObject.stringValue(),
                                vf.createURI(literalObject.getDatatype().toString()));
            }
            else if(literalObject.hasLang())
            {
                object = vf.createLiteral(literalObject.stringValue(), literalObject.getLang());
            }
            else
            {
                object = vf.createLiteral(literalObject.stringValue());
            }
        }
        else
        {
            // FIXME: When blank nodes are no longer represented as IRIs internally, need to fix
            // this
            if(triple.getObject().getIRI().toString().startsWith("_:"))
            {
                object = vf.createBNode(triple.getObject().getIRI().toString().substring(2));
            }
            else
            {
                object = vf.createBNode(triple.getObject().getIRI().toString());
            }
        }
        
        if(contexts == null || contexts.length == 0)
        {
            return Collections.singletonList(vf.createStatement(subject, predicate, object));
        }
        else
        {
            final ArrayList<Statement> results = new ArrayList<Statement>(contexts.length);
            
            for(final Resource nextContext : contexts)
            {
                results.add(vf.createStatement(subject, predicate, object, nextContext));
            }
            
            return results;
        }
        
    }
    
}
