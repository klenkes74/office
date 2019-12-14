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

package de.kaiserpfalzedv.base.api;


import de.kaiserpfalzedv.base.api.status.Status;

import java.io.Serializable;
import java.util.List;

public interface BaseAPI<T extends Serializable> extends Serializable {
    String VERSION = "1.0.0";

    /**
     * The type of object transmitted. The tupple of scope, kind and version must be unique.
     *
     * @return the kind of object.
     */
    default String getKind() {
        return getClass().getSimpleName();
    }

    /**
     * The version following {@linkplain https://semver.org/}.  The tupple of scope, kind and version must be unique.
     *
     * @return the version as string conforming to {@link https://semver.org/}.
     */
    default String getVersion() {
        return VERSION;
    }

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
