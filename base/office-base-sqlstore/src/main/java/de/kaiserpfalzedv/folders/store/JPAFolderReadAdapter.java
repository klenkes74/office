package de.kaiserpfalzedv.folders.store;

import de.kaiserpfalzedv.folders.FolderSpec;
import de.kaiserpfalzedv.folders.ImmutableFolderSpec;
import de.kaiserpfalzedv.folders.store.jpa.Folder;
import io.quarkus.arc.DefaultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@DefaultBean
@ApplicationScoped
public class JPAFolderReadAdapter implements FolderReadAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPAFolderReadAdapter.class);

    @Override
    public Optional<FolderSpec> loadById(UUID id) {
        Folder data = Folder.find("uuid", id).firstResult();

        if (data == null) {
            return Optional.empty();
        }

        return Optional.of(convertToSpec(data));
    }

    private FolderSpec convertToSpec(Folder data) {
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

        return Optional.of(convertToSpec(data));
    }

    @Override
    public Collection<FolderSpec> loadByScope(String scope) {
        List<Folder> data = Folder.list("scope", scope);
        ArrayList<FolderSpec> result = new ArrayList<>(data.size());

        data.forEach(e -> result.add(convertToSpec(e)));

        return result;
    }

    @Override
    public long count() {
        return Folder.count();
    }
}
