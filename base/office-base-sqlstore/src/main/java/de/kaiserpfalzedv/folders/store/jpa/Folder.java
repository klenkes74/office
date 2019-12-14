/*
 *  Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2019. All rights reserved.
 *
 *  This file is part of Kaiserpfalz EDV-Service Office.
 *
 *  This is free software: you can redistribute it and/or modify it under the terms of
 *   the GNU Lesser General Public License as published by the Free Software
 *   Foundation, either version 3 of the License.
 *
 *  This file is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *  License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along
 *  with this file. If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package de.kaiserpfalzedv.folders.store.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(schema = "FOLDERS", name = "FOLDERS")
public class Folder extends PanacheEntity {
    @Column(name = "_UUID", columnDefinition = "CHAR(36)", nullable = false, unique = true, updatable = false)
    @Convert(converter = UuidConverter.class)
    public UUID uuid;

    @Column(name = "_SCOPE")
    public String scope;
    @Column(name = "_KEY")
    public String key;

    @Column(name = "_NAME")
    public String name;
    @Column(name = "_SHORTNAME")
    public String shortName;

    @Column(name = "_DESCRIPTION")
    public String description;
    @Column(name = "_CLOSED")
    public OffsetDateTime closed;

    @Column(name = "_CREATED")
    public OffsetDateTime created;
    @Column(name = "_MODIFIED")
    public OffsetDateTime modified;
}
