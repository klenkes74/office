/*
 *  Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2019. All rights reserved.
 *
 *  This file is part of Kaiserpfalz EDV-Service Office.
 *
 *  This is free software: you can redistribute it and/or modify it under the terms of
 *   the GNU Lesser General Public License as published by the Free Software
 *   Foundation, either version 3 of the License.
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
import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.folders.api.FolderCommand;
import de.kaiserpfalzedv.folders.api.FolderCommandService;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableCloseFolder.class)
@JsonDeserialize(builder = ImmutableCloseFolder.Builder.class)
public interface DeleteFolder extends FolderCommand {
    String DELETE_FOLDER_KIND = Folder.KIND;
    String DELETE_FOLDER_VERSION = Folder.VERSION;

    @Value.Default
    default FolderClosed execute(FolderCommandService service) {
        return (FolderClosed) service.execute(this);
    }

    @Override
    @Value.Default
    default FolderSpec apply(final FolderSpec orig) {
        return ImmutableFolderSpec.copyOf(orig)
                .withMetadata(
                        ImmutableMetadata.copyOf(getMetadata())
                                .withInvalidAfter(getMetadata().getCreated())
                );
    }
}
