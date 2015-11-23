/**
 * 
 */
package org.semanticweb.owlapi.rio;

import info.aduna.iteration.CloseableIteration;
import info.aduna.iteration.CloseableIteratorIteration;

import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.openrdf.OpenRDFException;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.semanticweb.owlapi.formats.OWLOntologyFormatFactory;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLRuntimeException;

/**
 * An implementation of the OWLOntologyDocumentSource interface that does not implement any of the
 * InputStream, Reader, or IRI source retrieval methods. Instead it holds a reference to an iterator
 * that will generate bare triples to be interpreted by a Sesame Rio based OWLParser implementation.
 * 
 * @author Peter Ansell p_ansell@yahoo.com
 * 
 */
public class RioMemoryTripleSource implements OWLOntologyDocumentSource
{
    
    private final Map<String, String> namespaces = new LinkedHashMap<String, String>();
    private final Iterator<Statement> statementIterator;
    private IRI documentIRI;
    private static final AtomicInteger counter = new AtomicInteger(0);
    
    public RioMemoryTripleSource(final Iterator<Statement> statements)
    {
        this.documentIRI = IRI.create("urn:rio-memory-triples:"+counter.incrementAndGet());
        this.statementIterator = statements;
    }
    
    public RioMemoryTripleSource(final Iterator<Statement> statements, final Map<String, String> namespaces)
    {
        this(statements);
        this.namespaces.putAll(namespaces);
    }
    
    /**
     * Creates a RioMemoryTripleSource using a closeable iteration. Internally this wraps the
     * statements as a
     */
    public RioMemoryTripleSource(final CloseableIteration<Statement, ? extends OpenRDFException> statements)
    {
        this.documentIRI = IRI.create("urn:rio-memory-triples:"+counter.incrementAndGet());
        this.statementIterator = new Iterator<Statement>()
            {
                
                @Override
                public void remove()
                {
                    throw new UnsupportedOperationException("Cannot remove statements using this iterator");
                }
                
                @Override
                public Statement next()
                {
                    Statement nextStatement = null;
                    try
                    {
                        nextStatement = statements.next();
                        
                        if(nextStatement != null)
                        {
                            return nextStatement;
                        }
                        else
                        {
                            throw new NoSuchElementException("No more statements in this iterator");
                        }
                    }
                    catch(OpenRDFException e)
                    {
                        throw new OWLRuntimeException("Found exception while iterating", e);
                    }
                    finally
                    {
                        if(nextStatement == null)
                        {
                            try
                            {
                                statements.close();
                            }
                            catch(OpenRDFException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                
                @Override
                public boolean hasNext()
                {
                    boolean result = false;
                    
                    try
                    {
                        result = statements.hasNext();
                        
                        return result;
                    }
                    catch(OpenRDFException e)
                    {
                        throw new OWLRuntimeException("Found exception while iterating", e);
                    }
                    finally
                    {
                        if(!result)
                        {
                            try
                            {
                                statements.close();
                            }
                            catch(OpenRDFException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
    }
    
    /**
     * Creates a RioMemoryTripleSource using a closeable iteration. Internally this wraps the
     * statements as an Iterator.
     */
    public RioMemoryTripleSource(final CloseableIteration<Statement, ? extends OpenRDFException> statements,
            final Map<String, String> namespaces)
    {
        this(statements);
        
        this.namespaces.putAll(namespaces);
        
    }
    
    @Override
    public boolean isReaderAvailable()
    {
        return false;
    }
    
    @Override
    public Reader getReader()
    {
        return null;
    }
    
    @Override
    public boolean isInputStreamAvailable()
    {
        return false;
    }
    
    @Override
    public InputStream getInputStream()
    {
        return null;
    }
    
    @Override
    public IRI getDocumentIRI()
    {
        return this.documentIRI;
    }
    
    @Override
    public boolean isFormatKnown()
    {
        return false;
    }
    
    @Override
    public OWLOntologyFormatFactory getFormatFactory()
    {
        return null;
    }
    
    public Map<String, String> getNamespaces()
    {
        return this.namespaces;
    }
    
    public Iterator<Statement> getStatementIterator()
    {
        return this.statementIterator;
    }
    
    public void setNamespaces(Map<String, String> nextNamespaces)
    {
        this.namespaces.clear();
        this.namespaces.putAll(nextNamespaces);
    }
    
    public void setNamespaces(RepositoryResult<Namespace> namespaces) throws RepositoryException
    {
        this.namespaces.clear();
        try
        {
            while(namespaces.hasNext())
            {
                Namespace nextNamespace = namespaces.next();
                this.namespaces.put(nextNamespace.getPrefix(), nextNamespace.getName());
            }
        }
        finally
        {
            namespaces.close();
        }
    }
}
