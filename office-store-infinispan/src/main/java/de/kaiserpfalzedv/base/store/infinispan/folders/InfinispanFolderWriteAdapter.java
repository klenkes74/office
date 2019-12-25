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

import de.kaiserpfalzedv.base.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.base.cdi.Mock;
import de.kaiserpfalzedv.base.store.KeyAlreadyExistsException;
import de.kaiserpfalzedv.base.store.ModificationFailedException;
import de.kaiserpfalzedv.base.store.NoModifiableDataFoundException;
import de.kaiserpfalzedv.base.store.UuidAlreadyExistsException;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.ImmutableFolder;
import de.kaiserpfalzedv.folders.ImmutableFolderSpec;
import de.kaiserpfalzedv.folders.store.FolderWriteAdapter;

import javax.enterprise.context.Dependent;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author rlichti
 * @since 2019-12-15 20:06
 */
@Mock
@Dependent
public class InfinispanFolderWriteAdapter implements FolderWriteAdapter {
    InfinispanFolderStore store;

    public InfinispanFolderWriteAdapter(final InfinispanFolderStore store) {
        this.store = store;
    }

    @Override
    public void create(final Folder spec) throws UuidAlreadyExistsException, KeyAlreadyExistsException {
        if (store.loadByUuid(spec.getSpec().getIdentity().getTenant(), spec.getSpec().getIdentity().getUuid()).isPresent()) {
            throw new UuidAlreadyExistsException(spec.getSpec().getIdentity());
        }

        if (store.loadByScopeAndKey(
                spec.getSpec().getIdentity().getTenant(),
                spec.getSpec().getIdentity().getName().orElse(null)
        )
                .isPresent()) {
            throw new KeyAlreadyExistsException(spec.getSpec().getIdentity());
        }

        store.store(spec);
    }

    @Override
    public void modify(final Folder spec) throws ModificationFailedException {
        store.replace(spec);
    }

    @Override
    public void close(final String tenant, final UUID id) throws ModificationFailedException {
        Optional<Folder> spec = store.loadByUuid(tenant, id);

        if (!spec.isPresent()) {
            throw new NoModifiableDataFoundException(ImmutableObjectIdentity.builder()
                    .kind(Folder.KIND)
                    .version(Folder.VERSION)
                    .uuid(id)
                    .tenant(tenant)
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
    public void delete(final String tenant, final UUID id) {
        store.loadByUuid(tenant, id).ifPresent(folder -> store.remove(folder));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", InfinispanFolderWriteAdapter.class.getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .add("store=" + store)
                .toString();
    }
}
