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

package de.kaiserpfalzedv.base.store.jpa.folders.changes;

import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.base.store.jpa.JPAObjectReferenceHistory;
import de.kaiserpfalzedv.commons.ObjectReference;
import de.kaiserpfalzedv.folders.api.FolderCommand;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;


@Entity
public abstract class JPAFolderChangeWithContent<T extends FolderCommand> extends JPAFolderChange<T> {
    @Embedded
    public JPAIdentity identity;

    @OneToMany(
            mappedBy = "folder",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    public List<JPAObjectReferenceHistory> contents = new ArrayList<>();

    public void addContent(JPAObjectReferenceHistory content) {
        if (!contents.contains(content)) {
            contents.add(content);
            content.folder = this;
        }
    }

    public ConcurrentSkipListSet<ObjectReference> getDataModel() {
        ConcurrentSkipListSet<ObjectReference> result = new ConcurrentSkipListSet<>();

        contents.forEach(d -> result.add(d.toModel()));

        return result;
    }
}
