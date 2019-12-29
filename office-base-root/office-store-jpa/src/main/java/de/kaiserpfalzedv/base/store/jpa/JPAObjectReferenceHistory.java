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

import de.kaiserpfalzedv.base.store.jpa.folders.changes.JPAFolderChangeWithContent;
import de.kaiserpfalzedv.commons.ImmutableObjectReference;
import de.kaiserpfalzedv.commons.ObjectReference;
import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.Objects;


/**
 * @author rlichti
 * @since 2019-12-27T12:00Z
 */
@Entity
@Table(schema = "BASE", name = "OBJECT_REFERENCE_CHANGES")
public class JPAObjectReferenceHistory extends PanacheEntity {
    @Embedded
    public JPAIdentity identity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_FOLDER_ID")
    public JPAFolderChangeWithContent<?> folder;

    @Column(name = "_KIND")
    public String kind;
    @Column(name = "_VERSION")
    public String version;

    public static JPAObjectReferenceHistory fromModel(final ObjectReference model) {
        JPAObjectReferenceHistory result;

        try {
            result = JPAObjectReferenceHistory.find("identity.uuid", model.getMetadata().getIdentity().getUuid()).singleResult();
        } catch (NoResultException e) {
            result = new JPAObjectReferenceHistory();

            result.identity = new JPAIdentity().fromModel(model.getMetadata().getIdentity());
            result.kind = model.getKind();
            result.version = model.getVersion();
            result.persist();
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
        JPAObjectReferenceHistory that = (JPAObjectReferenceHistory) o;
        return identity.equals(that.identity) &&
                kind.equals(that.kind) &&
                version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity, kind, version);
    }
}
