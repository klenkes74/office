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

import de.kaiserpfalzedv.commons.BaseObject;
import de.kaiserpfalzedv.security.tenant.Tenant;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;


/**
 * @author rlichti
 * @since 2020-01-01T21:13Z
 */
@QuarkusTest
@Tag("integration")
public class TenantReaderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantReaderTest.class);

    @Inject
    TenantReader service;

    @Test
    public void shouldLoadWhenCorrectConfigurationIsSet() {
        Optional<Tenant> result = service.loadbyKey(BaseObject.EMPTY_STRING_MARKER, "de.kaiserpfalz-edv");
        LOGGER.trace("loaded tenant: {}", result);

        assert result.isPresent();
        assert "de.kaiserpfalz-edv".equals(result.get().getKey());
    }
}
