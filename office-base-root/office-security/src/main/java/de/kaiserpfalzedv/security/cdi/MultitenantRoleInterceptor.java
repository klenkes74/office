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

package de.kaiserpfalzedv.security.cdi;

import de.kaiserpfalzedv.security.tenant.cdi.Slf4jMDCTenant;
import de.kaiserpfalzedv.security.tenant.cdi.TenantProvider;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.security.identity.SecurityIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.lang.annotation.Annotation;

/*
 * The following interceptor is inspired by the GateKeeper interceptor of Dewald Pretorios.
 *
 * @author Dewald Pretorios (project Guardian, licensed as LGPL 3
 * @author rlichti
 * @since 2019-12-18 22:56
 */
@RequestScoped
@Interceptor
@MultitenantRolesAllowed("")
@Priority(1)
public class MultitenantRoleInterceptor implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultitenantRoleInterceptor.class);

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    TenantProvider tenantProvider;


    @AroundInvoke
    public Object logMethodCall(InvocationContext ctx) throws Exception {
        Annotation[] annotations = ctx.getMethod().getDeclaredAnnotations();

        String[] permissions = null;
        for (Annotation a : annotations) {
            if (a instanceof MultitenantRolesAllowed) {
                permissions = ((MultitenantRolesAllowed) a).value();
            }
        }

        if (permissions == null) {
            LOGGER.trace("No permission to check for multitenant permission!");

            return ctx.proceed();
        }

        if (securityIdentity == null || getTenant() == null) {
            LOGGER.error("No security context or tenant to work on (context={}, tenant={})!", securityIdentity, getTenant());

            throw new UnauthorizedException();
        }

        for (String p : permissions) {
            String roleToMatch = getTenant() + "." + p.toLowerCase();
            if (securityIdentity.getRoles().contains(roleToMatch)) {
                LOGGER.debug("Role matched: {}", roleToMatch);
                return ctx.proceed();
            }
        }

        LOGGER.error("User has no tenant roles: needed={}, user={}", permissions, securityIdentity.getRoles());
        throw new ForbiddenException();
    }

    private String getTenant() {
        return tenantProvider.getTenant().orElse(new Slf4jMDCTenant()).getKey();
    }
}