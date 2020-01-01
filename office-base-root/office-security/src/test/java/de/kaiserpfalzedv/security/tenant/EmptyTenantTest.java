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
import de.kaiserpfalzedv.commons.api.InvalidObjectIdentity;
import org.junit.jupiter.api.Test;


/**
 * @author rlichti
 * @since 2020-01-01T20:23Z
 */
public class EmptyTenantTest {
    private static final Tenant SERVICE = EmptyTenant.INSTANCE;

    @Test
    public void shouldReturnInvalidIdentityWhenAskedForIt() {
        assert InvalidObjectIdentity.INSTANCE.equals(SERVICE.getIdentity());
    }

    @Test
    public void shouldReturnEmptyStringMarkerWhenAskedForDisplayname() {
        assert BaseObject.EMPTY_STRING_MARKER.equals(SERVICE.getDisplayname());
    }

    @Test
    public void shouldReturnEmptyStringMarkerWhenAskedForKey() {
        assert BaseObject.EMPTY_STRING_MARKER.equals(SERVICE.getKey());
    }
}
