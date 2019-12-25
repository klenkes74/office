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

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.contacts.ImmutableNaturalPerson;
import de.kaiserpfalzedv.contacts.NaturalPerson;
import de.kaiserpfalzedv.contacts.NaturalPersonSpec;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author rlichti
 * @since 2019-12-22 12:20
 */
@Entity
@Table(name = "NATURAL_PERSONS", schema = "BASE")
public class JPANaturalPerson extends JPAPerson<JPANaturalPerson, JPANaturalPersonSpec, NaturalPerson, NaturalPersonSpec>
        implements Serializable {
    @Embedded
    public JPANaturalPersonSpec spec;

    public static PanacheQuery<JPANaturalPerson> findByUuid(final String tenant, final UUID uuid) {
        return find("identity.tenant=?1 and identity.uuid=?2", tenant, uuid);
    }

    public static PanacheQuery<JPANaturalPerson> findByTenant(final String tenant) {
        return find("identity.tenant=?1", tenant);
    }

    public static PanacheQuery<JPANaturalPerson> findByTenantAndKey(final String tenant, final String key) {
        return find("identity.tenant=?1 and identity.key=?2", tenant, key);
    }


    public NaturalPerson toModel() {
        if (spec == null) {
            throw new IllegalStateException("JPANaturalPerson: spec is not initialized.");
        }

        return ImmutableNaturalPerson.builder()
                .kind(NaturalPerson.KIND)
                .version(NaturalPerson.VERSION)
                .metadata(ImmutableMetadata.builder()
                        .identity(identity.toModel(NaturalPerson.KIND, NaturalPerson.VERSION))
                        .build()
                )
                .spec(spec.toModel())
                .build();
    }

    public JPANaturalPerson fromModel(final NaturalPersonSpec model) {
        spec = new JPANaturalPersonSpec().fromModel(model);

        return this;
    }

    @Override
    public String getKind() {
        return NaturalPerson.KIND;
    }

    @Override
    public String getVersion() {
        return NaturalPerson.VERSION;
    }
}
