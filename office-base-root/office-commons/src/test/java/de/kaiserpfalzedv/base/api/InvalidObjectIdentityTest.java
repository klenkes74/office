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

package de.kaiserpfalzedv.base.api;

import de.kaiserpfalzedv.commons.BaseObject;
import de.kaiserpfalzedv.commons.api.InvalidObjectIdentity;
import de.kaiserpfalzedv.commons.api.ObjectIdentity;
import org.junit.jupiter.api.Test;

/**
 * Just checks the behavior of {@link InvalidObjectIdentity} which should behave like an object identity but return
 * invalid data. Serves as <a href="https://en.wikipedia.org/wiki/Null_object_pattern">null object</a> for identities.
 *
 * @author rlichti
 * @since 2020-01-01T20:15Z
 */
public class InvalidObjectIdentityTest {
    private static final ObjectIdentity SERVICE = new InvalidObjectIdentity();

    @Test
    public void shouldReturnInvalidKindWhenAskedForKind() {
        assert InvalidObjectIdentity.INVALID_KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnInvalidVersionWhenAskedForVersion() {
        assert InvalidObjectIdentity.INVALID_VERSION.equals(SERVICE.getVersion());
    }


    @Test
    public void shouldReturnInvalidUuidWhenAskedForUuid() {
        assert BaseObject.INVALID_UUID.equals(SERVICE.getUuid());
    }

    @Test
    public void shouldReturnEmptyStringMarkerWhenAskedForTenant() {
        assert BaseObject.EMPTY_STRING_MARKER.equals(SERVICE.getTenant());
    }

    @Test
    public void shouldReturnEmptyStringMarkerWhenAskedForKey() {
        assert BaseObject.EMPTY_STRING_MARKER.equals(SERVICE.getKey());
    }
}
