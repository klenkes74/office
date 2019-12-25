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

package de.kaiserpfalzedv.base.store.jpa.folders;

import de.kaiserpfalzedv.base.WrappingException;
import de.kaiserpfalzedv.base.cdi.EventLogged;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.CreationFailedException;
import de.kaiserpfalzedv.base.store.KeyAlreadyExistsException;
import de.kaiserpfalzedv.base.store.UuidAlreadyExistsException;
import de.kaiserpfalzedv.folders.FolderCreated;
import de.kaiserpfalzedv.folders.FolderSpec;
import de.kaiserpfalzedv.folders.api.FolderResultService;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

@JPA
@EventLogged
@Dependent
public class JPAFolderService implements FolderResultService<FolderCreated> {
    @Transactional
    public void observe(@Observes final FolderCreated command) {
        FolderSpec spec = command.getSpec();

        if (JPAFolder.findByUuid(spec.getIdentity().getTenant(), spec.getIdentity().getUuid()).count() != 0) {
            throw new WrappingException(new UuidAlreadyExistsException(spec.getIdentity()));
        }

        if (spec.getIdentity().getName().isPresent()) {
            if (JPAFolder.findByTenantAndKey(spec.getIdentity().getTenant(), spec.getIdentity().getName().orElse(null)).count() != 0) {
                throw new WrappingException(new KeyAlreadyExistsException(spec.getIdentity()));
            }
        }

        JPAFolder jpa;
        try {
            jpa = new JPAFolder();
            jpa.spec = new JPAFolderSpec().fromModel(spec);
        } catch (IllegalArgumentException e) {
            throw new WrappingException(new CreationFailedException(command.getMetadata().getIdentity(), e));
        }

        try {
            jpa.persistAndFlush();
        } catch (PersistenceException e) {
            throw new WrappingException(new CreationFailedException(command.getMetadata().getIdentity(), e));
        }
    }
}
