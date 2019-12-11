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

import com.jcabi.manifests.Manifests;
import de.kaiserpfalzedv.base.Metadata;
import de.kaiserpfalzedv.base.status.Status;
import de.kaiserpfalzedv.office.folders.*;
import de.kaiserpfalzedv.office.folders.Folder;
import de.kaiserpfalzedv.office.folders.api.FolderCommandService;

import javax.persistence.*;
import javax.swing.text.html.Option;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@DiscriminatorValue(CreateFolder.KIND)
@Access(AccessType.PROPERTY)
public class FolderCreate extends FolderChange implements FolderSpec, CreateFolder {
    private ModifiableFolderSpec data;

    public FolderCreate() {
        data = ModifiableFolderSpec.create();
    }

    @Override
    @Column(name = "_UUID")
    public UUID getUuid() {
        return data.getUuid();
    }

    public void setUuid(final UUID uuid) {
        data.setUuid(uuid);
    }

    @Transient
    @Override
    public Optional<String> getScope() {
        return Optional.ofNullable(getPersistentScope());
    }

    @Column(name = "_SCOPE")
    public String getPersistentScope() {
        return data.getScope().orElse(null);
    }

    public void setPersistentScope(final String scope) {
        data.setScope(scope);
    }

    public void setPersistentScope(
            @SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<String> scope) {
        data.setScope(scope);
    }

    @Override
    @Column(name = "_KEY")
    public String getKey() {
        return data.getKey();
    }

    public void setKey(final String key) {
        data.setKey(key);
    }

    @Override
    @Column(name = "_NAME")
    public String getName() {
        return data.getName();
    }

    public void setName(final String name) {
        data.setName(name);
    }

    @Transient
    @Override
    public Optional<String> getShortName() {
        return Optional.ofNullable(getPersistentShortName());
    }

    @Column(name = "_SHORTNAME")
    public String getPersistentShortName() {
        return data.getShortName().orElse(null);
    }

    public void setPersistentShortName(final String shortName) {
        data.setShortName(shortName);
    }

    @Transient
    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(getPersistentDescription());
    }

    @Column(name = "_DESCRIPTION")
    public String getPersistentDescription() {
        return data.getDescription().orElse(null);
    }

    public void setPersistentDescription(final String description) {
        data.setDescription(description);
    }

    @Transient
    @Override
    public Optional<OffsetDateTime> getClosed() {
        return Optional.ofNullable(getPersistentClosed());
    }

    @Column(name = "_CLOSED")
    public OffsetDateTime getPersistentClosed() {
        return data.getClosed().orElse(null);
    }

    public void setPersistentClosed(final OffsetDateTime closed) {
        data.setClosed(closed);
    }

    @Override
    @Column(name = "_CREATE")
    public OffsetDateTime getCreated() {
        return data.getCreated();
    }

    public void setCreated(final OffsetDateTime created) {
        data.setCreated(created);
    }

    @Override
    @Column(name = "_MODIFIED")
    public OffsetDateTime getModified() {
        return data.getModified();
    }

    public void setModified(final OffsetDateTime modified) {
        data.setModified(modified);
    }

    @Transient
    @Override
    public Optional<FolderSpec> getSpec() {
        return Optional.of(this);
    }

    @Transient
    @Override
    public Metadata getMetadata() {
        return data.getMetadata();
    }

    @Transient
    @Override
    public List<Status> getStatus() {
        return new ArrayList<>(0);
    }

    @Override
    public FolderCreated execute(FolderCommandService service) {
        return (FolderCreated) service.execute(this);
    }


    @Transient
    @Override
    public String getKind() {
        return Folder.KIND;
    }

    @Transient
    @Override
    public String getVersion() {
        return Manifests.read("Implementation-Version");
    }


    @Override
    public boolean equals(Object another) {
        return data.equals(another);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
