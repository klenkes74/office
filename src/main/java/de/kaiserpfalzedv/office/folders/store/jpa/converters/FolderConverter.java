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

package de.kaiserpfalzedv.office.folders.store.jpa.converters;

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.store.DataConverter;
import de.kaiserpfalzedv.office.folders.Folder;
import de.kaiserpfalzedv.office.folders.ImmutableFolder;
import de.kaiserpfalzedv.office.folders.ImmutableFolderSpec;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolder;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolderSpec;
import de.kaiserpfalzedv.office.folders.store.jpa.base.JPAIdentity;

import javax.enterprise.context.Dependent;
import java.util.Optional;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-15 18:40
 */
@Dependent
public class FolderConverter implements DataConverter<Folder, JPAFolder> {
    @Override
    public Folder convertToAPI(final JPAFolder data) {
        ObjectIdentifier identity = ImmutableObjectIdentifier.builder()
                .kind(Folder.KIND)
                .version(Folder.KIND)

                .uuid(data.identity.uuid)
                .scope(Optional.ofNullable(data.identity.scope))
                .name(Optional.ofNullable(data.identity.key))

                .build();

        return ImmutableFolder.builder()
                .kind(Folder.KIND)
                .version(Folder.VERSION)

                .metadata(ImmutableMetadata.builder()
                        .identity(identity)
                        .build()
                )

                .spec(ImmutableFolderSpec.builder()
                        .name(data.spec.name)
                        .shortName(Optional.ofNullable(data.spec.shortName))
                        .description(Optional.ofNullable(data.spec.description))

                        .created(data.spec.created)
                        .modified(data.spec.modified)
                        .build()
                )

                .build();
    }

    @Override
    public JPAFolder convertFromAPI(final Folder folder) {
        JPAFolder result = new JPAFolder();
        result.spec = new JPAFolderSpec();

        result.identity = new JPAIdentity();
        result.identity.uuid = folder.getSpec().getIdentity().getUuid();
        result.identity.scope = folder.getSpec().getIdentity().getScope().orElse(null);
        result.identity.key = folder.getSpec().getIdentity().getName().orElse(null);

        result.spec.name = folder.getSpec().getName();
        result.spec.shortName = folder.getSpec().getShortName().orElse(null);
        result.spec.description = folder.getSpec().getDescription().orElse(null);
        result.spec.closed = folder.getSpec().getClosed().orElse(null);

        result.spec.created = folder.getSpec().getCreated();
        result.spec.modified = folder.getSpec().getModified();

        return result;
    }
}
