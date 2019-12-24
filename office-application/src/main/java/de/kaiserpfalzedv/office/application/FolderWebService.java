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

package de.kaiserpfalzedv.office.application;

import de.kaiserpfalzedv.base.cdi.CorrelationLogged;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.folders.CreateFolder;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.ImmutableCreateFolder;
import de.kaiserpfalzedv.folders.store.FolderReadAdapter;
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
@Path("/folders/{tenant}/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CorrelationLogged
public class FolderWebService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderWebService.class);

    @Inject
    @JPA
    FolderReadAdapter reader;

    @Inject
    Event<CreateFolder> createEvent;


    @GET
    @RolesAllowed({"user", "admin"})
    @Metered(name = "folders.loadByUuid")
    @Counted(name = "folders.loadByUuid.count")
    @ConcurrentGauge(name = "folders.loadByUuid.concurrent")
    public Folder getByUuid(@QueryParam("uuid") final UUID uuid) {
        Optional<Folder> result = reader.loadById(uuid);

        if (result.isPresent()) {
            return result.get();
        } else {
            LOGGER.info("JPAFolderSpec not found: uuid={}", uuid);

            throw new WebApplicationException("JPAFolderSpec with uuid='" + uuid + "' not found.",
                    Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("{key}")
    @RolesAllowed({"user", "admin"})
    @Metered(name = "folders.loadByKey")
    @Counted(name = "folders.loadByKey.count")
    @ConcurrentGauge(name = "folders.loadByKey.concurrent")
    public Folder getByKey(
            @PathParam("tenant") final String tenant,
            @PathParam("key") final String key
    ) {
        Optional<Folder> result = reader.loadByScopeAndKey(tenant, key);

        if (result.isPresent()) {
            return result.get();
        } else {
            LOGGER.info("JPAFolderSpec not found: tenant={}, key={}", tenant, key);

            throw new WebApplicationException("JPAFolderSpec with tenant='" + tenant + "' and key='" + key + "' not found.",
                    Response.Status.NOT_FOUND);
        }
    }


    @PUT
    @RolesAllowed("admin")
    @Metered(name = "folders.createFolder")
    @Counted(name = "folders.createFolder.count")
    @ConcurrentGauge(name = "folders.createFolder.concurrent")
    public void createFolder(final ImmutableCreateFolder command) {
        try {
            createEvent.fire(command);
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException(
                    e.getCause() != null ? e.getCause().getMessage() : "Can't create new folder.",
                    Response.Status.CONFLICT
            );
        }
    }
}