package de.kaiserpfalzedv.folders.api;

import de.kaiserpfalzedv.base.api.Metadata;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.api.WorkflowData;
import de.kaiserpfalzedv.base.api.spec.EmptySpec;
import de.kaiserpfalzedv.base.api.status.Status;
import de.kaiserpfalzedv.folders.FolderSpec;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 10:42
 */
public class FolderCommandTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderCommandTest.class);

    private static final FolderCommand SERVICE = new FolderCommand() {
        @Override
        public Metadata getMetadata() {
            return new Metadata() {
                @Override
                public ObjectIdentifier getIdentity() {
                    return new ObjectIdentifier() {
                        @Override
                        public String getKind() {
                            return "kind";
                        }

                        @Override
                        public UUID getUuid() {
                            return UUID.randomUUID();
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
                }

                @Override
                public Optional<ObjectIdentifier> getOwner() {
                    return Optional.empty();
                }

                @Override
                public Optional<WorkflowData> getWorkflowData() {
                    return Optional.empty();
                }

                @Override
                public OffsetDateTime getCreated() {
                    return OffsetDateTime.now();
                }

                @Override
                public Optional<OffsetDateTime> getModified() {
                    return Optional.empty();
                }

                @Override
                public Optional<OffsetDateTime> getInvalidAfter() {
                    return Optional.empty();
                }
            };
        }

        @Override
        public List<Status<EmptySpec<Serializable>>> getStatus() {
            return new ArrayList<>();
        }

        @Override
        public Optional<FolderSpec> getSpec() {
            return Optional.empty();
        }

        @Override
        public FolderSpec apply(FolderSpec orig) {
            return orig;
        }
    };

    @Test
    public void shouldReturnCorrectKindOfFolder() {
        assert FolderCommand.FOLDER_COMMAND_KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfFolder() {
        assert FolderCommand.FOLDER_COMMAND_VERSION.equals(SERVICE.getVersion());
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
