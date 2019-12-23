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

package de.kaiserpfalzedv.folders.store.jpa;

import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.folders.FolderSpec;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Embeddable
public class JPAFolderSpec implements Serializable {
    @Embedded
    public JPAIdentity identity;

    @Column(name = "_NAME", nullable = false)
    public String name;
    @Column(name = "_DISPLAYNAME")
    public String displayname;

    @Column(name = "_DESCRIPTION")
    public String description;
    @Column(name = "_CLOSED")
    public OffsetDateTime closed;

    @Column(name = "_CREATED", nullable = false)
    public OffsetDateTime created;
    @Column(name = "_MODIFIED", nullable = false)
    public OffsetDateTime modified;

    public JPAFolderSpec fromModel(final FolderSpec data) {
        identity = new JPAIdentity().fromModel(data.getIdentity());

        name = data.getName();
        displayname = data.getDisplayname().orElse(null);
        description = data.getDescription().orElse(null);
        closed = data.getClosed().orElse(null);
        created = data.getCreated();
        modified = data.getModified();

        return this;
    }

    public FolderSpec toModel() {
        return ImmutableFolderSpec().builder()
                // FIXME 2019-12-22 rlichti Implement the toModel() for JPAFolderSpec
                .build();
    }
}
