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

package de.kaiserpfalzedv.base.cdi;

import de.kaiserpfalzedv.commons.cdi.EventLogged;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-21 08:43
 */
@QuarkusTest
@Tag("integration")
public class EventLoggedInteceptorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoggedInteceptorTest.class);

    @Inject
    TestedClassForEventLoggedInterceptor intercepted;

    @Test
    public void shouldLogNormalWhenOneParameterIsGiven() {
        intercepted.OneParameter("data");
    }

    @Test
    public void shouldLogNormalWhenTwoParametersAreGiven() {
        intercepted.TwoParameters("first", "second");
    }
}

@SuppressWarnings("unused")
class TestedClassForEventLoggedInterceptor {
    @EventLogged
    public void OneParameter(@Observes String observed) {
        // do nothing
    }

    @EventLogged
    public void TwoParameters(final String test, final String observed) {
        // do nothing
    }
}