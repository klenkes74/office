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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;


/**
 * @author rlichti
 * @since 2020-01-01T21:13Z
 */
public class TenantReaderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantReaderTest.class);

    private static final Properties applicationProperties = new Properties();

    private TenantReader service;


    @Test
    public void shouldLoadWhenCorrectConfigurationIsSet() {
        LOGGER.info("The service is created correctly, otherwise an exception should have been issued already");

        Optional<Tenant> result = service.loadbyKey(BaseObject.EMPTY_STRING_MARKER, "de.kaiserpfalz-edv");
        LOGGER.info("loaded tenant: {}", result);

        assert result.isPresent();
        assert "de.kaiserpfalz-edv".equals(result.get().getKey());
    }


    @BeforeAll
    public static void setUpProperties() throws IOException {
        LOGGER.trace("Loading file: {}", ClassLoader.getSystemResource("application.properties"));

        try (final InputStream stream = ClassLoader.getSystemResourceAsStream("application.properties")) {
            applicationProperties.load(stream);
            /* or properties.loadFromXML(...) */
        }

        LOGGER.debug(
                "Set up properties: oidc.uma.auth-realm={}, oidc.uma.client-id={}, oidc.uma.client-secret=***, oidc.uma.config-url={}",
                applicationProperties.getProperty("%test.oidc.uma.auth-realm", "---unset---"),
                applicationProperties.getProperty("oidc.uma.client-id", "---unset---"),
                applicationProperties.getProperty("oidc.uma.config-url", "---unset---")
        );
    }

    @BeforeEach
    public void setUpService() {
        service = new TenantReader();

        service.umaConfigUri = applicationProperties.getProperty("oidc.uma.config-url", "---unset---");
        service.umaConfigRealm = applicationProperties.getProperty("%test.oidc.uma.auth-realm", "---unset---");

        service.umaConfigClientId = applicationProperties.getProperty("oidc.uma.client-id", "---unset---");
        service.umaConfigClientSecret = applicationProperties.getProperty("%test.oidc.uma.client-secret", "---unset---");

        service.createClient();
    }
}
