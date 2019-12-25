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

package de.kaiserpfalzedv.security;

import de.kaiserpfalzedv.base.cdi.CurrentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

/**
 * @author rlichti
 * @since 2019-12-21 19:45
 */
@ApplicationScoped
public class TenantProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantProvider.class);

    private ThreadLocal<Tenant> tenants = new ThreadLocal<>();

    @Produces
    @CurrentRequest
    public Tenant getTenant() {
        Tenant tenant = tenants.get();
        LOGGER.trace("Providing tenant: {}", tenant);
        return tenant;
    }

    public void setTenant(@Observes Tenant tenant) {
        MDC.put("tenant", tenant.getTenant());
        LOGGER.debug("Saving tenant to request: {}", tenant);

        tenants.set(tenant);
    }

    public void unsetTenant(@Observes EmptyTenant empty) {
        LOGGER.debug("Removing tenant from request: {}", tenants.get());
        MDC.remove("tenant");

        tenants.remove();
    }
}