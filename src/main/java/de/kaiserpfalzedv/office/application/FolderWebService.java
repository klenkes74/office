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

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.office.folders.Folder;
import de.kaiserpfalzedv.office.folders.FolderCreated;
import de.kaiserpfalzedv.office.folders.ImmutableCreateFolder;
import de.kaiserpfalzedv.office.folders.ImmutableFolderCreated;
import de.kaiserpfalzedv.office.folders.store.FolderReadAdapter;
import io.quarkus.security.identity.SecurityIdentity;
import org.eclipse.microprofile.metrics.annotation.ConcurrentGauge;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Path("/folders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FolderWebService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderWebService.class);

    @Inject
    @JPA
    FolderReadAdapter reader;

    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @Path("/{uuid}")
    @RolesAllowed({"user", "admin"})
    @Metered(name = "folders.loadByUuid")
    @Counted(name = "folders.loadByUuid.count")
    @ConcurrentGauge(name = "folders.loadByUuid.concurrent")
    public Folder getByUuid(
            @PathParam("uuid") final UUID uuid
    ) {
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
    @Path("/{scope}/{key}")
    @RolesAllowed({"user", "admin"})
    @Metered(name = "folders.loadByKey")
    @Counted(name = "folders.loadByKey.count")
    @ConcurrentGauge(name = "folders.loadByKey.concurrent")
    public Folder getByKey(
            @PathParam("scope") final String scope,
            @PathParam("key") final String key
    ) {
        Optional<Folder> result = reader.loadByScopeAndKey(scope, key);

        if (result.isPresent()) {
            return result.get();
        } else {
            LOGGER.info("JPAFolderSpec not found: scope={}, key={}", scope, key);

            throw new WebApplicationException("JPAFolderSpec with scope='" + scope + "' and key='" + key + "' not found.",
                    Response.Status.NOT_FOUND);
        }
    }


    @PUT
    @RolesAllowed("admin")
    @Metered(name = "folders.createFolder")
    @Counted(name = "folders.createFolder.count")
    @ConcurrentGauge(name = "folders.createFolder.concurrent")
    public FolderCreated createFolder(final ImmutableCreateFolder command) {
        Optional<Folder> result = reader.loadById(command.getSpec().getIdentity().getUuid());

        if (result.isPresent()) {
            return ImmutableFolderCreated.builder()
                    .metadata(ImmutableMetadata.builder()
                            .identity(result.get().getSpec().getIdentity())
                            .build()
                    )
                    .spec(result.get().getSpec())
                    .build();
        } else {
            LOGGER.warn("Can't create new folder: uuid={}", command.getSpec().getIdentity().getUuid());

            throw new WebApplicationException("Cant create new folder.", Response.Status.EXPECTATION_FAILED);
        }
    }
}