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

package de.kaiserpfalzedv.office.folders.store;

import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.*;
import de.kaiserpfalzedv.office.folders.FolderSpec;
import de.kaiserpfalzedv.office.folders.ImmutableFolderSpec;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolderSpec;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

@QuarkusTest
@Tag("integration")
public class JPAFolderWriteAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderWriteAdapterTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String SCOPE = "kes";
    private static final String KEY = "I'19-0000";
    private static final String NAME = "Softwaretest Akte";
    private static final String SHORTNAME = "Softwaretest";
    private static final String DESCRIPTION = "Einfache Akte für Softwaretests";
    private static final OffsetDateTime CREATED = OffsetDateTime.of(2019, 12, 11, 12, 0, 0, 0, ZoneOffset.ofHours(1));
    private static final OffsetDateTime MODIFIED = CREATED;
    private static final FolderSpec FOLDER = ImmutableFolderSpec.builder()
            .identity(ImmutableObjectIdentifier.builder()
                    .kind(de.kaiserpfalzedv.office.folders.Folder.KIND)
                    .version(de.kaiserpfalzedv.office.folders.Folder.VERSION)
                    .uuid(ID)
                    .scope(SCOPE)
                    .name(KEY)
                    .build()
            )
            .name(NAME)
            .shortName(SHORTNAME)
            .description(DESCRIPTION)
            .created(CREATED)
            .modified(MODIFIED)
            .build();

    @Inject
    @JPA
    FolderReadAdapter readAdapter;
    @Inject
    @JPA
    FolderWriteAdapter storeAdapter;

    @BeforeEach
    @Transactional
    public void generateMockService() throws CreationFailedException {
        try {
            storeAdapter.create(FOLDER);
        } catch (DataAlreadyExistsException e) {
            // ignore, we want the data to be there and there it is ...
        }

        assert JPAFolderSpec.count("uuid", FOLDER.getIdentity().getUuid()) == 1;
    }


    @Test
    public void shouldFailWhenUuidIsADoublette() {
        try {
            storeAdapter.create(FOLDER);

            fail("The UUID is a doublette. The save should have failed!");
        } catch (UuidAlreadyExistsException | KeyAlreadyExistsException | CreationFailedException ex) {
            assert ex.getIdentifier().getUuid().equals(FOLDER.getIdentity().getUuid());
        }
    }


    @Test
    public void shouldFailWhenScopeAndKeyIsADoublette() {
        try {
            storeAdapter.create(ImmutableFolderSpec.copyOf(FOLDER)
                    .withIdentity(ImmutableObjectIdentifier.copyOf(FOLDER.getIdentity())
                            .withUuid(UUID.randomUUID())
                    )
            );

            fail("The UUID is a doublette. The save should have failed!");
        } catch (UuidAlreadyExistsException | KeyAlreadyExistsException | CreationFailedException ex) {
            ObjectIdentifier identifier = ex.getIdentifier();
            assert identifier.getScope().equals(FOLDER.getIdentity().getScope());
            assert identifier.getName().equals(FOLDER.getIdentity().getName());
        }
    }

    @Test
    @Transactional
    public void shouldSetCloseDateWhenFolderIsClosed() throws ModificationFailedException {
        storeAdapter.close(ID);

        Optional<FolderSpec> result = readAdapter.loadById(ID);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
        assert result.get().getClosed().isPresent();
    }
}
