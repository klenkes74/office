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

import de.kaiserpfalzedv.commons.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.commons.api.ObjectIdentity;
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
public class ObjectIdentityTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectIdentityTest.class);

    private static final String KIND = ObjectIdentity.class.getCanonicalName();
    private static final String VERSION = "0.0.0";
    private static final UUID ID = UUID.randomUUID();
    private static final String TENANT = "scope";
    private static final String KEY = "key";


    @Test
    public void shouldWorkWhenthStaticFactoryMethodWithUuid() {
        ObjectIdentity service = ImmutableObjectIdentity.builder()
                .kind(KIND)
                .version(VERSION)
                .uuid(ID)
                .tenant(TENANT)
                .key(KEY)
                .build();

        assert KIND.equals(service.getKind());
        assert VERSION.equals(service.getVersion());
        assert ID.equals(service.getUuid());
        assert !service.getTenant().isEmpty();
        assert !service.getKey().isEmpty();
    }

    @Test
    public void shouldFailWhenthStaticFactoryMethodWithOnlyScope() {
        try {
            ImmutableObjectIdentity.builder()
                    .kind(KIND)
                    .version(VERSION)
                    .tenant(TENANT)
                    .build();

            fail("A NullPointerException should have been thrown when setting an identitiy without uuid and without key!");
        } catch (IllegalStateException e) {
            // everything is fine.
        }
    }

    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", ObjectIdentity.class.getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", ObjectIdentity.class.getCanonicalName());
    }
}
