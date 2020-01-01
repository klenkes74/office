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

package de.kaiserpfalzedv.security.tenant.cdi;

import de.kaiserpfalzedv.commons.BaseObject;
import de.kaiserpfalzedv.commons.cdi.CurrentRequest;
import de.kaiserpfalzedv.security.tenant.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * @author rlichti
 * @since 2019-12-21 19:45
 */
@Singleton
public class TenantProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantProvider.class);

    public static final String TENANT_MDC_MARKER = "tenant";

    private ThreadLocal<Tenant> tenants = new ThreadLocal<>();

    @Produces
    @CurrentRequest
    public Optional<Tenant> getTenant() {
        Tenant tenant = tenants.get();

        LOGGER.trace("Providing tenant: {}", tenant);
        return Optional.ofNullable(tenant);
    }

    public void setTenant(@Observes Tenant tenant) {
        if (!BaseObject.EMPTY_STRING_MARKER.equals(tenant.getKey())) {
            MDC.put(TenantProvider.TENANT_MDC_MARKER, tenant.getKey());

            LOGGER.debug("Saving tenant to thread: {}", tenant);
            tenants.set(tenant);
        } else {
            LOGGER.debug("Removing tenant from thread: {}", tenants.get());

            tenants.remove();
            MDC.remove(TenantProvider.TENANT_MDC_MARKER);
        }

    }
}