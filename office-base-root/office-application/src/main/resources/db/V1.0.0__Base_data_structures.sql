-- Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2019. All rights reserved.
--
--  This file is part of Kaiserpfalz EDV-Service Office.
--
--  This is free software: you can redistribute it and/or modify it under the terms of
--  the GNU Lesser General Public License as published by the Free Software
--  Foundation, either version 3 of the License.
--
--  This file is distributed in the hope that it will be useful, but WITHOUT ANY
--  WARRANTY; without even the implied warranty of MERCHANTABILITY or
--  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
--  License for more details.
--
--  You should have received a copy of the GNU Lesser General Public License along
--  with this file. If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.

-- Hibernate Internal Objects
CREATE SEQUENCE PUBLIC.HIBERNATE_SEQUENCE START 1;

-- JPAFolderSpec Data Table
CREATE TABLE BASE.FOLDERS
(
    id           INTEGER PRIMARY KEY,

    _UUID        CHAR(36)     NOT NULL UNIQUE,
    _TENANT      VARCHAR(256) NOT NULL DEFAULT './.',
    _KEY         VARCHAR(50),

    _NAME        VARCHAR(100) NOT NULL,
    _DISPLAYNAME VARCHAR(100),
    _DESCRIPTION VARCHAR(2048),
    _CLOSED      TIMESTAMP,

    _CREATED     TIMESTAMP    NOT NULL,
    _MODIFIED    TIMESTAMP    NOT NULL
);

CREATE TABLE BASE.FOLDERS_CHANGES
(
    id                          INTEGER PRIMARY KEY,
    _ACTION                     VARCHAR(256) NOT NULL,

    _COMMAND_UUID               char(36)     NOT NULL,
    _COMMAND_TENANT             VARCHAR(256) NOT NULL DEFAULT './.',
    _COMMAND_KEY                VARCHAR(50),
    _COMMAND_CREATED            TIMESTAMP    NOT NULL,

    _WORKFLOW_KIND              VARCHAR(256),
    _WORKFLOW_VERSION           VARCHAR(20),
    _WORKFLOW_REQUEST           char(36) UNIQUE,
    _WORKFLOW_CORRELATION       char(36),
    _WORKFLOW_SEQUENCE          INTEGER,
    _WORKFLOW_TIMESTAMP         TIMESTAMP,
    _WORKFLOW_DEFINITION_UUID   char(36),
    _WORKFLOW_DEFINITION_TENANT VARCHAR(256),
    _WORKFLOW_DEFINITION_KEY    VARCHAR(50),

    _UUID                       CHAR(36)     NOT NULL,
    _TENANT                     VARCHAR(256) NOT NULL DEFAULT './.',
    _KEY                        VARCHAR(50),

    -- JPAFolderCreate
    _NAME                       VARCHAR(100),
    _DISPLAYNAME                VARCHAR(100),
    _DESCRIPTION                VARCHAR(2048),
    _CLOSED                     TIMESTAMP,

    _CREATED                    TIMESTAMP,
    _MODIFIED                   TIMESTAMP
);


-- Base Person, should be empty
CREATE TABLE BASE.PERSONS
(
    id           INTEGER PRIMARY KEY,

    _UUID        CHAR(36)     NOT NULL UNIQUE,
    _TENANT      VARCHAR(256) NOT NULL DEFAULT './.',
    _KEY         VARCHAR(50),

    _DISPLAYNAME VARCHAR(100) NOT NULL,
    _CREATED     TIMESTAMP    NOT NULL,
    _MODIFIED    TIMESTAMP    NOT NULL
);

-- JPAContacts Data Table
CREATE TABLE BASE.NATURAL_PERSONS
(
    id                       INTEGER PRIMARY KEY,

    _UUID                    CHAR(36)     NOT NULL UNIQUE,
    _TENANT                  VARCHAR(256) NOT NULL DEFAULT './.',
    _KEY                     VARCHAR(50),

    _DISPLAYNAME             VARCHAR(100) NOT NULL,

    _GIVENNAME_PREFIX        VARCHAR(100),
    _GIVENNAME               VARCHAR(100),
    _GIVENNAME_POSTFIX       VARCHAR(100),

    _SURNAME_PREFIX          VARCHAR(100),
    _SURNAME                 VARCHAR(100),
    _SURNAME_POSTFIX         VARCHAR(100),

    _HONORIFIC_TITLE_PREFIX  VARCHAR(100),
    _HONORIFIC_TITLE_POSTFIX VARCHAR(100),

    _HERALDIC_TITLE_PREFIX   VARCHAR(100),
    _HERALDIC_TITLE_POSTFIX  VARCHAR(100),

    _CREATED                 TIMESTAMP    NOT NULL,
    _MODIFIED                TIMESTAMP    NOT NULL
);

CREATE TABLE BASE.NATURAL_PERSONS_CHANGES
(
    id                          INTEGER PRIMARY KEY,
    _ACTION                     VARCHAR(256) NOT NULL,

    _COMMAND_UUID               char(36)     NOT NULL UNIQUE,
    _COMMAND_TENANT             VARCHAR(256) NOT NULL DEFAULT './.',
    _COMMAND_KEY                VARCHAR(50),
    _COMMAND_CREATED            TIMESTAMP    NOT NULL,

    _WORKFLOW_KIND              VARCHAR(256),
    _WORKFLOW_VERSION           VARCHAR(20),
    _WORKFLOW_REQUEST           char(36),
    _WORKFLOW_CORRELATION       char(36),
    _WORKFLOW_SEQUENCE          INTEGER,
    _WORKFLOW_TIMESTAMP         TIMESTAMP,
    _WORKFLOW_DEFINITION_UUID   char(36),
    _WORKFLOW_DEFINITION_TENANT VARCHAR(256),
    _WORKFLOW_DEFINITION_KEY    VARCHAR(50),

    _UUID                       CHAR(36)     NOT NULL,
    _TENANT                     VARCHAR(256) NOT NULL DEFAULT './.',
    _KEY                        VARCHAR(50),

    -- JPAContactCreate
    _DISPLAYNAME                VARCHAR(100),

    _GIVENNAME_PREFIX           VARCHAR(100),
    _GIVENNAME                  VARCHAR(100),
    _GIVENNAME_POSTFIX          VARCHAR(100),

    _SURNAME_PREFIX             VARCHAR(100),
    _SURNAME                    VARCHAR(100),
    _SURNAME_POSTFIX            VARCHAR(100),

    _HONORIFIC_TITLE_PREFIX     VARCHAR(100),
    _HONORIFIC_TITLE_POSTFIX    VARCHAR(100),

    _HERALDIC_TITLE_PREFIX      VARCHAR(100),
    _HERALDIC_TITLE_POSTFIX     VARCHAR(100),

    _CREATED                    TIMESTAMP,
    _MODIFIED                   TIMESTAMP
);

CREATE TABLE BASE.OBJECT_REFERENCES
(
    id         INTEGER PRIMARY KEY,

    _FOLDER_ID BIGINT,

    _UUID      CHAR(36)     NOT NULL,
    _TENANT    VARCHAR(256) NOT NULL DEFAULT './.',
    _KEY       VARCHAR(50),

    _KIND      VARCHAR(256) NOT NULL,
    _VERSION   VARCHAR(50)  NOT NULL
);

CREATE TABLE BASE.OBJECT_REFERENCE_CHANGES
(
    id         INTEGER PRIMARY KEY,

    _FOLDER_ID BIGINT,

    _UUID      CHAR(36)     NOT NULL,
    _TENANT    VARCHAR(256) NOT NULL DEFAULT './.',
    _KEY       VARCHAR(50),

    _KIND      VARCHAR(256) NOT NULL,
    _VERSION   VARCHAR(50)  NOT NULL
);
