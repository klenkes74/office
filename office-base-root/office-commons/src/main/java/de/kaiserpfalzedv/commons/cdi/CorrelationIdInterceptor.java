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

package de.kaiserpfalzedv.commons.cdi;

import de.kaiserpfalzedv.commons.BaseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.UUID;

/*
 * The following interceptor is inspired by the GateKeeper interceptor of Dewald Pretorios.
 *
 * @author Dewald Pretorios (project Guardian, licensed as LGPL 3
 * @author rlichti
 * @since 2019-12-18 22:56
 */
@Priority(300)
@CorrelationLogged
@Interceptor
public class CorrelationIdInterceptor implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CorrelationIdInterceptor.class);

    public static final String MDC_CORRELATION = "correlation";
    public static final String MDC_REQUEST = "request";
    public static final String MDC_SEQUENCE = "sequence";


    @AroundInvoke
    public Object addCorrelationIdToLoggingContext(InvocationContext ctx) throws Exception {
        Object[] parameters = ctx.getParameters();
        Class<?>[] parameterTypes = ctx.getMethod().getParameterTypes();

        for (int index = 0; index < parameters.length; index++) {
            if (parameters[index] == null) continue;

            if (BaseObject.class.isAssignableFrom((parameterTypes[index]))) {
                @SuppressWarnings("rawtypes") BaseObject param = (BaseObject) parameters[index];

                if (param.getMetadata().getWorkflowdata().isPresent()) {
                    String correlation = param.getMetadata().getWorkflowdata().get().getCorrelation().toString();
                    String request = param.getMetadata().getWorkflowdata().get().getRequest().toString();
                    String sequence = param.getMetadata().getWorkflowdata().get().getSequence().orElse(0L).toString();

                    return invokeMethodWithCorrelationInLoggingContext(ctx, correlation, request, sequence, "Extracted correlation/request/sequence id of the call: {}/{}/{}");
                }
            }
        }

        String correlation = UUID.randomUUID().toString();
        String sequence = "0";
        return invokeMethodWithCorrelationInLoggingContext(ctx, correlation, correlation, sequence, "Created correlation/request/sequence id for call: {}/{}/{}");
    }

    public Object invokeMethodWithCorrelationInLoggingContext(
            InvocationContext ctx,
            String correlation,
            String request,
            String sequence,
            String s
    ) throws Exception {
        MDC.put(MDC_CORRELATION, correlation);
        MDC.put(MDC_REQUEST, request);
        MDC.put(MDC_SEQUENCE, sequence);
        LOGGER.trace(s, correlation, request, sequence);

        try {
            return ctx.proceed();
        } finally {
            MDC.remove(MDC_CORRELATION);
        }
    }
}
