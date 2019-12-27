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

package de.kaiserpfalzedv.base.store.jpa.folders.data;

import de.kaiserpfalzedv.base.WrappingException;
import de.kaiserpfalzedv.base.api.ObjectIdentity;
import de.kaiserpfalzedv.base.cdi.EventLogged;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.CreationFailedException;
import de.kaiserpfalzedv.base.store.KeyAlreadyExistsException;
import de.kaiserpfalzedv.base.store.ModificationFailedException;
import de.kaiserpfalzedv.base.store.UuidAlreadyExistsException;
import de.kaiserpfalzedv.base.store.jpa.JPAObjectReference;
import de.kaiserpfalzedv.folders.*;
import de.kaiserpfalzedv.folders.api.FolderResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.concurrent.ConcurrentSkipListSet;

@JPA
@EventLogged
@Dependent
public class JPAFolderService implements FolderResultService<FolderCreated> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderService.class);


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

        persist(command.getMetadata().getIdentity(), jpa);
    }

    private void persist(ObjectIdentity folderIdentity, JPAFolder jpa) {
        try {
            jpa.persistAndFlush();
            LOGGER.info("Saved folder ({} entries): {}", jpa.contents.size(), folderIdentity);
        } catch (PersistenceException e) {
            LOGGER.warn("Can't save folder: {}", folderIdentity, e);

            throw new WrappingException(new CreationFailedException(folderIdentity, e));
        }
    }

    @Transactional
    public void observe(@Observes final FolderModified command) {
        ObjectIdentity folderIdentity = command.getSpec().getIdentity();
        JPAFolder jpa = loadFolder(folderIdentity);

        jpa.spec = new JPAFolderSpec().fromModel(command.getSpec());

        persist(folderIdentity, jpa);
    }

    @Transactional
    public void observe(@Observes final FolderClosed command) {
        ObjectIdentity folderIdentity = command.getMetadata().getIdentity();
        JPAFolder jpa = loadFolder(folderIdentity);

        jpa.spec.closed = OffsetDateTime.now();

        persist(folderIdentity, jpa);
    }

    @Transactional
    public void observe(@Observes final FolderDeleted command) {
        ObjectIdentity folderIdentity = command.getMetadata().getIdentity();

        try {
            JPAFolder jpa = loadFolder(folderIdentity);
            jpa.delete();

            LOGGER.info("Folder deleted: {}", folderIdentity.getUuid());
        } catch (WrappingException e) {
            if (e.getWrapped() instanceof NoResultException) {
                LOGGER.info("Folder does not exist: {}", folderIdentity.getUuid());
                // everything is fine, we wanted the folder being deleted and it does not exist.
            } else {
                LOGGER.error("Can't delete folder {}: {}", folderIdentity.getUuid(), e.getWrapped());
                throw e; // rewthrow the exception, if it is another problem.
            }
        }
    }


    @Transactional
    public void observe(@Observes final ContentAdded command) {
        ObjectIdentity folderIdentity = command.getMetadata().getIdentity();

        JPAFolder jpa = loadFolder(folderIdentity);
        LOGGER.trace("Folder {} contains {} elements.", folderIdentity.getUuid(), jpa.contents.size());

        addContentToFolder(jpa, command);

        LOGGER.trace("Folder {} contains {} elements.", folderIdentity.getUuid(), jpa.contents.size());
        persist(folderIdentity, jpa);
    }

    private JPAFolder loadFolder(ObjectIdentity folderIdentity) {
        JPAFolder jpa;
        try {
            jpa = JPAFolder.findByUuid(folderIdentity.getTenant(), folderIdentity.getUuid()).firstResult();
        } catch (NoResultException e) {
            throw new WrappingException(new ModificationFailedException(folderIdentity, "Can't load folder to add content", e));
        }
        return jpa;
    }

    private void addContentToFolder(JPAFolder jpa, ContentAdded command) {
        ConcurrentSkipListSet<JPAObjectReference> added = new ConcurrentSkipListSet<>();
        command.getData().forEach(d -> added.add(JPAObjectReference.fromModel(d)));
        added.forEach(jpa::addContent);
    }


    @Transactional
    public void observe(@Observes final ContentRemoved command) {
        ObjectIdentity folderIdentity = command.getMetadata().getIdentity();

        JPAFolder jpa = loadFolder(folderIdentity);

        removeContentFromFolder(jpa, command);

        persist(folderIdentity, jpa);
    }

    private void removeContentFromFolder(JPAFolder jpa, ContentRemoved command) {
        ConcurrentSkipListSet<JPAObjectReference> added = new ConcurrentSkipListSet<>();
        command.getData().forEach(d -> added.add(JPAObjectReference.fromModel(d)));
        added.forEach(jpa::removeContent);
    }
}
