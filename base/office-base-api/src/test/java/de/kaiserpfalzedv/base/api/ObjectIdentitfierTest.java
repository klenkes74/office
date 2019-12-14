package de.kaiserpfalzedv.base.api;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 08:40
 */
public class ObjectIdentitfierTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectIdentitfierTest.class);

    private static final String KIND = ObjectIdentifier.class.getCanonicalName();
    private static final String VERSION = BaseAPI.VERSION;
    private static final UUID ID = UUID.randomUUID();

    private static final ObjectIdentifier SERVICE = new ObjectIdentifier() {
        @Override
        public String getKind() {
            return KIND;
        }

        @Override
        public UUID getUuid() {
            return ID;
        }

        @Override
        public Optional<String> getScope() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getName() {
            return Optional.empty();
        }
    };


    @Test
    public void shouldWorkWihenthBasicInformationIsGiven() {
        assert ID.equals(SERVICE.getUuid());
        assert KIND.equals(SERVICE.getKind());
        assert VERSION.equals(SERVICE.getVersion());
        assert !SERVICE.getName().isPresent();
        assert !SERVICE.getScope().isPresent();
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
