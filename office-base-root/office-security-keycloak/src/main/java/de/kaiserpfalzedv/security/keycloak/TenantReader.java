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
import de.kaiserpfalzedv.security.oidc.OidcUmaClient;
import de.kaiserpfalzedv.security.store.TenantReadAdapter;
import de.kaiserpfalzedv.security.tenant.ImmutableTenant;
import de.kaiserpfalzedv.security.tenant.Tenant;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.resource.ProtectionResource;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * @author rlichti
 * @since 2020-01-01T20:50Z
 */
@Default
@Singleton
public class TenantReader implements TenantReadAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantReader.class);


    @Inject
    @OidcUmaClient
    AuthzClient client;


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
