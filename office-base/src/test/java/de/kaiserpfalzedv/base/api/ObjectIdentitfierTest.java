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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 08:40
 */
public class ObjectIdentitfierTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectIdentitfierTest.class);

    private static final String KIND = ObjectIdentifier.class.getCanonicalName();
    private static final String VERSION = "0.0.0";
    private static final UUID ID = UUID.randomUUID();
    private static final String SCOPE = "scope";
    private static final String KEY = "key";


    @Test
    public void shouldWorkWhenthStaticFactoryMethodWithUuid() {
        ObjectIdentifier service = ObjectIdentifier.create(KIND, VERSION, ID);

        assert KIND.equals(service.getKind());
        assert VERSION.equals(service.getVersion());
        assert ID.equals(service.getUuid());
        assert !service.getTenant().isPresent();
        assert !service.getName().isPresent();
    }

    @Test
    public void shouldWorkWhenthStaticFactoryMethodWithScopeAndKey() {
        ObjectIdentifier service = ObjectIdentifier.create(KIND, VERSION, SCOPE, KEY);

        assert KIND.equals(service.getKind());
        assert VERSION.equals(service.getVersion());
        assert service.getUuid() != null;
        assert SCOPE.equals(service.getTenant().orElse(null));
        assert KEY.equals(service.getName().orElse(null));
    }

    @Test
    public void shouldWorkWhenthStaticFactoryMethodWithOnlyKey() {
        ObjectIdentifier service = ObjectIdentifier.create(KIND, VERSION, null, KEY);

        assert KIND.equals(service.getKind());
        assert VERSION.equals(service.getVersion());
        assert service.getUuid() != null;
        assert !service.getTenant().isPresent();
        assert KEY.equals(service.getName().orElse(null));
    }

    @Test
    public void shouldFailWhenthStaticFactoryMethodWithOnlyScope() {
        try {
            ObjectIdentifier.create(KIND, VERSION, SCOPE, null);

            fail("A NullPointerException should have been thrown when setting an identitiy without uuid and without key!");
        } catch (NullPointerException e) {
            // everything is fine.
        }
    }

    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", ObjectIdentifier.class.getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", ObjectIdentifier.class.getCanonicalName());
    }
}
