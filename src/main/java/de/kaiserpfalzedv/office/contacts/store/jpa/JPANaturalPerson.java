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

import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.office.contacts.ImmutableNaturalPersonContactSpec;
import de.kaiserpfalzedv.office.contacts.NaturalPersonSpec;
import de.kaiserpfalzedv.office.contacts.api.NaturalPersonResultWithSpec;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Optional;

/**
 * @author rlichti
 * @since 2019-12-22 12:20
 */
@Entity
@Table(name = "NATURAL_PERSONS", schema = "BASE")
public class JPANaturalPerson extends JPAContact implements Serializable {
    @Embedded
    JPANaturalPersonSpec spec;

    public JPANaturalPerson fromModel(NaturalPersonResultWithSpec model) {
        spec = new JPANaturalPersonSpec();

        NaturalPersonSpec modelSpec = model.getSpec();
        ObjectIdentifier modelIdentity = modelSpec.getIdentity();

        spec = new JPANaturalPersonSpec();
        spec.identity = new JPAIdentity();
        spec.identity = new JPAIdentity();
        spec.identity.uuid = modelIdentity.getUuid();
        spec.identity.tenant = modelIdentity.getTenant().orElse(null);
        spec.identity.key = modelIdentity.getName().orElse(null);

        spec.displayname = modelSpec.getDisplayname();
        spec.created = modelSpec.getCreated();
        spec.modified = modelSpec.getModified();


        spec.givennamePrefix = modelSpec.getGivennamePrefix().orElse(null);
        spec.givenname = modelSpec.getGivenname();
        spec.givennamePostfix = modelSpec.getGivennamePostfix().orElse(null);

        spec.surnamePrefix = modelSpec.getSurnamePrefix().orElse(null);
        spec.surname = modelSpec.getSurname();
        spec.surnamePostfix = modelSpec.getSurnamePostfix().orElse(null);

        spec.honorificPrefixTitle = modelSpec.getHonorificPrefixTitle().orElse(null);
        spec.honorificPostfixTitle = modelSpec.getHonorificPostfixTitle().orElse(null);

        spec.heraldicPrefixTitle = modelSpec.getHeraldicPrefixTitle().orElse(null);
        spec.heraldicPostfixTitle = modelSpec.getHeraldicPostfixTitle().orElse(null);

        return this;
    }

    public NaturalPersonSpec toModel() {
        if (spec == null) {
            throw new IllegalStateException("JPANaturalPerson: spec is not initialized.");
        }

        return ImmutableNaturalPersonContactSpec.builder()
                .kind(NaturalPersonSpec.KIND)
                .version(NaturalPersonSpec.VERSION)

                .identity(ImmutableObjectIdentifier.builder()
                        .kind(NaturalPersonSpec.KIND)
                        .version(NaturalPersonSpec.VERSION)

                        .uuid(spec.identity.uuid)
                        .tenant(spec.identity.tenant)
                        .name(spec.identity.key)

                        .build()
                )

                .givennamePrefix(Optional.ofNullable(spec.givennamePrefix))
                .givenname(spec.givenname)
                .givennamePostfix(Optional.ofNullable(spec.givennamePostfix))

                .surnamePrefix(Optional.ofNullable(spec.surnamePrefix))
                .surname(spec.surname)
                .surnamePostfix(Optional.ofNullable(spec.surnamePostfix))

                .honorificPrefixTitle(Optional.ofNullable(spec.honorificPrefixTitle))
                .honorificPostfixTitle(Optional.ofNullable(spec.honorificPostfixTitle))

                .heraldicPrefixTitle(Optional.ofNullable(spec.heraldicPrefixTitle))
                .heraldicPostfixTitle(Optional.ofNullable(spec.heraldicPostfixTitle))

                .created(spec.created)
                .modified(spec.modified)

                .build();
    }
}
