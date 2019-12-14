package de.kaiserpfalzedv.base.api.status;

import de.kaiserpfalzedv.base.api.spec.EmptySpec;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 08:57
 */
public class StatusTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusTest.class);

    private static final Status<EmptySpec<Serializable>> SERVICE = (Status<EmptySpec<Serializable>>) () -> null;

    @Test
    public void shouldReturnCorrectMessage() {
        assert SERVICE.DEFAULT_MESSAGE.equals(SERVICE.getMessage().orElse(null));
    }

    @Test
    public void shouldReturnCorrectValue() {
        assert SERVICE.DEFAULT_VALUE.equals(SERVICE.getValue().orElse(null));
    }

    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", SERVICE.getClass().getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", SERVICE.getClass().getCanonicalName());
    }
}
