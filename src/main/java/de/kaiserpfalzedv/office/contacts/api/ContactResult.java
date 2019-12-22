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

package de.kaiserpfalzedv.office.contacts.api;

import de.kaiserpfalzedv.base.actions.results.Result;
import de.kaiserpfalzedv.office.contacts.ContactSpec;

/**
 * @param <T> The contact command type this is the result to.
 * @author rlichti
 * @since 2019-12-22 13:35
 */
public interface ContactResult<T extends ContactCommand<? extends ContactSpec>> extends Result<ContactSpec> {

}
