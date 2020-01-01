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

package de.kaiserpfalzedv.commons.api;

import de.kaiserpfalzedv.commons.BaseObject;
import de.kaiserpfalzedv.commons.ObjectReference;

import java.util.Optional;
import java.util.UUID;


/**
 * This is an invalid object identity. Serves as
 * <a href="https://en.wikipedia.org/wiki/Null_object_pattern">null object</a> for identities.
 *
 * @author rlichti
 * @since 2020-01-01T21:12
 */
public class InvalidObjectIdentity implements ObjectIdentity {
    public static final String INVALID_KIND = BaseObject.EMPTY_STRING_MARKER;
    public static final String INVALID_VERSION = "0.0.0";

    public static final ObjectIdentity INSTANCE = new InvalidObjectIdentity();

    @Override
    public String getKind() {
        return INVALID_KIND;
    }

    @Override
    public String getVersion() {
        return INVALID_VERSION;
    }


    @Override
    public UUID getUuid() {
        return BaseObject.INVALID_UUID;
    }


    @Override
    public String getKey() {
        return BaseObject.EMPTY_STRING_MARKER;
    }

    @Override
    public Optional<ObjectReference> getOwner() {
        return Optional.empty();
    }
}
