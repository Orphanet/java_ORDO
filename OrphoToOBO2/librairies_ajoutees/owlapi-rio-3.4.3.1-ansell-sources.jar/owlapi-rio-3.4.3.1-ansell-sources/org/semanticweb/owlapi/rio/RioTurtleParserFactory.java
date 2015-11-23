package org.semanticweb.owlapi.rio;

import org.kohsuke.MetaInfServices;
import org.semanticweb.owlapi.formats.RioRDFOntologyFormatFactory;
import org.semanticweb.owlapi.formats.TurtleOntologyFormatFactory;
import org.semanticweb.owlapi.io.OWLParserFactory;

/**
 * 
 * @author Peter Ansell p_ansell@yahoo.com
 * 
 */
@MetaInfServices(OWLParserFactory.class)
public class RioTurtleParserFactory extends AbstractRioParserFactory implements OWLParserFactory
{
    
    @Override
    public RioRDFOntologyFormatFactory getRioFormatFactory()
    {
        return new TurtleOntologyFormatFactory();
    }
}
