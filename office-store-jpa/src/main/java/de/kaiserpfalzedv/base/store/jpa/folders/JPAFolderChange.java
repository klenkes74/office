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

package de.kaiserpfalzedv.base.store.jpa.folders;

import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import de.kaiserpfalzedv.base.store.jpa.JPAWorkflowData;
import de.kaiserpfalzedv.folders.api.FolderCommand;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Table(schema = "BASE", name = "FOLDERS_CHANGES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "_ACTION", length = 256)
public abstract class JPAFolderChange<T extends FolderCommand> extends PanacheEntity {
    @Column(name = "_ACTION", length = 256, insertable = false, updatable = false, nullable = false)
    public String kind;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "uuid", column = @Column(name = "_COMMAND_UUID")),
            @AttributeOverride(name = "tenant", column = @Column(name = "_COMMAND_TENANT")),
            @AttributeOverride(name = "key", column = @Column(name = "_COMMAND_KEY"))
    })
    public JPAIdentity command;

    @Embedded
    public JPAWorkflowData workflow;

    @Column(name = "_COMMAND_CREATED", nullable = false, updatable = false)
    public OffsetDateTime created;


    @Transient
    public abstract JPAFolderChange<T> fromModel(final T event);

    @Transient
    public abstract T toModel();
}
