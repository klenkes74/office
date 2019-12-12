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

package de.kaiserpfalzedv.office.folders.store;

import de.kaiserpfalzedv.office.folders.FolderSpec;
import de.kaiserpfalzedv.office.folders.ImmutableFolderSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFolderStoreAdapter implements FolderStoreAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryFolderStoreAdapter.class);

    private static final int INITIAL_SIZE = 20;

    private final ConcurrentHashMap<UUID, FolderSpec> folders = new ConcurrentHashMap<>(INITIAL_SIZE);
    private final ConcurrentHashMap<String, Set<FolderSpec>> folderByScope = new ConcurrentHashMap<>(INITIAL_SIZE);
    private final ConcurrentHashMap<String, FolderSpec> folderByScopeAndKey = new ConcurrentHashMap<>(INITIAL_SIZE);

    @Override
    public Optional<FolderSpec> loadById(final UUID uuid) {
        LOGGER.debug("Loading by UUID: uuid={}", uuid);

        return folders.containsKey(uuid) ? Optional.of(folders.get(uuid)) : Optional.empty();
    }

    @Override
    public Optional<FolderSpec> loadByName(String scope, String name) {
        LOGGER.debug("Loading by scope and key: scope={}, key={}", scope, name);
        String scopeAndKey = scope + "::" + name;

        return folderByScopeAndKey.containsKey(scopeAndKey) ?
                Optional.of(folderByScopeAndKey.get(scopeAndKey))
                : Optional.empty();
    }

    @Override
    public Set<FolderSpec> loadByScope(String scope) {
        LOGGER.debug("Loading by scope: scope={}", scope);

        return folderByScope.containsKey(scope) ?
                folderByScope.get(scope)
                : new HashSet<>(0);
    }

    @Override
    public FolderSpec save(final FolderSpec data) {
        LOGGER.debug("Saving data: data={}", data);

        OffsetDateTime now = OffsetDateTime.now();

        FolderSpec saved = ImmutableFolderSpec
                .copyOf(data)
                .withCreated(now)
                .withModified(now);

        folders.put(saved.getUuid(), saved);

        String scopeAndKey = data.getScope().orElse("./") + "::" + data.getKey();
        folderByScopeAndKey.put(scopeAndKey, saved);

        if (!folderByScope.containsKey(saved.getScope().orElse("./."))) {
            folderByScope.put(saved.getScope().orElse("./."), new HashSet<>());
        }
        folderByScope.get(saved.getScope().orElse("./.")).add(saved);

        return saved;
    }

    @Override
    public void close(final UUID id) {
        LOGGER.debug("Closing folder: uuid={}", id);

        if (folders.containsKey(id)) {
            FolderSpec data = folders.remove(id);

            folderByScope.get(data.getScope().orElse("./.")).remove(data);
            folderByScopeAndKey.remove(data.getScope() + "::" + data.getKey());

            data = ImmutableFolderSpec.copyOf(data).withClosed(OffsetDateTime.now()).withModified(OffsetDateTime.now());
            folders.put(id, data);
        }
    }
}
