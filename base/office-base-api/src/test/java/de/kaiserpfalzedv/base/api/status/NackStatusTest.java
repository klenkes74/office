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
public class NackStatusTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NackStatusTest.class);

    private static final NackStatus<EmptySpec<Serializable>> SERVICE = new NackStatus<EmptySpec<Serializable>>() {
    };

    @Test
    public void shouldReturnCorrectTypeOfStatus() {
        assert SERVICE.TYPE.equals(SERVICE.getType());
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
