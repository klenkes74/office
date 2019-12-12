package de.kaiserpfalzedv.office.folders;

import de.kaiserpfalzedv.base.*;
import de.kaiserpfalzedv.base.status.ImmutableNackStatus;
import de.kaiserpfalzedv.base.status.ImmutableOkStatus;
import de.kaiserpfalzedv.base.status.Status;
import de.kaiserpfalzedv.base.store.DataAlreadyExistsException;
import org.eclipse.microprofile.metrics.annotation.ConcurrentGauge;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.OffsetDateTime;
import java.util.*;

@ApplicationScoped
@Path("/folders")
public class FolderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderService.class);

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
        List<Folder> folders = new ArrayList<>(data.size());

        for (FolderSpec a : data) {
            folders.add(convertFolderSpecToFolder(a));
        }

        Status<?> status;
        if (data.isEmpty()) {
            status = ImmutableNackStatus.builder().value(Optional.of("200")).message(Optional.of("No entries found.")).build();
        } else {
            status = ImmutableOkStatus.builder().message((Optional.of(data.size() + " entries found."))).build();
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
                .addStatus(status)

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
        return ImmutableFolder.builder()
                .from(service.load(uuid))
                .addStatus(ImmutableOkStatus.builder().message(Optional.empty()).build())
                .build();
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
        return ImmutableFolder.builder().from(service.load(scope, key)).build();
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
                    .addStatus(ImmutableOkStatus.builder().message(Optional.empty()).build())
                    .build();
        } catch (DataAlreadyExistsException e) {
            return ImmutableFolderCreated.builder()
                    .metadata(command.getMetadata())
                    .spec(command.getSpec())
                    .addStatus(ImmutableNackStatus.builder()
                            .message(Optional.of(e.getMessage() + e.getIdentifier()))
                            .build()
                    )
                    .build();
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
                .addStatus(ImmutableOkStatus.builder().message(Optional.empty()).build())
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
                .addStatus(ImmutableOkStatus.builder().message(Optional.empty()).build())
                .build();
    }
}