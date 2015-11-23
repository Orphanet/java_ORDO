/**
 * 
 */
package org.semanticweb.owlapi.rio;

import org.kohsuke.MetaInfServices;
import org.semanticweb.owlapi.formats.OWLOntologyFormatFactory;
import org.semanticweb.owlapi.formats.RdfJsonOntologyFormatFactory;
import org.semanticweb.owlapi.model.OWLOntologyStorer;
import org.semanticweb.owlapi.model.OWLOntologyStorerFactory;

/**
 * @author Peter Ansell p_ansell@yahoo.com
 * 
 */
@MetaInfServices(OWLOntologyStorerFactory.class)
public class RioJsonOntologyStorerFactory implements OWLOntologyStorerFactory
{
    
    @Override
    public OWLOntologyStorer createStorer()
    {
        return new RioOntologyStorer(new RdfJsonOntologyFormatFactory());
    }
    
    @Override
    public OWLOntologyFormatFactory getFormatFactory()
    {
        return new RdfJsonOntologyFormatFactory();
    }
    
}
