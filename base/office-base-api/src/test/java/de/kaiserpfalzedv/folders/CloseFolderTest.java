package de.kaiserpfalzedv.folders;

import de.kaiserpfalzedv.base.api.Metadata;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.api.WorkflowData;
import de.kaiserpfalzedv.base.api.spec.EmptySpec;
import de.kaiserpfalzedv.base.api.status.Status;
import de.kaiserpfalzedv.folders.api.FolderCommandService;
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
public class CloseFolderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseFolderTest.class);

    private static final CloseFolder SERVICE = new CloseFolder() {
        @Override
        public Metadata getMetadata() {
            return new Metadata() {
                @Override
                public ObjectIdentifier getIdentity() {
                    return new ObjectIdentifier() {
                        @Override
                        public String getKind() {
                            return "KIND";
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
            return Optional.of(new FolderSpec() {
                @Override
                public UUID getUuid() {
                    return UUID.randomUUID();
                }

                @Override
                public Optional<String> getScope() {
                    return Optional.empty();
                }

                @Override
                public String getKey() {
                    return "OLD";
                }

                @Override
                public String getName() {
                    return "OLD";
                }

                @Override
                public Optional<String> getShortName() {
                    return Optional.empty();
                }

                @Override
                public Optional<String> getDescription() {
                    return Optional.empty();
                }

                @Override
                public Optional<OffsetDateTime> getClosed() {
                    return Optional.empty();
                }

                @Override
                public OffsetDateTime getCreated() {
                    return OffsetDateTime.now();
                }

                @Override
                public OffsetDateTime getModified() {
                    return OffsetDateTime.now();
                }
            });
        }
    };

    private static final FolderCommandService FOLDER_COMMAND_SERVICE = command -> new FolderClosed() {
        @Override
        public Metadata getMetadata() {
            return new Metadata() {
                @Override
                public ObjectIdentifier getIdentity() {
                    return new ObjectIdentifier() {
                        @Override
                        public String getKind() {
                            return "KIND";
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
    };

    private static final FolderSpec FOLDER = new FolderSpec() {
        @Override
        public UUID getUuid() {
            return UUID.randomUUID();
        }

        @Override
        public Optional<String> getScope() {
            return Optional.empty();
        }

        @Override
        public String getKey() {
            return "OLD";
        }

        @Override
        public String getName() {
            return "OLD";
        }

        @Override
        public Optional<String> getShortName() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getDescription() {
            return Optional.empty();
        }

        @Override
        public Optional<OffsetDateTime> getClosed() {
            return Optional.of(OffsetDateTime.now());
        }

        @Override
        public OffsetDateTime getCreated() {
            return OffsetDateTime.now();
        }

        @Override
        public OffsetDateTime getModified() {
            return OffsetDateTime.now();
        }
    };

    @Test
    public void shouldReturnCorrectKindOfFolder() {
        assert CloseFolder.KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfFolder() {
        assert CloseFolder.VERSION.equals(SERVICE.getVersion());
    }

    @Test
    public void shouldExecuteTheCommandCorrectly() {
        assert SERVICE.execute(FOLDER_COMMAND_SERVICE) != null;
    }

    @Test
    public void shouldApplyTheCommandCorrectly() {
        assert "KEY".equals(SERVICE.apply(ImmutableFolderSpec.copyOf(FOLDER).withKey("KEY")).getKey());
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
