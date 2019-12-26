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
import de.kaiserpfalzedv.base.api.KindHolding;
import de.kaiserpfalzedv.contacts.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.persistence.*;
import java.util.UUID;


/**
 * @author rlichti
 * @since 2019-12-25T10:00
 */
@Entity
@Table(schema = "BASE", name = "PERSONS")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class JPAPerson<T extends JPAPerson, P extends BasePerson, S extends BasePersonSpec> extends PanacheEntity implements KindHolding {
    @Embedded
    public JPAPersonSpec<JPAPersonSpec, PersonSpec, JPAPersonData> spec;


    public static <T extends JPAPerson> PanacheQuery<T> findByUuid(final String tenant, final UUID uuid) {
        return find("spec.identity.tenant=?1 and spec.identity.uuid=?2", tenant, uuid);
    }

    public static <T extends JPAPerson> PanacheQuery<T> findByTenant(final String tenant) {
        return find("spec.identity.tenant=?1", tenant);
    }

    public static <T extends JPAPerson> PanacheQuery<T> findByTenantAndKey(final String tenant, final String key) {
        return find("spec.identity.tenant=?1 and spec.identity.key=?2", tenant, key);
    }

    public P toModel() {
        //noinspection unchecked
        return (P) ImmutablePerson.builder()
                .kind(getKind())
                .version(getVersion())

                .metadata(ImmutableMetadata.builder()
                        .identity(spec.identity.toModel(getKind(), getVersion()))
                        .build())

                .spec(spec.toModel())

                .build();
    }

    @Override
    public String getKind() {
        return Person.KIND;
    }

    @Override
    public String getVersion() {
        return Person.VERSION;
    }
}
