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

import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.store.DataConverter;
import de.kaiserpfalzedv.office.folders.FolderSpec;
import de.kaiserpfalzedv.office.folders.ImmutableFolderSpec;
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
public class FolderSpecConverter implements DataConverter<FolderSpec, JPAFolderSpec> {
    @Override
    public FolderSpec convertToAPI(final JPAFolderSpec data) {
        ObjectIdentifier identity = ImmutableObjectIdentifier.builder()
                .kind(FolderSpec.KIND)
                .version(FolderSpec.KIND)

                .uuid(data.identity.uuid)
                .scope(Optional.ofNullable(data.identity.scope))
                .name(Optional.ofNullable(data.identity.key))

                .build();

        return ImmutableFolderSpec.builder()
                .identity(identity)

                .name(data.name)
                .shortName(Optional.ofNullable(data.shortName))
                .description(Optional.ofNullable(data.description))

                .created(data.created)
                .modified(data.modified)

                .build();
    }

    @Override
    public JPAFolderSpec convertFromAPI(final FolderSpec spec) {
        JPAFolderSpec result = new JPAFolderSpec();

        result.identity = new JPAIdentity();
        result.identity.uuid = spec.getIdentity().getUuid();
        result.identity.scope = spec.getIdentity().getScope().orElse(null);
        result.identity.key = spec.getIdentity().getName().orElse(null);

        result.name = spec.getName();
        result.shortName = spec.getShortName().orElse(null);
        result.description = spec.getDescription().orElse(null);
        result.closed = spec.getClosed().orElse(null);

        result.created = spec.getCreated();
        result.modified = spec.getModified();

        return result;
    }
}
