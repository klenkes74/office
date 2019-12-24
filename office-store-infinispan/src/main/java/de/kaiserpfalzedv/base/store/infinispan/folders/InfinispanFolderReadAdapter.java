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

import de.kaiserpfalzedv.base.cdi.Mock;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.store.FolderReadAdapter;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-15 19:57
 */
@Mock
@Dependent
public class InfinispanFolderReadAdapter implements FolderReadAdapter {
    InfinispanFolderStore store;

    public InfinispanFolderReadAdapter(final InfinispanFolderStore store) {
        this.store = store;
    }

    @Override
    public Optional<Folder> loadById(UUID id) {
        return store.loadByUuid(id);
    }

    @Override
    public Optional<Folder> loadByScopeAndKey(String scope, String key) {
        return store.loadByScopeAndKey(scope, key);
    }

    @Override
    public ArrayList<Folder> loadByTenant(String scope) {
        return store.loadByScope(scope);
    }

    @Override
    public ArrayList<Folder> loadByTenant(String scope, int index, int size) {
        ArrayList<Folder> data = store.loadByScope(scope);

        if (index >= data.size()) {
            return new ArrayList<>(0);
        }

        int toIndex = index + size < data.size() ? index + size : data.size() - 1;

        return new ArrayList<>(data.subList(index, toIndex));
    }

    @Override
    public long count() {
        return store.getObjectCount();
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", InfinispanFolderReadAdapter.class.getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .add("store=" + store)
                .toString();
    }
}
