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

import de.kaiserpfalzedv.base.store.UuidConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-17 09:22
 */
@Embeddable
public class JPAWorkflowData implements Serializable {
    @Column(name = "_WORKFLOW_KIND", nullable = false, updatable = false)
    public String kind;
    @Column(name = "_WORKFLOW_VERSION", nullable = false, updatable = false)
    public String version;

    @Column(name = "_WORKFLOW_REQUEST", columnDefinition = "CHAR(36)", updatable = false, unique = true)
    @Convert(converter = UuidConverter.class)
    public UUID request;
    @Column(name = "_WORKFLOW_CORRELATION", columnDefinition = "CHAR(36)", updatable = false, unique = true)
    @Convert(converter = UuidConverter.class)
    public UUID correlation;
    @Column(name = "_WORKFLOW_SEQUENCE", updatable = false)
    public Long sequence;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "uuid", column = @Column(name = "_WORKFLOW_DEFINITION_UUID")),
            @AttributeOverride(name = "scope", column = @Column(name = "_WORKFLOW_DEFINITION_TENANT")),
            @AttributeOverride(name = "key", column = @Column(name = "_WORKFLOW_DEFINITION_KEY"))
    })
    public JPAIdentity workflow;
}