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

package de.kaiserpfalzedv.office.folders.store.jpa;

import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.office.folders.Folder;
import de.kaiserpfalzedv.office.folders.store.FolderReadAdapter;
import de.kaiserpfalzedv.office.folders.store.jpa.converters.FolderConverter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-15 17:57
 */
@JPA
@Dependent
public class JPAFolderReadAdapter implements FolderReadAdapter {
    @Inject
    FolderConverter converter;

    @Override
    public Optional<Folder> loadById(UUID id) {
        JPAFolder data = JPAFolder.find("identity.uuid", id).firstResult();

        if (data == null) {
            return Optional.empty();
        }

        return Optional.of(converter.convertToAPI(data));
    }

    @Override
    public Optional<Folder> loadByScopeAndKey(String scope, String key) {
        JPAFolder data = JPAFolder.find("identity.scope=?1 and identity.key=?2", scope, key).firstResult();

        if (data == null) {
            return Optional.empty();
        }

        return Optional.of(converter.convertToAPI(data));
    }

    @Override
    public ArrayList<Folder> loadByScope(String scope) {
        List<JPAFolder> data = JPAFolder.find("identity.scope", scope).list();

        ArrayList<Folder> result = new ArrayList<>(data.size());
        data.forEach(d -> result.add(converter.convertToAPI(d)));

        return result;
    }

    @Override
    public ArrayList<Folder> loadByScope(final String scope, final int index, final int size) {
        List<JPAFolder> data = JPAFolder.find("identity.scope", scope).page(index, size).list();

        ArrayList<Folder> result = new ArrayList<>(data.size());
        data.forEach(d -> result.add(converter.convertToAPI(d)));

        return result;
    }

    @Override
    public long count() {
        return JPAFolder.count();
    }
}