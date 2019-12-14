package de.kaiserpfalzedv.base.store;

import de.kaiserpfalzedv.folders.FolderSpec;
import de.kaiserpfalzedv.folders.ImmutableFolderSpec;
import de.kaiserpfalzedv.folders.store.FolderStoreAdapter;
import io.quarkus.arc.DefaultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.*;

@DefaultBean
@ApplicationScoped
public class JPAFolderStoreAdapter implements FolderStoreAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderStoreAdapter.class);

    @Override
    public Optional<FolderSpec> loadById(UUID id) {
        Folder data = Folder.find("uuid", id).firstResult();

        if (data == null) {
            return Optional.empty();
        }

        return Optional.of(convert(data));
    }

    private FolderSpec convert(Folder data) {
        return ImmutableFolderSpec.builder()
                .uuid(data.uuid)
                .scope(Optional.ofNullable(data.scope))
                .key(data.key)
                .name(data.name)
                .shortName(data.shortName)
                .description(Optional.ofNullable(data.description))
                .closed(Optional.ofNullable(data.closed))
                .created(data.created)
                .modified(data.modified)
                .build();
    }

    @Override
    public Optional<FolderSpec> loadByName(String scope, String name) {
        Folder data = Folder.find("scope = ?1 and key = ?2", scope, name).firstResult();

        if (data == null) {
            return Optional.empty();
        }

        return Optional.of(convert(data));
    }

    @Override
    public Collection<FolderSpec> loadByScope(String scope) {
        List<Folder> data = Folder.list("scope", scope);
        ArrayList<FolderSpec> result = new ArrayList<>(data.size());

        data.forEach(e -> result.add(convert(e)));

        return result;
    }

    @Override
    @Transactional
    public FolderSpec save(FolderSpec data) throws DataAlreadyExistsException {
        LOGGER.info("Saving folder: spec={}", data);

        if (Folder.find("uuid", data.getUuid()).count() != 0) {
            throw new DataAlreadyExistsException(data.getIdentity());
        }


        Folder save = convert(data);
        save.persistAndFlush();
        LOGGER.trace("data: {}", save);

        return convert(save);
    }

    private Folder convert(FolderSpec data) {
        Folder result = new Folder();

        if (data != null) {
            result.uuid = data.getUuid();
            result.scope = data.getScope().orElse(null);
            result.key = data.getKey();
            result.name = data.getName();
            result.shortName = data.getShortName().orElse(null);
            result.description = data.getDescription().orElse(null);
            result.closed = data.getClosed().orElse(null);
            result.created = data.getCreated();
            result.modified = data.getModified();
        }

        return result;
    }

    @Override
    public void close(UUID id) {
        Folder data = Folder.find("uuid", id).firstResult();

        if (data != null) {
            data.closed = OffsetDateTime.now();
            data.persistAndFlush();
        }
    }

    @Override
    public long count() {
        return Folder.count();
    }
}