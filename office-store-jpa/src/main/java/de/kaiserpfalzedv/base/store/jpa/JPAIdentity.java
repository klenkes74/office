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

package de.kaiserpfalzedv.base.store.jpa;

import de.kaiserpfalzedv.base.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.base.api.ObjectIdentity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-17 09:22
 */
@Embeddable
public class JPAIdentity implements Serializable {
    @Column(name = "_UUID", columnDefinition = "CHAR(36)", updatable = false, unique = true)
    @Convert(converter = UuidConverter.class)
    public UUID uuid;
    @Column(name = "_TENANT", updatable = false)
    public String tenant;
    @Column(name = "_KEY", updatable = false)
    public String key;

    @Transient
    public JPAIdentity fromModel(ObjectIdentity identity) {
        uuid = identity.getUuid();
        tenant = identity.getTenant();
        key = identity.getName().orElse(null);

        return this;
    }

    @Transient
    public ObjectIdentity toModel(final String kind, final String version) {
        return ImmutableObjectIdentity.builder()
                .kind(kind)
                .version(version)

                .uuid(uuid)
                .tenant(tenant)
                .name(key)

                .build();
    }
}
