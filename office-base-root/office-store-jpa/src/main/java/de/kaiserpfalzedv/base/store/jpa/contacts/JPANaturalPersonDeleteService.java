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

import de.kaiserpfalzedv.commons.WrappingException;
import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import de.kaiserpfalzedv.commons.cdi.EventLogged;
import de.kaiserpfalzedv.commons.cdi.JPA;
import de.kaiserpfalzedv.commons.store.CreationFailedException;
import de.kaiserpfalzedv.commons.store.NoModifiableDataFoundException;
import de.kaiserpfalzedv.contacts.DeleteNaturalPerson;
import de.kaiserpfalzedv.contacts.ImmutableNaturalPersonDeleted;
import de.kaiserpfalzedv.contacts.NaturalPerson;
import de.kaiserpfalzedv.contacts.NaturalPersonDeleted;
import de.kaiserpfalzedv.contacts.api.NaturalPersonCommandService;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

@JPA
@EventLogged
@Dependent
public class JPANaturalPersonDeleteService implements NaturalPersonCommandService<DeleteNaturalPerson> {
    @Inject
    Event<NaturalPersonDeleted> eventSink;


    @Transactional
    public void observe(@Observes final DeleteNaturalPerson command) {
        if (JPANaturalPerson.findByUuid(command.getMetadata().getIdentity().getTenant(), command.getMetadata().getIdentity().getUuid()).count() != 1) {
            throw new WrappingException(new NoModifiableDataFoundException(command.getMetadata().getIdentity()));
        }

        try {
            JPANaturalPersonDelete jpa = new JPANaturalPersonDelete().fromModel(command);
            jpa.persist();

            ImmutableNaturalPersonDeleted event = ImmutableNaturalPersonDeleted.builder()
                    .metadata(ImmutableMetadata.builder()
                            .identity(jpa.command.toModel(NaturalPerson.KIND, NaturalPerson.VERSION))
                            .workflowdata(jpa.workflow.toModel())
                            .build()
                    )

                    .build();

            eventSink.fire(event);
        } catch (IllegalArgumentException e) {
            throw new WrappingException(new CreationFailedException(command.getMetadata().getIdentity(), e));
        }
    }
}
