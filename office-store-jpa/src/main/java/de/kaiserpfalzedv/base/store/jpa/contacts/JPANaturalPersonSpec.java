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

import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.contacts.ImmutableNaturalPersonSpec;
import de.kaiserpfalzedv.contacts.NaturalPersonSpec;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * @author rlichti
 * @since 2019-12-22 12:20
 */
@Embeddable
public class JPANaturalPersonSpec implements Serializable {
    @Embedded
    public JPAIdentity identity;

    @Column(name = "_DISPLAYNAME")
    public String displayname;

    @Column(name = "_CREATED", nullable = false)
    public OffsetDateTime created;
    @Column(name = "_MODIFIED", nullable = false)
    public OffsetDateTime modified;

    @Column(name = "_GIVENNAME_PREFIX")
    public String givennamePrefix;
    @Column(name = "_GIVENNAME")
    public String givenname;
    @Column(name = "_GIVENNAME_POSTFIX")
    public String givennamePostfix;

    @Column(name = "_SURNAME_PREFIX")
    public String surnamePrefix;
    @Column(name = "_SURNAME")
    public String surname;
    @Column(name = "_SURNAME_POSTFIX")
    public String surnamePostfix;

    @Column(name = "_HONORIFIC_TITLE_PREFIX")
    public String honorificPrefixTitle;
    @Column(name = "_HONORIFIC_POSTFIX")
    public String honorificPostfixTitle;

    @Column(name = "_HERALDIC_TITLE_PREFIX")
    public String heraldicPrefixTitle;
    @Column(name = "_HERALDIC_POSTFIX")
    public String heraldicPostfixTitle;

    public JPANaturalPersonSpec fromModel(NaturalPersonSpec model) {
        identity = new JPAIdentity().fromModel(model.getIdentity());

        displayname = model.getDisplayname();
        created = model.getCreated();
        modified = model.getModified();

        givennamePrefix = model.getGivennamePrefix().orElse(null);
        givenname = model.getGivenname();
        givennamePostfix = model.getGivennamePostfix().orElse(null);

        surnamePrefix = model.getSurnamePrefix().orElse(null);
        surname = model.getSurname();
        surnamePostfix = model.getSurnamePostfix().orElse(null);

        honorificPrefixTitle = model.getHonorificPrefixTitle().orElse(null);
        honorificPostfixTitle = model.getHonorificPostfixTitle().orElse(null);

        heraldicPrefixTitle = model.getHeraldicPrefixTitle().orElse(null);
        heraldicPostfixTitle = model.getHeraldicPostfixTitle().orElse(null);

        return this;
    }

    public NaturalPersonSpec toModel() {
        return ImmutableNaturalPersonSpec.builder()
                .kind(NaturalPersonSpec.KIND)
                .version(NaturalPersonSpec.VERSION)

                .identity(identity.toModel())

                .displayname(displayname)

                .givennamePrefix(Optional.ofNullable(givennamePrefix))
                .givenname(givenname)
                .givennamePostfix(Optional.ofNullable(givennamePostfix))

                .surnamePrefix(Optional.ofNullable(surnamePrefix))
                .surname(surname)
                .surnamePostfix(Optional.ofNullable(surnamePostfix))

                .honorificPrefixTitle(Optional.ofNullable(honorificPrefixTitle))
                .honorificPostfixTitle(Optional.ofNullable(honorificPostfixTitle))

                .heraldicPrefixTitle(Optional.ofNullable(heraldicPrefixTitle))
                .heraldicPostfixTitle(Optional.ofNullable(heraldicPostfixTitle))

                .created(created)
                .modified(modified)

                .build();
    }
}
