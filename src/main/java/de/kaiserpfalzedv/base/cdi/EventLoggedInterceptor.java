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

package de.kaiserpfalzedv.base.cdi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;
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
@Priority(300)
@EventLogged
@Interceptor
public class EventLoggedInterceptor implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoggedInterceptor.class);


    @AroundInvoke
    public Object logMethodCall(InvocationContext ctx) throws Exception {
        if (!LOGGER.isTraceEnabled()) return ctx.proceed();

        Object[] parameters = ctx.getMethod().getParameters();
        if (parameters == null || parameters.length < 1) return ctx.proceed();

        Annotation[][] annotations = ctx.getMethod().getParameterAnnotations();
        Class<?>[] paramterTypes = ctx.getMethod().getParameterTypes();


        for (int index = 0; index < parameters.length; index++) {
            if (parameters[index] == null) continue;

            for (Annotation annotation : annotations[index]) {
                if (!(annotation instanceof Observes)) continue;

                String className = ctx.getMethod().getDeclaringClass().getCanonicalName();
                String methodName = ctx.getMethod().getName();

                Class<?> clasz = paramterTypes[index];

                LOGGER.trace("{\"class\": \"{}\", \"method\": \"{}\", \"event\": \"{}\", \"type\": \"{}\"}",
                        className, methodName, parameters[index], clasz.getSimpleName());

                return ctx.proceed();
            }
        }

        return ctx.proceed();
    }
}
