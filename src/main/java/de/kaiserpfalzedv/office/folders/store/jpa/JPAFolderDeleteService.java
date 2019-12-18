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
import de.kaiserpfalzedv.base.store.CreationFailedException;
import de.kaiserpfalzedv.office.folders.DeleteFolder;
import de.kaiserpfalzedv.office.folders.FolderDeleted;
import de.kaiserpfalzedv.office.folders.api.FolderCommandService;
import de.kaiserpfalzedv.office.folders.store.jpa.converters.DeleteFolderConverter;
import de.kaiserpfalzedv.office.folders.store.jpa.converters.FolderDeletedConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

@JPA
@Dependent
public class JPAFolderDeleteService implements FolderCommandService<DeleteFolder> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderDeleteService.class);

    @Inject
    DeleteFolderConverter folderConverter;
    @Inject
    FolderDeletedConverter eventConverter;
    @Inject
    Event<FolderDeleted> eventSource;


    @Transactional
    public void observe(@Observes final DeleteFolder command) {
        try {
            JPAFolderDelete jpa = folderConverter.convertFromAPI(command);
            jpa.persist();

            eventSource.fire(eventConverter.convertToAPI(jpa));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(new CreationFailedException(command.getMetadata().getIdentity(), e));
        }
    }
}
