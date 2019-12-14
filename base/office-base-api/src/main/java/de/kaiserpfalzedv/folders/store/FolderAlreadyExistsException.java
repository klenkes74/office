package de.kaiserpfalzedv.folders.store;

import de.kaiserpfalzedv.base.api.ObjectIdentifier;

public class FolderAlreadyExistsException extends Exception {
    private final ObjectIdentifier identifier;


    public FolderAlreadyExistsException(final ObjectIdentifier identifier) {
        this.identifier = identifier;
    }

    public FolderAlreadyExistsException(final ObjectIdentifier identifier, final Throwable cause) {
        super("Object with matching identifier already exists: " + identifier, cause);

        this.identifier = identifier;
    }

    public ObjectIdentifier getIdentifier() {
        return identifier;
    }
}
