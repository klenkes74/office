/*
 * Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2020. All rights reserved.
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

package de.kaiserpfalzedv.security.oidc.resource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableSet;
import de.kaiserpfalzedv.commons.ObjectReference;
import de.kaiserpfalzedv.commons.api.DisplaynameHolding;
import de.kaiserpfalzedv.commons.api.IdentityHolding;
import de.kaiserpfalzedv.security.oidc.IconUriHolding;
import de.kaiserpfalzedv.security.oidc.OidcScope;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author rlichti
 * @since 2020-01-01T18:36Z
 */
@Value.Immutable
@JsonSerialize(as = ImmutableOidcUmaResource.class)
@JsonDeserialize(builder = ImmutableOidcUmaResource.Builder.class)
public interface OidcUmaResource extends Serializable, DisplaynameHolding, IdentityHolding, IconUriHolding {
    ImmutableSet<String> getUris();

    ImmutableSet<OidcScope> getScopes();

    default ObjectReference getOwner() {
        return getIdentity().getOwner().orElseThrow(SecurityException::new);
    }

    Boolean getOwnerManagedAccess();

    Map<String, List<String>> getAttributes();
}
