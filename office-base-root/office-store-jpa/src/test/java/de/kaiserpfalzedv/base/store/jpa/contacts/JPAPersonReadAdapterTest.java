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

package de.kaiserpfalzedv.base.store.jpa.contacts;


import de.kaiserpfalzedv.commons.cdi.JPA;
import de.kaiserpfalzedv.contacts.BasePerson;
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
public class JPAPersonReadAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAPersonReadAdapterTest.class);

    protected static final UUID ID = UUID.fromString("f422fcf1-ebe1-4b1e-adc2-0a0cfdeb0d81");
    protected static final String TENANT = "de.kaiserpfalz-edv";
    protected static final String KEY = "D10009";
    protected static final String DISPLAYNAME = "Kardinal Roland T. Lichti";
    protected static final OffsetDateTime CREATED = OffsetDateTime.of(2019, 1, 6, 18, 12, 0, 0, ZoneOffset.ofHours(0));
    protected static final OffsetDateTime MODIFIED = OffsetDateTime.of(2019, 12, 17, 18, 12, 0, 0, ZoneOffset.ofHours(0));


    @Inject
    @JPA
    JPAPersonReadAdapter readAdapter;

    @Test
    public void shouldRetrieveEmptyOptionalIfStoreIsEmpty() {
        Optional<BasePerson<?,?>> result = readAdapter.loadById(TENANT, UUID.randomUUID());
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    public void shouldRetrieveDataById() {
        Optional<BasePerson<?,?>> result = readAdapter.loadById(TENANT, ID);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
        BasePerson<?,?> data = result.get();

        assert ID.equals(data.getMetadata().getIdentity().getUuid());
        assert TENANT.equals(data.getMetadata().getIdentity().getTenant());
        assert KEY.equals(data.getMetadata().getIdentity().getName().orElse(null));
        assert DISPLAYNAME.equals(data.getSpec().getDisplayname());
        assert CREATED.isEqual(data.getSpec().getCreated());
        assert MODIFIED.isEqual(data.getSpec().getModified());
    }

    @Test
    public void shouldRetrieveDataWhenLoadingByScope() {
        Collection<BasePerson<?,?>> result = readAdapter.loadByTenant(TENANT);
        LOGGER.trace("result: {}", result);

        assert result.size() > 0;
    }

    @Test
    public void shouldRetrieveEmptySetWhenInvalidScopeIsQueried() {
        Collection<BasePerson<?,?>> result = readAdapter.loadByTenant("empty");
        LOGGER.trace("result: {}", result);

        assert result.isEmpty();
    }

    @Test
    public void shouldRetrieveDataWhenLoadedByName() {
        Optional<BasePerson<?,?>> result = readAdapter.loadbyKey(TENANT, KEY);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
    }

    @Test
    public void shouldRetrieveEmptyWhenInvalidNameIsGiven() {
        Optional<BasePerson<?,?>> result = readAdapter.loadbyKey("empty", KEY);
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    @Transactional
    public void shouldRetrieveALotOfFoldersWhenTheyExist() {
        ArrayList<BasePerson<?,?>> result = readAdapter.loadByTenant(TENANT);

        long count = readAdapter.count();
        LOGGER.info("Loaded ({}/{}) entries: {}", result.size(), count, result);

        assert result.size() >= 1;
        assert result.size() <= count;
    }
}
