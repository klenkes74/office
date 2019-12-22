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
import de.kaiserpfalzedv.base.store.NoModifiableDataFoundException;
import de.kaiserpfalzedv.office.folders.Folder;
import de.kaiserpfalzedv.office.folders.ImmutableFolder;
import de.kaiserpfalzedv.office.folders.ImmutableFolderSpec;
import org.infinispan.manager.DefaultCacheManager;
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
class InMemoryFolderStoreTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryFolderStoreTest.class);

    private static final ZoneOffset EuropeBerlin = ZoneOffset.ofHours(-1);
    private static final ArrayList<Folder> data = FolderData.generateFolderData("scope", 50);

    private InMemoryFolderStore service;
    private UUID validUUID = data.get(0).getMetadata().getIdentity().getUuid();
    private String[] validScopeAndKey = {
            data.get(0).getMetadata().getIdentity().getTenant().orElse(null),
            data.get(0).getMetadata().getIdentity().getName().orElse(null)
    };

    @BeforeEach
    void setUp() {
        service = new InMemoryFolderStore();
        service.createCaches(); // call PostConstruct to get the caches prepared
        data.forEach(service::store);

        LOGGER.debug("Stored folder count: {}", service.getObjectCount());
    }


    @Test
    void shouldCreateAValidServiceWhenCacheManagerIsSpecified() {
        service = new InMemoryFolderStore(new DefaultCacheManager());
        service.createCaches();
        data.forEach(service::store);

        assert service.getObjectCount() == 50;
    }

    @Test
    void shouldPrintAValidStringRepresentation() {
        LOGGER.debug("result: {}", service);

        assertThat(service.toString(), matchesRegex("InMemoryFolderStore\\[identity=[0-9]+, cacheManager=org.infinispan.manager.DefaultCacheManager@[0-9a-z]+@Address:null\\]"));
    }

    @Test
    void shouldLoadFolderWhenValidUuidIsGiven() {
        Optional<Folder> result = service.loadByUuid(validUUID);
        LOGGER.debug("result: {}", result);

        assert result.isPresent();
        assert result.get().getMetadata().getIdentity().getUuid().equals(validUUID);
        assert validScopeAndKey[0].equals(result.get().getMetadata().getIdentity().getTenant().orElse(null));
        assert validScopeAndKey[1].equals(result.get().getMetadata().getIdentity().getName().orElse(null));
    }


    @Test
    void shouldNotLoadFolderWhenRandomUuidIsGiven() {
        Optional<Folder> result = service.loadByUuid(UUID.randomUUID());
        LOGGER.debug("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    void shouldLoadFolderWithValidScopeAndKey() {
        Optional<Folder> result = service.loadByScopeAndKey(validScopeAndKey[0], validScopeAndKey[1]);
        LOGGER.debug("result: {}", result);

        assert result.isPresent();
        assert result.get().getMetadata().getIdentity().getUuid().equals(validUUID);
        assert validScopeAndKey[0].equals(result.get().getMetadata().getIdentity().getTenant().orElse(null));
        assert validScopeAndKey[1].equals(result.get().getMetadata().getIdentity().getName().orElse(null));
    }

    @Test
    void shouldNotLoadFolderWithValidRandomScopeAndKey() {
        Optional<Folder> result = service.loadByScopeAndKey(
                Integer.valueOf(FolderData.random(1000, 2000)).toString(),
                Integer.valueOf(FolderData.random(10000, 99999)).toString()
        );
        LOGGER.debug("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    void shouldNotFindAnyEntryWithoutScopeWhenNoScopelessDataIsSaved() {
        Optional<Folder> result = service.loadByKey("I'19-0050");
        LOGGER.debug("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    void shouldFindAnEntryWithoutScopeWhenScopelessDataIsSaved() {
        ArrayList<Folder> scopelessData = FolderData.generateFolderData(null, 1);
        service.store(scopelessData.get(0));

        Optional<Folder> result = service.loadByKey(scopelessData.get(0).getMetadata().getIdentity().getName().orElse(null));
        LOGGER.debug("result: {}", result);

        assert result.isPresent();
        assert result.get().getMetadata().getIdentity().getName().equals(scopelessData.get(0).getMetadata().getIdentity().getName());
    }

    @Test
    void shouldReturn50FoldersWhenScopeIsCorrect() {
        ArrayList<Folder> result = service.loadByScope("scope");
        LOGGER.debug("result.size: {}", result.size());

        assert result.size() == 50;
    }

    @Test
    void shouldReturnNoFoldersWhenScopeIsInvalid() {
        ArrayList<Folder> result = service.loadByScope("invalid");
        LOGGER.debug("result.size: {}", result.size());

        assert result.isEmpty();
    }

    @Test
    void shouldLoadNoEntriesWithoutScopeWhenScopelessEntriesDoNotExist() {
        ArrayList<Folder> result = service.loadEntriesWithoutScope();
        LOGGER.debug("result count: {}", result.size());

        assert result.isEmpty();
    }

    @Test
    void shouldLoadAllEntriesWithoutScopeWhenScopelessEntriesExist() {
        ArrayList<Folder> scopelessData = FolderData.generateFolderData(null, 10);
        scopelessData.forEach(service::store);

        ArrayList<Folder> result = service.loadEntriesWithoutScope();
        LOGGER.debug("result count: {}", result.size());

        assert result.size() == 10;
    }

    @Test
    void shouldReplaceFolderInStoreWhenOriginalDataExists() throws NoModifiableDataFoundException {
        Optional<Folder> replaced = service.loadByUuid(validUUID);
        assert replaced.isPresent();

        OffsetDateTime modified = OffsetDateTime.now();
        Folder data = ImmutableFolder.builder()
                .from(replaced.get())
                .spec(ImmutableFolderSpec.builder()
                        .from(replaced.get().getSpec())
                        .name("new name")
                        .modified(modified)
                        .build()
                )
                .build();

        service.replace(data);

        replaced = service.loadByUuid(validUUID);
        LOGGER.debug("result: {}", replaced);

        assert replaced.isPresent();
        assert replaced.get().getSpec().getName().equals("new name");
        assert replaced.get().getSpec().getModified().isEqual(modified);
    }

    @Test
    void shouldFailDuringReplaceFolderInStoreWhenOriginalDataDoesNotExist() {
        UUID uuid = UUID.randomUUID();
        Optional<Folder> replaced = service.loadByUuid(uuid);
        assert !replaced.isPresent();

        Folder data = FolderData.generateFolderData("scope", 1).get(0);

        try {
            service.replace(data);

            fail("There should have been a NoModifiableDataFoundException!");
        } catch (NoModifiableDataFoundException e) {
            LOGGER.debug("Correct exception caught: {}", e.getMessage(), e);
        }
    }

    @Test
    void shouldRemoveFolderFromStoreWhenStoredFolderIsRemoved() {
        Optional<Folder> deleted = service.loadByUuid(validUUID);
        assert deleted.isPresent();

        service.remove(deleted.get());

        assert service.getObjectCount() == 49;
    }

    @Test
    void getObjectCount() {
        assert service.getObjectCount() == 50;
    }


    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", InMemoryFolderStore.class.getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", InMemoryFolderStore.class.getCanonicalName());
    }


    static private class FolderData {
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