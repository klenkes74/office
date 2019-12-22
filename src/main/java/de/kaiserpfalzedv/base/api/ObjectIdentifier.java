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

package de.kaiserpfalzedv.base.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableObjectIdentifier.class)
@JsonDeserialize(builder = ImmutableObjectIdentifier.Builder.class)
public interface ObjectIdentifier extends Serializable {

    /**
     * Creates an ObjectIdentifier with UUID and without scope and key.
     *
     * @param kind    The kind of the identifier object.
     * @param version The version of the identified object kind.
     * @param uuid    The UUID of the object (may not be null).
     * @return an immutable ObjectIdentifier
     */
    static ImmutableObjectIdentifier create(final String kind, final String version, final UUID uuid) {
        return ImmutableObjectIdentifier.builder()
                .kind(kind)
                .version(version)
                .uuid(uuid)
                .build();
    }

    /**
     * Creates an ObjectIdentifier with tenant and key. The UUID is randomized, so please beware if this is applicable
     * on your use case!
     *
     * @param kind    The kind of the identifier object.
     * @param version The version of the identified object kind.
     * @param tenant  The tenant of the object (may be null).
     * @param key     The ke of the object (may not be null).
     * @return an immutable ObjectIdentifier
     */
    static ImmutableObjectIdentifier create(
            final String kind,
            final String version,
            final String tenant,
            final String key
    ) {
        return ImmutableObjectIdentifier.builder()
                .kind(kind)
                .version(version)
                .uuid(UUID.randomUUID())
                .tenant(Optional.ofNullable(tenant))
                .name(Optional.of(key))
                .build();
    }

    /**
     * Tpye of the object that is identified.
     *
     * @return the unique type of the object.
     */
    String getKind();

    /**
     * The version of the object type.
     *
     * @return the version of the object specified by {@link #getKind()}.
     */
    String getVersion();

    /**
     * The single point of truth of uniqueness. No two objects of the world should have the same UUID.
     *
     * @return The UUID of the object. This identifier is universal unique.
     */
    UUID getUuid();

    /**
     * The tenant or scope of the object. The tuple (Kind, Scope, Name) has to be unique. The interpretation of this
     * scope may differ in different subsystems. You may use it for multitenancy.
     *
     * @return the scope of this object.
     */
    Optional<String> getTenant();

    /**
     * A unique within scope name for the object. The tuple (Kind, Scope, Name) has to be unique.
     *
     * @return the name that is unique within the scope for a certain kind of objects.
     */
    Optional<String> getName();

    /**
     * This is the owning object. The exact meaning may differ between different domains.
     *
     * @return The owning object of the current one.
     */
    Optional<ObjectIdentifier> getOwner();
}