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

package de.kaiserpfalzedv.base.store.jpa.contacts;

import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import de.kaiserpfalzedv.contacts.ImmutableModifyNaturalPerson;
import de.kaiserpfalzedv.contacts.ModifyNaturalPerson;
import de.kaiserpfalzedv.contacts.NaturalPerson;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author rlichti
 * @since 2019-20-22 13:40
 */
@Entity
@DiscriminatorValue(ModifyNaturalPerson.KIND)
public class JPANaturalPersonModify extends JPANaturalPersonChangeWithSpec<ModifyNaturalPerson> {
    @Override
    public JPANaturalPersonModify fromModel(final ModifyNaturalPerson event) {
        fillData(event);

        return this;
    }

    @Override
    public ModifyNaturalPerson toModel() {
        return ImmutableModifyNaturalPerson.builder()
                .kind(ModifyNaturalPerson.KIND)
                .version(ModifyNaturalPerson.VERSION)

                .metadata(ImmutableMetadata.builder()
                        .identity(command.toModel(NaturalPerson.KIND, NaturalPerson.VERSION))
                        .workflowdata(workflow.toModel())
                        .build()
                )

                .spec(data.toModel(spec))

                .build();
    }
}
