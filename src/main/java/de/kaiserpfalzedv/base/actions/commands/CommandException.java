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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-15 10:32
 */
public abstract class CommandException extends Exception {
    private Command<? extends Serializable> command;

    public CommandException(@NotNull final Command<? extends Serializable> command) {
        super("Command '" + command.toString() + "' failed.");

        this.command = command;
    }

    public CommandException(@NotNull final Command<? extends Serializable> command, final Throwable cause) {
        super("Command '" + command.toString() + "' failed.", cause);

        this.command = command;
    }

    public CommandException(@NotNull final Command<? extends Serializable> command, final String message) {
        super("Command '" + command.toString() + "' failed: " + message);

        this.command = command;
    }

    public CommandException(@NotNull final Command<? extends Serializable> command,
                            final String message,
                            final Throwable cause) {
        super("Command '" + command.toString() + "' failed: " + message, cause);

        this.command = command;
    }

    public Command<? extends Serializable> getCommand() {
        return command;
    }
}
