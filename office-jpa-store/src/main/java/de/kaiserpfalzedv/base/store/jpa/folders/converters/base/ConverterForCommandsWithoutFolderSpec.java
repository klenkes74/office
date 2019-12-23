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

package de.kaiserpfalzedv.base.store.jpa.folders.converters.base;

import de.kaiserpfalzedv.base.api.*;
import de.kaiserpfalzedv.base.store.DataConverter;
import de.kaiserpfalzedv.base.store.jpa.folders.JPAFolderChange;
import de.kaiserpfalzedv.folders.api.FolderCommand;
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
            result.workflow.workflow.tenant = workflow.getDefinition().getTenant().orElse(null);
            result.workflow.workflow.key = workflow.getDefinition().getName().orElse(null);

            result.workflow.correlation = workflow.getCorrelation();
            result.workflow.request = workflow.getRequest();
            result.workflow.sequence = workflow.getSequence().orElse(null);
        }
    }

    private void convertCommand(A data, D result) {
        result.command = new JPAIdentity();
        result.command.uuid = data.getMetadata().getIdentity().getUuid();
        result.command.tenant = data.getMetadata().getIdentity().getTenant().orElse(null);
        result.command.key = data.getMetadata().getIdentity().getName().orElse(null);
    }

    private void convertIdentity(A data, D result) {
        result.identity = new JPAIdentity();
        result.identity.uuid = data.getMetadata().getIdentity().getUuid();
        result.identity.tenant = data.getMetadata().getIdentity().getTenant().orElse(null);
        result.identity.key = data.getMetadata().getIdentity().getName().orElse(null);
    }


    protected Metadata createMetadata(D data, ObjectIdentity identity) {
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
                            .tenant(data.workflow.workflow.tenant)
                            .name(data.workflow.workflow.key)

                            .build()
                    )
                    .build()
            );
        }
    }

    protected ObjectIdentity createSpecIdentity(
            @NotNull final JPAIdentity identity,
            @NotNull final String kind,
            @NotNull final String version
    ) {
        return ImmutableObjectIdentifier.builder()
                .kind(kind)
                .version(version)

                .uuid(identity.uuid)
                .tenant(Optional.ofNullable(identity.tenant))
                .name(Optional.ofNullable(identity.key))
                .build();
    }
}
