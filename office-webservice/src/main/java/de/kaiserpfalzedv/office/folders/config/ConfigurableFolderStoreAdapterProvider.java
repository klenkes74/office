package de.kaiserpfalzedv.office.folders.config;

import de.kaiserpfalzedv.folders.store.FolderReadAdapter;
import de.kaiserpfalzedv.folders.store.FolderStoreAdapter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@Dependent
public class ConfigurableFolderStoreAdapterProvider {
    @Inject
    @ConfigProperty(name = "office-folders.store.write-adapter")
    String storeWriteClass;

    @Inject
    @ConfigProperty(name = "office-folders.store.read-adapter")
    String storeReadClass;

    @Inject
    @Any
    Instance<FolderStoreAdapter> folderStoreAdapters;
    private FolderStoreAdapter folderStoreAdapter;

    @Inject
    @Any
    Instance<FolderReadAdapter> folderReadAdapters;
    private FolderReadAdapter folderReadAdapter;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Produces
    public FolderStoreAdapter provideFolderStoreAdapter() {
        if (this.folderStoreAdapter == null) {
            Class wantedStoreClass = null;
            try {
                wantedStoreClass = getClass().getClassLoader().loadClass(storeWriteClass);

                if (folderStoreAdapters.select(wantedStoreClass).isResolvable()) {
                    folderStoreAdapter = (FolderStoreAdapter) folderStoreAdapters.select(wantedStoreClass).iterator().next();
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Wrong configuration for office-folder.store.write-adapter. Can't find class '"
                        + wantedStoreClass + "'.");
            }
        }

        return folderStoreAdapter;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    @Produces
    public FolderReadAdapter provideFolderReadAdapter() {
        if (this.folderReadAdapter == null) {
            Class wantedStoreClass = null;
            try {
                wantedStoreClass = getClass().getClassLoader().loadClass(storeReadClass);

                if (folderReadAdapters.select(wantedStoreClass).isResolvable()) {
                    folderReadAdapter = (FolderReadAdapter) folderReadAdapters.select(wantedStoreClass).iterator().next();
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Wrong configuration for office-folder.store.read-adapter. Can't find class '"
                        + wantedStoreClass + "'.");
            }
        }

        return folderReadAdapter;
    }
}
