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

import de.kaiserpfalzedv.contacts.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Optional;

/**
 * @author rlichti
 * @since 2019-12-22 12:20
 */
@Embeddable
public class JPANaturalPersonData extends JPAPersonData {
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
    @Column(name = "_HONORIFIC_TITLE_POSTFIX")
    public String honorificPostfixTitle;

    @Column(name = "_HERALDIC_TITLE_PREFIX")
    public String heraldicPrefixTitle;
    @Column(name = "_HERALDIC_TITLE_POSTFIX")
    public String heraldicPostfixTitle;

    public JPANaturalPersonData fromModel(NaturalPersonSpec spec) {
        NaturalPersonData model = spec.getData();

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

    public NaturalPersonSpec toModel(JPAPersonSpec spec) {
        return ImmutableNaturalPersonSpec.builder()
                .identity(spec.identity.toModel(NaturalPerson.KIND, NaturalPerson.VERSION))
                .displayname(spec.displayname)
                .created(spec.created)
                .modified(spec.modified)

                .data(ImmutableNaturalPersonData.builder()
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

                        .build()
                )

                .build();
    }
}
