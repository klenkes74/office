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

package de.kaiserpfalzedv.base.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-21 02:29
 */
public class UuidConverterTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String STRING = ID.toString();

    private UuidConverter service;

    @BeforeEach
    public void setUpService() {
        service = new UuidConverter();
    }

    @Test
    public void shouldConvertToStringWhenGivenAnUuid() {
        String result = service.convertToDatabaseColumn(ID);

        assert STRING.equals(result);
    }

    @Test
    public void shouldConvertToUuidWhenGivenAnString() {
        UUID result = service.convertToEntityAttribute(STRING);

        assert ID.equals(result);
    }
}