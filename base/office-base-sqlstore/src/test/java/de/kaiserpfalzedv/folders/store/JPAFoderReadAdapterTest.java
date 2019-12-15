package de.kaiserpfalzedv.folders.store;

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
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@QuarkusTest
@Tag("integration")
public class JPAFoderReadAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFoderReadAdapterTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String SCOPE = "kes";
    private static final String KEY = "I'19-0001";
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
    public void shouldRetrieveEmptyOptionalIfStoreIsEmpty() {
        Optional<FolderSpec> result = readAdapter.loadById(UUID.randomUUID());
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    public void shouldRetrieveDataById() {
        Optional<FolderSpec> result = readAdapter.loadById(ID);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
        FolderSpec data = result.get();

        assert ID.equals(data.getUuid());
        assert SCOPE.equals(data.getScope().orElse(""));
        assert KEY.equals(data.getKey());
        assert NAME.equals(data.getName());
        assert SHORTNAME.equals(data.getShortName().orElse(null));
        assert DESCRIPTION.equals(data.getDescription().orElse(null));
        assert !data.getClosed().isPresent();
    }

    @Test
    public void shouldRetrieveDataWhenLoadingByScope() {
        Collection<FolderSpec> result = readAdapter.loadByScope(SCOPE);
        LOGGER.trace("result: {}", result);

        assert result.size() > 0;
    }

    @Test
    public void shouldRetrieveEmptySetWhenInvalidScopeIsQueried() {
        Collection<FolderSpec> result = readAdapter.loadByScope("empty");
        LOGGER.trace("result: {}", result);

        assert result.isEmpty();
    }

    @Test
    public void shouldRetrieveDataWhenLoadedByName() {
        Optional<FolderSpec> result = readAdapter.loadByName(SCOPE, KEY);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
    }

    @Test
    public void shouldRetrieveEmptyWhenInvalidNameIsGiven() {
        Optional<FolderSpec> result = readAdapter.loadByName("empty", KEY);
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    @Transactional
    public void shouldRetrieveALotOfFoldersWhenTheyExist() throws DataAlreadyExistsException {
        for (int i = 2; i < 50; i++) {
            storeAdapter.save(
                    ImmutableFolderSpec.copyOf(FOLDER)
                            .withUuid(UUID.randomUUID())
                            .withKey("I'19-00" + (i <= 9 ? "0" + i : i))
                            .withName(FOLDER.getName() + " Nr. " + i)
                            .withShortName(FOLDER.getShortName() + " Nr. " + i)
                            .withDescription(FOLDER.getDescription() + " (Nr. " + i + ")")
            );
        }

        Collection<FolderSpec> result = readAdapter.loadByScope(FOLDER.getScope().orElse("./."));
        long count = readAdapter.count();
        LOGGER.info("Loaded {} entries: {}", count, result);

        assert result.size() == count;
    }
}
