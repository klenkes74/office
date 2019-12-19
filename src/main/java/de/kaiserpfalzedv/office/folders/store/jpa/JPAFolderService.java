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

import de.kaiserpfalzedv.base.cdi.EventLogged;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.CreationFailedException;
import de.kaiserpfalzedv.base.store.KeyAlreadyExistsException;
import de.kaiserpfalzedv.base.store.UuidAlreadyExistsException;
import de.kaiserpfalzedv.office.folders.FolderCreated;
import de.kaiserpfalzedv.office.folders.FolderSpec;
import de.kaiserpfalzedv.office.folders.api.FolderResultService;
import de.kaiserpfalzedv.office.folders.store.jpa.converters.FolderCreatedFolderConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

@JPA
@EventLogged
@Dependent
public class JPAFolderService implements FolderResultService<FolderCreated> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderService.class);

    @Inject
    FolderCreatedFolderConverter commandConverter;

    @Transactional
    public void observe(@Observes final FolderCreated command) {
        LOGGER.debug("Received: {}", command);
        FolderSpec spec = command.getSpec();

        if (JPAFolder.find("identity.uuid", spec.getIdentity().getUuid()).count() != 0) {
            throw new IllegalArgumentException(new UuidAlreadyExistsException(spec.getIdentity()));
        }

        if (!spec.getIdentity().getScope().orElse("./").isEmpty() && spec.getIdentity().getName().isPresent()) {
            if (JPAFolder.find("identity.scope = ?1 and identity.key = ?2", spec.getIdentity().getScope().orElse("./."), spec.getIdentity().getName().orElse(null)).count() != 0) {
                throw new IllegalArgumentException(new KeyAlreadyExistsException(spec.getIdentity()));
            }
        }


        try {
            JPAFolder jpa = commandConverter.convertFromAPI(command);
            jpa.persistAndFlush();

            LOGGER.info("Saved folder: {}", jpa);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(new CreationFailedException(command.getMetadata().getIdentity(), e));
        }
    }
}
