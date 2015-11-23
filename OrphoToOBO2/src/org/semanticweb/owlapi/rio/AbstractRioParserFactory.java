package org.semanticweb.owlapi.rio;

import org.semanticweb.owlapi.formats.OWLOntologyFormatFactory;
import org.semanticweb.owlapi.formats.RioRDFOntologyFormatFactory;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserFactory;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public abstract class AbstractRioParserFactory implements OWLParserFactory
{
    
    public AbstractRioParserFactory()
    {
        super();
    }
    
    @Override
    public OWLParser createParser(final OWLOntologyManager owlOntologyManager)
    {
        return new RioParserImpl(this.getRioFormatFactory());
    }
    
    public final OWLOntologyFormatFactory getFormatFactory() {
        return getRioFormatFactory();
    }
    
    public abstract RioRDFOntologyFormatFactory getRioFormatFactory();
    
}