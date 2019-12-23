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

package de.kaiserpfalzedv.folders;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.base.actions.commands.ModifyCommand;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.folders.api.FolderCommandWithSpec;
import org.immutables.value.Value;

/**
 * The action to close a folder. The exact meaning of a closed folder depends on the using application. Normally closed
 * should mean no further changes to the contents may be done.
 *
 * @author rlichti@kaiserpfalz-edv.de
 * @since 2019-12-15T10:20Z
 */
@Value.Immutable
@JsonSerialize(as = ImmutableModifyFolder.class)
@JsonDeserialize(builder = ImmutableModifyFolder.Builder.class)
public interface ModifyFolder extends FolderCommandWithSpec, ModifyCommand<FolderSpec> {
    String KIND = "de.kaiserpfalzedv.folders.ModifyFolder";
    String VERSION = "1.0.0";

    @Override
    @Value.Default
    default String getKind() {
        return KIND;
    }

    @Override
    @Value.Default
    default String getVersion() {
        return VERSION;
    }

    @Override
    @Value.Default
    default FolderSpec apply(final FolderSpec orig) {
        return ImmutableFolderSpec.copyOf(orig)
                .withIdentity(ImmutableObjectIdentifier.builder()
                        .from(getSpec().getIdentity())
                        .name(getSpec().getName())
                        .build()
                )
                .withName(getSpec().getName())
                .withDisplayname(getSpec().getDisplayname())
                .withDescription(getSpec().getDescription())
                .withClosed(getSpec().getClosed())
                .withModified(getSpec().getModified());
    }
}
