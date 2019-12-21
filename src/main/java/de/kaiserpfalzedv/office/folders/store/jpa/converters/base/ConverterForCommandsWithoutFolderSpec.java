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

package de.kaiserpfalzedv.office.folders.store.jpa.converters.base;

import de.kaiserpfalzedv.base.api.*;
import de.kaiserpfalzedv.base.store.DataConverter;
import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.base.store.jpa.JPAWorkflowData;
import de.kaiserpfalzedv.office.folders.api.FolderCommand;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolderChange;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.Optional;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-17 10:22
 */
public abstract class ConverterForCommandsWithoutFolderSpec<A extends FolderCommand, D extends JPAFolderChange> implements DataConverter<A, D> {
    protected void convert(A data, D result) {
        convertIdentity(data, result);
        convertCommand(data, result);
        convertWorkflow(data, result);

        result.created = OffsetDateTime.now();
    }

    private void convertWorkflow(A data, D result) {
        if (data.getMetadata().getWorkflowdata().isPresent()) {
            WorkflowData workflow = data.getMetadata().getWorkflowdata().get();

            result.workflow = new JPAWorkflowData();
            result.workflow.workflow = new JPAIdentity();
            result.workflow.kind = workflow.getDefinition().getKind();
            result.workflow.version = workflow.getDefinition().getVersion();
            result.workflow.workflow.uuid = workflow.getDefinition().getUuid();
            result.workflow.workflow.scope = workflow.getDefinition().getScope().orElse(null);
            result.workflow.workflow.key = workflow.getDefinition().getName().orElse(null);

            result.workflow.correlation = workflow.getCorrelation();
            result.workflow.request = workflow.getRequest();
            result.workflow.sequence = workflow.getSequence().orElse(null);
        }
    }

    private void convertCommand(A data, D result) {
        result.command = new JPAIdentity();
        result.command.uuid = data.getMetadata().getIdentity().getUuid();
        result.command.scope = data.getMetadata().getIdentity().getScope().orElse(null);
        result.command.key = data.getMetadata().getIdentity().getName().orElse(null);
    }

    private void convertIdentity(A data, D result) {
        result.identity = new JPAIdentity();
        result.identity.uuid = data.getMetadata().getIdentity().getUuid();
        result.identity.scope = data.getMetadata().getIdentity().getScope().orElse(null);
        result.identity.key = data.getMetadata().getIdentity().getName().orElse(null);
    }


    protected Metadata createMetadata(D data, ObjectIdentifier identity) {
        ImmutableMetadata.Builder result = ImmutableMetadata.builder()
                .identity(identity);

        convertWorkflow(data, result);

        return result.build();
    }

    private void convertWorkflow(D data, ImmutableMetadata.Builder result) {
        if (data.workflow.correlation != null) {
            result.workflowdata(ImmutableWorkflowData.builder()
                    .definition(ImmutableObjectIdentifier.builder()
                            .kind(data.workflow.kind)
                            .version(data.workflow.version)

                            .uuid(data.workflow.workflow.uuid)
                            .scope(data.workflow.workflow.scope)
                            .name(data.workflow.workflow.key)

                            .build()
                    )
                    .build()
            );
        }
    }

    protected ObjectIdentifier createSpecIdentity(
            @NotNull final JPAIdentity identity,
            @NotNull final String kind,
            @NotNull final String version
    ) {
        return ImmutableObjectIdentifier.builder()
                .kind(kind)
                .version(version)

                .uuid(identity.uuid)
                .scope(Optional.ofNullable(identity.scope))
                .name(Optional.ofNullable(identity.key))
                .build();
    }
}
