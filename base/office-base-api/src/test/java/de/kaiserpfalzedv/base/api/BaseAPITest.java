package de.kaiserpfalzedv.base.api;

import de.kaiserpfalzedv.base.api.spec.EmptySpec;
import de.kaiserpfalzedv.base.api.status.Status;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 08:57
 */
public class BaseAPITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseAPITest.class);

    private static final BaseAPI<EmptySpec<Serializable>> SERVICE = new BaseAPI<EmptySpec<Serializable>>() {
        @Override
        public Metadata getMetadata() {
            return null;
        }

        @Override
        public List<Status<EmptySpec<Serializable>>> getStatus() {
            return null;
        }
    };

    @Test
    public void shouldReturnCorrectKindOfBaseAPI() {
        assert BaseAPI.KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfBaseAPI() {
        assert BaseAPI.VERSION.equals(SERVICE.getVersion());
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
