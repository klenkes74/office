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

package de.kaiserpfalzedv.commons.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableMap;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.Optional;

@Value.Immutable
@JsonSerialize(as = ImmutableMetadata.class)
@JsonDeserialize(builder = ImmutableMetadata.Builder.class)
public interface Metadata extends Serializable, IdentityHolding {
    /**
     * This is the identity of the object, not the request. To identify the request, please use
     * {@link WorkflowData#getRequest()}.
     *
     * @return the identity of the object maintained.
     */
    ObjectIdentity getIdentity();

    Optional<WorkflowData> getWorkflowdata();

    ImmutableMap<String, String> getLabels();
}
