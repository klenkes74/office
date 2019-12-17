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

package de.kaiserpfalzedv.base;


import de.kaiserpfalzedv.base.api.Metadata;

import java.io.Serializable;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-04
 */
public interface BaseObject<T extends Serializable> extends Serializable {
    String getKind();

    /**
     * The version of the workflow or the defined BaseObject canonical name.
     *
     * @return the version as string conforming to {@linkplain <a href="https://semver.org/">semver.org</a>}.
     */
    String getVersion();

    /**
     * The metadata of the object. It's an identifier plus some technical data like modification times.
     *
     * @return The metadata of the object.
     */
    Metadata getMetadata();
}
