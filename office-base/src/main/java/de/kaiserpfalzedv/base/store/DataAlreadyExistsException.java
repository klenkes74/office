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

import de.kaiserpfalzedv.base.api.ObjectIdentity;

@SuppressWarnings("CdiInjectionPointsInspection")
public abstract class DataAlreadyExistsException extends DataException {
    private final ObjectIdentity identifier;

    public DataAlreadyExistsException(final ObjectIdentity identifier, final String message, final Throwable cause) {
        super(identifier, message, cause);

        this.identifier = identifier;
    }

    public DataAlreadyExistsException(final ObjectIdentity identifier, final Throwable cause) {
        super(identifier, "Object with matching identifier already exists", cause);

        this.identifier = identifier;
    }

    public DataAlreadyExistsException(final ObjectIdentity identifier) {
        super(identifier, "Object with matching identifier already exists");

        this.identifier = identifier;
    }

    public ObjectIdentity getIdentifier() {
        return identifier;
    }
}
