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

package de.kaiserpfalzedv.office.folders.store.jpa.converters.base;

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.api.Metadata;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.store.DataConverter;
import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.office.folders.Folder;
import de.kaiserpfalzedv.office.folders.FolderSpec;
import de.kaiserpfalzedv.office.folders.ImmutableFolder;
import de.kaiserpfalzedv.office.folders.ImmutableFolderSpec;
import de.kaiserpfalzedv.office.folders.api.FolderResult;
import de.kaiserpfalzedv.office.folders.api.FolderResultWithSpec;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolder;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolderSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-17 10:22
 */
public abstract class ConverterForFolder<A extends FolderResult, D extends JPAFolder> implements DataConverter<A, D> {
    protected void convert(A data, D result) {
        convertIdentity(data, result);

        if (FolderResultWithSpec.class.isAssignableFrom(data.getClass())) {
            FolderSpec spec = (FolderSpec) ((FolderResultWithSpec) data).getSpec();

            if (spec != null) {
                convertSpec(spec, result);
            } else {
                throw new IllegalArgumentException("The data contains no spec element: " + data);
            }
        }
    }

    private void convertIdentity(A data, D result) {
        result.identity = new JPAIdentity();
        result.identity.uuid = data.getMetadata().getIdentity().getUuid();
        result.identity.uuid = data.getMetadata().getIdentity().getUuid();
        result.identity.tenant = data.getMetadata().getIdentity().getTenant().orElse(null);
        result.identity.key = data.getMetadata().getIdentity().getName().orElse(null);
    }

    private void convertSpec(@NotNull FolderSpec data, D result) {
        result.spec = new JPAFolderSpec();
        result.spec.name = data.getName();
        result.spec.shortName = data.getDisplayname().orElse(null);
        result.spec.description = data.getDescription().orElse(null);
        result.spec.closed = data.getClosed().orElse(null);
        result.spec.created = data.getCreated();
        result.spec.modified = data.getModified();
    }

    protected Folder convert(D data) {
        ObjectIdentifier identity = createSpecIdentity(data.identity, Folder.KIND, Folder.VERSION);

        FolderSpec spec = ImmutableFolderSpec.builder()
                .identity(identity)

                .name(data.spec.name)
                .displayname(Optional.ofNullable(data.spec.shortName))
                .description(Optional.ofNullable(data.spec.description))
                .closed(Optional.ofNullable(data.spec.closed))

                .created(data.spec.created)
                .modified(data.spec.modified)

                .build();

        return ImmutableFolder.builder()
                .kind(Folder.KIND)
                .version(Folder.VERSION)

                .metadata(createMetadata(identity))

                .spec(spec)

                .build();
    }

    protected Metadata createMetadata(ObjectIdentifier identity) {
        return ImmutableMetadata.builder()
                .identity(identity)
                .build();
    }

    protected ObjectIdentifier createSpecIdentity(
            @NotNull final JPAIdentity identity,
            @NotNull final String kind,
            @NotNull final String version
    ) {
        return ImmutableObjectIdentifier.builder()
                .kind(kind)
                .version(version)

                .uuid(identity.uuid)
                .tenant(Optional.ofNullable(identity.tenant))
                .name(Optional.ofNullable(identity.key))
                .build();
    }
}
