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

package de.kaiserpfalzedv.base;

import de.kaiserpfalzedv.commons.BaseObject;
import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import de.kaiserpfalzedv.commons.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.commons.api.Metadata;
import de.kaiserpfalzedv.commons.api.Spec;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 08:57
 */
public class BaseObjectTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseObjectTest.class);

    private static final String KIND = "TestKind";
    private static final String VERSION = "0.0.0";
    private static final java.util.UUID UUID = java.util.UUID.randomUUID();
    private static final String SCOPE = "scope";
    private static final String NAME = "key";

    private static final BaseObject<Spec<Serializable>> SERVICE = new BaseObject<Spec<Serializable>>() {
        @Override
        public String getKind() {
            return KIND;
        }

        @Override
        public String getVersion() {
            return VERSION;
        }

        @Override
        public Metadata getMetadata() {
            return ImmutableMetadata.builder()
                    .identity(ImmutableObjectIdentity.builder()
                            .kind(KIND)
                            .version(VERSION)
                            .uuid(UUID)
                            .tenant(SCOPE)
                            .name(NAME)
                            .build()
                    )
                    .build();
        }
    };


    @Test
    public void shouldReturnCorrectKindOfBaseAPI() {
        String result = SERVICE.getKind();
        LOGGER.trace("result: {}", result);

        assert KIND.equals(result);
    }

    @Test
    public void shouldReturnCorrectVersionOfBaseAPI() {
        assert VERSION.equals(SERVICE.getVersion());
    }


    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", SERVICE.getClass().getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", SERVICE.getClass().getCanonicalName());
    }
}
