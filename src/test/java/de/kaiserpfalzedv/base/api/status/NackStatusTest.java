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

package de.kaiserpfalzedv.base.api.status;

import de.kaiserpfalzedv.base.api.Spec;
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
public class NackStatusTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NackStatusTest.class);

    private static final NackStatus<Spec<Serializable>> SERVICE = new NackStatus<Spec<Serializable>>() {
    };

    @Test
    public void shouldReturnCorrectValue() {
        assert SERVICE.GENERIC_FAILURE == SERVICE.getValue();
    }

    @Test
    public void shouldReturnCorrectMessage() {
        assert SERVICE.DEFAULT_MESSAGE.equals(SERVICE.getMessage());
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
