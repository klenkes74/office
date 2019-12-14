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
import de.kaiserpfalzedv.folders.api.FolderCommand;
import de.kaiserpfalzedv.folders.api.FolderCommandService;
import org.immutables.value.Value;

/**
 * The action to close a folder. The exact meaning of a closed folder depends on the using application. Normally closed
 * should mean no further changes to the contents may be done.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableCloseFolder.class)
@JsonDeserialize(builder = ImmutableCloseFolder.Builder.class)
public interface CloseFolder extends FolderCommand {
    String KIND = Folder.KIND;
    String VERSION = Folder.VERSION;

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

    @Value.Default
    default FolderClosed execute(FolderCommandService service) {
        return (FolderClosed) service.execute(this);
    }

    @Override
    @Value.Default
    default FolderSpec apply(final FolderSpec orig) {
        return ImmutableFolderSpec.copyOf(orig)
                .withClosed(getSpec().orElseThrow(IllegalStateException::new).getClosed());
    }
}
