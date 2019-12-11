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

package de.kaiserpfalzedv.office.folders.data;

import de.kaiserpfalzedv.base.Metadata;
import de.kaiserpfalzedv.base.status.Status;
import de.kaiserpfalzedv.office.folders.FolderSpec;
import de.kaiserpfalzedv.office.folders.api.FolderCommand;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "FOLDER_HISTORY")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "_ACTION", length = 256)
@Access(AccessType.PROPERTY)
public class FolderChange extends PanacheEntity implements FolderCommand {
    private String kind;


    public long getId() {
        return super.id;
    }

    public void setId(final Long id) {
        super.id = id;
    }


    @Column(name = "_ACTION", insertable = false, updatable = false)
    public String getKind() {
        return kind;
    }

    @Transient
    @Override
    public Metadata getMetadata() {
        return null;
    }

    @Transient
    @Override
    public List<Status> getStatus() {
        return new ArrayList<>(0);
    }

    public void setKind(final String kind) {
        this.kind = kind;
    }

    @Transient
    @Override
    public Optional<FolderSpec> getSpec() {
        return Optional.empty();
    }
}
