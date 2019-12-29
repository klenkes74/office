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

package de.kaiserpfalzedv.folders.store;

import de.kaiserpfalzedv.commons.ImmutableObjectReference;
import de.kaiserpfalzedv.commons.ObjectReference;
import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import de.kaiserpfalzedv.commons.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.commons.api.Metadata;
import de.kaiserpfalzedv.commons.api.ObjectIdentity;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.FolderSpec;
import de.kaiserpfalzedv.folders.ImmutableFolder;
import de.kaiserpfalzedv.folders.ImmutableFolderSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * @author rlichti
 * @since 2019-12-27T10:41
 */
public class TestDefaultFolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDefaultFolder.class);

    public static final UUID FOLDER_UUID = UUID.randomUUID();
    public static final String FOLDER_TENANT = "tenant";
    public static final String FOLDER_KEY = "key";
    public static final String FOLDER_DISPLAYNAME = "Folder displayname";
    public static final String FOLDER_NAME = "The full and correct name of the folder";
    public static final String FOLDER_DESCRIPTION = "This is a test folder";
    public static final OffsetDateTime FOLDER_CREATED = OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    public static final OffsetDateTime FOLDER_MODIFIED = OffsetDateTime.of(2019, 12, 27, 8, 48, 54, 738493, ZoneOffset.UTC);

    public static final ObjectIdentity FOLDER_IDENTITY = ImmutableObjectIdentity.builder()
            .kind(Folder.KIND)
            .version(Folder.VERSION)
            .uuid(FOLDER_UUID)
            .tenant(FOLDER_TENANT)
            .name(FOLDER_KEY)
            .build();

    public static final Metadata FOLDER_METADATA = ImmutableMetadata.builder()
            .identity(FOLDER_IDENTITY)
            .build();

    public static final FolderSpec FOLDER_SPEC = ImmutableFolderSpec.builder()
            .identity(FOLDER_IDENTITY)
            .displayname(FOLDER_DISPLAYNAME)
            .created(FOLDER_CREATED)
            .modified(FOLDER_MODIFIED)

            .name(FOLDER_NAME)
            .description(FOLDER_DESCRIPTION)
            .build();

    public static final ConcurrentSkipListSet<ObjectReference> FOLDER_CONTENT;

    static {
        FOLDER_CONTENT = new ConcurrentSkipListSet<>();
        for (int i = 0; i <= 20; i++) {
            String counter = generateCounter(i, 4);

            FOLDER_CONTENT.add(
                    ImmutableObjectReference.builder()
                            .metadata(ImmutableMetadata.builder()
                                    .identity(ImmutableObjectIdentity.builder()
                                            .kind("unspecified")
                                            .version("0.0.0")
                                            .uuid(UUID.randomUUID())
                                            .tenant(FOLDER_TENANT)
                                            .name("fck-" + counter)
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
            );
        }
    }

    public static String generateCounter(final int count, final int size) {
        return String.format("%0" + size + "d", count);
    }

    public static final Folder FOLDER = ImmutableFolder.builder()
            .metadata(FOLDER_METADATA)
            .envelope(FOLDER_SPEC)
            .spec(FOLDER_CONTENT)
            .build();
}
