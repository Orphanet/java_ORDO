/**
 * 
 */
package org.semanticweb.owlapi.rio.utils;

import java.util.Comparator;

import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;

/**
 * @author Peter Ansell p_ansell@yahoo.com
 * 
 */
public class OWLAPICompatibleComparator implements Comparator<Statement>
{
    private final Comparator<Value> valueComparator = new OWLAPICompatibleValueComparator();
    
    public final static int BEFORE = -1;
    public final static int EQUALS = 0;
    public final static int AFTER = 1;
    
    @Override
    public int compare(final Statement first, final Statement second)
    {
        if(first == second)
        {
            return OWLAPICompatibleComparator.EQUALS;
        }
        
        // sort rdf.type statements before others
        if(first.getPredicate().equals(RDF.TYPE))
        {
            if(second.getPredicate().equals(RDF.TYPE))
            {
                // sort all rdf.type owl.ontology statements before all other statements
                if(first.getObject().equals(OWL.ONTOLOGY))
                {
                    if(second.getObject().equals(OWL.ONTOLOGY))
                    {
                        return this.valueComparator.compare(first.getSubject(), second.getSubject());
                    }
                    else
                    {
                        // load RDF.TYPE OWL.ONTOLOGY statements before all other statements
                        return OWLAPICompatibleComparator.BEFORE;
                    }
                }
                else if(second.getObject().equals(OWL.ONTOLOGY))
                {
                    // load everything after RDF.TYPE OWL.ONTOLOGY statements
                    return OWLAPICompatibleComparator.AFTER;
                }
                
                // OWL:EquivalentClass need to be loaded before RDFS:Datatype to ensure that the datatype is recognised as the IRI, and not the blank node
                if(first.getObject().equals(OWL.EQUIVALENTCLASS))
                {
                    if(second.getObject().equals(OWL.EQUIVALENTCLASS))
                    {
                        return this.valueComparator.compare(first.getSubject(), second.getSubject());
                    }
                    else
                    {
                        return OWLAPICompatibleComparator.BEFORE;
                    }
                }
                else if(second.getObject().equals(OWL.EQUIVALENTCLASS))
                {
                    return OWLAPICompatibleComparator.AFTER;
                }
                
                // RDFS:Datatype statements need to be loaded early to avoid issues with AbstractResourceTripleHandler.inferTypes not being able to infer types during 
                // streaming before they need to be used in handleStreaming and OWLRDFConsumer.translateDataRange, which calls OWLRDFConsumer.isDataRange
                // The element that is causing the circular calls to translateDataRange is the OptimisedListTranslator when it is used with DataRangeListItemTranslator
                if(first.getObject().equals(RDFS.DATATYPE))
                {
                    if(second.getObject().equals(RDFS.DATATYPE))
                    {
                        return this.valueComparator.compare(first.getSubject(), second.getSubject());
                    }
                    else
                    {
                        return OWLAPICompatibleComparator.BEFORE;
                    }
                }
                else if(second.getObject().equals(RDFS.DATATYPE))
                {
                    return OWLAPICompatibleComparator.AFTER;
                }
                // else
                // {
                // int subjectCompare = valueComparator.compare(first.getSubject(),
                // second.getSubject());
                //
                // if(subjectCompare == EQUALS)
                // {
                // return valueComparator.compare(first.getObject(), second.getObject());
                // }
                // else
                // {
                // return subjectCompare;
                // }
                // }
            }
            else
            {
                // RDF.TYPE comes before all others, to try to ensure that the types of entities are
                // available before the entities are used to create axioms etc.
                return OWLAPICompatibleComparator.BEFORE;
            }
        }
        else if(second.getPredicate().equals(RDF.TYPE))
        {
            return OWLAPICompatibleComparator.AFTER;
        }
        
        if(first.getSubject().equals(second.getSubject()))
        {
            if(first.getPredicate().equals(second.getPredicate()))
            {
                if(first.getObject().equals(second.getObject()))
                {
                    return OWLAPICompatibleComparator.EQUALS;
                }
                else
                {
                    return this.valueComparator.compare(first.getObject(), second.getObject());
                }
            }
            else
            {
                return this.valueComparator.compare(first.getPredicate(), second.getPredicate());
            }
        }
        else
        {
            return this.valueComparator.compare(first.getSubject(), second.getSubject());
        }
    }
    
}
