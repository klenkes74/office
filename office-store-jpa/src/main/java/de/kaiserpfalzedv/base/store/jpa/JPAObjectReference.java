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

package de.kaiserpfalzedv.base.store.jpa;

import de.kaiserpfalzedv.base.ImmutableObjectReference;
import de.kaiserpfalzedv.base.ObjectReference;
import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.store.jpa.folders.data.JPAFolder;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Objects;


/**
 * @author rlichti
 * @since 2019-12-27T11:50
 */
@Entity
@Table(schema = "BASE", name = "OBJECT_REFERENCES")
public class JPAObjectReference extends PanacheEntity implements Comparable<JPAObjectReference> {
    @Embedded
    public JPAIdentity identity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_FOLDER_ID")
    public JPAFolder folder;

    @Column(name = "_KIND")
    public String kind;
    @Column(name = "_VERSION")
    public String version;

    public static JPAObjectReference fromModel(final ObjectReference model) {
        JPAObjectReference result;
        try {
            result = JPAObjectReference.find("identity.uuid", model.getMetadata().getIdentity().getUuid()).singleResult();
        } catch (NoResultException e) {
            result = new JPAObjectReference();

            result.identity = new JPAIdentity().fromModel(model.getMetadata().getIdentity());
            result.kind = model.getKind();
            result.version = model.getVersion();
        }

        return result;
    }

    public ObjectReference toModel() {
        return ImmutableObjectReference.builder()
                .metadata(ImmutableMetadata.builder()
                        .identity(identity.toModel(kind, version))
                        .build()
                )
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JPAObjectReference that = (JPAObjectReference) o;
        return identity.equals(that.identity) &&
                kind.equals(that.kind) &&
                version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity, kind, version);
    }

    @Override
    public int compareTo(@NotNull JPAObjectReference other) {
        int tenantComparison = identity.tenant.compareTo(other.identity.tenant);

        int nameComparison = (identity.key != null) ? identity.key.compareTo(other.identity.key) : -1;

        return tenantComparison != 0 ? tenantComparison : nameComparison;
    }
}
