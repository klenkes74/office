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

package de.kaiserpfalzedv.base.store.jpa.folders;


import de.kaiserpfalzedv.base.ImmutableObjectReference;
import de.kaiserpfalzedv.base.ObjectReference;
import de.kaiserpfalzedv.base.WrappingException;
import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.base.api.ObjectIdentity;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.jpa.folders.changes.JPAFolderReadAdapter;
import de.kaiserpfalzedv.folders.*;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import static de.kaiserpfalzedv.folders.store.TestDefaultFolder.FOLDER_IDENTITY;
import static de.kaiserpfalzedv.folders.store.TestDefaultFolder.FOLDER_METADATA;
import static org.junit.jupiter.api.Assertions.fail;


@QuarkusTest
@Tag("integration")
public class JPAFolderChangeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderChangeTest.class);

    private static final UUID ID = UUID.fromString("3ca1aa42-4ae0-4066-ae5b-1ab2d1eab7f8");
    private static final String TENANT = "de.kaiserpfalz-edv";
    private static final String KEY = "I-19-0001";

    @Inject
    @JPA
    JPAFolderReadAdapter readAdapter;

    @Inject
    Event<ModifyFolder> modifyFolderEventSink;

    @Inject
    Event<CloseFolder> closeFolderEventSink;

    @Inject
    Event<DeleteFolder> deleteFolderEventSink;

    @Inject
    Event<AddContent> addContentEventSink;

    @Inject
    Event<RemoveContent> removeContentEventSink;

    private static final ConcurrentSkipListSet<ObjectReference> ADDED_DATA;

    static {
        ADDED_DATA = new ConcurrentSkipListSet<>();

        for (int i = 1; i <= 10; i++) {
            String counter = generateCounterString(i);

            ADDED_DATA.add(ImmutableObjectReference.builder()
                    .kind("test")
                    .version("0.0.0")
                    .metadata(ImmutableMetadata.copyOf(FOLDER_METADATA)
                            .withIdentity(ImmutableObjectIdentity.copyOf(FOLDER_IDENTITY)
                                    .withUuid(UUID.randomUUID())
                                    .withTenant(TENANT)
                                    .withName("REF-" + KEY + "-" + counter)
                            )
                    )
                    .build()
            );
        }
    }

    private static String generateCounterString(int i) {
        return ("0000" + i).substring(Integer.valueOf(i).toString().length());
    }

    private static final ConcurrentSkipListSet<ObjectReference> REMOVED_DATA;

    static {
        REMOVED_DATA = new ConcurrentSkipListSet<>();
        REMOVED_DATA.add(ImmutableObjectReference.builder()
                .kind("test")
                .version("0.0.0")
                .metadata(ImmutableMetadata.builder()
                        .identity(ImmutableObjectIdentity.builder()
                                .kind("test")
                                .version("0.0.0")
                                .uuid(UUID.fromString("bf18e36a-47b7-496e-9f43-a2dbb7d3f211"))
                                .tenant("de.kaiserpfalz-edv")
                                .name("Test-Reference 3")
                                .build()
                        )
                        .build()
                )
                .build()
        );
    }

    private Folder data;

    @BeforeEach
    public void setUpOldFolder() {
        Optional<Folder> db = readAdapter.loadById(TENANT, ID);
        assert db.isPresent();

        data = db.get();
        LOGGER.trace("Original data (containing {} entries): {}", data.getSpec().size(), data);
    }

    @Test
    public void shouldChangeDisplayNameWhenNewDisplayNameIsProvided() {
        ObjectIdentity identity = ImmutableObjectIdentity.builder()
                .kind(DeleteFolder.KIND)
                .version(DeleteFolder.VERSION)
                .uuid(UUID.fromString("76351c86-e920-4ae0-9591-115ea7d4f1ad"))
                .tenant("de.kaiserpfalz-edv")
                .name("I-19-0003")
                .build();

        ModifyFolder event = ImmutableModifyFolder.builder()
                .metadata(ImmutableMetadata.builder()
                        .identity(identity)
                        .build()
                )

                .spec(ImmutableFolderSpec.copyOf(data.getEnvelope())
                        .withIdentity(identity)
                        .withDisplayname("new displayname")
                )

                .build();

        try {
            modifyFolderEventSink.fire(event);
        } catch (WrappingException e) {
            LOGGER.error("Modification failed: {}", e.getWrapped().getMessage(), e.getWrapped());

            fail("Modification failed: " + e.getWrapped().getMessage());
        }
    }

    @Test
    public void shouldCloseFolderWhenAskedToDoSo() {
        CloseFolder event = ImmutableCloseFolder.builder()
                .metadata(data.getMetadata())
                .build();

        try {
            closeFolderEventSink.fire(event);
        } catch (WrappingException e) {
            LOGGER.error("Close failed: {}", e.getWrapped().getMessage(), e.getWrapped());

            fail("Close failed: " + e.getWrapped().getMessage());
        }
    }

    @Test
    public void shouldDeleteFolderWhenAskedToDoSo() {
        DeleteFolder event = ImmutableDeleteFolder.builder()
                .metadata(ImmutableMetadata.builder()
                        .identity(ImmutableObjectIdentity.builder()
                                .kind(DeleteFolder.KIND)
                                .version(DeleteFolder.VERSION)
                                .uuid(UUID.fromString("066ed1a6-a699-49e4-a756-b17816ecf7e8"))
                                .tenant("de.kaiserpfalz-edv")
                                .name("I-19-0002")
                                .build()
                        )
                        .build()
                )
                .build();

        try {
            deleteFolderEventSink.fire(event);
        } catch (WrappingException e) {
            LOGGER.error("Delete failed: {}", e.getWrapped().getMessage(), e.getWrapped());

            fail("Delete failed: " + e.getWrapped().getMessage());
        }
    }

    @Test
    public void shouldAddNewContentWhenNewContentIsProvided() {
        AddContent event = ImmutableAddContent.builder()
                .metadata(ImmutableMetadata.builder()
                        .identity(ImmutableObjectIdentity.builder()
                                .kind(AddContent.KIND)
                                .version(AddContent.VERSION)
                                .uuid(ID)
                                .tenant(TENANT)
                                .name(KEY)
                                .build()
                        )
                        .build()
                )
                .data(ADDED_DATA)
                .build();

        try {
            addContentEventSink.fire(event);
        } catch (WrappingException e) {
            LOGGER.error("Adding content failed: {}", e.getWrapped().getMessage(), e.getWrapped());

            fail("Adding content failed: " + e.getWrapped().getMessage());
        }

        Optional<Folder> result = readAdapter.loadById(TENANT, ID);
        assert result.isPresent();
        LOGGER.trace("result (containing {} entries: {}", result.get().getSpec().size(), result.get());
    }

    @Test
    public void shouldRemoveOldContentWhenContentIsProvided() {
        RemoveContent event = ImmutableRemoveContent.builder()
                .metadata(ImmutableMetadata.builder()
                        .identity(ImmutableObjectIdentity.builder()
                                .kind(RemoveContent.KIND)
                                .version(RemoveContent.VERSION)
                                .uuid(ID)
                                .tenant(TENANT)
                                .name(KEY)
                                .build()
                        )
                        .build()
                )
                .data(REMOVED_DATA)
                .build();

        try {
            removeContentEventSink.fire(event);
        } catch (WrappingException e) {
            LOGGER.error("Removing content failed: {}", e.getWrapped().getMessage(), e.getWrapped());

            fail("Removing content failed: " + e.getWrapped().getMessage());
        }

        Optional<Folder> result = readAdapter.loadById(TENANT, ID);
        assert result.isPresent();
        LOGGER.trace("result (containing {} entries: {}", result.get().getSpec().size(), result.get());
    }
}
