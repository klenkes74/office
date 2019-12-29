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

package de.kaiserpfalzedv.base.store.jpa.folders.changes;

import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.base.store.jpa.JPAWorkflowData;
import de.kaiserpfalzedv.base.store.jpa.folders.data.JPAFolderSpec;
import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import de.kaiserpfalzedv.folders.CreateFolder;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.ImmutableCreateFolder;
import de.kaiserpfalzedv.folders.ModifyFolder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.OffsetDateTime;

@Entity
@DiscriminatorValue(CreateFolder.KIND)
public class JPAFolderCreate extends JPAFolderChangeWithSpec<CreateFolder> {
    @Override
    public JPAFolderCreate fromModel(final CreateFolder event) {
        command = new JPAIdentity().fromModel(event.getMetadata().getIdentity());
        workflow = new JPAWorkflowData().fromModel(event.getMetadata().getWorkflowdata());
        spec = new JPAFolderSpec().fromModel(event.getSpec());
        created = OffsetDateTime.now();

        return this;
    }

    @Override
    public CreateFolder toModel() {
        return ImmutableCreateFolder.builder()
                .kind(ModifyFolder.KIND)
                .version(ModifyFolder.VERSION)
                .metadata(ImmutableMetadata.builder()
                        .identity(command.toModel(Folder.KIND, Folder.VERSION))
                        .workflowdata(workflow.toModel())
                        .build()
                )
                .spec(spec.toModel())
                .build();
    }
}
