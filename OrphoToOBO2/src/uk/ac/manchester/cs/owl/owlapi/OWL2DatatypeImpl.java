/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package uk.ac.manchester.cs.owl.owlapi;

import static org.semanticweb.owlapi.util.OWLAPIPreconditions.checkNotNull;
import static org.semanticweb.owlapi.vocab.OWL2Datatype.*;

import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.util.HashCode;
import org.semanticweb.owlapi.util.OWLObjectTypeIndexProvider;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

/**
 * An optimised implementation of OWLDatatype for OWL2Datatypes.
 * 
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics
 *         Research Group
 * @since 3.2.0
 */
public class OWL2DatatypeImpl implements OWLDatatype {

    private static final long serialVersionUID = 40000L;

    /**
     * Creates an instance of {@code OWLDatatypeImplForOWL2Datatype} for the
     * specified {@link OWL2Datatype}.
     * 
     * @param owl2Datatype
     *        The datatype. Not {@code null}.
     * @throws NullPointerException
     *         if {@code owl2Datatype} is {@code null}.
     */
    public OWL2DatatypeImpl(@Nonnull OWL2Datatype owl2Datatype) {
        this.owl2Datatype = checkNotNull(owl2Datatype, "owl2Datatype must not be null");
    }

    @Nonnull
    private final OWL2Datatype owl2Datatype;


    public OWL2Datatype getBuiltInDatatype() {
        return owl2Datatype;
    }


    public boolean isString() {
        return owl2Datatype == XSD_STRING;
    }


    public boolean isInteger() {
        return owl2Datatype == XSD_INTEGER;
    }


    public boolean isFloat() {
        return owl2Datatype == XSD_FLOAT;
    }


    public boolean isDouble() {
        return owl2Datatype == XSD_DOUBLE;
    }


    public boolean isBoolean() {
        return owl2Datatype == XSD_BOOLEAN;
    }


    public boolean isRDFPlainLiteral() {
        return owl2Datatype == RDF_PLAIN_LITERAL;
    }


    public boolean isDatatype() {
        return true;
    }


    public boolean isTopDatatype() {
        return owl2Datatype == RDFS_LITERAL;
    }

    @Nonnull

    public OWLDatatype asOWLDatatype() {
        return this;
    }


    public DataRangeType getDataRangeType() {
        return DataRangeType.DATATYPE;
    }


    public void accept(OWLDataVisitor visitor) {
        visitor.visit(this);
    }


    public <O> O accept(OWLDataVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public void accept(OWLDataRangeVisitor visitor) {
        visitor.visit(this);
    }


    public <O> O accept(OWLDataRangeVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public EntityType<?> getEntityType() {
        return EntityType.DATATYPE;
    }


    public boolean isType(EntityType<?> entityType) {
        return EntityType.DATATYPE.equals(entityType);
    }


    public boolean isBuiltIn() {
        return true;
    }


    public boolean isOWLClass() {
        return false;
    }


    public OWLClass asOWLClass() {
        throw new UnsupportedOperationException("Not an OWLClass");
    }


    public boolean isOWLObjectProperty() {
        return false;
    }


    public OWLObjectProperty asOWLObjectProperty() {
        throw new UnsupportedOperationException("Not an OWLObjectProperty");
    }


    public boolean isOWLDataProperty() {
        return false;
    }


    public OWLDataProperty asOWLDataProperty() {
        throw new UnsupportedOperationException("Not an OWLDataProperty");
    }


    public boolean isOWLNamedIndividual() {
        return false;
    }


    public OWLNamedIndividual asOWLNamedIndividual() {
        throw new UnsupportedOperationException("Not an OWLNamedIndividual");
    }


    public boolean isOWLDatatype() {
        return true;
    }


    public boolean isOWLAnnotationProperty() {
        return false;
    }


    public OWLAnnotationProperty asOWLAnnotationProperty() {
        throw new UnsupportedOperationException("Not an OWLAnnotationProperty");
    }


    public String toStringID() {
        return owl2Datatype.getIRI().toString();
    }

    @Override
    public String toString() {
        return toStringID();
    }


    public void accept(OWLEntityVisitor visitor) {
        visitor.visit(this);
    }


    public <O> O accept(OWLEntityVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public IRI getIRI() {
        return owl2Datatype.getIRI();
    }


    public void accept(OWLNamedObjectVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }


    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public <O> O accept(OWLNamedObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OWLDatatype)) {
            return false;
        }
        OWLDatatype other = (OWLDatatype) obj;
        return owl2Datatype.getIRI().equals(other.getIRI());
    }


    public Set<OWLEntity> getSignature() {
        return CollectionFactory.getCopyOnRequestSetFromImmutableCollection(Collections.<OWLEntity> singleton(this));
    }


    public boolean containsEntityInSignature(OWLEntity owlEntity) {
        return equals(owlEntity);
    }


    public Set<OWLAnonymousIndividual> getAnonymousIndividuals() {
        return CollectionFactory.getCopyOnRequestSetFromImmutableCollection(CollectionFactory
            .<OWLAnonymousIndividual> emptySet());
    }


    public Set<OWLClass> getClassesInSignature() {
        return CollectionFactory.getCopyOnRequestSetFromImmutableCollection(CollectionFactory.<OWLClass> emptySet());
    }


    public Set<OWLDataProperty> getDataPropertiesInSignature() {
        return CollectionFactory.getCopyOnRequestSetFromImmutableCollection(CollectionFactory
            .<OWLDataProperty> emptySet());
    }


    public Set<OWLObjectProperty> getObjectPropertiesInSignature() {
        return CollectionFactory.getCopyOnRequestSetFromImmutableCollection(CollectionFactory
            .<OWLObjectProperty> emptySet());
    }


    public Set<OWLAnnotationProperty> getAnnotationPropertiesInSignature() {
        return CollectionFactory.getCopyOnRequestSetFromImmutableCollection(CollectionFactory
            .<OWLAnnotationProperty> emptySet());
    }


    public Set<OWLNamedIndividual> getIndividualsInSignature() {
        return CollectionFactory.getCopyOnRequestSetFromImmutableCollection(CollectionFactory
            .<OWLNamedIndividual> emptySet());
    }


    public Set<OWLDatatype> getDatatypesInSignature() {
        return CollectionFactory.getCopyOnRequestSetFromImmutableCollection(Collections.singleton((OWLDatatype) this));
    }


    public Set<OWLClassExpression> getNestedClassExpressions() {
        return CollectionFactory.getCopyOnRequestSetFromImmutableCollection(CollectionFactory
            .<OWLClassExpression> emptySet());
    }


    public boolean isTopEntity() {
        return owl2Datatype == RDF_PLAIN_LITERAL;
    }


    public boolean isBottomEntity() {
        return false;
    }


    public int compareTo(OWLObject o) {
        if (!(o instanceof OWLDatatype)) {
            OWLObjectTypeIndexProvider provider = new OWLObjectTypeIndexProvider();
            return provider.getTypeIndex(o);
        }
        OWLDatatype other = (OWLDatatype) o;
        return getIRI().compareTo(other.getIRI());
    }

    @Override
    public int hashCode() {
        return HashCode.hashCode(this);
    }
}
