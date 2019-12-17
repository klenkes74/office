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

import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.cdi.Mock;
import de.kaiserpfalzedv.base.store.KeyAlreadyExistsException;
import de.kaiserpfalzedv.base.store.ModificationFailedException;
import de.kaiserpfalzedv.base.store.NoModifiableDataFoundException;
import de.kaiserpfalzedv.base.store.UuidAlreadyExistsException;
import de.kaiserpfalzedv.office.folders.FolderSpec;
import de.kaiserpfalzedv.office.folders.store.FolderWriteAdapter;

import javax.enterprise.context.Dependent;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-15 20:06
 */
@Mock
@Dependent
public class InMemoryFolderWriteAdapter implements FolderWriteAdapter {
    InMemoryFolderStore store;

    public InMemoryFolderWriteAdapter(final InMemoryFolderStore store) {
        this.store = store;
    }

    @Override
    public void create(FolderSpec spec) throws UuidAlreadyExistsException, KeyAlreadyExistsException {
        if (store.loadByUuid(spec.getIdentity().getUuid()).isPresent()) {
            throw new UuidAlreadyExistsException(spec.getIdentity());
        }

        if (store.loadByScopeAndKey(
                spec.getIdentity().getScope().orElse(InMemoryFolderStore.DEFAULT_SCOPE),
                spec.getIdentity().getName().orElse(null)
        )
                .isPresent()) {
            throw new KeyAlreadyExistsException(spec.getIdentity());
        }

        store.store(spec);
    }

    @Override
    public void modify(FolderSpec spec) throws ModificationFailedException {
        store.replace(spec);
    }

    @Override
    public void close(UUID id) throws ModificationFailedException {
        Optional<FolderSpec> spec = store.loadByUuid(id);

        if (!spec.isPresent()) {
            throw new NoModifiableDataFoundException(ImmutableObjectIdentifier.builder()
                    .uuid(id)
                    .build()
            );
        }

        store.remove(spec.get());
    }

    @Override
    public void delete(UUID id) {
        store.loadByUuid(id).ifPresent(folderSpec -> store.remove(folderSpec));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", InMemoryFolderWriteAdapter.class.getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .add("store=" + store)
                .toString();
    }
}
