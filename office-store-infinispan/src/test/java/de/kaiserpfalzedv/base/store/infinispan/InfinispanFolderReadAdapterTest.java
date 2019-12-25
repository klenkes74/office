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

package de.kaiserpfalzedv.base.store.infinispan;

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.base.api.ObjectIdentity;
import de.kaiserpfalzedv.base.store.infinispan.folders.InfinispanFolderReadAdapter;
import de.kaiserpfalzedv.base.store.infinispan.folders.InfinispanFolderStore;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.ImmutableFolder;
import de.kaiserpfalzedv.folders.ImmutableFolderSpec;
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


/*
 *
 *
 * @author rlichti
 * @since 2019-12-19 15:05
 */
class InfinispanFolderReadAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanFolderReadAdapterTest.class);

    private static final ZoneOffset EuropeBerlin = ZoneOffset.ofHours(-1);
    private static final String VALID_TENANT = "tenant";
    private static final ArrayList<Folder> data = FolderData.generateFolderData(VALID_TENANT, 50);

    private InfinispanFolderStore store;
    private UUID validUUID = data.get(0).getMetadata().getIdentity().getUuid();
    private String[] validScopeAndKey = {
            data.get(0).getMetadata().getIdentity().getTenant(),
            data.get(0).getMetadata().getIdentity().getName().orElse(null)
    };

    private InfinispanFolderReadAdapter service;


    @BeforeEach
    void setUp() {
        store = new InfinispanFolderStore();
        store.createCaches(); // call PostConstruct to get the caches prepared
        data.forEach(store::store);

        service = new InfinispanFolderReadAdapter(store);
        LOGGER.debug("Stored folder count: {}", store.getObjectCount());
    }

    @Test
    void shouldPrintAValidStringRepresentation() {
        LOGGER.debug("result: {}", service);

        assertThat(service.toString(), matchesRegex("InfinispanFolderReadAdapter\\[identity=[0-9]+, store=.*"));
    }

    @Test
    void shouldLoadFolderWhenValidUuidIsGiven() {
        Optional<Folder> result = service.loadById(VALID_TENANT, validUUID);
        LOGGER.debug("result: {}", result);

        assert result.isPresent();
        assert result.get().getMetadata().getIdentity().getUuid().equals(validUUID);
        assert validScopeAndKey[0].equals(result.get().getMetadata().getIdentity().getTenant());
        assert validScopeAndKey[1].equals(result.get().getMetadata().getIdentity().getName().orElse(null));
    }


    @Test
    void shouldNotLoadFolderWhenRandomUuidIsGiven() {
        Optional<Folder> result = service.loadById(VALID_TENANT, UUID.randomUUID());
        LOGGER.debug("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    void shouldLoadFolderWithValidScopeAndKey() {
        Optional<Folder> result = service.loadbyKey(validScopeAndKey[0], validScopeAndKey[1]);
        LOGGER.debug("result: {}", result);

        assert result.isPresent();
        assert result.get().getMetadata().getIdentity().getUuid().equals(validUUID);
        assert validScopeAndKey[0].equals(result.get().getMetadata().getIdentity().getTenant());
        assert validScopeAndKey[1].equals(result.get().getMetadata().getIdentity().getName().orElse(null));
    }

    @Test
    void shouldReturn10FoldersWhenScopeIsCorrect() {
        ArrayList<Folder> result = service.loadByTenant(VALID_TENANT);
        LOGGER.debug("result.size: {}", result.size());

        assert result.size() == 50;
    }

    @Test
    void shouldReturn10FoldersWhenScopeIsCorrectAndThePageIsGiven() {
        ArrayList<Folder> result = service.loadByTenant(VALID_TENANT, 0, 10);
        LOGGER.debug("result.size: {}", result.size());

        assert result.size() == 10;
    }

    @Test
    void shouldReturn5FoldersWhenScopeIsCorrectAndThePageIsGivenButLastPageIsSmaller() {
        ArrayList<Folder> result = service.loadByTenant(VALID_TENANT, 44, 10);
        LOGGER.debug("result.size: {}", result.size());

        assert result.size() == 5;
    }

    @Test
    void shouldReturnEmptyArrayWhenScopeIsCorrectAndThePageIsGivenButAfterTheLastPage() {
        ArrayList<Folder> result = service.loadByTenant(VALID_TENANT, 51, 10);
        LOGGER.debug("result.size: {}", result.size());

        assert result.isEmpty();
    }

    @Test
    void shouldReturnNoFoldersWhenScopeIsInvalid() {
        ArrayList<Folder> result = service.loadByTenant("invalid");
        LOGGER.debug("result.size: {}", result.size());

        assert result.isEmpty();
    }

    @Test
    void getObjectCount() {
        assert service.count() == 50;
    }


    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", InfinispanFolderReadAdapter.class.getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", InfinispanFolderReadAdapter.class.getCanonicalName());
    }


    static private class FolderData {
        @SuppressWarnings("SameParameterValue")
        static ArrayList<Folder> generateFolderData(final String tenant, final int count) {
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

                ObjectIdentity identity = ImmutableObjectIdentity.builder()
                        .kind(Folder.KIND)
                        .version(Folder.VERSION)

                        .uuid(UUID.randomUUID())
                        .tenant(tenant)
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