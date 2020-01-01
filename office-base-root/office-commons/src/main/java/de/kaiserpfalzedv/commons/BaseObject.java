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

package de.kaiserpfalzedv.commons;


import de.kaiserpfalzedv.commons.api.KindHolding;
import de.kaiserpfalzedv.commons.api.MetadataHolding;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author rlichti
 * @since 2019-12-04
 */
public interface BaseObject<T extends Serializable> extends KindHolding, MetadataHolding, Serializable {
    /**
     * This marks an empty string. It is for example used to mark a tenantless data entry.
     */
    String EMPTY_STRING_MARKER = "./.";

    /**
     * This marks an invalid UUID.
     */
    UUID INVALID_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

}
