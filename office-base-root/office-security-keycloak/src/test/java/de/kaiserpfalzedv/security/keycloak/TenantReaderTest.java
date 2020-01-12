/*
 * Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2020. All rights reserved.
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

package de.kaiserpfalzedv.security.keycloak;

import de.kaiserpfalzedv.security.tenant.Tenant;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

import static de.kaiserpfalzedv.commons.BaseObject.EMPTY_STRING_MARKER;


/**
 * A fairly fragile test. It assumes that a special resource with a certain ID exists.
 *
 * @author rlichti
 * @since 2020-01-01T21:13Z
 */
@QuarkusTest
@Tag("integration")
public class TenantReaderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantReaderTest.class);

    private static final String EXISTING_RESOURCE_NAME = "de.kaiserpfalz-edv";
    private static final UUID EXISTING_ID = UUID.fromString("1b9dac8b-55f5-418a-a46a-90fdcdbfcaeb");

    @Inject
    TenantReader service;

    @Test
    public void shouldLoadWhenCorrectConfigurationIsSet() {
        Optional<Tenant> result = service.loadbyKey(EMPTY_STRING_MARKER, EXISTING_RESOURCE_NAME);
        LOGGER.trace("loaded tenant: {}", result);

        assert result.isPresent();
        assert EXISTING_ID.equals(result.get().getIdentity().getUuid());
        assert EXISTING_RESOURCE_NAME.equals(result.get().getKey());
    }

    @Test
    public void shouldLoadWhenCorrectIdIsGiven() {
        Optional<Tenant> result = service.loadById(EMPTY_STRING_MARKER, EXISTING_ID);
        LOGGER.trace("loaded tenant: {}", result);

        assert result.isPresent();
        assert EXISTING_ID.equals(result.get().getIdentity().getUuid());
        assert EXISTING_RESOURCE_NAME.equals(result.get().getKey());
    }

    @Test
    public void shouldCountAllTenants() {
        LOGGER.trace("Number of tenants: {}", service.count());

        assert service.count() >= 1;
    }
}
