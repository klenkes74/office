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

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.base.store.jpa.JPAWorkflowData;
import de.kaiserpfalzedv.base.store.jpa.folders.data.JPAFolderSpec;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.ImmutableModifyFolder;
import de.kaiserpfalzedv.folders.ModifyFolder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.OffsetDateTime;

/**
 * @author rlichti
 * @since 2019-12-22
 */
@Entity
@DiscriminatorValue(ModifyFolder.KIND)
public class JPAFolderModify extends JPAFolderChangeWithSpec<ModifyFolder> {
    @Override
    public JPAFolderModify fromModel(ModifyFolder event) {
        command = new JPAIdentity().fromModel(event.getMetadata().getIdentity());
        workflow = new JPAWorkflowData().fromModel(event.getMetadata().getWorkflowdata());
        spec = new JPAFolderSpec().fromModel(event.getSpec());
        created = OffsetDateTime.now();

        return this;
    }

    @Override
    public ModifyFolder toModel() {
        return ImmutableModifyFolder.builder()
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
