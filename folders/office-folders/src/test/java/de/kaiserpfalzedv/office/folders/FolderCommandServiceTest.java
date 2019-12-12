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

import de.kaiserpfalzedv.office.folders.store.InMemoryFolderStoreAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.UUID;

public class FolderCommandServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderCommandServiceTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final UUID EMPTY_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");


    private FolderCommandService service;

    @BeforeEach
    void setUpFolderCommandService() {
        service = new FolderCommandService(new InMemoryFolderStoreAdapter());
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
    public void shouldReturnEmptyDataWhenGiven0000UUID() {
        Folder result = service.load(EMPTY_ID);
        LOGGER.trace("Loaded: {}", result);

        assert !result.getSpec().isPresent();
    }
}
