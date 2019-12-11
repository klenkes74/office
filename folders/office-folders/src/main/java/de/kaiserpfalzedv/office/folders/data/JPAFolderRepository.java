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

package de.kaiserpfalzedv.office.folders.data;

import de.kaiserpfalzedv.office.folders.FolderSpec;
import de.kaiserpfalzedv.office.folders.api.FolderRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class JPAFolderRepository implements FolderRepository, PanacheRepository<FolderSpec> {
    @Override
    public Optional<FolderSpec> loadById(final UUID id) {
        return Optional.ofNullable(find("uuid", id).firstResult());
    }

    @Override
    public Optional<FolderSpec> loadByName(final String scope, final String name) {
        return Optional.ofNullable(find("scope = ?1 and name = ?2", scope, name).firstResult());
    }

    @Override
    public Collection<FolderSpec> loadByScope(final String scope) {
        return list("scope", scope);
    }

    @Override
    public FolderSpec write(final FolderSpec data) {
        Folder persistent = new Folder(data);

        persistent.persist();

        return persistent;
    }

    @Override
    public void close(UUID id) {

    }
}
