-- Hibernate Internal Objects
CREATE SEQUENCE PUBLIC.HIBERNATE_SEQUENCE START 1;
CREATE SEQUENCE BASE.HIBERNATE_SEQUENCE START 1;
CREATE SEQUENCE FOLDERS.HIBERNATE_SEQUENCE START 1;

-- Folder Data Table
CREATE TABLE FOLDERS.FOLDERS (
    id INTEGER PRIMARY KEY,

    _UUID CHAR(36) NOT NULL UNIQUE,
    _SCOPE VARCHAR(50) NOT NULL DEFAULT './.',
    _KEY VARCHAR(50) NOT NULL,

    _NAME VARCHAR(100) NOT NULL,
    _SHORTNAME VARCHAR(100),
    _DESCRIPTION VARCHAR(2048),
    _CLOSED TIMESTAMP,

    _CREATED TIMESTAMP NOT NULL,
    _MODIFIED TIMESTAMP NOT NULL
);

CREATE TABLE FOLDERS.FOLDERS_CHANGES (
    id INTEGER PRIMARY KEY,
    _ACTION VARCHAR(256) NOT NULL,

    _UUID CHAR(36) NOT NULL UNIQUE,
    _SCOPE VARCHAR(50) NOT NULL DEFAULT './.',
    _KEY VARCHAR(50) NOT NULL,

    -- FolderCreate
    _NAME VARCHAR(100),
    _SHORTNAME VARCHAR(100),
    _DESCRIPTION VARCHAR(2048),

    _CREATED TIMESTAMP NOT NULL
    -- No _MODIFIED since these entries are never modified. It's an audit log ...
)