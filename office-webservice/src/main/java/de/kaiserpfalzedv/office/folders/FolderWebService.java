package de.kaiserpfalzedv.office.folders;

import de.kaiserpfalzedv.base.api.*;
import de.kaiserpfalzedv.base.store.DataAlreadyExistsException;
import de.kaiserpfalzedv.folders.*;
import org.eclipse.microprofile.metrics.annotation.ConcurrentGauge;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.OffsetDateTime;
import java.util.*;

@ApplicationScoped
@Path("/folders")
public class FolderWebService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderWebService.class);

    @Inject
    FolderCommandService service;


    @GET
    @Path("/{scope}")
    @Produces(MediaType.APPLICATION_JSON)
    @Metered(name = "folders.listAll")
    @Counted(name = "folders.listAll.count")
    @ConcurrentGauge(name = "folders.listAll.concurrent")
    public ObjectList<SingleObject<?>> listAll(
            @PathParam("scope") final String scope,
            @QueryParam("start") final Long start,
            @QueryParam("size") final Long size
    ) {
        OffsetDateTime now = OffsetDateTime.now();

        Collection<FolderSpec> data = service.load(scope, start, size);
        if (data.isEmpty()) {
            LOGGER.info("No folders for scope found: scope={}", scope);
            throw new WebApplicationException("No folders for scope='" + scope + "' found", Response.Status.NOT_FOUND);
        }


        List<Folder> folders = new ArrayList<>(data.size());
        for (FolderSpec a : data) {
            folders.add(convertFolderSpecToFolder(a));
        }

        return ImmutableObjectList.builder()
                .metadata(ImmutableMetadata.builder()
                        .identity(
                                ImmutableObjectIdentifier.builder()
                                        .kind(ObjectList.KIND)
                                        .uuid(UUID.randomUUID())
                                        .build()
                        )
                        .created(now)
                        .build())
                .spec(folders)
                .build();
    }

    private Folder convertFolderSpecToFolder( final FolderSpec spec) {
        LOGGER.trace("Converting: {}", spec);

        return ImmutableFolder.builder()
                .metadata(ImmutableMetadata.builder()
                        .identity(
                                ImmutableObjectIdentifier.builder()
                                        .kind(Folder.KIND)
                                        .uuid(spec.getUuid())
                                        .scope(spec.getScope())
                                        .name(spec.getKey())
                                        .build()
                        )
                        .created(spec.getCreated())
                        .modified(spec.getModified())
                        .build())
                .spec(spec)
                .build();
    }

    @GET
    @Path("/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Metered(name = "folders.loadByUuid")
    @Counted(name = "folders.loadByUuid.count")
    @ConcurrentGauge(name = "folders.loadByUuid.concurrent")
    public Folder getByUuid(
            @PathParam("uuid") final UUID uuid
    ) {
        Optional<Folder> result = service.load(uuid);

        if (result.isPresent()) {
            return ImmutableFolder.builder()
                    .from(result.get())
                    .build();
        } else {
            LOGGER.info("Folder not found: uuid={}", uuid);

            throw new WebApplicationException("Folder with uuid='" + uuid + "' not found.",
                    Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("/{scope}/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @Metered(name = "folders.loadByKey")
    @Counted(name = "folders.loadByKey.count")
    @ConcurrentGauge(name = "folders.loadByKey.concurrent")
    public Folder getByKey(
            @PathParam("scope") final String scope,
            @PathParam("key") final String key
    ) {
        Optional<Folder> result = service.load(scope, key);

        if (result.isPresent()) {
            return ImmutableFolder.builder()
                    .from(result.get())
                    .build();
        } else {
            LOGGER.info("Folder not found: scope={}, key={}", scope, key);

            throw new WebApplicationException("Folder with scope='" + scope + "' and key='" + key + "' not found.",
                    Response.Status.NOT_FOUND);
        }
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Metered(name = "folders.createFolder")
    @Counted(name = "folders.createFolder.count")
    @ConcurrentGauge(name = "folders.createFolder.concurrent")
    public FolderCreated createFolder(final ImmutableCreateFolder command) {
        try {
            return ImmutableFolderCreated.builder()
                    .from(service.write(command))
                    .build();
        } catch (DataAlreadyExistsException e) {
            LOGGER.warn(e.getMessage(), e);

            throw new WebApplicationException(e.getMessage(), Response.Status.EXPECTATION_FAILED);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Metered(name = "folders.closeFolder")
    @Counted(name = "folders.closeFolder.count")
    @ConcurrentGauge(name = "folders.closeFolder.concurrent")
    public FolderClosed closeFolder(final ImmutableCloseFolder command) {
        return ImmutableFolderClosed.builder()
                .from(service.close(command))
                .build();
    }

    @DELETE
    @Path("/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Metered(name = "folders.closeFolder")
    @Counted(name = "folders.closeFolder.count")
    @ConcurrentGauge(name = "folders.closeFolder.concurrent")
    public FolderClosed closeFolder(@PathParam("uuid") final UUID id) {
        return ImmutableFolderClosed.builder()
                .from(service.close(id))
                .build();
    }
}