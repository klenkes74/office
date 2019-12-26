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

package de.kaiserpfalzedv.base.store.jpa.folders;

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.base.store.jpa.JPAWorkflowData;
import de.kaiserpfalzedv.folders.CloseFolder;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.ImmutableCloseFolder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.OffsetDateTime;

@Entity
@DiscriminatorValue(CloseFolder.KIND)
public class JPAFolderClose extends JPAFolderChange<CloseFolder> {
    @Override
    public JPAFolderClose fromModel(CloseFolder event) {
        command = new JPAIdentity().fromModel(event.getMetadata().getIdentity());
        workflow = new JPAWorkflowData().fromModel(event.getMetadata().getWorkflowdata());
        created = OffsetDateTime.now();

        return this;
    }

    @Override
    public CloseFolder toModel() {
        return ImmutableCloseFolder.builder()
                .kind(CloseFolder.KIND)
                .version(CloseFolder.VERSION)
                .metadata(ImmutableMetadata.builder()
                        .identity(command.toModel(Folder.KIND, Folder.VERSION))
                        .workflowdata(workflow.toModel())
                        .build()
                )
                .build();
    }
}
