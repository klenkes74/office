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

import de.kaiserpfalzedv.base.WrappingException;
import de.kaiserpfalzedv.base.cdi.EventLogged;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.CreationFailedException;
import de.kaiserpfalzedv.folders.DeleteFolder;
import de.kaiserpfalzedv.folders.FolderDeleted;
import de.kaiserpfalzedv.folders.ImmutableFolderDeleted;
import de.kaiserpfalzedv.folders.api.FolderCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

@JPA
@EventLogged
@Dependent
public class JPAFolderDeleteService implements FolderCommandService<DeleteFolder> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderDeleteService.class);

    @Inject
    Event<FolderDeleted> eventSink;


    @Transactional
    public void observe(@Observes final DeleteFolder command) {
        JPAFolderDelete jpa;
        try {
            jpa = new JPAFolderDelete().fromModel(command);
            jpa.persist();
        } catch (PersistenceException e) {
            throw new WrappingException(new CreationFailedException(command.getMetadata().getIdentity(), e));
        }

        try {
            FolderDeleted event = ImmutableFolderDeleted.builder()
                    .metadata(jpa.toModel().getMetadata())
                    .build();

            eventSink.fire(event);
        } catch (IllegalArgumentException e) {
            throw new WrappingException(new CreationFailedException(command.getMetadata().getIdentity(), e));
        }
    }
}
