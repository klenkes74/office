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

import de.kaiserpfalzedv.commons.api.OkStatus;
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
public class OkStatusTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(OkStatusTest.class);

    private static final OkStatus<Spec<Serializable>> SERVICE = new OkStatus<Spec<Serializable>>() {
    };

    @Test
    public void shouldReturnCorrectMessage() {
        assert OkStatus.DEFAULT_MESSAGE.equals(SERVICE.getMessage());
    }

    @Test
    public void shouldReturnCorrectValue() {
        assert SERVICE.DEFAULT_STATUS_CODE == SERVICE.getValue();
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
