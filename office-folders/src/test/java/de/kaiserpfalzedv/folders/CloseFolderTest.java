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

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.base.api.ImmutableWorkflowData;
import de.kaiserpfalzedv.base.api.ObjectIdentity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

/**
 * @author rlichti
 * @since 2019-12-14 10:42
 */
public class CloseFolderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseFolderTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String SCOPE = "scope";
    private static final String KEY = "key";
    private static final String NAME = "name";
    private static final String DISPLAYNAME = "displayname";
    private static final OffsetDateTime CREATED = OffsetDateTime.now();
    private static final OffsetDateTime MODIFIED = CREATED;


    private static final FolderSpec FOLDER = new FolderSpec() {
        @Override
        public ObjectIdentity getIdentity() {
            return ImmutableObjectIdentity.builder()
                    .kind(Folder.KIND)
                    .version(Folder.VERSION)
                    .uuid(ID)
                    .tenant(SCOPE)
                    .name(KEY)
                    .build();
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public String getDisplayname() {
            return DISPLAYNAME;
        }

        @Override
        public Optional<String> getDescription() {
            return Optional.empty();
        }

        @Override
        public Optional<OffsetDateTime> getClosed() {
            return Optional.empty();
        }

        @Override
        public OffsetDateTime getCreated() {
            return CREATED;
        }

        @Override
        public OffsetDateTime getModified() {
            return MODIFIED;
        }
    };

    private static final CloseFolder SERVICE = (CloseFolder) () -> ImmutableMetadata.builder()
            .identity(ImmutableObjectIdentity.builder()
                    .kind(CloseFolder.KIND)
                    .version(CloseFolder.VERSION)
                    .uuid(ID)
                    .tenant(SCOPE)
                    .name(KEY)
                    .build()
            )
            .build();

    @Test
    public void shouldReturnCorrectKindOfFolder() {
        assert CloseFolder.KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfFolder() {
        assert CloseFolder.VERSION.equals(SERVICE.getVersion());
    }

    @Test
    public void shouldApplyTheCommandCorrectlyWhenNoWorkflowDataIsGiven() {
        FolderSpec result = SERVICE.apply(FOLDER);
        LOGGER.trace("result: {}", result);

    }

    @Test
    public void shouldApplyTheCommandCorrectlyWhenWorkflowDataIsGiven() {
        OffsetDateTime modificationTime = OffsetDateTime.of(2019, 12, 18, 0, 0, 0, 0, ZoneOffset.UTC);
        CloseFolder service = ImmutableCloseFolder.builder()
                .from(SERVICE)
                .metadata(ImmutableMetadata.builder()
                        .from(SERVICE.getMetadata())
                        .workflowdata(ImmutableWorkflowData.builder()
                                .definition(ImmutableObjectIdentity.builder()
                                        .kind("de.kaiserpfalzedv.wf.office.folders.close")
                                        .version("1.0.0")
                                        .uuid(UUID.randomUUID())
                                        .tenant("test")
                                        .name("close-folder")
                                        .build()
                                )
                                .correlation(UUID.randomUUID())
                                .request(UUID.randomUUID())
                                .sequence(1L)
                                .timestamp(modificationTime)
                                .build()
                        )
                        .build()
                )
                .build();

        FolderSpec result = service.apply(FOLDER);
        LOGGER.trace("result: {}", result);

        assert result.getClosed().orElse(OffsetDateTime.now()).isEqual(modificationTime);
    }

    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", CloseFolder.class.getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", CloseFolder.class.getCanonicalName());
    }
}
