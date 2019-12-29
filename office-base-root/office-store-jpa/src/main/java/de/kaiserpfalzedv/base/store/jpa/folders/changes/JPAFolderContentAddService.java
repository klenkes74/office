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

import de.kaiserpfalzedv.commons.WrappingException;
import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import de.kaiserpfalzedv.commons.cdi.EventLogged;
import de.kaiserpfalzedv.commons.cdi.JPA;
import de.kaiserpfalzedv.commons.store.ModificationFailedException;
import de.kaiserpfalzedv.folders.AddContent;
import de.kaiserpfalzedv.folders.ContentAdded;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.ImmutableContentAdded;
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
public class JPAFolderContentAddService implements FolderCommandService<AddContent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderContentAddService.class);

    @Inject
    Event<ContentAdded> eventSink;


    @Transactional
    public void observe(@Observes final AddContent command) {
        JPAFolderContentAdd jpa;
        try {
            jpa = new JPAFolderContentAdd().fromModel(command);
            LOGGER.trace("New folder add content event: {}", jpa);
            jpa.persist();
        } catch (PersistenceException e) {
            LOGGER.error("Caught a persistence Exception: {}", e.getMessage(), e);

            throw new WrappingException(new ModificationFailedException(command.getMetadata().getIdentity(), e));
        }

        try {
            ContentAdded event = ImmutableContentAdded.builder()
                    .metadata(ImmutableMetadata.builder()
                            .identity(jpa.command.toModel(Folder.KIND, Folder.VERSION))
                            .workflowdata(jpa.workflow.toModel())
                            .build()
                    )
                    .data(jpa.getDataModel())
                    .build();
            LOGGER.debug("Firing: {}", event);
            eventSink.fire(event);
        } catch (IllegalArgumentException e) {
            throw new WrappingException(new ModificationFailedException(command.getMetadata().getIdentity(), e));
        }
    }
}
