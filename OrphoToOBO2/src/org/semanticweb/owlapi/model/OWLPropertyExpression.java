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
package org.semanticweb.owlapi.model;

import javax.annotation.Nonnull;

/**
 * Represents a property or possibly the inverse of a property.
 * 
 * @author Matthew Horridge, The University Of Manchester, Bio-Health
 *         Informatics Group
 * @since 2.0.0
 */
public interface OWLPropertyExpression extends OWLObject {

    /**
     * Determines if this property expression is anonymous.
     * 
     * @return {@code true} if the property expression is anonymous (because it
     *         is the inverse of a property). {@code false} if this property is
     *         a named object property or named data property.
     */
    boolean isAnonymous();

    /**
     * @param visitor
     *        visitor to accept
     */
    void accept(@Nonnull OWLPropertyExpressionVisitor visitor);

    /**
     * @param visitor
     *        visitor to accept
     * @param <O>
     *        visitor return type
     * @return visitor value
     */
    @Nonnull
    <O> O accept(@Nonnull OWLPropertyExpressionVisitorEx<O> visitor);

    /** @return true if this is a data property */
    boolean isDataPropertyExpression();

    /** @return true if this is an object property */
    boolean isObjectPropertyExpression();

    /**
     * Determines if this is the owl:topObjectProperty.
     * 
     * @return {@code true} if this property is the owl:topObjectProperty
     *         otherwise {@code false}
     */
    boolean isOWLTopObjectProperty();

    /**
     * Determines if this is the owl:bottomObjectProperty.
     * 
     * @return {@code true} if this property is the owl:bottomObjectProperty
     *         otherwise {@code false}
     */
    boolean isOWLBottomObjectProperty();

    /**
     * Determines if this is the owl:topDataProperty.
     * 
     * @return {@code true} if this property is the owl:topDataProperty
     *         otherwise {@code false}
     */
    boolean isOWLTopDataProperty();

    /**
     * Determines if this is the owl:bottomDataProperty.
     * 
     * @return {@code true} if this property is the owl:bottomDataProperty
     *         otherwise {@code false}
     */
    boolean isOWLBottomDataProperty();
}
