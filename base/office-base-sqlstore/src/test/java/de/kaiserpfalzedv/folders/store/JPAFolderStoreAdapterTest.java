package de.kaiserpfalzedv.folders.store;

import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.base.store.DataAlreadyExistsException;
import de.kaiserpfalzedv.folders.FolderSpec;
import de.kaiserpfalzedv.folders.ImmutableFolderSpec;
import de.kaiserpfalzedv.folders.store.jpa.Folder;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

@QuarkusTest
@Tag("integration")
public class JPAFolderStoreAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderStoreAdapterTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String SCOPE = "kes";
    private static final String KEY = "I'19-0000";
    private static final String NAME = "Softwaretest Akte";
    private static final String SHORTNAME = "Softwaretest";
    private static final String DESCRIPTION = "Einfache Akte für Softwaretests";
    private static final OffsetDateTime CREATED = OffsetDateTime.of(2019, 12, 11, 12, 0, 0, 0, ZoneOffset.ofHours(1));
    private static final OffsetDateTime MODIFIED = CREATED;
    private static final FolderSpec FOLDER = ImmutableFolderSpec.builder()
            .uuid(ID)
            .scope(SCOPE)
            .key(KEY)
            .name(NAME)
            .shortName(SHORTNAME)
            .description(DESCRIPTION)
            .created(CREATED)
            .modified(MODIFIED)
            .build();

    private final FolderReadAdapter readAdapter = new JPAFolderReadAdapter();
    private final FolderStoreAdapter storeAdapter = new JPAFolderStoreAdapter();

    @BeforeEach
    @Transactional
    public void generateMockService() throws DataAlreadyExistsException {
        try {
            storeAdapter.save(FOLDER);
        } catch (DataAlreadyExistsException e) {
            // ignore, we want the data to be there and there it is ...
        }

        assert Folder.count("uuid", FOLDER.getUuid()) == 1;
    }


    @Test
    public void shouldFailWhenUuidIsADoublette() {
        try {
            storeAdapter.save(FOLDER);

            fail("The UUID is a doublette. The save should have failed!");
        } catch (DataAlreadyExistsException ex) {
            assert ex.getIdentifier().getUuid().equals(FOLDER.getUuid());
        }
    }


    @Test
    public void shouldFailWhenScopeAndKeyIsADoublette() {
        try {
            storeAdapter.save(ImmutableFolderSpec.copyOf(FOLDER).withUuid(UUID.randomUUID()));

            fail("The UUID is a doublette. The save should have failed!");
        } catch (DataAlreadyExistsException ex) {
            ObjectIdentifier identifier = ex.getIdentifier();
            assert identifier.getScope().equals(FOLDER.getIdentity().getScope());
            assert identifier.getName().equals(FOLDER.getIdentity().getName());
        }
    }

    @Test
    @Transactional
    public void shouldSetCloseDateWhenFolderIsClosed() {
        storeAdapter.close(ID);

        Optional<FolderSpec> result = readAdapter.loadById(ID);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
        assert result.get().getClosed().isPresent();
    }
}
