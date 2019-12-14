package de.kaiserpfalzedv.base.store;

import de.kaiserpfalzedv.base.api.BaseAPI;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.UUID;

public class DataAlreadyExistsExceptionTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataAlreadyExistsExceptionTest.class);

    private static final ObjectIdentifier IDENTIFIER = ImmutableObjectIdentifier.builder()
            .kind(DataAlreadyExistsExceptionTest.class.getCanonicalName())
            .version(BaseAPI.VERSION)

            .uuid(UUID.randomUUID())
            .scope("kes")
            .name("Object")
            .build();


    private DataAlreadyExistsException service;

    @BeforeEach
    public void setUpService() {
        service = new DataAlreadyExistsException(IDENTIFIER);
    }

    @Test
    public void shouldReturnCorrectIdentifier() {
        assert IDENTIFIER.equals(service.getIdentifier());
    }

    @Test
    public void shouldWorkWithThrowable() {
        IllegalArgumentException cause = new IllegalArgumentException("This is the cause!");

        DataAlreadyExistsException result = new DataAlreadyExistsException(IDENTIFIER, cause);

        assert cause.getMessage().equals(result.getCause().getMessage());
    }

    @BeforeAll
    static void redirectJavaUtilLogging() {
        if (!SLF4JBridgeHandler.isInstalled()) {
            SLF4JBridgeHandler.install();
            LOGGER.debug("SLF4Bridge for JUL installed ...");
        }
    }

    @AfterAll
    static void removeJavaUtilLogging() {
        if (SLF4JBridgeHandler.isInstalled()) {
            SLF4JBridgeHandler.uninstall();
            LOGGER.debug("SLF4JBridge for JUL deinstalled ...");
        }
    }
}
