package de.kaiserpfalzedv.folders;

import de.kaiserpfalzedv.base.api.Metadata;
import de.kaiserpfalzedv.base.api.spec.EmptySpec;
import de.kaiserpfalzedv.base.api.status.Status;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 10:42
 */
public class FolderCreatedTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderCreatedTest.class);

    private static final FolderCreated SERVICE = new FolderCreated() {
        @Override
        public Metadata getMetadata() {
            return null;
        }

        @Override
        public List<Status<EmptySpec<Serializable>>> getStatus() {
            return null;
        }

        @Override
        public Optional<FolderSpec> getSpec() {
            return Optional.empty();
        }
    };

    @Test
    public void shouldReturnCorrectKindOfFolder() {
        assert FolderCreated.FOLDER_CREATED_KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfFolder() {
        assert FolderCreated.FOLDER_CREATED_VERSION.equals(SERVICE.getVersion());
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
