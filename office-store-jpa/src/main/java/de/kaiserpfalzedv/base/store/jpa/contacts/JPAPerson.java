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
import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.contacts.ImmutablePerson;
import de.kaiserpfalzedv.contacts.ImmutablePersonSpec;
import de.kaiserpfalzedv.contacts.Person;
import de.kaiserpfalzedv.contacts.PersonSpec;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;


/**
 * @author rlichti
 * @since 2019-12-25T10:00
 */
@SuppressWarnings("rawtypes")
@Entity
@Table(schema = "BASE", name = "PERSONS")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class JPAPerson<J extends JPAPerson, T extends JPAPersonSpec, P extends Person, S extends PersonSpec> extends PanacheEntity implements KindHolding {
    @Embedded
    public JPAIdentity identity;

    @Column(name = "_DISPLAYNAME")
    public String displayname;
    @Column(name = "_CREATED")
    public OffsetDateTime created;
    @Column(name = "_MODIFIED")
    public OffsetDateTime modified;


    public static <J extends JPAPerson> PanacheQuery<J> findByUuid(final String tenant, final UUID uuid) {
        return find("identity.tenant=?1 and identity.uuid=?2", tenant, uuid);
    }

    public static <J extends JPAPerson> PanacheQuery<J> findByTenant(final String tenant) {
        return find("identity.tenant=?1", tenant);
    }

    public static <J extends JPAPerson> PanacheQuery<J> findByTenantAndKey(final String tenant, final String key) {
        return find("identity.tenant=?1 and identity.key=?2", tenant, key);
    }

    public P toModel() {
        //noinspection unchecked
        return (P) ImmutablePerson.builder()
                .kind("undefined")
                .version("0.0.0")
                .metadata(ImmutableMetadata.builder()
                        .identity(identity.toModel("undefined", "0.0.0"))
                        .build())
                .spec(ImmutablePersonSpec.builder()
                        .kind(getKind())
                        .version(getVersion())
                        .identity(identity.toModel(getKind(), getVersion()))
                        .displayname(displayname)
                        .created(created)
                        .modified(modified)
                        .build())
                .build();
    }

    @Override
    public String getKind() {
        return "unused";
    }

    @Override
    public String getVersion() {
        return "0.0.0";
    }
}
