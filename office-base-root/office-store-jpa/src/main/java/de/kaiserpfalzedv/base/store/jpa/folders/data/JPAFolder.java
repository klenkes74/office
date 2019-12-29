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

package de.kaiserpfalzedv.base.store.jpa.folders.data;

import de.kaiserpfalzedv.base.store.jpa.JPAObjectReference;
import de.kaiserpfalzedv.commons.ObjectReference;
import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.ImmutableFolder;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-17 09:47
 */
@Entity
@Table(name = "FOLDERS", schema = "BASE")
public class JPAFolder extends PanacheEntity implements Serializable {
    @Embedded
    public JPAFolderSpec spec;

    @OneToMany(
            mappedBy = "folder",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    public List<JPAObjectReference> contents = new ArrayList<>();


    public static PanacheQuery<JPAFolder> findByUuid(final String tenant, final UUID uuid) {
        return find("spec.identity.tenant=?1 and spec.identity.uuid=?2", tenant, uuid);
    }

    public static PanacheQuery<JPAFolder> findByTenant(final String tenant) {
        return find("spec.identity.tenant=?1", tenant);
    }

    public static PanacheQuery<JPAFolder> findByTenantAndKey(final String tenant, final String key) {
        return find("spec.identity.tenant=?1 and spec.identity.key=?2", tenant, key);
    }

    public void addContent(JPAObjectReference content) {
        if (!contents.contains(content)) {
            contents.add(content);
            content.folder = this;
        }
    }

    public void removeContent(JPAObjectReference content) {
        if (contents.contains(content)) {
            contents.remove(content);
            content.folder = null;
        }
    }

    public JPAFolder fromModel(final Folder folder) {
        spec = new JPAFolderSpec().fromModel(folder.getEnvelope());

        List<JPAObjectReference> newContents = getNewContentFromModel(folder);
        removeOldContent(newContents);
        addNewContent(newContents);

        return this;
    }

    @NotNull
    public List<JPAObjectReference> getNewContentFromModel(Folder folder) {
        List<JPAObjectReference> newContents = new ArrayList<>(contents);
        folder.getSpec().forEach(e -> newContents.add(JPAObjectReference.fromModel(e)));
        return newContents;
    }

    public void removeOldContent(List<JPAObjectReference> newContents) {
        for (JPAObjectReference e : contents) {
            if (!newContents.contains(e)) {
                removeContent(e);
            }
        }
    }

    public void addNewContent(List<JPAObjectReference> newContents) {
        for (JPAObjectReference e : newContents) {
            addContent(e);
        }
    }

    public Folder toModel() {
        ConcurrentSkipListSet<ObjectReference> contentModel = new ConcurrentSkipListSet<>();
        contents.forEach(d -> contentModel.add(d.toModel()));

        return ImmutableFolder.builder()
                .kind(Folder.KIND)
                .version(Folder.VERSION)

                .metadata(ImmutableMetadata.builder()
                        .identity(spec.identity.toModel(Folder.KIND, Folder.VERSION))
                        .build()
                )

                .envelope(spec.toModel())

                .spec(contentModel)

                .build();
    }
}
