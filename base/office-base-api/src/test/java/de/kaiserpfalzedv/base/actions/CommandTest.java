package de.kaiserpfalzedv.base.actions;

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

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 10:18
 */
public class CommandTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandTest.class);

    private static final Command<EmptySpec<Serializable>> SERVICE = new Command<EmptySpec<Serializable>>() {
        @Override
        public EmptySpec<Serializable> apply(EmptySpec<Serializable> orig) {
            return null;
        }

        @Override
        public Metadata getMetadata() {
            return null;
        }

        @Override
        public List<Status<EmptySpec<Serializable>>> getStatus() {
            return null;
        }
    };

    private static final CommandService<EmptySpec<Serializable>> COMMAND_SERVICE = command -> new Result<EmptySpec<Serializable>>() {
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
    public void shouldExecuteCorrectly() {
        Result<EmptySpec<Serializable>> result = SERVICE.execute(COMMAND_SERVICE);
        LOGGER.trace("result: {}", result);

        assert result != null;
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
