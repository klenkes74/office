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
import de.kaiserpfalzedv.folders.store.TestDefaultFolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 10:42
 */
public class FolderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderTest.class);

    private static final Folder SERVICE = TestDefaultFolder.FOLDER;

    @Test
    public void shouldReturnCorrectKindOfFolder() {
        assert Folder.KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfFolder() {
        assert Folder.VERSION.equals(SERVICE.getVersion());
    }

    @Test
    public void shouldReturnCorrectComparisonBetweenFoldersWhenTenantsMatchAndKeysDontMatchAndOtherIsSmaller() {
        Folder other = ImmutableFolder.copyOf(SERVICE)
                .withMetadata(ImmutableMetadata.copyOf(SERVICE.getMetadata())
                        .withIdentity(ImmutableObjectIdentity.copyOf(SERVICE.getMetadata().getIdentity())
                                .withName("akey")
                        )
                );
        LOGGER.debug("Comparing {} and {} result: {}", SERVICE, other, SERVICE.compareTo(other));

        assert SERVICE.compareTo(other) > 0;
    }

    @Test
    public void shouldReturnCorrectComparisonBetweenFoldersWhenTenantsAndKeysMatch() {
        Folder other = ImmutableFolder.copyOf(SERVICE);
        LOGGER.debug("Comparing {} and {} result: {}", SERVICE, other, SERVICE.compareTo(other));

        assert SERVICE.compareTo(other) == 0;
    }

    @Test
    public void shouldReturnCorrectComparisonBetweenFoldersWhenTenantsMatchAndKeysDontMatchAndOtherIsBigger() {
        Folder other = ImmutableFolder.copyOf(SERVICE)
                .withMetadata(ImmutableMetadata.copyOf(SERVICE.getMetadata())
                        .withIdentity(ImmutableObjectIdentity.copyOf(SERVICE.getMetadata().getIdentity())
                                .withName("natural-key")
                        )
                );
        LOGGER.debug("Comparing {} and {} result: {}", SERVICE, other, SERVICE.compareTo(other));

        assert SERVICE.compareTo(other) < 0;
    }

    @Test
    public void shouldReturnCorrectComparisonBetweenFoldersWhenTenantsDontMatchAndOtherIsSmaller() {
        Folder other = ImmutableFolder.copyOf(SERVICE)
                .withMetadata(ImmutableMetadata.copyOf(SERVICE.getMetadata())
                        .withIdentity(ImmutableObjectIdentity.copyOf(SERVICE.getMetadata().getIdentity())
                                .withName("natural-key")
                                .withTenant("akey")
                        )
                );
        LOGGER.debug("Comparing {} and {} result: {}", SERVICE, other, SERVICE.compareTo(other));

        assert SERVICE.compareTo(other) > 0;
    }

    @Test
    public void shouldReturnCorrectComparisonBetweenFoldersWhenTenantsDontMatchAndOtherIsBigger() {
        Folder other = ImmutableFolder.copyOf(SERVICE)
                .withMetadata(ImmutableMetadata.copyOf(SERVICE.getMetadata())
                        .withIdentity(ImmutableObjectIdentity.copyOf(SERVICE.getMetadata().getIdentity())
                                .withName("akey")
                                .withTenant("very-large-tenant")
                        )
                );
        LOGGER.debug("Comparing {} and {} result: {}", SERVICE, other, SERVICE.compareTo(other));

        assert SERVICE.compareTo(other) < 0;
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
