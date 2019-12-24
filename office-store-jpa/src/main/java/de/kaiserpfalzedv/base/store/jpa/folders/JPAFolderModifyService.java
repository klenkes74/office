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

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.cdi.EventLogged;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.CreationFailedException;
import de.kaiserpfalzedv.base.store.KeyAlreadyExistsException;
import de.kaiserpfalzedv.base.store.UuidAlreadyExistsException;
import de.kaiserpfalzedv.folders.FolderModified;
import de.kaiserpfalzedv.folders.FolderSpec;
import de.kaiserpfalzedv.folders.ImmutableFolderModified;
import de.kaiserpfalzedv.folders.ModifyFolder;
import de.kaiserpfalzedv.folders.api.FolderCommandService;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@JPA
@EventLogged
@Dependent
public class JPAFolderModifyService implements FolderCommandService<ModifyFolder> {
    @Inject
    Event<FolderModified> eventSink;


    @Transactional
    public void observe(@Observes final ModifyFolder command) {
        FolderSpec spec = command.getSpec();

        if (JPAFolder.findByUuid(spec.getIdentity().getUuid()).count() != 0) {
            throw new IllegalArgumentException(new UuidAlreadyExistsException(spec.getIdentity()));
        }

        if (
                JPAFolder.findByTenantAndKey(
                        spec.getIdentity().getTenant().orElse("./."),
                        spec.getIdentity().getName().orElse("./.")
                ).count() != 0
        ) {
            throw new IllegalArgumentException(new KeyAlreadyExistsException(spec.getIdentity()));
        }


        try {
            JPAFolderModify jpa = new JPAFolderModify().fromModel(command);
            jpa.persist();

            FolderModified event = ImmutableFolderModified.builder()
                    .metadata(ImmutableMetadata.builder()
                            .identity(jpa.command.toModel())
                            .workflowdata(Optional.ofNullable(jpa.workflow.toModel()))
                            .build()
                    )
                    .build();
            eventSink.fire(event);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(new CreationFailedException(command.getMetadata().getIdentity(), e));
        }
    }
}
