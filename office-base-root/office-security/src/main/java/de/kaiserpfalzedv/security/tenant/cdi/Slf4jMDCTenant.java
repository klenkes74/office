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

import de.kaiserpfalzedv.commons.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.commons.api.InvalidObjectIdentity;
import de.kaiserpfalzedv.commons.api.ObjectIdentity;
import de.kaiserpfalzedv.security.tenant.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


/**
 * @author rlichti
 * @since 2019-12-26T14:24
 */
public class Slf4jMDCTenant implements Tenant {
    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jMDCTenant.class);


    @Override
    public String getKey() {
        return getIdentity().getKey();
    }

    @Override
    public ObjectIdentity getIdentity() {
        ObjectIdentity result;

        String key = MDC.get(TenantProvider.TENANT_MDC_MARKER);
        if (key != null) {
            LOGGER.warn("Providing tenant identity from MDC instead from real provider: {}", key);

            result = ImmutableObjectIdentity.copyOf(InvalidObjectIdentity.INSTANCE).withKey(key);
        } else {
            LOGGER.warn("No tenant set in MDC. Will return the tenant InvalidObjectIdentity instead!");
            result = InvalidObjectIdentity.INSTANCE;
        }

        return result;
    }
}
