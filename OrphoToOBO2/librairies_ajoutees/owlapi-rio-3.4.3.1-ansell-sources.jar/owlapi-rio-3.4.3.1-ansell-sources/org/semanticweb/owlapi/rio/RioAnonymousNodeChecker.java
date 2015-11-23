/**
 * 
 */
package org.semanticweb.owlapi.rio;

import org.coode.owlapi.rdfxml.parser.AnonymousNodeChecker;
import org.semanticweb.owlapi.model.IRI;

/**
 * @author Peter Ansell p_ansell@yahoo.com
 * 
 */
public class RioAnonymousNodeChecker implements AnonymousNodeChecker
{
    
    /**
     * 
     */
    public RioAnonymousNodeChecker()
    {
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.coode.owlapi.rdfxml.parser.AnonymousNodeChecker#isAnonymousNode(org.semanticweb.owlapi
     * .model.IRI)
     */
    @Override
    public boolean isAnonymousNode(final IRI iri)
    {
        return this.isAnonymousNode(iri.toString());
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.coode.owlapi.rdfxml.parser.AnonymousNodeChecker#isAnonymousNode(java.lang.String)
     */
    @Override
    public boolean isAnonymousNode(final String iri)
    {
        return iri.startsWith("_:");
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.coode.owlapi.rdfxml.parser.AnonymousNodeChecker#isAnonymousSharedNode(java.lang.String)
     */
    @Override
    public boolean isAnonymousSharedNode(final String iri)
    {
        return false;
    }
    
}
