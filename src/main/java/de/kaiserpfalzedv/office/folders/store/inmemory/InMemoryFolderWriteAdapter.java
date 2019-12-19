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
import de.kaiserpfalzedv.office.folders.Folder;
import de.kaiserpfalzedv.office.folders.ImmutableFolder;
import de.kaiserpfalzedv.office.folders.ImmutableFolderSpec;
import de.kaiserpfalzedv.office.folders.store.FolderWriteAdapter;

import javax.enterprise.context.Dependent;
import java.time.OffsetDateTime;
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
    public void create(Folder spec) throws UuidAlreadyExistsException, KeyAlreadyExistsException {
        if (store.loadByUuid(spec.getSpec().getIdentity().getUuid()).isPresent()) {
            throw new UuidAlreadyExistsException(spec.getSpec().getIdentity());
        }

        if (store.loadByScopeAndKey(
                spec.getSpec().getIdentity().getScope().orElse(InMemoryFolderStore.DEFAULT_SCOPE),
                spec.getSpec().getIdentity().getName().orElse(null)
        )
                .isPresent()) {
            throw new KeyAlreadyExistsException(spec.getSpec().getIdentity());
        }

        store.store(spec);
    }

    @Override
    public void modify(Folder spec) throws ModificationFailedException {
        store.replace(spec);
    }

    @Override
    public void close(UUID id) throws ModificationFailedException {
        Optional<Folder> spec = store.loadByUuid(id);

        if (!spec.isPresent()) {
            throw new NoModifiableDataFoundException(ImmutableObjectIdentifier.builder()
                    .kind(Folder.KIND)
                    .version(Folder.VERSION)
                    .uuid(id)
                    .build()
            );
        }

        store.replace(ImmutableFolder.builder()
                .from(spec.get())
                .spec(ImmutableFolderSpec.builder()
                        .from(spec.get().getSpec())
                        .closed(OffsetDateTime.now())

                        .build()
                )

                .build()
        );
    }

    @Override
    public void delete(UUID id) {
        store.loadByUuid(id).ifPresent(folder -> store.remove(folder));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", InMemoryFolderWriteAdapter.class.getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .add("store=" + store)
                .toString();
    }
}
