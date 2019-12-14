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
public class ObjectListTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectListTest.class);

    private static final ObjectList<SingleObject<EmptySpec<Serializable>>> SERVICE = new ObjectList<SingleObject<EmptySpec<Serializable>>>() {
        @Override
        public List<? extends SingleObject<EmptySpec<Serializable>>> getSpec() {
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

    @Test
    public void shouldReturnCorrectKindOfObjectList() {
        assert ObjectList.KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectScopeOfObjectList() {
        assert ObjectList.class.getPackage().getName().equals(SERVICE.getScope().orElse(null));
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
