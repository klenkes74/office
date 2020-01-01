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
import de.kaiserpfalzedv.commons.ImmutableObjectReference;
import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import de.kaiserpfalzedv.commons.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.commons.api.InvalidObjectIdentity;
import de.kaiserpfalzedv.security.store.TenantReadAdapter;
import de.kaiserpfalzedv.security.tenant.ImmutableTenant;
import de.kaiserpfalzedv.security.tenant.Tenant;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.resource.AuthorizationResource;
import org.keycloak.authorization.client.resource.ProtectionResource;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;


/**
 * @author rlichti
 * @since 2020-01-01T20:50Z
 */
@Default
@Singleton
public class TenantReader implements TenantReadAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantReader.class);

    @Inject
    @ConfigProperty(name = "oidc.uma.client-id")
    String umaConfigClientId;

    @Inject
    @ConfigProperty(name = "oidc.uma.client-secret")
    String umaConfigClientSecret;

    @Inject
    @ConfigProperty(name = "oidc.uma.auth-ream")
    String umaConfigRealm;

    @Inject
    @ConfigProperty(name = "oidc.uma.config-url")
    String umaConfigUri;

    private AuthzClient client;


    @PostConstruct
    public void createClient() {
        Configuration config = new Configuration();

        config.setAuthServerUrl(umaConfigUri);
        config.setRealm(umaConfigRealm);

        config.setResource(umaConfigClientId);

        Map<String, Object> credentials = new HashMap<>(1);
        credentials.put("secret", umaConfigClientSecret);
        config.setCredentials(credentials);

        client = AuthzClient.create(config);

        AuthorizationResource authorization = client.authorization();
        LOGGER.debug("Created keycloak auth client: client={}, authorization={}", client, authorization);
    }

    @Override
    public Optional<Tenant> loadById(final String tenant, final UUID id) {
        throw new UnsupportedOperationException("Loading tenants by UUID is not supported!");
    }

    @Override
    public Optional<Tenant> loadbyKey(final String tenant, final String key) {
        LOGGER.debug("Loading tenant: key={}", key);

        ProtectionResource permission = client.protection();
        List<ResourceRepresentation> resources = permission.resource().findByMatchingUri("/tenants/de.kaiserpfalz-edv");

        if (resources.isEmpty()) {
            LOGGER.debug("No tenant found. key={}", key);
            return Optional.empty();
        }
        ResourceRepresentation resource = resources.get(0);

        Tenant result = ImmutableTenant.builder()
                .identity(ImmutableObjectIdentity.builder()
                        .kind(resource.getType())
                        .version(InvalidObjectIdentity.INVALID_VERSION)

                        .uuid(UUID.fromString(resource.getId()))

                        .tenant(BaseObject.EMPTY_STRING_MARKER)
                        .key(resource.getName())

                        .owner(ImmutableObjectReference.builder()
                                .kind("owner")
                                .version(InvalidObjectIdentity.INVALID_VERSION)

                                .metadata(ImmutableMetadata.builder()
                                        .identity(ImmutableObjectIdentity.builder()
                                                .kind("owner")
                                                .version(InvalidObjectIdentity.INVALID_VERSION)
                                                .uuid(UUID.fromString(resource.getOwner().getId()))
                                                .tenant(BaseObject.EMPTY_STRING_MARKER)
                                                .key(resource.getOwner().getName())
                                                .build()
                                        )
                                        .build()
                                )

                                .displayname(resource.getOwner().getName())
                                .build())

                        .build()
                )
                .build();

        LOGGER.trace("Loaded tenant: {}", result);
        return Optional.of(result);
    }

    @Override
    public ArrayList<Tenant> loadByTenant(final String tenant) {
        throw new UnsupportedOperationException("Can't load tenant by tenant!");
    }

    @Override
    public ArrayList<Tenant> loadByTenant(final String tenant, final int index, final int size) {
        throw new UnsupportedOperationException("Can't load tenant by tenant!");
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Can't count tenants!");
    }
}
