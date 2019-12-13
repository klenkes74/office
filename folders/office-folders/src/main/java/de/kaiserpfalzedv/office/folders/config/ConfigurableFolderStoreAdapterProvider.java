package de.kaiserpfalzedv.office.folders.config;

import de.kaiserpfalzedv.office.folders.store.FolderStoreAdapter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@Dependent
public class ConfigurableFolderStoreAdapterProvider {
    @Inject
    @ConfigProperty(name = "office-folders.store")
    String storeClass;

    @Inject
    @Any
    Instance<FolderStoreAdapter> folderStoreAdapters;
    private FolderStoreAdapter folderStoreAdapter;

    @Produces
    public FolderStoreAdapter provideFolderStoreAdapter() {
        if (this.folderStoreAdapter == null) {
            Class wantedStoreClass = null;
            try {
                wantedStoreClass = getClass().getClassLoader().loadClass(storeClass);

                if (folderStoreAdapters.select(wantedStoreClass).isResolvable()) {
                    folderStoreAdapter = (FolderStoreAdapter) folderStoreAdapters.select(wantedStoreClass).iterator().next();
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Wrong configuration for office-folder.store. Can't find class '"
                        + wantedStoreClass + "'.");
            }
        }

        return folderStoreAdapter;
    }

}
