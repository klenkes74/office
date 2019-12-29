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

package de.kaiserpfalzedv.commons.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableObjectIdentity.class)
@JsonDeserialize(builder = ImmutableObjectIdentity.Builder.class)
public interface ObjectIdentity extends KindHolding, Serializable, Comparable<ObjectIdentity> {
    /**
     * The single point of truth of uniqueness. No two objects of the world should have the same UUID.
     *
     * @return The UUID of the object. This identifier is universal unique.
     */
    UUID getUuid();

    /**
     * The tenant or scope of the object. The tuple (Kind, Scope, Name) has to be unique. The interpretation of this
     * scope may differ in different subsystems.
     *
     * @return the tenant of this object.
     */
    String getTenant();

    /**
     * A unique within scope name for the object. The tuple (Kind, Scope, Name) has to be unique.
     *
     * @return the name that is unique within the scope for a certain kind of objects.
     */
    String getKey();

    /**
     * This is the owning object. The exact meaning may differ between different domains.
     *
     * @return The owning object of the current one.
     */
    Optional<ObjectIdentity> getOwner();

    @Override
    default int compareTo(@NotNull ObjectIdentity other) {
        int tenantComparison = getTenant().compareTo(other.getTenant());
        int nameComparison = getKey().compareTo(other.getKey());

        return tenantComparison != 0 ? tenantComparison : nameComparison;
    }
}