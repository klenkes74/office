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

package de.kaiserpfalzedv.base.store.jpa.folders.changes;

import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.jpa.folders.data.JPAFolder;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.store.FolderReadAdapter;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author rlichti
 * @since 2019-12-15 17:57
 */
@JPA
@Dependent
public class JPAFolderReadAdapter implements FolderReadAdapter {
    @Override
    public Optional<Folder> loadById(final String tenant, final UUID uuid) {
        JPAFolder data = JPAFolder.findByUuid(tenant, uuid).firstResult();

        if (data == null) {
            return Optional.empty();
        }

        return Optional.of(data.toModel());
    }

    @Override
    public Optional<Folder> loadbyKey(final String tenant, final String key) {
        JPAFolder data = JPAFolder.findByTenantAndKey(tenant, key).firstResult();

        if (data == null) {
            return Optional.empty();
        }

        return Optional.of(data.toModel());
    }

    @Override
    public ArrayList<Folder> loadByTenant(final String tenant) {
        List<JPAFolder> data = JPAFolder.findByTenant(tenant).list();

        ArrayList<Folder> result = new ArrayList<>(data.size());
        data.forEach(d -> result.add(d.toModel()));

        return result;
    }

    @Override
    public ArrayList<Folder> loadByTenant(final String tenant, final int index, final int size) {
        List<JPAFolder> data = JPAFolder.findByTenant(tenant).page(index, size).list();

        ArrayList<Folder> result = new ArrayList<>(data.size());
        data.forEach(d -> result.add(d.toModel()));

        return result;
    }

    @Override
    public long count() {
        return JPAFolder.count();
    }
}
