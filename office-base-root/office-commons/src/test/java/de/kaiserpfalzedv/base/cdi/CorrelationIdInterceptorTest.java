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

import de.kaiserpfalzedv.commons.BaseObject;
import de.kaiserpfalzedv.commons.api.ImmutableMetadata;
import de.kaiserpfalzedv.commons.api.ImmutableObjectIdentity;
import de.kaiserpfalzedv.commons.api.ImmutableWorkflowData;
import de.kaiserpfalzedv.commons.api.Metadata;
import de.kaiserpfalzedv.commons.cdi.CorrelationLogged;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-21 08:43
 */
@QuarkusTest
@Tag("integration")
public class CorrelationIdInterceptorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoggedInteceptorTest.class);

    @Inject
    TestedClassForCorrelationIdInterceptor intercepted;

    @Test
    public void shouldCreateACorrelationIdWhenNoParameterIsGiven() {
        intercepted.callWithoutParameter();
    }

    @Test
    public void shouldCreateACorrelationIdWhenABaseObjectWithoutWorkflowdataIsGiven() {
        BaseObject<String> event = new BaseObject<String>() {
            @Override
            public String getKind() {
                return "kind";
            }

            @Override
            public String getVersion() {
                return "1.0.0";
            }

            @Override
            public Metadata getMetadata() {
                return ImmutableMetadata.builder()
                        .identity(ImmutableObjectIdentity.builder()
                                .kind("kind")
                                .version("1.0.0")
                                .uuid(UUID.randomUUID())
                                .tenant("tenant")
                                .name("key")
                                .build()
                        )
                        .build();
            }
        };

        intercepted.callWithBaseObject(event);
    }


    @Test
    public void shouldGetAnUnsupportedOperationExceptionWhenCallingTheCorrectMethod() {
        try {
            intercepted.callWithException();

            fail("Should have received an UnsuportedOperationException!");
        } catch (UnsupportedOperationException e) {
            // every thing is fine. That's what we wanted.
        }
    }


    @Test
    public void shouldReadTheCorrelationIdWhenABaseObjectWithWorkflowdataIsGiven() {
        UUID correlationId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();
        Long sequence = 23L;

        BaseObject<String> event = new BaseObject<String>() {
            @Override
            public String getKind() {
                return "kind";
            }

            @Override
            public String getVersion() {
                return "1.0.0";
            }

            @Override
            public Metadata getMetadata() {
                return ImmutableMetadata.builder()
                        .identity(ImmutableObjectIdentity.builder()
                                .kind("kind")
                                .version("1.0.0")
                                .uuid(UUID.randomUUID())
                                .tenant("tenant")
                                .name("key")
                                .build()
                        )
                        .workflowdata(ImmutableWorkflowData.builder()
                                .definition(ImmutableObjectIdentity.builder()
                                        .kind("wf")
                                        .version("1.0.0")
                                        .uuid(UUID.randomUUID())
                                        .tenant("de.kaiserpfalz-edv")
                                        .name("test-correlation-id-cdi-interceptor")
                                        .build()
                                )
                                .correlation(correlationId)
                                .request(requestId)
                                .sequence(sequence)
                                .timestamp(OffsetDateTime.now())
                                .build()
                        )
                        .build();
            }
        };

        LOGGER.debug("Created event with correlation id: {}", correlationId);

        intercepted.callWithBaseObject(event);
    }
}

@SuppressWarnings("unused")
@Dependent
class TestedClassForCorrelationIdInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoggedInteceptorTest.class);

    @CorrelationLogged
    void callWithoutParameter() {
        String correlationId = MDC.get("correlation");

        if (correlationId == null) {
            throw new IllegalStateException("No correlation id is set!");
        }

        LOGGER.info("CorrelationId is: {}", correlationId);
    }

    @CorrelationLogged
    void callWithException() {
        throw new UnsupportedOperationException();
    }

    @CorrelationLogged
    void callWithBaseObject(final BaseObject<String> object) {
        String correlationId = MDC.get("correlation");
        String requestId = MDC.get("request");
        String sequence = MDC.get("sequence");

        if (correlationId == null) {
            throw new IllegalStateException("No correlation id is set!");
        }

        if (object.getMetadata().getWorkflowdata().isPresent()) {
            String workflowCorrelationId = object.getMetadata().getWorkflowdata().get()
                    .getCorrelation().toString();
            if (!correlationId.equals(workflowCorrelationId)) {
                throw new IllegalStateException("Wrong correlation id is set: " + correlationId + " != "
                        + workflowCorrelationId);
            }

            String workflowRequestId = object.getMetadata().getWorkflowdata().get()
                    .getRequest().toString();
            if (!requestId.equals(workflowRequestId)) {
                throw new IllegalStateException("Wrong request id is set: " + requestId + " != "
                        + workflowRequestId);
            }

            String workflowSequence = object.getMetadata().getWorkflowdata().get()
                    .getSequence().orElse(0L).toString();
            if (!sequence.equals(workflowSequence)) {
                throw new IllegalStateException("Wrong sequence is set: " + sequence + " != "
                        + workflowCorrelationId);
            }
        }

        LOGGER.info("CorrelationId/RequestId/Sequence is: {}/{}/{}", correlationId, requestId, sequence);
    }

}