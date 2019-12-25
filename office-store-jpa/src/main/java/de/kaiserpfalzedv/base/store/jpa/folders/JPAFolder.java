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

package de.kaiserpfalzedv.base.store.jpa.folders;

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.ImmutableFolder;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

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


    public static PanacheQuery<JPAFolder> findByUuid(final String tenant, final UUID uuid) {
        return find("spec.identity.tenant=?1 and spec.identity.uuid=?2", tenant, uuid);
    }

    public static PanacheQuery<JPAFolder> findByTenant(final String tenant) {
        return find("spec.identity.tenant=?1", tenant);
    }

    public static PanacheQuery<JPAFolder> findByTenantAndKey(final String tenant, final String key) {
        return find("spec.identity.tenant=?1 and spec.identity.key=?2", tenant, key);
    }


    public JPAFolder fromModel(final Folder folder) {
        spec = new JPAFolderSpec().fromModel(folder.getSpec());

        return this;
    }

    public Folder toModel() {
        return ImmutableFolder.builder()
                .kind(Folder.KIND)
                .version(Folder.VERSION)

                .metadata(ImmutableMetadata.builder()
                        .identity(spec.identity.toModel(Folder.KIND, Folder.VERSION))
                        .build()
                )

                .spec(spec.toModel())

                .build();
    }
}
