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

package de.kaiserpfalzedv.base.api;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Optional;


/**
 * @author rlichti
 * @since 2019-12-15 12:02
 */
public interface Spec<T extends Serializable> extends Serializable {
    /**
     * A string to be displayed to users in lists, titles, ...
     *
     * @return a string that is used by human users to identify the data within this spec.
     */
    Optional<String> getDisplayname();

    /**
     * The creation date of the spec data.
     *
     * @return when the spec has been created.
     */
    OffsetDateTime getCreated();

    /**
     * The last modification timestamp of the spec data.
     *
     * @return when the spec has been modified latest. If there has been no modification it is the creation timestamp.
     */
    OffsetDateTime getModified();
}
