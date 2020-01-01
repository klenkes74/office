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

package de.kaiserpfalzedv.security.tenant;

import de.kaiserpfalzedv.commons.BaseObject;
import de.kaiserpfalzedv.security.tenant.cdi.Slf4jMDCTenant;
import de.kaiserpfalzedv.security.tenant.cdi.TenantProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;


/**
 * @author rlichti
 * @since 2020-01-01T20:37Z
 */
public class Slf4jMdcTenantTest {
    private Slf4jMDCTenant service;

    @BeforeEach
    public void setUpService() {
        service = new Slf4jMDCTenant();
    }

    @Test
    public void ShouldReturnEmptyStringMarkerWhenNoMdcTenantIsSet() {
        MDC.remove(TenantProvider.TENANT_MDC_MARKER);

        assert BaseObject.EMPTY_STRING_MARKER.equals(service.getKey());
    }

    @Test
    public void ShouldReturnInvalidObjectIdentityWhenAskedForIdentityAndNoMdcTenantIsSet() {
        MDC.remove(TenantProvider.TENANT_MDC_MARKER);

        assert BaseObject.INVALID_UUID.equals(service.getIdentity().getUuid());
    }

    @Test
    public void ShouldReturnInvalidObjectIdentityWhenAskedForIdentityAndMdcTenantIsSet() {
        MDC.put(TenantProvider.TENANT_MDC_MARKER, "test-tenant");

        assert BaseObject.INVALID_UUID.equals(service.getIdentity().getUuid());
    }

    @Test
    public void ShouldReturnValueOfMdcWhenMdcTenantIsSet() {
        MDC.put(TenantProvider.TENANT_MDC_MARKER, "test-tenant");

        assert "test-tenant".equals(service.getKey());
        assert "test-tenant".equals(service.getIdentity().getKey());
    }
}
