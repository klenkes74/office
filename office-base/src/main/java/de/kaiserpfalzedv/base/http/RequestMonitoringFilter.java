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

package de.kaiserpfalzedv.base.http;

import io.quarkus.security.identity.SecurityIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;

@Provider
@Priority(90)
public class RequestMonitoringFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestMonitoringFilter.class);

    @Context
    UriInfo info;

    @Inject
    SecurityIdentity securityIdentity;


    @Override
    public void filter(ContainerRequestContext context) {
        if (!LOGGER.isDebugEnabled()) return;

        ArrayList<String> headers = new ArrayList<>();
        context.getHeaders().forEach((k, v) -> headers.add("{'" + k + "': '" + v + "'}"));
        ArrayList<String> queryParameters = new ArrayList<>();
        info.getQueryParameters().forEach((k, v) -> queryParameters.add("{'" + k + "': '" + v + "'}"));

        LOGGER.debug("{'method': '{}', 'path': '{}', 'principal': '{}', 'roles': {}, 'headers': '{}', 'query': '{}'}",
                context.getMethod(), info.getPath(),
                securityIdentity.getPrincipal().getName(), securityIdentity.getRoles(),
                headers,
                queryParameters
        );
    }
}
