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

package de.kaiserpfalzedv.base.store.jpa.contacts;

import de.kaiserpfalzedv.base.WrappingException;
import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.cdi.EventLogged;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.CreationFailedException;
import de.kaiserpfalzedv.base.store.KeyAlreadyExistsException;
import de.kaiserpfalzedv.base.store.UuidAlreadyExistsException;
import de.kaiserpfalzedv.base.store.jpa.folders.JPAFolder;
import de.kaiserpfalzedv.contacts.CreateNaturalPerson;
import de.kaiserpfalzedv.contacts.ImmutableNaturalPersonCreated;
import de.kaiserpfalzedv.contacts.NaturalPersonCreated;
import de.kaiserpfalzedv.contacts.NaturalPersonSpec;
import de.kaiserpfalzedv.contacts.api.NaturalPersonCommandService;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@JPA
@EventLogged
@Dependent
public class JPANaturalPersonCreateService implements NaturalPersonCommandService<CreateNaturalPerson> {
    @Inject
    Event<NaturalPersonCreated> eventSink;


    @Transactional
    public void observe(@Observes final CreateNaturalPerson command) {
        NaturalPersonSpec spec = command.getSpec();

        if (JPAFolder.find("spec.identity.uuid", spec.getIdentity().getUuid()).count() != 0) {
            throw new WrappingException(new UuidAlreadyExistsException(spec.getIdentity()));
        }

        if (!spec.getIdentity().getTenant().orElse("./").isEmpty() && spec.getIdentity().getName().isPresent()) {
            if (JPAFolder.find("spec.identity.tenant = ?1 and spec.identity.key = ?2", spec.getIdentity().getTenant().orElse("./."), spec.getIdentity().getName().orElse(null)).count() != 0) {
                throw new WrappingException(new KeyAlreadyExistsException(spec.getIdentity()));
            }
        }


        try {
            JPANaturalPersonCreate jpa = new JPANaturalPersonCreate().fromModel(command);
            jpa.persist();

            NaturalPersonCreated event = ImmutableNaturalPersonCreated.builder()
                    .metadata(ImmutableMetadata.builder()
                            .identity(jpa.command.toModel())
                            .workflowdata(Optional.ofNullable(jpa.workflow.toModel()))
                            .build()
                    )

                    .spec(jpa.spec.toModel())

                    .build();

            eventSink.fire(event);
        } catch (IllegalArgumentException e) {
            throw new WrappingException(new CreationFailedException(command.getMetadata().getIdentity(), e));
        }
    }
}
