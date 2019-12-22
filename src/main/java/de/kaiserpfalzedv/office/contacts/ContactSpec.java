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

package de.kaiserpfalzedv.office.contacts;

import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.api.Spec;

import java.io.Serializable;
import java.time.OffsetDateTime;

/*
 *
 *
 * @author rlichti@kaiserpfalz-edv.de
 * @since 2019-12-15T10:20Z
 */
public interface ContactSpec<T extends Serializable> extends Spec<T> {
    /**
     * The object identity of the person dataset.
     *
     * @return
     */
    ObjectIdentifier getIdentity();

    /**
     * The name to be displayed in the software.
     *
     * @return an optimised string for being displayed (e.g. in headers) for this person.
     */
    String getDisplayname();

    OffsetDateTime getCreated();

    OffsetDateTime getModified();
}
