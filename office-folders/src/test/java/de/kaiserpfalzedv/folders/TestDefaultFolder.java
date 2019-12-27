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

import de.kaiserpfalzedv.base.ImmutableObjectReference;
import de.kaiserpfalzedv.base.ObjectReference;
import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.base.api.Metadata;
import de.kaiserpfalzedv.base.api.ObjectIdentity;
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
            String counter = (i < 1000 ? "0" : "")
                    + (i < 100 ? "0" : "")
                    + (i < 10 ? "0" : "")
                    + i;

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

    public static final Folder FOLDER = ImmutableFolder.builder()
            .metadata(FOLDER_METADATA)
            .envelope(FOLDER_SPEC)
            .spec(FOLDER_CONTENT)
            .build();
}
