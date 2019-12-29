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

import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import de.kaiserpfalzedv.commons.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.folders.store.TestDefaultFolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.kaiserpfalzedv.folders.store.TestDefaultFolder.FOLDER_SPEC;

/**
 * @author rlichti
 * @since 2019-12-14 10:42
 */
public class CreateFolderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateFolderTest.class);

    private static final CreateFolder SERVICE = ImmutableCreateFolder.builder()
            .metadata(ImmutableMetadata.builder()
                    .identity(ImmutableObjectIdentity.copyOf(TestDefaultFolder.FOLDER_IDENTITY)
                            .withKind(CreateFolder.KIND)
                            .withVersion(CreateFolder.VERSION)
                    )
                    .build())
            .spec(FOLDER_SPEC)
            .build();


    @Test
    public void shouldReturnCorrectKindOfFolder() {
        assert CreateFolder.KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfFolder() {
        assert CreateFolder.VERSION.equals(SERVICE.getVersion());
    }

    @Test
    public void shouldApplyTheCommandCorrectly() {
        assert FOLDER_SPEC.getDisplayname().equals(SERVICE.apply(FOLDER_SPEC).getDisplayname());
    }

    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", CreateFolder.class.getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", CreateFolder.class.getCanonicalName());
    }
}
