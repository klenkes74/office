/*
 *  Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2019. All rights reserved.
 *
 *  This file is part of Kaiserpfalz EDV-Service Office.
 *
 *  This is free software: you can redistribute it and/or modify it under the terms of
 *   the GNU Lesser General Public License as published by the Free Software
 *   Foundation, either version 3 of the License.
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

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.store.DataAlreadyExistsException;
import de.kaiserpfalzedv.folders.CreateFolder;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.FolderSpec;
import de.kaiserpfalzedv.folders.store.InMemoryFolderStoreAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

public class FolderCommandServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderCommandServiceTest.class);

    private static final UUID ID = UUID.randomUUID();

    private static final CreateFolder CREATE_FOLDER = ImmutableCreateFolder.builder()
            .kind(Folder.KIND)
            .metadata(ImmutableMetadata.builder()
                    .identity(ImmutableObjectIdentifier.builder()
                            .kind(Folder.KIND)
                            .version(Folder.VERSION)
                            .uuid(ID)
                            .scope("kes")
                            .name("I'19-0001")
                            .build()
                    )
                    .created(OffsetDateTime.now())
                    .build()
            )
            .spec(ImmutableFolderSpec.builder()
                    .uuid(ID)
                    .scope("kes")
                    .key("I'19-0001")
                    .name("Adoptionsverfahren Lichti ./. Hellwig")
                    .shortName("Lichti ./. Hellwig")
                    .description("Adoptionsverfahren Alexandra Maria Lichti")
                    .created(OffsetDateTime.now())
                    .modified(OffsetDateTime.now())
                    .build()
            )
            .build();

    private FolderCommandService service;

    @BeforeEach
    void setUpFolderCommandService() throws DataAlreadyExistsException {
        service = new FolderCommandService(new InMemoryFolderStoreAdapter());
        service.write(CREATE_FOLDER);
    }


    @Test
    public void shouldReturnValidDataWhenLoadingById() {
        Folder result = service.load(ID);
        LOGGER.trace("Loaded: {}", result);

        assert result.getSpec().isPresent();
        assert ID.equals(result.getSpec().get().getUuid());
    }

    @Test
    public void shouldReturnValidDataWhenLoadingByScope() {
        Collection<FolderSpec> result = service.load("kes", null, null);
        LOGGER.trace("Loaded: {}", result);

        assert result.size() >= 1;
    }

    @Test
    public void shouldReturnEmptyDataWhenGivenRandomUUID() {
        Folder result = service.load(UUID.randomUUID());
        LOGGER.trace("Loaded: {}", result);

        assert !result.getSpec().isPresent();
    }
}
