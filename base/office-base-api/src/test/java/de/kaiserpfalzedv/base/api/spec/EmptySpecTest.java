package de.kaiserpfalzedv.base.api.spec;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptySpecTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmptySpecTest.class);

    @Test
    public void shouldCreateAnEmptySpec() {
        EmptySpec<EmptySpec> service = new EmptySpec<>();

        assert service != null;
    }

    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started test for: {}", EmptySpec.class.getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended test for: {}", EmptySpec.class.getCanonicalName());
    }
}
