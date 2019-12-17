-- Hibernate Internal Objects
CREATE SEQUENCE PUBLIC.HIBERNATE_SEQUENCE START 1;

-- JPAFolderSpec Data Table
CREATE TABLE FOLDERS.FOLDERS
(
    id           INTEGER PRIMARY KEY,

    _UUID        CHAR(36)     NOT NULL UNIQUE,
    _SCOPE       VARCHAR(256) NOT NULL DEFAULT './.',
    _KEY         VARCHAR(50)  NOT NULL,

    _NAME        VARCHAR(100) NOT NULL,
    _SHORTNAME   VARCHAR(100),
    _DESCRIPTION VARCHAR(2048),
    _CLOSED      TIMESTAMP,

    _CREATED     TIMESTAMP    NOT NULL,
    _MODIFIED    TIMESTAMP    NOT NULL
);

CREATE TABLE FOLDERS.FOLDERS_CHANGES
(
    id                         INTEGER PRIMARY KEY,
    _ACTION                    VARCHAR(256) NOT NULL,

    _COMMAND_UUID              char(36)     NOT NULL UNIQUE,
    _COMMAND_SCOPE             VARCHAR(256) NOT NULL DEFAULT './.',
    _COMMAND_KEY               VARCHAR(50),

    _WORKFLOW_KIND             VARCHAR(256) NOT NULL DEFAULT 'UNDEFINED',
    _WORKFLOW_VERSION          VARCHAR(20)  NOT NULL DEFAULT '1.0.0',
    _WORKFLOW_REQUEST          char(36),
    _WORKFLOW_CORRELATION      char(36),
    _WORKFLOW_SEQUENCE         INTEGER,
    _WORKFLOW_DEFINITION_UUID  char(36),
    _WORKFLOW_DEFINITION_SCOPE VARCHAR(256) NOT NULL DEFAULT './,',
    _WORKFLOW_DEFINITION_KEY   VARCHAR(50),

    _UUID                      CHAR(36)     NOT NULL UNIQUE,
    _SCOPE                     VARCHAR(256) NOT NULL DEFAULT './.',
    _KEY                       VARCHAR(50)  NOT NULL,

    -- JPAFolderCreate
    _NAME                      VARCHAR(100),
    _SHORTNAME                 VARCHAR(100),
    _DESCRIPTION               VARCHAR(2048),

    _CREATED                   TIMESTAMP    NOT NULL
    -- No _MODIFIED since these entries are never modified. It's an audit log ...
)