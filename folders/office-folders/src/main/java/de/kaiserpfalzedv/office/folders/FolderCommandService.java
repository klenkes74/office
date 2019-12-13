/*
 *  Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2019. All rights reserved.
 *
 *  This file is part of Kaiserpfalz EDV-Service Office.
 *
 *  This is free software: you can redistribute it and/or modify it under the terms of
 *   the GNU Lesser General Public License as published by the Free Software
 *   Foundation, either version 3 of the License.
 *
 *  This file is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *  License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along
 *  with this file. If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package de.kaiserpfalzedv.office.folders;

import de.kaiserpfalzedv.base.BaseAPI;
import de.kaiserpfalzedv.base.ImmutableMetadata;
import de.kaiserpfalzedv.base.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.actions.CommandService;
import de.kaiserpfalzedv.base.actions.Result;
import de.kaiserpfalzedv.base.status.ImmutableNackStatus;
import de.kaiserpfalzedv.base.status.ImmutableOkStatus;
import de.kaiserpfalzedv.base.store.DataAlreadyExistsException;
import de.kaiserpfalzedv.office.folders.store.FolderStoreAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FolderCommandService implements CommandService<FolderSpec>, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderCommandService.class);

    private FolderStoreAdapter storeAdapter;

    @Inject
    public FolderCommandService(final FolderStoreAdapter adapter) {
        this.storeAdapter = adapter;
    }

    @Override
    public Result<FolderSpec> execute(BaseAPI<FolderSpec> command) {
        LOGGER.debug("Writing unknown command: {}", command);

        throw new IllegalArgumentException("Unknown argument of type " + command.getClass().getSimpleName());
    }

    public FolderCreated write(final CreateFolder command) throws DataAlreadyExistsException {
        LOGGER.debug("Creating folder: {}", command);

        ImmutableFolderCreated.Builder result = ImmutableFolderCreated.builder()
                .metadata(ImmutableMetadata.builder()
                        .from(command.getMetadata())
                        .build()
                );

        if (command.getSpec().isPresent()) {
            result.spec(storeAdapter.save(command.getSpec().get()));
        }

        return result.build();
    }

    public FolderClosed close(final CloseFolder command) {
        LOGGER.debug("Closing folder: {}", command);

        storeAdapter.close(command.getMetadata().getIdentity().getUuid());

        return ImmutableFolderClosed.builder()
                .metadata(ImmutableMetadata.builder()
                        .from(command.getMetadata())
                        .build()
                )
                .addStatus(ImmutableOkStatus.builder()
                        .message(Optional.of("Folder " + command.getMetadata().getIdentity().getUuid() + " closed"))
                        .build())
                .build();
    }

    public FolderClosed close(final UUID id) {
        LOGGER.debug("Closing folder: {}", id);

        storeAdapter.close(id);

        return ImmutableFolderClosed.builder()
                .metadata(ImmutableMetadata.builder()
                        .identity(ImmutableObjectIdentifier.builder()
                                .kind(Folder.KIND)
                                .uuid(id)
                                .name(id.toString())
                                .build()
                        )
                        .created(OffsetDateTime.now())
                        .build()
                )
                .addStatus(ImmutableOkStatus.builder()
                        .message(Optional.of("Folder " + id + " closed"))
                        .build())
                .build();
    }

    public Folder load(final String scope, final String key) {
        LOGGER.debug("Loading folder: scope={}, key={}", scope, key);

        Optional<FolderSpec> data = storeAdapter.loadByName(scope, key);


        ImmutableFolder.Builder result = ImmutableFolder.builder();

        if (data.isPresent()) {
            FolderSpec spec = data.get();

            result
                    .metadata(ImmutableMetadata.builder()
                            .identity(ImmutableObjectIdentifier.builder()
                                    .kind(Folder.KIND)
                                    .uuid(spec.getUuid())
                                    .scope(scope)
                                    .name(spec.getKey())
                                    .build()
                            )
                            .created(spec.getCreated())
                            .modified(spec.getModified())
                            .build()
                    )
                    .spec(spec)
                    .addStatus(ImmutableOkStatus.builder()
                            .message(Optional.of("Folder " + spec.getUuid() + " found"))
                            .build()
                    );
        } else {
            result
                    .metadata(ImmutableMetadata.builder()
                            .identity(ImmutableObjectIdentifier.builder()
                                    .kind(Folder.KIND)
                                    .uuid(UUID.randomUUID())
                                    .scope(scope)
                                    .name(key)
                                    .build()
                            )
                            .created(OffsetDateTime.now())
                            .modified(OffsetDateTime.now())
                            .invalidAfter(OffsetDateTime.now().plusHours(1))
                            .build()
                    )
                    .addStatus(ImmutableNackStatus.builder()
                            .message(Optional.of("Folder scope=" + scope + ":" + key + " not found"))
                            .build()
                    );
        }

        return result.build();
    }

    public Folder load(final UUID uuid) {
        LOGGER.debug("Loading folder: uuid={}", uuid);

        ImmutableFolder.Builder result = ImmutableFolder.builder()
                .metadata(ImmutableMetadata.builder()
                        .identity(ImmutableObjectIdentifier.builder()
                                .kind(Folder.KIND)
                                .uuid(uuid)
                                .name(uuid.toString())
                                .build()
                        )
                        .created(OffsetDateTime.now())
                        .build()
                )
                .addStatus(ImmutableNackStatus.builder()
                        .message(Optional.of("Folder " + uuid + " not found"))
                        .build()
                );
        return storeAdapter
                .loadById(uuid)
                .map(f -> result
                        .metadata(ImmutableMetadata.builder()
                                .identity(ImmutableObjectIdentifier.builder()
                                        .kind(Folder.KIND)
                                        .uuid(f.getUuid())
                                        .name(f.getKey())
                                        .scope(f.getScope())
                                        .build()
                                )
                                .created(OffsetDateTime.now())
                                .build()
                        )
                        .spec(f)
                        .addStatus(ImmutableOkStatus.builder()
                                .message(Optional.of("Folder " + uuid + " found"))
                                .build()
                        )
                        .build())
                .orElseGet(result::build);

    }

    public Collection<FolderSpec> load(final String scope, final Long start, final Long size) {
        LOGGER.debug("Loading folders for scope: scope={}, start={}, size={}",
                scope,
                start != null ? start : "undefined",
                size != null ? size : "undefined");


        return storeAdapter.loadByScope(scope);
    }
}
