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

package de.kaiserpfalzedv.office.folders.store;


import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.CreationFailedException;
import de.kaiserpfalzedv.base.store.DataAlreadyExistsException;
import de.kaiserpfalzedv.base.store.KeyAlreadyExistsException;
import de.kaiserpfalzedv.base.store.UuidAlreadyExistsException;
import de.kaiserpfalzedv.office.folders.FolderSpec;
import de.kaiserpfalzedv.office.folders.ImmutableFolderSpec;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolderCreateService;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolderReadAdapter;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolderSpec;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@QuarkusTest
@Tag("integration")
public class JPAFolderReadAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderReadAdapterTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String SCOPE = "kes";
    private static final String KEY = "I'19-0001";
    private static final String NAME = "Softwaretest Akte";
    private static final String SHORTNAME = "Softwaretest";
    private static final String DESCRIPTION = "Einfache Akte für Softwaretests";
    private static final OffsetDateTime CREATED = OffsetDateTime.of(2019, 12, 11, 12, 0, 0, 0, ZoneOffset.ofHours(1));
    private static final OffsetDateTime MODIFIED = CREATED;


    private FolderSpec folder;

    @Inject
    @JPA
    JPAFolderReadAdapter readAdapter;
    @Inject
    @JPA
    JPAFolderCreateService storeAdapter;


    @BeforeEach
    @Transactional
    public void generateMockService() throws CreationFailedException {
        folder = ImmutableFolderSpec.builder()
                .identity(ImmutableObjectIdentifier.builder()
                        .kind(de.kaiserpfalzedv.office.folders.Folder.KIND)
                        .version(de.kaiserpfalzedv.office.folders.Folder.VERSION)

                        .uuid(ID)
                        .scope(SCOPE)
                        .name(KEY)
                        .build()
                )
                .name(NAME)
                .shortName(SHORTNAME)
                .description(DESCRIPTION)
                .created(CREATED)
                .modified(MODIFIED)
                .build();

        try {
            storeAdapter.create(folder);
        } catch (UuidAlreadyExistsException | KeyAlreadyExistsException e) {
            // ignore, we want the data to be there and there it is ...
        }

        assert JPAFolderSpec.count("uuid", folder.getIdentity().getUuid()) == 1;
    }


    @Test
    public void shouldRetrieveEmptyOptionalIfStoreIsEmpty() {
        Optional<FolderSpec> result = readAdapter.loadById(UUID.randomUUID());
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    public void shouldRetrieveDataById() {
        Optional<FolderSpec> result = readAdapter.loadById(ID);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
        FolderSpec data = result.get();

        assert ID.equals(data.getIdentity().getUuid());
        assert SCOPE.equals(data.getIdentity().getScope().orElse(""));
        assert KEY.equals(data.getIdentity().getName().orElse(null));
        assert NAME.equals(data.getName());
        assert SHORTNAME.equals(data.getShortName().orElse(null));
        assert DESCRIPTION.equals(data.getDescription().orElse(null));
        assert !data.getClosed().isPresent();
    }

    @Test
    public void shouldRetrieveDataWhenLoadingByScope() {
        Collection<FolderSpec> result = readAdapter.loadByScope(SCOPE);
        LOGGER.trace("result: {}", result);

        assert result.size() > 0;
    }

    @Test
    public void shouldRetrieveEmptySetWhenInvalidScopeIsQueried() {
        Collection<FolderSpec> result = readAdapter.loadByScope("empty");
        LOGGER.trace("result: {}", result);

        assert result.isEmpty();
    }

    @Test
    public void shouldRetrieveDataWhenLoadedByName() {
        Optional<FolderSpec> result = readAdapter.loadByScopeAndKey(SCOPE, KEY);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
    }

    @Test
    public void shouldRetrieveEmptyWhenInvalidNameIsGiven() {
        Optional<FolderSpec> result = readAdapter.loadByScopeAndKey("empty", KEY);
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    @Transactional
    public void shouldRetrieveALotOfFoldersWhenTheyExist() throws DataAlreadyExistsException, CreationFailedException {
        for (int i = 2; i < 50; i++) {
            storeAdapter.create(
                    ImmutableFolderSpec.copyOf(folder)
                            .withIdentity(ImmutableObjectIdentifier.builder()
                                    .kind(de.kaiserpfalzedv.office.folders.Folder.KIND)
                                    .version(de.kaiserpfalzedv.office.folders.Folder.VERSION)
                                    .uuid(UUID.randomUUID())
                                    .scope(SCOPE)
                                    .name("I'19-00" + (i <= 9 ? "0" + i : i))
                                    .build()
                            )
                            .withName(folder.getName() + " Nr. " + i)
                            .withShortName(folder.getShortName() + " Nr. " + i)
                            .withDescription(folder.getDescription() + " (Nr. " + i + ")")
            );
        }

        ArrayList<FolderSpec> result = readAdapter.loadByScope(folder.getIdentity().getScope().orElse("./."));
        long count = readAdapter.count();
        LOGGER.info("Loaded {} entries: {}", count, result);

        assert result.size() == count;
    }
}
