package de.kaiserpfalzedv.base.store;

import de.kaiserpfalzedv.folders.FolderSpec;
import de.kaiserpfalzedv.folders.ImmutableFolderSpec;
import de.kaiserpfalzedv.folders.store.FolderStoreAdapter;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
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
public class JPAFolderStoreAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderStoreAdapterTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String SCOPE = "kes";
    private static final String KEY = "I'19-0001";
    private static final String NAME = "Adoption Lichti ./. Hellwig";
    private static final String SHORTNAME = "Lichti ./. Hellwig";
    private static final String DESCRIPTION = "Adoptionsverfahren Alexandra Maria Lichti";
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

    private FolderStoreAdapter service;

    @BeforeEach
    @Transactional
    public void generateMockService() throws DataAlreadyExistsException {
        service = new JPAFolderStoreAdapter();

        if (Folder.find("uuid", FOLDER.getUuid()).count() == 0) {
            service.save(FOLDER);
        }

        assert Folder.count("uuid", FOLDER.getUuid()) == 1;
    }

    @Test
    public void shouldRetrieveEmptyOptionalIfStoreIsEmpty() {
        Optional<FolderSpec> result = service.loadById(UUID.randomUUID());
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    public void shouldRetrieveDataById() {
        Optional<FolderSpec> result = service.loadById(ID);
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
        Collection<FolderSpec> result = service.loadByScope(SCOPE);
        LOGGER.trace("result: {}", result);

        assert result.size() > 0;
    }

    @Test
    public void shouldRetrieveEmptySetWhenInvalidScopeIsQueried() {
        Collection<FolderSpec> result = service.loadByScope("empty");
        LOGGER.trace("result: {}", result);

        assert result.isEmpty();
    }

    @Test
    public void shouldRetrieveDataWhenLoadedByName() {
        Optional<FolderSpec> result = service.loadByName(SCOPE, KEY);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
    }

    @Test
    public void shouldRetrieveEmptyWhenInvalidNameIsGiven() {
        Optional<FolderSpec> result = service.loadByName("empty", KEY);
        LOGGER.trace("result: {}", result);

        assert !result.isPresent();
    }

    @Test
    @Transactional
    public void shouldSetCloseDateWhenFolderIsClosed() {
        service.close(ID);

        Optional<FolderSpec> result = service.loadById(ID);
        LOGGER.trace("result: {}", result);

        assert result.isPresent();
        assert result.get().getClosed().isPresent();
    }

    @Test
    @Transactional
    public void shouldRetrieveALotOfFoldersWhenTheyExist() throws DataAlreadyExistsException {
        for (int i = 10; i < 50; i++) {
            service.save(
                    ImmutableFolderSpec.copyOf(FOLDER)
                            .withUuid(UUID.randomUUID())
                            .withKey("I'19-00" + i)
                            .withName(FOLDER.getName() + " Nr. " + i)
                            .withShortName(FOLDER.getShortName() + " Nr. " + i)
                            .withDescription(FOLDER.getDescription() + " (Nr. " + i + ")")
            );
        }

        Collection<FolderSpec> result = service.loadByScope(FOLDER.getScope().orElse("./."));
        LOGGER.info("Loaded: {}", result);

        assert result.size() == Folder.count();
    }
}