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

import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.util.OWLObjectTypeIndexProvider;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import com.google.common.base.Optional;

/**
 * An OWLLiteral with xsd:string datatype and no language tag
 * 
 * @author Matthew Horridge, The University Of Manchester, Bio-Health
 *         Informatics Group, Date: 26-Oct-2006
 */
public class OWLLiteralImplString implements OWLLiteral {

    private static final long serialVersionUID = 30406L;
    @Nonnull
    private static final OWLDatatype XSD_STRING = new OWL2DatatypeImpl(OWL2Datatype.XSD_STRING);
    @Nonnull
    private final String literal;

    /**
     * @param literal
     *        the lexical form
     */
    public OWLLiteralImplString(@Nonnull String literal) {
        this.literal = literal;
    }

    private static int index() {
        return OWLObjectTypeIndexProvider.DATA_TYPE_INDEX_BASE + 8;
    }


    public String getLiteral() {
        return literal;
    }


    public boolean hasLang() {
        return false;
    }

    public int parseInteger() throws NumberFormatException {
        return Integer.parseInt(getLiteral());
    }

    public boolean isRDFPlainLiteral() {
        return false;
    }

    public boolean isInteger() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }


    public boolean isDouble() {
        return false;
    }


    public boolean isFloat() {
        return false;
    }


    public boolean parseBoolean() {
        throw new OWLRuntimeException(getClass().getName() + " does not have a boolean value");
    }


    public double parseDouble() {
        throw new OWLRuntimeException(getClass().getName() + " does not have a double value");
    }


    public float parseFloat() {
        throw new OWLRuntimeException(getClass().getName() + " does not have a float value");
    }


    public String getLang() {
        return "";
    }


    public boolean hasLang(String l) {
        return false;
    }


    public OWLDatatype getDatatype() {
        return XSD_STRING;
    }

    @Override
    public int hashCode() {
        return literal.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OWLLiteral)) {
            return false;
        }
        OWLLiteral other = (OWLLiteral) obj;
        return getLiteral().equals(other.getLiteral()) && getDatatype().equals(other.getDatatype()) && getLang().equals(
            other.getLang());
    }


    public void accept(OWLDataVisitor visitor) {
        visitor.visit(this);
    }


    public <O> O accept(OWLDataVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public void accept(OWLAnnotationValueVisitor visitor) {
        visitor.visit(this);
    }


    public <O> O accept(OWLAnnotationValueVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    protected int compareObjectOfSameType(OWLObject object) {
        OWLLiteral other = (OWLLiteral) object;
        int diff = getLiteral().compareTo(other.getLiteral());
        if (diff != 0) {
            return diff;
        }
        diff = getDatatype().compareTo(other.getDatatype());
        if (diff != 0) {
            return diff;
        }
        return getLang().compareTo(other.getLang());
    }


    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }


    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public Set<OWLEntity> getSignature() {
        return Collections.singleton((OWLEntity) XSD_STRING);
    }


    public Set<OWLAnonymousIndividual> getAnonymousIndividuals() {
        return CollectionFactory.emptySet();
    }


    public Set<OWLClass> getClassesInSignature() {
        return CollectionFactory.emptySet();
    }


    public Set<OWLAnnotationProperty> getAnnotationPropertiesInSignature() {
        return CollectionFactory.emptySet();
    }


    public Set<OWLDataProperty> getDataPropertiesInSignature() {
        return CollectionFactory.emptySet();
    }


    public Set<OWLObjectProperty> getObjectPropertiesInSignature() {
        return CollectionFactory.emptySet();
    }


    public Set<OWLNamedIndividual> getIndividualsInSignature() {
        return CollectionFactory.emptySet();
    }


    public Set<OWLDatatype> getDatatypesInSignature() {
        return CollectionFactory.emptySet();
    }


    public Set<OWLClassExpression> getNestedClassExpressions() {
        return CollectionFactory.emptySet();
    }


    public boolean isTopEntity() {
        return false;
    }


    public boolean isBottomEntity() {
        return false;
    }


    public int compareTo(OWLObject o) {
        int thisTypeIndex = index();
        int otherTypeIndex = 0;
        if (o instanceof OWLObjectImplWithEntityAndAnonCaching) {
            otherTypeIndex = ((OWLObjectImplWithEntityAndAnonCaching) o).index();
        } else {
            otherTypeIndex = OWLObjectImplWithEntityAndAnonCaching.OWLOBJECT_TYPEINDEX_PROVIDER.getTypeIndex(o);
        }
        int diff = thisTypeIndex - otherTypeIndex;
        if (diff == 0) {
            // Objects are the same type
            return compareObjectOfSameType(o);
        } else {
            return diff;
        }
    }


    public boolean containsEntityInSignature(OWLEntity owlEntity) {
        return false;
    }


    public Optional<IRI> asIRI() {
        return Optional.absent();
    }


    public Optional<OWLAnonymousIndividual> asAnonymousIndividual() {
        return Optional.absent();
    }


    public Optional<OWLLiteral> asLiteral() {
        return Optional.<OWLLiteral> of(this);
    }
}
