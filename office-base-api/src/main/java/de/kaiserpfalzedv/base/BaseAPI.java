/*
 *  Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2019. All rights reserved.
 *
 *  This file is part of Kaiserpfalz EDV-Service Office.
 *
 *  This is free software: you can redistribute it and/or modify it under the terms of
 *   the GNU Lesser General Public License as published by the Free Software
 *   Foundation, either version 3 of the License.
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


import de.kaiserpfalzedv.base.status.Status;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseAPI<T extends Serializable> extends Serializable {
    /**
     * The type of object transmitted. The tupple of scope, kind and version must be unique.
     *
     * @return the kind of object.
     */
    default String getKind() { return getClass().getSimpleName(); }

    /**
     * The version following {@link https://semver.org/}.  The tupple of scope, kind and version must be unique.
     *
     * @return the version as string conforming to {@link https://semver.org/}.
     */
    default String getVersion() { return "1.0.0"; }

    /**
     * The scope of this object. May be used for plugin systems to make the {@link #getKind()} unique. The tupple of
     * scope, kind and version must be unique.
     *
     * @return the scope of this object.
     */
    default Optional<String> getScope() { return Optional.of(getClass().getPackage().getName()); }

    /**
     * The metadata of the object. It's an identifier plus some technical data like modification times.
     *
     * @return The metadata of the object.
     */
    Metadata getMetadata();

    /**
     * A list of status (changes) of the object.
     *
     * @return a list of status changes or status messages.
     */
    List<Status> getStatus();
}
