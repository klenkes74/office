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

import de.kaiserpfalzedv.base.actions.CommandService;
import de.kaiserpfalzedv.base.actions.Result;
import de.kaiserpfalzedv.base.api.BaseAPI;
import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.store.DataAlreadyExistsException;
import de.kaiserpfalzedv.folders.*;
import de.kaiserpfalzedv.folders.store.FolderStoreAdapter;
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

    @Inject
    FolderStoreAdapter storeAdapter;

    public FolderCommandService() {
    }

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
                .build();
    }

    public Optional<Folder> load(final String scope, final String key) {
        LOGGER.debug("Loading folder: scope={}, key={}", scope, key);

        return processLoadedFolder(storeAdapter.loadByName(scope, key));
    }

    private Optional<Folder> processLoadedFolder(Optional<FolderSpec> data) {
        if (data.isPresent()) {
            FolderSpec spec = data.get();

            return Optional.of(ImmutableFolder.builder()
                    .metadata(ImmutableMetadata.builder()
                            .identity(ImmutableObjectIdentifier.builder()
                                    .kind(Folder.KIND)
                                    .uuid(spec.getUuid())
                                    .scope(spec.getScope())
                                    .name(spec.getKey())
                                    .build()
                            )
                            .created(spec.getCreated())
                            .modified(spec.getModified())
                            .build()
                    )
                    .spec(spec)
                    .build()
            );
        } else {
            return Optional.empty();
        }
    }

    public Optional<Folder> load(final UUID uuid) {
        LOGGER.debug("Loading folder: uuid={}", uuid);

        return processLoadedFolder(storeAdapter.loadById(uuid));
    }

    public Collection<FolderSpec> load(final String scope, final Long start, final Long size) {
        LOGGER.debug("Loading folders for scope: scope={}, start={}, size={}",
                scope,
                start != null ? start : "undefined",
                size != null ? size : "undefined");


        return storeAdapter.loadByScope(scope);
    }
}
