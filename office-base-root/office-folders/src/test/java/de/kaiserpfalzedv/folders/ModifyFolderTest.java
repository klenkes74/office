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

import java.util.Objects;

import static de.kaiserpfalzedv.folders.store.TestDefaultFolder.*;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 10:42
 */
public class ModifyFolderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyFolderTest.class);


    private static final ModifyFolder SERVICE = ImmutableModifyFolder.builder()
            .metadata(ImmutableMetadata.builder()
                    .identity(ImmutableObjectIdentity.copyOf(TestDefaultFolder.FOLDER_IDENTITY)
                            .withKind(ModifyFolder.KIND)
                            .withVersion(ModifyFolder.VERSION)
                    )
                    .build())
            .spec(FOLDER_SPEC)
            .build();


    @Test
    public void shouldReturnCorrectKindOfFolder() {
        assert ModifyFolder.KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfFolder() {
        assert ModifyFolder.VERSION.equals(SERVICE.getVersion());
    }

    @Test
    public void shouldApplyTheCommandCorrectly() {
        FolderSpec newSpec = ImmutableFolderSpec.copyOf(FOLDER_SPEC)
                .withName("old name")
                .withDisplayname("old shortname");

        LOGGER.trace("input: {}", SERVICE);
        FolderSpec result = SERVICE.apply(newSpec);
        LOGGER.debug("result: {}", result);

        assert SERVICE.getSpec().getIdentity().getUuid().equals(result.getIdentity().getUuid());
        assert Objects.equals(SERVICE.getSpec().getIdentity().getTenant(), result.getIdentity().getTenant());
        assert FOLDER_DISPLAYNAME.equals(result.getDisplayname());
        assert FOLDER_NAME.equals(result.getIdentity().getName().orElse(null));
        assert SERVICE.getSpec().getName().equals(result.getName());
        assert SERVICE.getSpec().getDescription().equals(result.getDescription());
        assert SERVICE.getSpec().getModified().equals(result.getModified());
    }

    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", ModifyFolder.class.getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", ModifyFolder.class.getCanonicalName());
    }
}
