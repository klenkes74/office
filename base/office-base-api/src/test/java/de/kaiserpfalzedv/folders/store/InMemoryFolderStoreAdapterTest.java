package de.kaiserpfalzedv.folders.store;

import de.kaiserpfalzedv.base.store.DataAlreadyExistsException;
import de.kaiserpfalzedv.folders.FolderSpec;
import de.kaiserpfalzedv.folders.ImmutableFolderSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

public class InMemoryFolderStoreAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryFolderStoreAdapterTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String SCOPE = "kes";
    private static final String KEY = "I'19-0001";
    private static final String NAME = "Testakte Softwaretest";
    private static final String SHORTNAME = "Softwaretest";
    private static final String DESCRIPTION = "Eine einfache Akte für Softwaretests";
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

    private FolderStoreAdapter storeAdapter;
    private FolderReadAdapter readAdapter;

    @BeforeEach
    public void generateMockService() throws DataAlreadyExistsException {
        InMemoryFolderStoreAdapter adapter = new InMemoryFolderStoreAdapter();
        storeAdapter = adapter;
        readAdapter = adapter;

        LOGGER.info("Setup for test: service={}, folder={}", readAdapter, FOLDER);
        storeAdapter.save(FOLDER);
    }

    @Test
    public void shouldReturnACountAbove1() {
        long result = readAdapter.count();
        LOGGER.trace("result: {}", result);

        assert result >= 1;
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
    public void shouldSetCloseDateWhenFolderIsClosed() {
        storeAdapter.close(ID);

        Optional<FolderSpec> result = readAdapter.loadById(ID);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
        assert result.get().getClosed().isPresent();
    }

    @Test
    public void shouldRejectDoubleUUID() {
        try {
            storeAdapter.save(FOLDER);

            fail("An exception of type '" + DataAlreadyExistsException.class.getSimpleName()
                    + "' should have been thrown due to doublette UUID.");
        } catch (DataAlreadyExistsException e) {
            assert e.getIdentifier().getUuid().equals(ID);
        }
    }


    @Test
    public void shouldRejectDoubleScopeAndKey() {
        try {
            storeAdapter.save(ImmutableFolderSpec.copyOf(FOLDER).withUuid(UUID.randomUUID()));

            fail("An exception of type '" + DataAlreadyExistsException.class.getSimpleName()
                    + "' should have been thrown due to doublette UUID.");
        } catch (DataAlreadyExistsException e) {
            assert e.getIdentifier().getUuid().equals(ID);
        }
    }
}
