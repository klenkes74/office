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

package de.kaiserpfalzedv.security.tenant.http;

import de.kaiserpfalzedv.commons.BaseObject;
import de.kaiserpfalzedv.security.store.TenantReadAdapter;
import de.kaiserpfalzedv.security.tenant.EmptyTenant;
import de.kaiserpfalzedv.security.tenant.Tenant;
import de.kaiserpfalzedv.security.tenant.cdi.TenantProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.util.Optional;

@Provider
@Priority(Priorities.AUTHORIZATION + 20)
public class TenantRequestFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantRequestFilter.class);

    @Context
    UriInfo info;

    @Inject
    TenantReadAdapter tenantReadAdapter;

    @Inject
    Event<Tenant> tenantEventSink;

    @Override
    public void filter(ContainerRequestContext context) {
        String data = info.getPathParameters().getFirst(TenantProvider.TENANT_MDC_MARKER);
        if (data != null) {
            Optional<Tenant> tenant = tenantReadAdapter.loadbyKey(BaseObject.EMPTY_STRING_MARKER, data);

            if (tenant.isPresent()) {
                tenantEventSink.fire(tenant.get());
                return; // tenant is set we don't need to do anything else
            }
        }

        tenantEventSink.fire(new EmptyTenant());

        LOGGER.debug("Can't set the tenant. No tenant given in request.");
    }
}
