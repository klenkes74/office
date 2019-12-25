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


import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.contacts.NaturalPerson;
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
public class JPANaturalPersonReadAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPANaturalPersonReadAdapterTest.class);

    private static final UUID ID = UUID.fromString("f422fcf1-ebe1-4b1e-adc2-0a0cfdeb0d81");
    private static final String TENANT = "de.kaiserpfalz-edv";
    private static final String KEY = "D10009";
    private static final String DISPLAYNAME = "Kardinal Roland T. Lichti";
    private static final String GIVENNAME = "Roland Thomas";
    private static final String SURNAME = "Lichti";
    private static final String HERALDIC_PREFIX = "Kardinal";
    private static final OffsetDateTime CREATED = OffsetDateTime.of(2019, 1, 6, 18, 12, 0, 0, ZoneOffset.ofHours(0));
    private static final OffsetDateTime MODIFIED = OffsetDateTime.of(2019, 12, 17, 18, 12, 0, 0, ZoneOffset.ofHours(0));


    @Inject
    @JPA
    JPANaturalPersonReadAdapter readAdapter;

    @Test
    public void shouldRetrieveEmptyOptionalIfStoreIsEmpty() {
        Optional<NaturalPerson> result = readAdapter.loadById(TENANT, UUID.randomUUID());
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    public void shouldRetrieveDataById() {
        Optional<NaturalPerson> result = readAdapter.loadById(TENANT, ID);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
        NaturalPerson data = result.get();

        assert ID.equals(data.getSpec().getIdentity().getUuid());
        assert TENANT.equals(data.getSpec().getIdentity().getTenant());
        assert KEY.equals(data.getSpec().getIdentity().getName().orElse(null));
        assert DISPLAYNAME.equals(data.getSpec().getDisplayname());
        assert GIVENNAME.equals(data.getSpec().getGivenname());
        assert SURNAME.equals(data.getSpec().getSurname());
        assert HERALDIC_PREFIX.equals(data.getSpec().getHeraldicPrefixTitle().orElse(null));
        assert CREATED.isEqual(data.getSpec().getCreated());
        assert MODIFIED.isEqual(data.getSpec().getModified());
    }

    @Test
    public void shouldRetrieveDataWhenLoadingByScope() {
        Collection<NaturalPerson> result = readAdapter.loadByTenant(TENANT);
        LOGGER.trace("result: {}", result);

        assert result.size() > 0;
    }

    @Test
    public void shouldRetrieveEmptySetWhenInvalidScopeIsQueried() {
        Collection<NaturalPerson> result = readAdapter.loadByTenant("empty");
        LOGGER.trace("result: {}", result);

        assert result.isEmpty();
    }

    @Test
    public void shouldRetrieveDataWhenLoadedByName() {
        Optional<NaturalPerson> result = readAdapter.loadbyKey(TENANT, KEY);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
    }

    @Test
    public void shouldRetrieveEmptyWhenInvalidNameIsGiven() {
        Optional<NaturalPerson> result = readAdapter.loadbyKey("empty", KEY);
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    @Transactional
    public void shouldRetrieveALotOfFoldersWhenTheyExist() {
        ArrayList<NaturalPerson> result = readAdapter.loadByTenant(TENANT);

        long count = readAdapter.count();
        LOGGER.info("Loaded ({}/{}) entries: {}", result.size(), count, result);

        assert result.size() >= 1;
        assert result.size() <= count;
    }
}
