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

import de.kaiserpfalzedv.office.folders.FolderSpec;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.boot.model.source.spi.IdentifierSourceSimple;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@MappedSuperclass
@Table(name = "FOLDER")
@Access(AccessType.PROPERTY)
public class Folder extends PanacheEntity implements FolderSpec {
    private UUID uuid;

    private String scope;
    private String key;

    private String name;
    private String shortName;

    private String description;
    private OffsetDateTime closed;

    private OffsetDateTime created;
    private OffsetDateTime modified;


    public Folder() {}

    public Folder(FolderSpec orig) {
        uuid = orig.getUuid();
        scope = orig.getScope().orElse(null);
        key = orig.getKey();
        name = orig.getName();
        shortName = orig.getShortName().orElse(null);
        description = orig.getDescription().orElse(null);
        closed = orig.getClosed().orElse(null);
        created = orig.getCreated();
        modified = orig.getModified();
    }


    public long getId() {
        return super.id;
    }

    public void setId(final Long id) {
        super.id = id;
    }


    @Override
    @Column(name = "_UUID")
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }


    @Transient
    @Override
    public Optional<String> getScope() {
        return Optional.ofNullable(getPersistentScope());
    }

    public void setScope(final String scope) {
        setPersistentScope(scope);
    }

    public void setScope(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<String> scope) {
        setPersistentScope(scope.orElse(null));
    }

    @Column(name = "_SCOPE")
    public String getPersistentScope() {
        return scope;
    }

    public void setPersistentScope(final String scope) {
        this.scope = scope;
    }

    @Override
    @Column(name = "_KEY")
    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    @Override
    @Column(name = "_NAME")
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Transient
    @Override
    public Optional<String> getShortName() {
        return Optional.ofNullable(shortName);
    }

    public void setShortName(final String shortName) {
        setPersistentShortName(shortName);
    }

    public void setShortName(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<String> shortName) {
        setPersistentShortName(shortName.orElse(null));
    }


    @Column(name = "_SHORTNAME")
    public String getPersistentShortName() {
        return shortName;
    }

    public void setPersistentShortName(final String shortName) {
        this.shortName = shortName;
    }

    @Transient
    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(final String description) {
        setPersistentDescription(description);
    }

    public void setDescription(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<String> description) {
        setPersistentDescription(description.orElse(null));
    }

    @Column(name = "_DESCRIPTION")
    public String getPersistentDescription() {
        return description;
    }
    public void setPersistentDescription(final String description) {
        this.description = description;
    }


    @Transient
    @Override
    public Optional<OffsetDateTime> getClosed() {
        return Optional.ofNullable(getPersistentClosed());
    }

    public void setClosed(final OffsetDateTime closed) {
        setPersistentClosed(closed);
    }

    public void setClosed(
            @SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<OffsetDateTime> closed) {
        setPersistentClosed(closed.orElse(null));
    }

    @Column(name = "_CLOSED")
    public OffsetDateTime getPersistentClosed() {
        return closed;
    }

    public void setPersistentClosed(final OffsetDateTime closed) {
        this.closed = closed;
    }

    @Override
    @Column(name = "_CREATED")
    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(final OffsetDateTime created) {
        this.created = created;
    }


    @Override
    @Column(name = "_MODIFIED")
    public OffsetDateTime getModified() {
        return modified;
    }

    public void setModified(final OffsetDateTime modified) {
        this.modified = modified;
    }
}
