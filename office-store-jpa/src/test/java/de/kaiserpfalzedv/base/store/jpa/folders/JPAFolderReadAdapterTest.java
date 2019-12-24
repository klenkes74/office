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


import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.folders.Folder;
import io.quarkus.test.junit.QuarkusTest;
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

    private static final UUID ID = UUID.fromString("3ca1aa42-4ae0-4066-ae5b-1ab2d1eab7f8");
    private static final String SCOPE = "de.kaiserpfalz-edv";
    private static final String KEY = "I-19-0001";
    private static final String NAME = "Softwaretest Akte 1";
    private static final String DISPLAYNAME = "SW Test 1";
    private static final String DESCRIPTION = "Einfache Akte für Softwaretests";
    private static final OffsetDateTime CREATED = OffsetDateTime.of(2018, 12, 17, 18, 12, 0, 0, ZoneOffset.ofHours(0));
    private static final OffsetDateTime MODIFIED = OffsetDateTime.of(2018, 12, 17, 18, 12, 0, 0, ZoneOffset.ofHours(0));


    @Inject
    @JPA
    JPAFolderReadAdapter readAdapter;

    @Test
    public void shouldRetrieveEmptyOptionalIfStoreIsEmpty() {
        Optional<Folder> result = readAdapter.loadById(UUID.randomUUID());
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    public void shouldRetrieveDataById() {
        Optional<Folder> result = readAdapter.loadById(ID);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
        Folder data = result.get();

        assert ID.equals(data.getSpec().getIdentity().getUuid());
        assert SCOPE.equals(data.getSpec().getIdentity().getTenant().orElse(""));
        assert KEY.equals(data.getSpec().getIdentity().getName().orElse(null));
        assert NAME.equals(data.getSpec().getName());
        assert DISPLAYNAME.equals(data.getSpec().getDisplayname());
        assert DESCRIPTION.equals(data.getSpec().getDescription().orElse(null));
        assert !data.getSpec().getClosed().isPresent();
    }

    @Test
    public void shouldRetrieveDataWhenLoadingByScope() {
        Collection<Folder> result = readAdapter.loadByScope(SCOPE);
        LOGGER.trace("result: {}", result);

        assert result.size() > 0;
    }

    @Test
    public void shouldRetrieveEmptySetWhenInvalidScopeIsQueried() {
        Collection<Folder> result = readAdapter.loadByScope("empty");
        LOGGER.trace("result: {}", result);

        assert result.isEmpty();
    }

    @Test
    public void shouldRetrieveDataWhenLoadedByName() {
        Optional<Folder> result = readAdapter.loadByScopeAndKey(SCOPE, KEY);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
    }

    @Test
    public void shouldRetrieveEmptyWhenInvalidNameIsGiven() {
        Optional<Folder> result = readAdapter.loadByScopeAndKey("empty", KEY);
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    @Transactional
    public void shouldRetrieveALotOfFoldersWhenTheyExist() {
        ArrayList<Folder> result = readAdapter.loadByScope(SCOPE);

        long count = readAdapter.count();
        LOGGER.info("Loaded ({}/{}) entries: {}", result.size(), count, result);

        assert result.size() >= 1;
        assert result.size() <= count;
    }
}
