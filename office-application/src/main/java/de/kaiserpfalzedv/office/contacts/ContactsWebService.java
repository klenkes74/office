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

package de.kaiserpfalzedv.office.contacts;

import de.kaiserpfalzedv.base.WrappingException;
import de.kaiserpfalzedv.base.cdi.CorrelationLogged;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.contacts.CreateNaturalPerson;
import de.kaiserpfalzedv.contacts.ModifyNaturalPerson;
import de.kaiserpfalzedv.contacts.NaturalPerson;
import de.kaiserpfalzedv.contacts.store.NaturalPersonReadAdapter;
import org.eclipse.microprofile.metrics.annotation.ConcurrentGauge;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Path("/contacts/{tenant}/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CorrelationLogged
public class ContactsWebService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactsWebService.class);

    @Inject
    @JPA
    NaturalPersonReadAdapter reader;

    @Inject
    Event<CreateNaturalPerson> naturalPersonCreateEventSink;
    @Inject
    Event<ModifyNaturalPerson> naturalPersonModifyEventSink;


    @GET
    @Path("natural/")
    @RolesAllowed({"viewer", "editor", "admin"})
    @Metered(name = "contacts.loadByUuid")
    @Counted(name = "contacts.loadByUuid.count")
    @ConcurrentGauge(name = "contacts.loadByUuid.concurrent")
    public NaturalPerson getByUuid(
            @PathParam("tenant") final String tenant,
            @QueryParam("uuid") final UUID uuid
    ) {
        Optional<NaturalPerson> result = reader.loadById(tenant, uuid);

        if (result.isPresent()) {
            return result.get();
        } else {
            LOGGER.info("Person not found: uuid={}", uuid);

            throw new WebApplicationException("Person with uuid='" + uuid + "' not found.",
                    Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("natural/{key}")
    @RolesAllowed({"viewer", "editor", "admin"})
    @Metered(name = "contacts.loadByKey")
    @Counted(name = "contacts.loadByKey.count")
    @ConcurrentGauge(name = "contacts.loadByKey.concurrent")
    public NaturalPerson getByKey(
            @PathParam("tenant") final String tenant,
            @PathParam("key") final String key
    ) {
        Optional<NaturalPerson> result = reader.loadbyKey(tenant, key);

        if (result.isPresent()) {
            LOGGER.info("Found person: tenant={}, key={}", tenant, key);
            return result.get();
        } else {
            LOGGER.info("person not found: tenant={}, key={}", tenant, key);

            throw new NotFoundException("Person not found. tenant=" + tenant + ", key=" + key);
        }
    }


    @PUT
    @Path("natural")
    @RolesAllowed({"editor", "admin"})
    @Metered(name = "contacts.createNaturalPerson")
    @Counted(name = "contacts.createNaturalPerson.count")
    @ConcurrentGauge(name = "contacts.createNaturalPerson.concurrent")
    public void createNaturalPerson(
            @PathParam("tenant") final String tenant,
            final CreateNaturalPerson command
    ) {
        if (
                !tenant.equals(command.getMetadata().getIdentity().getTenant())
                        || !tenant.equals(command.getSpec().getIdentity().getTenant())
        ) {
            throw new ForbiddenException();
        }

        try {
            naturalPersonCreateEventSink.fire(command);
        } catch (WrappingException e) {
            throw new WebApplicationException(
                    e.getCause() != null ? e.getCause().getMessage() : "Can't create new natural person.",
                    Response.Status.CONFLICT
            );
        }
    }

    @POST
    @Path("natural/")
    @RolesAllowed({"editor", "admin"})
    @Metered(name = "contacts.modifyNaturalPerson")
    @Counted(name = "contacts.modifyNaturalPerson.count")
    @ConcurrentGauge(name = "contacts.modifyNaturalPerson.concurrent")
    public void modifyNaturalPerson(
            @PathParam("tenant") final String tenant,
            final ModifyNaturalPerson command
    ) {
        if (
                !tenant.equals(command.getMetadata().getIdentity().getTenant())
                        || !tenant.equals(command.getSpec().getIdentity().getTenant())
        ) {
            throw new ForbiddenException();
        }

        try {
            naturalPersonModifyEventSink.fire(command);
            LOGGER.info("Modified natural person: {}", command.getSpec());
        } catch (WrappingException e) {
            LOGGER.error("Can't modify natural person: {}", e.getWrapped().getMessage(), e.getCause());
            throw new WebApplicationException(
                    e.getCause() != null ? e.getCause().getMessage() : "Can't modify natural person.",
                    Response.Status.NOT_MODIFIED
            );
        }
    }
}