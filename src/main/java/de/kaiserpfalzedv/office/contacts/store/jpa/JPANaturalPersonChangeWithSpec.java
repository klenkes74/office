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

package de.kaiserpfalzedv.office.contacts.store.jpa;

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.base.store.jpa.JPAWorkflowData;
import de.kaiserpfalzedv.office.contacts.ImmutableCreateNaturalPerson;
import de.kaiserpfalzedv.office.contacts.api.NaturalPersonCommandWithSpec;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.time.OffsetDateTime;
import java.util.Optional;


@Entity
public abstract class JPANaturalPersonChangeWithSpec extends JPANaturalPersonChange<NaturalPersonCommandWithSpec> {
    @Embedded
    public JPANaturalPersonSpec spec;

    protected NaturalPersonCommandWithSpec toModel(final String kind, final String version) {
        return ImmutableCreateNaturalPerson.builder()
                .kind(kind)
                .version(version)

                .metadata(ImmutableMetadata.builder()
                        .identity(spec.identity.toModel())
                        .workflowdata(Optional.ofNullable(workflow.toModel()))
                        .build()
                )

                .spec(spec.toModel())

                .build();
    }

    @Override
    public JPANaturalPersonChange<NaturalPersonCommandWithSpec> fromModel(final NaturalPersonCommandWithSpec event) {
        command = new JPAIdentity().fromModel(event.getMetadata().getIdentity());

        workflow = new JPAWorkflowData().fromModel(event.getMetadata().getWorkflowdata());

        spec = new JPANaturalPersonSpec().fromModel(event.getSpec());

        created = OffsetDateTime.now();

        return this;
    }
}
