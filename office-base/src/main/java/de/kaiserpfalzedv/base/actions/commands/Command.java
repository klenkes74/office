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

package de.kaiserpfalzedv.base.actions.commands;

import de.kaiserpfalzedv.base.api.KindHolding;
import de.kaiserpfalzedv.base.api.MetadataHolding;
import de.kaiserpfalzedv.base.api.Spec;

import java.io.Serializable;

/**
 * @param <T> The {@link Spec} this command works on.
 * @author rlichti
 * @since 2019-12-15
 */
public interface Command<T extends Spec<? extends Serializable>> extends KindHolding, MetadataHolding {
    T apply(final T orig) throws CommandException;
}
