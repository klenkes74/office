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

package de.kaiserpfalzedv.office.folders.mocks;

import de.kaiserpfalzedv.office.folders.*;
import de.kaiserpfalzedv.office.folders.api.FolderRepository;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class MockFolderRepository implements FolderRepository {
    private static final UUID EMPTY_ID = UUID.fromString("000000000-0000-0000-0000-000000000000");

    private static final String SCOPE = "./.";
    private static final String KEY = "I19-001";
    private static final String NAME = "Sorgerecht Lichti ./. Hellwig";
    private static final String SHORT_NAME = "Lichti ./. Hellwig";

    @Override
    public Optional<FolderSpec> loadById(final UUID uuid) {
        if (EMPTY_ID.equals(uuid))
            return Optional.empty();

        return Optional.of(create(uuid, SCOPE, KEY, NAME, SHORT_NAME));
    }

    @Override
    public Optional<FolderSpec> loadByName(String scope, String name) {
        if ("empty".equals(scope))
            return Optional.empty();

        return Optional.of(create(UUID.randomUUID(), scope, KEY, name, SHORT_NAME));
    }

    @Override
    public Set<FolderSpec> loadByScope(String scope) {
        if ("empty".equals(scope))
            return new HashSet<>(0);

        HashSet<FolderSpec> result = new HashSet<>(5);
        result.add(create(UUID.randomUUID(), scope, KEY, NAME, SHORT_NAME));
        result.add(create(UUID.randomUUID(), scope, KEY, NAME, SHORT_NAME));
        result.add(create(UUID.randomUUID(), scope, KEY, NAME, SHORT_NAME));
        result.add(create(UUID.randomUUID(), scope, KEY, NAME, SHORT_NAME));
        result.add(create(UUID.randomUUID(), scope, KEY, NAME, SHORT_NAME));

        return result;
    }

    @Override
    public FolderSpec write(FolderSpec data) {
        OffsetDateTime now = OffsetDateTime.now();

        return ImmutableFolderSpec
                .copyOf(data)
                .withCreated(now)
                .withModified(now);
    }

    @Override
    public void close(final UUID id) {

    }


    private FolderSpec create(
            final UUID id,
            final String scope,
            @SuppressWarnings("SameParameterValue") final String key,
            final String name,
            @SuppressWarnings("SameParameterValue") final String shortName
    ) {
        OffsetDateTime created = OffsetDateTime.now().minusYears(1);
        @SuppressWarnings("MathRandomCastToInt") OffsetDateTime modified = created.plusMonths((int)(Math.random() * 5) +1);

        return ImmutableFolderSpec.builder()
                                .uuid(id)
                                .scope(scope)
                                .key(key)
                                .name(name)
                                .shortName(shortName)
                                .created(created)
                                .modified(modified)
                                .build();
    }
}
