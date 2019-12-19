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

package de.kaiserpfalzedv.office.folders.store.inmemory;

import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.store.NoModifiableDataFoundException;
import de.kaiserpfalzedv.office.folders.Folder;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-15 19:26
 */
@ApplicationScoped
public class InMemoryFolderStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryFolderStore.class);

    public static final String DEFAULT_SCOPE = "./.";

    EmbeddedCacheManager cacheManager;

    private Cache<UUID, Folder> folders;
    private Cache<String, HashMap<String, Folder>> scopeAndKey;


    public InMemoryFolderStore() {
        this.cacheManager = new DefaultCacheManager();
    }

    @Inject
    public InMemoryFolderStore(final EmbeddedCacheManager cacheManager) {
        LOGGER.debug("Using injected CacheManager: {}", cacheManager);

        this.cacheManager = cacheManager;
    }

    @PostConstruct
    public void createCaches() {
        if (!this.cacheManager.cacheExists("folders")) {
            folders = this.cacheManager.createCache("folders", new ConfigurationBuilder().memory().build());
        }

        if (!this.cacheManager.cacheExists(("scopeAndKey"))) {
            scopeAndKey = this.cacheManager.createCache("scopeAndKey", new ConfigurationBuilder().memory().build());
        }

        LOGGER.debug("Caches for FolderStore prepared: {} and {}", folders, scopeAndKey);
    }


    public Optional<Folder> loadByUuid(final UUID uuid) {
        return Optional.ofNullable(folders.get(uuid));
    }

    public Optional<Folder> loadByScopeAndKey(final String scope, final String key) {
        try {
            return Optional.ofNullable(scopeAndKey.get(scope).get(key));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    public Optional<Folder> loadByKey(final String key) {
        return loadByScopeAndKey(DEFAULT_SCOPE, key);
    }

    public ArrayList<Folder> loadByScope(final String scope) {
        if (scopeAndKey.containsKey(scope)) {
            return new ArrayList<>(scopeAndKey.get(scope).values());
        } else {
            return new ArrayList<>(0);
        }
    }

    public ArrayList<Folder> loadEntriesWithoutScope() {
        return loadByScope(DEFAULT_SCOPE);
    }

    public void store(final Folder folder) {
        ObjectIdentifier identity = folder.getSpec().getIdentity();
        LOGGER.trace("Storing Folder: {}", identity);

        folders.put(identity.getUuid(), folder);

        if (identity.getName().isPresent()) {
            scopeAndKey.putIfAbsent(identity.getScope().orElse(DEFAULT_SCOPE), new HashMap<>(5));
            scopeAndKey.get(identity.getScope().orElse(DEFAULT_SCOPE)).put(identity.getName().get(), folder);
        }
    }

    public void replace(final Folder folder) throws NoModifiableDataFoundException {
        LOGGER.trace("Replacing data for Folder: {}", folder.getSpec().getIdentity());

        if (!folders.containsKey(folder.getSpec().getIdentity().getUuid())) {
            throw new NoModifiableDataFoundException(folder.getSpec().getIdentity());
        }

        folders.replace(folder.getSpec().getIdentity().getUuid(), folder);
    }

    public void remove(final Folder folder) {
        ObjectIdentifier identity = folder.getSpec().getIdentity();
        LOGGER.trace("Deleting Folder: {}", identity);

        if (identity.getName().isPresent()
                && scopeAndKey.containsKey(identity.getScope().orElse(DEFAULT_SCOPE))) {
            scopeAndKey.get(identity.getScope().orElse(DEFAULT_SCOPE)).remove(identity.getName().get());
        }

        folders.remove(identity.getUuid());
    }

    public long getObjectCount() {
        return folders.size();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", InMemoryFolderStore.class.getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .add("cacheManager=" + cacheManager)
                .toString();
    }
}
