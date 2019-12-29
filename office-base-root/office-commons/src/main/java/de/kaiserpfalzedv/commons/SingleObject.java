/*
 * Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2019. All rights reserved.
 *
 *  This file is part of Kaiserpfalz EDV-Service Office.
 *
 *  This is free software: you can redistribute it and/or modify it under the terms of
 *  the GNU Lesser General Public License as published by the Free Software
 *  Foundation, either version 3 of the License.
 *
 *  This file is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *  License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along
 *  with this file. If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package de.kaiserpfalzedv.commons;

import de.kaiserpfalzedv.commons.api.SpecHolding;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author rlichti
 * @since 2019-12-08
 */
public interface SingleObject<T extends Serializable> extends BaseObject<T>, SpecHolding<T> {
    @Value.Default
    @Value.Lazy
    default int compareTo(@NotNull SingleObject<T> other) {
        if (getSpec() instanceof Comparable && other.getSpec() instanceof Comparable) {
            //noinspection unchecked,rawtypes
            return ((Comparable) getSpec()).compareTo(other.getSpec());

        }

        return getMetadata().getIdentity().compareTo(other.getMetadata().getIdentity());
    }

    @Override
    @Value.Lazy
    default String getKind() {
        return getMetadata().getIdentity().getKind();
    }

    @Override
    @Value.Lazy
    default String getVersion() {
        return getMetadata().getIdentity().getVersion();
    }

}
