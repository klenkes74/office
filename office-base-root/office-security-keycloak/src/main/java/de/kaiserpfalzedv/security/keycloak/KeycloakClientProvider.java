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

import de.kaiserpfalzedv.security.oidc.OidcUmaClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;


/**
 * @author rlichti
 * @since 2020-01-02T06:55Z
 */
@Singleton
public class KeycloakClientProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakClientProvider.class);

    @Inject
    @ConfigProperty(name = "oidc.uma.client-id", defaultValue = "tenant-service")
    String umaConfigClientId;

    @Inject
    @ConfigProperty(name = "oidc.uma.client-secret")
    String umaConfigClientSecret;

    @Inject
    @ConfigProperty(name = "oidc.uma.auth-realm")
    String umaConfigRealm;

    @Inject
    @ConfigProperty(name = "oidc.uma.config-url")
    String umaConfigUri;


    @PostConstruct
    public void reportConfiguration() {
        LOGGER.debug("Created Keycloak AuthzClient provider.");
    }

    @Produces
    @OidcUmaClient
    public AuthzClient getClient(@OidcUmaClient final Configuration config) {
        return AuthzClient.create(config);
    }

    @Produces
    @OidcUmaClient
    public Configuration getKeyCloackUmaConfig() {
        Configuration result = new Configuration();

        result.setAuthServerUrl(umaConfigUri);
        result.setRealm(umaConfigRealm);

        result.setResource(umaConfigClientId);

        Map<String, Object> credentials = new HashMap<>(1);
        credentials.put("secret", umaConfigClientSecret);
        result.setCredentials(credentials);

        return result;
    }
}
