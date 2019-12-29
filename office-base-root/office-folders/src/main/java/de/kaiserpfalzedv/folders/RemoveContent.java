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
import de.kaiserpfalzedv.commons.ObjectReference;
import de.kaiserpfalzedv.folders.api.FolderCommand;
import org.immutables.value.Value;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * The action to remove content from a folder.
 *
 * @author rlichti@kaiserpfalz-edv.de
 * @since 2019-12-15T12:50Z
 */
@Value.Immutable
@JsonSerialize(as = ImmutableRemoveContent.class)
@JsonDeserialize(builder = ImmutableRemoveContent.Builder.class)
public interface RemoveContent extends FolderCommand {
    String KIND = "de.kaiserpfalzedv.folders.RemoveContent";
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

    @Value.Redacted
    ConcurrentSkipListSet<ObjectReference> getData();

    @Override
    @Value.Default
    default FolderSpec apply(final FolderSpec orig) {
        return orig;
    }
}
