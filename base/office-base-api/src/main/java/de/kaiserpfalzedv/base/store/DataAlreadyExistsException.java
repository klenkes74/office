package de.kaiserpfalzedv.base.store;

import de.kaiserpfalzedv.base.api.ObjectIdentifier;

public class DataAlreadyExistsException extends Exception {
    private final ObjectIdentifier identifier;

    public DataAlreadyExistsException(final ObjectIdentifier identifier, final Throwable cause) {
        super("Object with matching identifier already exists: " + identifier, cause);

        this.identifier = identifier;
    }

    public DataAlreadyExistsException(final ObjectIdentifier identifier) {
        super("Object with matching identifier already exists: " + identifier);

        this.identifier = identifier;
    }

    public ObjectIdentifier getIdentifier() {
        return identifier;
    }
}
