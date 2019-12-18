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

INSERT INTO FOLDERS.FOLDERS (id, _UUID, _SCOPE, _KEY, _NAME, _SHORTNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (1, '3ca1aa42-4ae0-4066-ae5b-1ab2d1eab7f8', 'de.kaiserpfalz-edv', 'I-19-0001', 'Softwaretest Akte 1',
        'SW Test 1', 'Einfache Akte für Softwaretests', null,
        parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO FOLDERS.FOLDERS (id, _UUID, _SCOPE, _KEY, _NAME, _SHORTNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (2, '066ed1a6-a699-49e4-a756-b17816ecf7e8', 'de.kaiserpfalz-edv', 'I-19-0002', 'Softwaretest Akte 2',
        'SW Test 2', null, null, parsedatetime('2018-10-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO FOLDERS.FOLDERS (id, _UUID, _SCOPE, _KEY, _NAME, _SHORTNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (3, '76351c86-e920-4ae0-9591-115ea7d4f1ad', 'de.kaiserpfalz-edv', 'I-19-0003', 'Softwaretest Akte 3',
        'SW Test 3', null, null, parsedatetime('2018-11-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO FOLDERS.FOLDERS (id, _UUID, _SCOPE, _KEY, _NAME, _SHORTNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (4, '568928de-0940-48bd-a298-36cebca58596', 'de.kaiserpfalz-edv', 'I-19-0004', 'Softwaretest Akte 4',
        'SW Test 4', null, null, parsedatetime('2018-08-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO FOLDERS.FOLDERS (id, _UUID, _SCOPE, _KEY, _NAME, _SHORTNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (5, '640f8211-05ef-489f-9dac-b8719f95c33a', 'de.kaiserpfalz-edv', 'I-19-0005', 'Softwaretest Akte 5',
        'SW Test 5', null, null, parsedatetime('2018-06-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO FOLDERS.FOLDERS (id, _UUID, _SCOPE, _KEY, _NAME, _SHORTNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (6, '55758405-33ec-4640-b861-daee96c3be07', 'de.kaiserpfalz-edv', 'I-19-0006', 'Softwaretest Akte 6',
        'SW Test 6', null, null, parsedatetime('2018-05-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO FOLDERS.FOLDERS (id, _UUID, _SCOPE, _KEY, _NAME, _SHORTNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (7, 'c9922e42-fc87-442c-a80a-ad385c680512', 'de.kaiserpfalz-edv', 'I-19-0007', 'Softwaretest Akte 7',
        'SW Test 7', null, null, parsedatetime('2018-03-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO FOLDERS.FOLDERS (id, _UUID, _SCOPE, _KEY, _NAME, _SHORTNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (8, 'e1227030-46bc-49c2-b75a-6f7340b50f56', 'de.kaiserpfalz-edv', 'I-19-0008', 'Softwaretest Akte 8',
        'SW Test 8', null, null, parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO FOLDERS.FOLDERS (id, _UUID, _SCOPE, _KEY, _NAME, _SHORTNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (10, '81eb2a31-d669-4e7e-981a-d7b69f792da1', 'de.paladins-inn', '2019-0137', 'Softwaretest Akte 9', 'SW Test 9',
        null, null, parsedatetime('2019-01-06 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO FOLDERS.FOLDERS (id, _UUID, _SCOPE, _KEY, _NAME, _SHORTNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (9, '5446dbbe-44ec-44b9-b021-d22040848f22', 'de.lichti', 'KD-2019-M-135', 'Softwaretest Akte 10', 'SW Test 10',
        null, null, parsedatetime('2019-01-10 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));

CREATE TABLE FOLDERS.FOLDERS_CHANGES
(
    id                         INTEGER PRIMARY KEY,
    _ACTION                    VARCHAR(256) NOT NULL,

    _COMMAND_UUID              char(36)     NOT NULL UNIQUE,
    _COMMAND_SCOPE             VARCHAR(256) NOT NULL DEFAULT './.',
    _COMMAND_KEY               VARCHAR(50),
    _COMMAND_CREATED           TIMESTAMP    NOT NULL,

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