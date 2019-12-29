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
import de.kaiserpfalzedv.base.store.jpa.JPAObjectReferenceHistory;
import de.kaiserpfalzedv.base.store.jpa.JPAWorkflowData;
import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import de.kaiserpfalzedv.folders.AddContent;
import de.kaiserpfalzedv.folders.Folder;
import de.kaiserpfalzedv.folders.ImmutableAddContent;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.OffsetDateTime;

/**
 * @author rlichti
 * @since 2019-12-27T12:02Z
 */
@Entity
@DiscriminatorValue(AddContent.KIND)
public class JPAFolderContentAdd extends JPAFolderChangeWithContent<AddContent> {

    @Override
    public JPAFolderContentAdd fromModel(final AddContent event) {
        identity = new JPAIdentity().fromModel(event.getMetadata().getIdentity());
        command = new JPAIdentity().fromModel(event.getMetadata().getIdentity());
        workflow = new JPAWorkflowData().fromModel(event.getMetadata().getWorkflowdata());
        created = OffsetDateTime.now();

        this.persist();
        event.getData().forEach(d -> addContent(JPAObjectReferenceHistory.fromModel(d)));

        return this;
    }

    @Override
    public AddContent toModel() {
        return ImmutableAddContent.builder()
                .kind(AddContent.KIND)
                .version(AddContent.VERSION)
                .metadata(ImmutableMetadata.builder()
                        .identity(identity.toModel(Folder.KIND, Folder.VERSION))
                        .workflowdata(workflow.toModel())
                        .build()
                )
                .data(getDataModel())
                .build();
    }
}
