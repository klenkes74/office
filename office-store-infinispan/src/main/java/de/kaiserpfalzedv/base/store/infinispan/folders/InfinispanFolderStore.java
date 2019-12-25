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

package de.kaiserpfalzedv.base.store.infinispan.folders;

import de.kaiserpfalzedv.base.api.ObjectIdentity;
import de.kaiserpfalzedv.base.store.NoModifiableDataFoundException;
import de.kaiserpfalzedv.folders.Folder;
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
public class InfinispanFolderStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanFolderStore.class);

    public static final String DEFAULT_TENANT = "./.";

    EmbeddedCacheManager cacheManager;

    private Cache<UUID, Folder> folders;
    private Cache<String, HashMap<String, Folder>> scopeAndKey;


    public InfinispanFolderStore() {
        this.cacheManager = new DefaultCacheManager();
    }

    @Inject
    public InfinispanFolderStore(final EmbeddedCacheManager cacheManager) {
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


    public Optional<Folder> loadByUuid(final String tenant, final UUID uuid) {
        Folder result = folders.get(uuid);
        if (result == null) {
            return Optional.empty();
        }

        if (result.getMetadata().getIdentity().getTenant().equals(tenant)) {
            return Optional.ofNullable(folders.get(uuid));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Folder> loadByScopeAndKey(final String scope, final String key) {
        try {
            return Optional.ofNullable(scopeAndKey.get(scope).get(key));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    public Optional<Folder> loadByKey(final String tenant, final String key) {
        return loadByScopeAndKey(tenant, key);
    }

    public ArrayList<Folder> loadByTenant(final String tenant) {
        if (scopeAndKey.containsKey(tenant)) {
            return new ArrayList<>(scopeAndKey.get(tenant).values());
        } else {
            return new ArrayList<>(0);
        }
    }

    public ArrayList<Folder> loadEntriesWithoutScope() {
        return loadByTenant(DEFAULT_TENANT);
    }

    public void store(final Folder folder) {
        ObjectIdentity identity = folder.getSpec().getIdentity();
        LOGGER.trace("Storing Folder: {}", identity);

        folders.put(identity.getUuid(), folder);

        if (identity.getName().isPresent()) {
            scopeAndKey.putIfAbsent(identity.getTenant(), new HashMap<>(5));
            scopeAndKey.get(identity.getTenant()).put(identity.getName().get(), folder);
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
        ObjectIdentity identity = folder.getSpec().getIdentity();
        LOGGER.trace("Deleting Folder: {}", identity);

        if (identity.getName().isPresent()
                && scopeAndKey.containsKey(identity.getTenant())) {
            scopeAndKey.get(identity.getTenant()).remove(identity.getName().get());
        }

        folders.remove(identity.getUuid());
    }

    public long getObjectCount() {
        return folders.size();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", InfinispanFolderStore.class.getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .add("cacheManager=" + cacheManager)
                .toString();
    }
}
