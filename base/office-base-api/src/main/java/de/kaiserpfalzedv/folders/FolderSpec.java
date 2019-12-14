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
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.api.Metadata;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.api.spec.Spec;
import org.immutables.value.Value;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * This is the base folder. It is used to organize other objects.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableFolderSpec.class)
@JsonDeserialize(builder = ImmutableFolderSpec.Builder.class)
public interface FolderSpec extends Spec<FolderSpec> {
    UUID getUuid();
    Optional<String> getScope();
    String getKey();
    String getName();

    Optional<String> getShortName();
    Optional<String> getDescription();

    Optional<OffsetDateTime> getClosed();
    OffsetDateTime getCreated();
    OffsetDateTime getModified();

    @Value.Default
    default ObjectIdentifier getIdentity() {
        return ImmutableObjectIdentifier.builder()
                .kind(Folder.KIND)
                .version(Folder.VERSION)
                .uuid(getUuid())
                .scope(getScope())
                .name(getKey())
                .build();
    }

    @Value.Default
    default Metadata getMetadata() {
        return ImmutableMetadata.builder()
                .identity(getIdentity())
                .created(getCreated())
                .modified(getModified())
                .build();
    }
}
