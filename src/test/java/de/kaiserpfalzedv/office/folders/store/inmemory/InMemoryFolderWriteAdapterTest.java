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

package de.kaiserpfalzedv.office.folders.store.inmemory;

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.store.KeyAlreadyExistsException;
import de.kaiserpfalzedv.base.store.ModificationFailedException;
import de.kaiserpfalzedv.base.store.UuidAlreadyExistsException;
import de.kaiserpfalzedv.office.folders.Folder;
import de.kaiserpfalzedv.office.folders.ImmutableFolder;
import de.kaiserpfalzedv.office.folders.ImmutableFolderSpec;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.fail;


/*
 *
 *
 * @author rlichti
 * @since 2019-12-19 15:05
 */
class InMemoryFolderWriteAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryFolderWriteAdapterTest.class);

    private static final ZoneOffset EuropeBerlin = ZoneOffset.ofHours(-1);
    private static final ArrayList<Folder> data = FolderData.generateFolderData("scope", 50);

    private InMemoryFolderStore store;
    private Folder validFolder = data.get(0);
    private UUID validUUID = validFolder.getMetadata().getIdentity().getUuid();
    private String[] validScopeAndKey = {
            validFolder.getMetadata().getIdentity().getTenant().orElse(null),
            validFolder.getMetadata().getIdentity().getName().orElse(null)
    };

    private InMemoryFolderWriteAdapter service;


    @BeforeEach
    void setUp() {
        store = new InMemoryFolderStore();
        store.createCaches(); // call PostConstruct to get the caches prepared
        data.forEach(store::store);

        service = new InMemoryFolderWriteAdapter(store);
        LOGGER.debug("Stored folder count: {}", store.getObjectCount());
    }

    @Test
    void shouldPrintAValidStringRepresentation() {
        LOGGER.debug("result: {}", service);

        assertThat(service.toString(), matchesRegex("InMemoryFolderWriteAdapter\\[identity=[0-9]+, store=.*"));
    }

    @Test
    void shouldCreateAFolderWhenTheDataIsNotStoredAlready() throws KeyAlreadyExistsException, UuidAlreadyExistsException {
        Folder data = FolderData.generateFolderData("other scope", 1).get(0);
        service.create(data);

        Optional<Folder> result = store.loadByUuid(data.getMetadata().getIdentity().getUuid());

        assert result.isPresent();
        assert result.get().getMetadata().getIdentity().getUuid().equals(data.getMetadata().getIdentity().getUuid());
    }


    @Test
    void shouldNotCreateAFolderWhenTheUuidIsNotUnique() throws KeyAlreadyExistsException {
        try {
            service.create(validFolder);

            fail("There should have been an UuidAlreadyExistsException!");
        } catch (UuidAlreadyExistsException e) {
            LOGGER.debug("The expected exception has been thrown: {}", e.getMessage(), e);
        }
    }

    @Test
    void shouldNotCreateAFolderWhenTheScopeAndKeyAreNotUnique() throws UuidAlreadyExistsException {
        ObjectIdentifier identity = ImmutableObjectIdentifier.builder()
                .kind(Folder.KIND)
                .version(Folder.VERSION)

                .uuid(UUID.randomUUID())
                .tenant(validFolder.getMetadata().getIdentity().getTenant())
                .name(validFolder.getMetadata().getIdentity().getName())

                .build();

        Folder data = ImmutableFolder.builder()
                .from(validFolder)
                .metadata(ImmutableMetadata.builder()
                        .identity(identity)
                        .build()
                )
                .spec(ImmutableFolderSpec.builder()
                        .from(validFolder.getSpec())
                        .identity(identity)
                        .build()
                )

                .build();

        try {
            service.create(data);

            fail("There should have been an KeyAlreadyExistsException!");
        } catch (KeyAlreadyExistsException e) {
            LOGGER.debug("The expected exception has been thrown: {}", e.getMessage(), e);
        }
    }

    @Test
    void shouldCloseFolderWhenAValidFolderIsGiven() throws ModificationFailedException {
        assert !validFolder.getSpec().getClosed().isPresent();

        service.close(validUUID);

        Optional<Folder> result = store.loadByUuid(validUUID);
        LOGGER.debug("result: {}", result);

        assert result.isPresent();
        assert result.get().getSpec().getClosed().isPresent();
    }


    @Test
    void shouldThrowAnExceptionWhenClosingAnNonexistingFolder() {
        UUID uuid = UUID.randomUUID();

        assert !store.loadByUuid(uuid).isPresent();

        try {
            service.close(uuid);

            fail("A ModificationFailedException should have been thrown!");
        } catch (ModificationFailedException e) {
            LOGGER.debug("Correct exception thrown: {}", e.getMessage(), e);
        }
    }

    @Test
    void shouldDeleteFolderWhenAValidFolderIsGiven() {
        service.delete(validUUID);

        Optional<Folder> result = store.loadByUuid(validUUID);
        LOGGER.debug("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    void shouldModifyDataWhenAValidFolderIsGiven() throws ModificationFailedException {
        Folder data = ImmutableFolder.builder()
                .from(validFolder)
                .spec(ImmutableFolderSpec.builder()
                        .from(validFolder.getSpec())
                        .description("new description for all")
                        .build()
                )
                .build();

        service.modify(data);

        Optional<Folder> result = store.loadByUuid(validUUID);
        LOGGER.debug("result: {}", result);

        assert result.isPresent();
        assert result.get().getSpec().getDescription().isPresent();
        assert "new description for all".equals(result.get().getSpec().getDescription().get());
    }


    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", InMemoryFolderReadAdapter.class.getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", InMemoryFolderReadAdapter.class.getCanonicalName());
    }


    static private class FolderData {
        @SuppressWarnings("SameParameterValue")
        static ArrayList<Folder> generateFolderData(final String scope, final int count) {
            ArrayList<Folder> result = new ArrayList<>(count);

            for (int i = 1; i <= count; i++) {
                String key = "I'19-"
                        + (i <= 999 ? "0" : "")
                        + (i <= 99 ? "0" : "")
                        + (i <= 9 ? "0" : "")
                        + i;

                OffsetDateTime created = OffsetDateTime.of(
                        random(2015, 2019),
                        random(1, 12),
                        random(1, 28),
                        random(0, 23),
                        random(0, 59),
                        random(0, 59),
                        random(0, 999),
                        EuropeBerlin
                );

                OffsetDateTime modified = OffsetDateTime.of(
                        random(created.getYear(), 2019),
                        random(created.getMonthValue() + (created.getMonthValue() != 12 ? 1 : 0), 12),
                        random(1, 28),
                        random(0, 23),
                        random(0, 59),
                        random(0, 59),
                        random(0, 999),
                        EuropeBerlin
                );

                Optional<OffsetDateTime> closed = i % 10 == 0 ?
                        Optional.of(OffsetDateTime.of(
                                random(modified.getYear(), 2019),
                                random(created.getMonthValue() + (created.getMonthValue() != 12 ? 1 : 0), 12),
                                random(1, 28),
                                random(0, 23),
                                random(0, 59),
                                random(0, 59),
                                random(0, 999),
                                EuropeBerlin))
                        : Optional.empty();

                ObjectIdentifier identity = ImmutableObjectIdentifier.builder()
                        .kind(Folder.KIND)
                        .version(Folder.VERSION)

                        .uuid(UUID.randomUUID())
                        .tenant(Optional.ofNullable(scope))
                        .name(key)

                        .build();

                result.add(ImmutableFolder.builder()
                        .metadata(ImmutableMetadata.builder()
                                .identity(identity)
                                .build()
                        )
                        .spec(ImmutableFolderSpec.builder()
                                .identity(identity)
                                .name("Softwaretest Akte Nr. " + i)
                                .displayname("Akte Nr. " + i)
                                .created(created)
                                .modified(modified)
                                .closed(closed)
                                .build()
                        )
                        .build()
                );
            }

            return result;
        }

        private static int random(int min, int max) {
            return ((int) (Math.random() * (max - min)) + min);
        }
    }
}