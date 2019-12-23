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

package de.kaiserpfalzedv.contacts.jpa;


import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.io.Serializable;
import java.time.OffsetDateTime;


/**
 * @author rlichti
 * @since 2019-12-22 12:20
 */
@Embeddable
public class JPAContactSpec implements Serializable {
    @Embedded
    public JPAIdentity identity;

    @Column(name = "_DISPLAYNAME")
    public String displayname;

    @Column(name = "_CREATED", nullable = false)
    public OffsetDateTime created;
    @Column(name = "_MODIFIED", nullable = false)
    public OffsetDateTime modified;
}
