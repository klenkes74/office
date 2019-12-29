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

package de.kaiserpfalzedv.commons.actions.results;

import de.kaiserpfalzedv.commons.actions.commands.Command;
import de.kaiserpfalzedv.commons.api.KindHolding;
import de.kaiserpfalzedv.commons.api.MetadataHolding;

import java.io.Serializable;

/**
 * @param <T> The command this result answers.
 * @param <S> The spec of the data this result worked on.
 * @author rlichti
 * @since 2019-12-15
 */
public interface Result<T extends Command<? extends Serializable>, S extends Serializable>
        extends KindHolding, MetadataHolding {
}
