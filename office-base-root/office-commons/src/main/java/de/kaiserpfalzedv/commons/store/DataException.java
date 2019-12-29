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

package de.kaiserpfalzedv.commons.store;

import de.kaiserpfalzedv.commons.api.ObjectIdentity;


public abstract class DataException extends Exception {
    private final ObjectIdentity identifier;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public DataException(final ObjectIdentity identifier, final String message, final Throwable cause) {
        super(message + formatObjectIdentifier(identifier), cause);

        this.identifier = identifier;
    }

    public DataException(final ObjectIdentity identifier, final Throwable cause) {
        this(identifier, cause.getMessage(), cause);
    }

    public DataException(final ObjectIdentity identifier, final String message) {
        super(message + formatObjectIdentifier(identifier));

        this.identifier = identifier;
    }


    public ObjectIdentity getIdentifier() {
        return identifier;
    }


    static String formatObjectIdentifier(ObjectIdentity identifier) {
        return " (Object: uuid=" + identifier.getUuid()
                + ", tenant='" + identifier.getTenant() + "'"
                + "key='" + identifier.getKey() + "')";
    }
}
