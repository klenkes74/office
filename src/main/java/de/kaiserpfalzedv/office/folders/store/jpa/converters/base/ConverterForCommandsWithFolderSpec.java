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

import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.store.DataConverter;
import de.kaiserpfalzedv.office.folders.FolderSpec;
import de.kaiserpfalzedv.office.folders.ImmutableFolderSpec;
import de.kaiserpfalzedv.office.folders.api.FolderCommandWithSpec;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolderChangeWithSpec;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolderSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-17 10:22
 */
public abstract class ConverterForCommandsWithFolderSpec<A extends FolderCommandWithSpec, D extends JPAFolderChangeWithSpec> extends ConverterForCommandsWithoutFolderSpec<A, D> implements DataConverter<A, D> {
    protected void convert(
            @NotNull final A data,
            @NotNull final D result
    ) {
        super.convert(data, result);

        convertSpec(data, result);
    }

    private void convertSpec(@NotNull A data, @NotNull D result) {
        result.spec = new JPAFolderSpec();
        result.spec.name = data.getSpec().getName();
        result.spec.shortName = data.getSpec().getShortName().orElse(null);
        result.spec.description = data.getSpec().getDescription().orElse(null);

        result.spec.created = data.getSpec().getCreated();
        result.spec.modified = data.getSpec().getModified();
    }


    protected FolderSpec createSpec(
            @NotNull final D data,
            @NotNull final ObjectIdentifier identity
    ) {
        return ImmutableFolderSpec.builder()
                .identity(identity)

                .name(data.spec.name)
                .shortName(Optional.ofNullable(data.spec.shortName))
                .description(Optional.ofNullable(data.spec.description))

                .created(data.spec.created)
                .modified(data.spec.modified)
                .build();
    }
}
