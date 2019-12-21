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

package de.kaiserpfalzedv.office.folders.store.jpa;

import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.base.store.jpa.JPAWorkflowData;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Table(schema = "FOLDERS", name = "FOLDERS_CHANGES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "_ACTION", length = 256)
public abstract class JPAFolderChange extends PanacheEntity {
    @Column(name = "_ACTION", length = 256, insertable = false, updatable = false, nullable = false)
    public String kind;

    @Embedded
    public JPAIdentity identity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "uuid", column = @Column(name = "_COMMAND_UUID")),
            @AttributeOverride(name = "scope", column = @Column(name = "_COMMAND_SCOPE")),
            @AttributeOverride(name = "key", column = @Column(name = "_COMMAND_KEY"))
    })
    public JPAIdentity command;

    @Embedded
    public JPAWorkflowData workflow;

    @Column(name = "_COMMAND_CREATED", nullable = false, updatable = false)
    public OffsetDateTime created;
}
