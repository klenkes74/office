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

package de.kaiserpfalzedv.folders;

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.kaiserpfalzedv.folders.TestDefaultFolder.*;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 10:42
 */
public class FolderCreatedTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderCreatedTest.class);

    private static final FolderCreated SERVICE = ImmutableFolderCreated.builder()
            .metadata(ImmutableMetadata.copyOf(FOLDER_METADATA)
                    .withIdentity(ImmutableObjectIdentity.copyOf(FOLDER_IDENTITY)
                            .withKind(FolderCreated.KIND)
                            .withVersion(FolderCreated.VERSION)
                    )
            )
            .spec(FOLDER_SPEC)
            .build();

    @Test
    public void shouldReturnCorrectKindOfFolder() {
        assert FolderCreated.KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfFolder() {
        assert FolderCreated.VERSION.equals(SERVICE.getVersion());
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
