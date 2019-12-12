package de.kaiserpfalzedv.office.folders.store;

import de.kaiserpfalzedv.base.ObjectIdentifier;

public class FolderAlreadyExistsException extends Exception {
    private final ObjectIdentifier identifier;

    public FolderAlreadyExistsException(final ObjectIdentifier identifier, final Throwable cause) {
        super("Object with matching identifier already exists: " + identifier, cause);

        this.identifier = identifier;
    }

    public ObjectIdentifier getIdentifier() {
        return identifier;
    }
}
