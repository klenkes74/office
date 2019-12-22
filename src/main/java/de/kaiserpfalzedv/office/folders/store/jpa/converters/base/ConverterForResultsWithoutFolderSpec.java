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
import de.kaiserpfalzedv.office.folders.api.FolderResult;
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
public abstract class ConverterForResultsWithoutFolderSpec<A extends FolderResult, D extends JPAFolderChange> implements DataConverter<A, D> {
    protected void convert(A data, D result) {
        convertMetadata(data, result);
        convertCommand(data, result);
        convertIdentity(data, result);

        result.created = OffsetDateTime.now();
    }

    private void convertMetadata(A data, D result) {
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
        result.identity.uuid = data.getMetadata().getIdentity().getUuid();
        result.identity.tenant = data.getMetadata().getIdentity().getTenant().orElse(null);
        result.identity.key = data.getMetadata().getIdentity().getName().orElse(null);
    }


    protected Metadata createMetadata(JPAFolderChange data, ObjectIdentifier identity) {
        ImmutableMetadata.Builder result = ImmutableMetadata.builder()
                .identity(identity);

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

                    .correlation(data.workflow.correlation)
                    .request(data.workflow.request)
                    .sequence(Optional.ofNullable(data.workflow.sequence))

                    .timestamp(data.created)

                    .build()
            );
        }

        return result.build();
    }


    protected Metadata createMetadata(ObjectIdentifier identity) {
        return ImmutableMetadata.builder()
                .identity(identity)
                .build();
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
                .tenant(Optional.ofNullable(identity.tenant))
                .name(Optional.ofNullable(identity.key))
                .build();
    }
}
