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

package de.kaiserpfalzedv.office.folders;

import de.kaiserpfalzedv.office.folders.api.FolderCommand;
import de.kaiserpfalzedv.office.folders.api.FolderCommandService;
import org.immutables.value.Value;

@Value.Immutable
@Value.Modifiable
public interface CreateFolder extends FolderCommand {
    public static final String KIND = "de.kaiserpfalzedv.office.folders.CreateFolder";

    @Value.Default
    default String getKind() { return KIND; }

    default FolderCreated execute(FolderCommandService service) {
        return (FolderCreated) service.execute(this);
    }
}
