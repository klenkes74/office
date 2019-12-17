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

package de.kaiserpfalzedv.office.folders;

import de.kaiserpfalzedv.base.actions.commands.CreateCommand;
import de.kaiserpfalzedv.base.actions.commands.DeleteCommand;
import de.kaiserpfalzedv.base.actions.commands.DeleteCommandException;
import de.kaiserpfalzedv.base.actions.commands.ModifyCommand;
import de.kaiserpfalzedv.base.api.Metadata;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.office.folders.api.FolderCommandService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 10:42
 */
public class DeleteFolderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteFolderTest.class);

    static private final UUID ID = UUID.randomUUID();

    private static final DeleteFolder SERVICE = new DeleteFolder() {
        @Override
        public Metadata getMetadata() {
            return null;
        }

        @Override
        public FolderSpec getSpec() {
            return null;
        }
    };

    private static final FolderCommandService FOLDER_COMMAND_SERVICE = new FolderCommandService() {
        @Override
        public void execute(CreateCommand<FolderSpec> command) {
        }

        @Override
        public void execute(ModifyCommand<FolderSpec> command) {
        }

        @Override
        public void execute(DeleteCommand<FolderSpec> command) {
        }
    };

    private static final FolderSpec FOLDER = new FolderSpec() {
        @Override
        public ObjectIdentifier getIdentity() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Optional<String> getShortName() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getDescription() {
            return Optional.empty();
        }

        @Override
        public Optional<OffsetDateTime> getClosed() {
            return Optional.empty();
        }

        @Override
        public OffsetDateTime getCreated() {
            return null;
        }

        @Override
        public OffsetDateTime getModified() {
            return null;
        }
    };

    @Test
    public void shouldReturnCorrectKindOfFolder() {
        assert DeleteFolder.KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfFolder() {
        assert DeleteFolder.VERSION.equals(SERVICE.getVersion());
    }

    @Test
    public void shouldExecuteTheCommandCorrectly() throws DeleteCommandException {
        SERVICE.execute(FOLDER_COMMAND_SERVICE);
    }

    @Test
    public void shouldApplyTheCommandCorrectly() {
        assert FOLDER.getShortName().equals(SERVICE.apply(FOLDER).getShortName());
    }

    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", SERVICE.getClass().getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", SERVICE.getClass().getCanonicalName());
    }
}
