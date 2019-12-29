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

import de.kaiserpfalzedv.commons.actions.commands.CommandWithSpec;
import de.kaiserpfalzedv.commons.api.Spec;
import de.kaiserpfalzedv.commons.api.SpecHolding;

import java.io.Serializable;

/**
 * @param <T> The {@link CommandWithSpec} the result answers.
 * @param <S> The {@link Spec} the result worked on.
 * @author rlichti
 * @since 2019-12-15
 */
public interface ResultWithSpec<T extends CommandWithSpec<? extends Spec<? extends Serializable>>, S extends Spec<? extends Serializable>>
        extends Result<T, S>, SpecHolding<S> {
}
