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
import de.kaiserpfalzedv.folders.DeleteFolder;
import de.kaiserpfalzedv.folders.ImmutableDeleteFolder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.OffsetDateTime;
import java.util.Optional;

@Entity
@DiscriminatorValue(DeleteFolder.KIND)
public class JPAFolderDelete extends JPAFolderChange<DeleteFolder> {
    @Override
    public JPAFolderDelete fromModel(DeleteFolder event) {
        this.command = new JPAIdentity().fromModel(event.getMetadata().getIdentity());
        this.workflow = new JPAWorkflowData().fromModel(event.getMetadata().getWorkflowdata());
        this.created = OffsetDateTime.now();

        return this;
    }

    @Override
    public DeleteFolder toModel() {
        return ImmutableDeleteFolder.builder()
                .kind(DeleteFolder.KIND)
                .version(DeleteFolder.VERSION)
                .metadata(ImmutableMetadata.builder()
                        .identity(command.toModel())
                        .workflowdata(Optional.ofNullable(workflow.toModel()))
                        .build()
                )
                .build();
    }
}
