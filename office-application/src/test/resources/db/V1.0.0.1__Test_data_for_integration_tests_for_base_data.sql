/*
 * Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2019. All rights reserved.
 *
 *  This file is part of Kaiserpfalz EDV-Service Office.
 *
 *  This is free software: you can redistribute it and/or modify it under the terms of
 *  the GNU Lesser General Public License as published by the Free Software
 *  Foundation, either version 3 of the License.
 *
 *  This file is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *  License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along
 *  with this file. If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

-- Hibernate Internal Objects
DROP SEQUENCE PUBLIC.HIBERNATE_SEQUENCE;
CREATE SEQUENCE PUBLIC.HIBERNATE_SEQUENCE START 21;

INSERT INTO BASE.FOLDERS (id, _UUID, _TENANT, _KEY,
                          _DISPLAYNAME, _CREATED, _MODIFIED,
                          _NAME, _DESCRIPTION, _CLOSED)
VALUES (1, '3ca1aa42-4ae0-4066-ae5b-1ab2d1eab7f8', 'de.kaiserpfalz-edv', 'I-19-0001',
        'SW Test 1', parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        'Softwaretest Akte 1', 'Einfache Akte für Softwaretests', null);
INSERT INTO BASE.OBJECT_REFERENCES (id, _KIND, _VERSION, _FOLDER_ID, _UUID, _TENANT, _KEY)
VALUES (1, 'test', '0.0.0', 1, 'bbb4f55b-fe65-4361-b00a-d797f63a38e9', 'de.kaiserpfalz-edv', 'Test-Reference 1');
INSERT INTO BASE.OBJECT_REFERENCES (id, _KIND, _VERSION, _FOLDER_ID, _UUID, _TENANT, _KEY)
VALUES (2, 'test', '0.0.0', 1, 'a8ac8007-4cff-41c2-8cd8-62f31899d664', 'de.kaiserpfalz-edv', 'Test-Reference 2');
INSERT INTO BASE.OBJECT_REFERENCES (id, _KIND, _VERSION, _FOLDER_ID, _UUID, _TENANT, _KEY)
VALUES (3, 'test', '0.0.0', 1, 'bf18e36a-47b7-496e-9f43-a2dbb7d3f211', 'de.kaiserpfalz-edv', 'Test-Reference 3');

INSERT INTO BASE.FOLDERS_CHANGES(id, _ACTION,
                                 _COMMAND_UUID, _COMMAND_TENANT, _COMMAND_KEY,
                                 _COMMAND_CREATED,
                                 _WORKFLOW_KIND, _WORKFLOW_VERSION,
                                 _WORKFLOW_DEFINITION_UUID, _WORKFLOW_DEFINITION_TENANT, _WORKFLOW_DEFINITION_KEY,
                                 _WORKFLOW_CORRELATION, _WORKFLOW_REQUEST, _WORKFLOW_SEQUENCE,
                                 _WORKFLOW_TIMESTAMP,
                                 _UUID, _TENANT, _KEY,
                                 _DISPLAYNAME, _CREATED, _MODIFIED,
                                 _NAME, _DESCRIPTION, _CLOSED)
VALUES (1, 'de.kaiserpfalzedv.folders.CreateFolder',
        '9b8e5377-98c4-4622-957b-1555453e87b8', 'de.kaiserpfalz-edv', 'Create I-19-0001',
        parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        'Create Testdata', '1.0.0',
        '4b2074c3-bced-40ce-a46b-ab1bdeaa2209', 'de.kaiserpfalz-edv', 'Create Testdata',
        'ef920d61-2b16-4255-b0aa-5b6cbfd3e819', 'af7141e3-0023-4bb7-95d3-1fa31548511d', 1,
        parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        '3ca1aa42-4ae0-4066-ae5b-1ab2d1eab7f8', 'de.kaiserpfalz-edv', 'I-19-0001',
        'SW Test 1', parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        'Softwaretest Akte 1', 'Einfache Akte für Softwaretests', null);
INSERT INTO BASE.FOLDERS_CHANGES(id, _ACTION,
                                 _COMMAND_UUID, _COMMAND_TENANT, _COMMAND_KEY,
                                 _COMMAND_CREATED,
                                 _WORKFLOW_KIND, _WORKFLOW_VERSION,
                                 _WORKFLOW_DEFINITION_UUID, _WORKFLOW_DEFINITION_TENANT, _WORKFLOW_DEFINITION_KEY,
                                 _WORKFLOW_CORRELATION, _WORKFLOW_REQUEST, _WORKFLOW_SEQUENCE,
                                 _WORKFLOW_TIMESTAMP,
                                 _UUID, _TENANT, _KEY)
VALUES (2, 'de.kaiserpfalzedv.folders.AddContent',
        'aff5e3f6-7ae8-4220-badf-4e326c1bc0bd', 'de.kaiserpfalz-edv', 'Add Content I-19-0001',
        parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        'Create Testdata', '1.0.0',
        '4b2074c3-bced-40ce-a46b-ab1bdeaa2209', 'de.kaiserpfalz-edv', 'Create Testdata',
        'ef920d61-2b16-4255-b0aa-5b6cbfd3e819', 'fccbb00f-2ced-44e9-af0f-c7a0fc29ad45', 2,
        parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        '3ca1aa42-4ae0-4066-ae5b-1ab2d1eab7f8', 'de.kaiserpfalz-edv', 'I-19-0001');
INSERT INTO BASE.OBJECT_REFERENCE_CHANGES(id, _KIND, _VERSION, _FOLDER_ID, _UUID, _TENANT, _KEY)
VALUES (1, 'test', '0.0.0', 2, 'bbb4f55b-fe65-4361-b00a-d797f63a38e9', 'de.kaiserpfalz-edv', 'Test-Reference 1');
INSERT INTO BASE.OBJECT_REFERENCE_CHANGES(id, _KIND, _VERSION, _FOLDER_ID, _UUID, _TENANT, _KEY)
VALUES (2, 'test', '0.0.0', 2, 'a8ac8007-4cff-41c2-8cd8-62f31899d664', 'de.kaiserpfalz-edv', 'Test-Reference 2');
INSERT INTO BASE.OBJECT_REFERENCE_CHANGES(id, _KIND, _VERSION, _FOLDER_ID, _UUID, _TENANT, _KEY)
VALUES (3, 'test', '0.0.0', 2, 'bf18e36a-47b7-496e-9f43-a2dbb7d3f211', 'de.kaiserpfalz-edv', 'Test-Reference 3');


INSERT INTO BASE.FOLDERS (id, _UUID, _TENANT, _KEY, _NAME, _DISPLAYNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (2, '066ed1a6-a699-49e4-a756-b17816ecf7e8', 'de.kaiserpfalz-edv', 'I-19-0002', 'Softwaretest Akte 2',
        'SW Test 2', null, null, parsedatetime('2018-10-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.FOLDERS (id, _UUID, _TENANT, _KEY, _NAME, _DISPLAYNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (3, '76351c86-e920-4ae0-9591-115ea7d4f1ad', 'de.kaiserpfalz-edv', 'I-19-0003', 'Softwaretest Akte 3',
        'SW Test 3', null, null, parsedatetime('2018-11-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.FOLDERS (id, _UUID, _TENANT, _KEY, _NAME, _DISPLAYNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (4, '568928de-0940-48bd-a298-36cebca58596', 'de.kaiserpfalz-edv', 'I-19-0004', 'Softwaretest Akte 4',
        'SW Test 4', null, null, parsedatetime('2018-08-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.FOLDERS (id, _UUID, _TENANT, _KEY, _NAME, _DISPLAYNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (5, '640f8211-05ef-489f-9dac-b8719f95c33a', 'de.kaiserpfalz-edv', 'I-19-0005', 'Softwaretest Akte 5',
        'SW Test 5', null, null, parsedatetime('2018-06-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.FOLDERS (id, _UUID, _TENANT, _KEY, _NAME, _DISPLAYNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (6, '55758405-33ec-4640-b861-daee96c3be07', 'de.kaiserpfalz-edv', 'I-19-0006', 'Softwaretest Akte 6',
        'SW Test 6', null, null, parsedatetime('2018-05-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.FOLDERS (id, _UUID, _TENANT, _KEY, _NAME, _DISPLAYNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (7, 'c9922e42-fc87-442c-a80a-ad385c680512', 'de.kaiserpfalz-edv', 'I-19-0007', 'Softwaretest Akte 7',
        'SW Test 7', null, null, parsedatetime('2018-03-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.FOLDERS (id, _UUID, _TENANT, _KEY, _NAME, _DISPLAYNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (8, 'e1227030-46bc-49c2-b75a-6f7340b50f56', 'de.kaiserpfalz-edv', 'I-19-0008', 'Softwaretest Akte 8',
        'SW Test 8', null, null, parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.FOLDERS (id, _UUID, _TENANT, _KEY, _NAME, _DISPLAYNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (10, '81eb2a31-d669-4e7e-981a-d7b69f792da1', 'de.paladins-inn', '2019-0137', 'Softwaretest Akte 9', 'SW Test 9',
        null, null, parsedatetime('2019-01-06 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.FOLDERS (id, _UUID, _TENANT, _KEY, _NAME, _DISPLAYNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (9, '5446dbbe-44ec-44b9-b021-d22040848f22', 'de.lichti', 'KD-2019-M-135', 'Softwaretest Akte 10', 'SW Test 10',
        null, null, parsedatetime('2019-01-10 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));

INSERT INTO BASE.NATURAL_PERSONS (id, _UUID, _TENANT, _KEY, _DISPLAYNAME, _GIVENNAME, _SURNAME, _CREATED, _MODIFIED)
VALUES (10, 'a292b515-c84f-4777-981a-376e614f4fed', 'de.kaiserpfalz-edv', 'D10000', 'Roland T. Lichti',
        'Roland Thomas', 'Lichti', parsedatetime('2019-01-10 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.NATURAL_PERSONS (id, _UUID, _TENANT, _KEY, _DISPLAYNAME, _GIVENNAME, _SURNAME, _CREATED, _MODIFIED)
VALUES (11, '2ddbb50a-7536-48d3-81d8-6bfa46c9ebaa', 'de.kaiserpfalz-edv', 'D10001', 'Roland T. Lichti',
        'Roland Thomas', 'Lichti',
        parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.NATURAL_PERSONS (id, _UUID, _TENANT, _KEY, _DISPLAYNAME, _GIVENNAME, _SURNAME, _CREATED, _MODIFIED)
VALUES (12, 'eca15740-e2fd-4a36-9590-d44127ee6c12', 'de.kaiserpfalz-edv', 'D10002', 'Roland T. Lichti',
        'Roland Thomas', 'Lichti', parsedatetime('2018-10-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.NATURAL_PERSONS (id, _UUID, _TENANT, _KEY, _DISPLAYNAME, _GIVENNAME, _SURNAME, _CREATED, _MODIFIED)
VALUES (13, '40831d04-3855-4534-9837-f6f922319885', 'de.kaiserpfalz-edv', 'D10003', 'Roland T. Lichti',
        'Roland Thomas', 'Lichti', parsedatetime('2018-08-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.NATURAL_PERSONS (id, _UUID, _TENANT, _KEY, _DISPLAYNAME, _GIVENNAME, _SURNAME, _CREATED, _MODIFIED)
VALUES (14, '41d56361-c099-4a55-82cc-3c25680b7df6', 'de.kaiserpfalz-edv', 'D10004', 'Roland T. Lichti',
        'Roland Thomas', 'Lichti', parsedatetime('2018-10-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.NATURAL_PERSONS (id, _UUID, _TENANT, _KEY, _DISPLAYNAME, _GIVENNAME, _SURNAME, _CREATED, _MODIFIED)
VALUES (15, '762a14bc-0f6a-4e01-8ea4-9dfad33cdec8', 'de.kaiserpfalz-edv', 'D10005', 'Roland T. Lichti',
        'Roland Thomas', 'Lichti', parsedatetime('2018-06-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.NATURAL_PERSONS (id, _UUID, _TENANT, _KEY, _DISPLAYNAME, _GIVENNAME, _SURNAME, _CREATED, _MODIFIED)
VALUES (16, 'ac87cab2-6453-48ec-9201-96440668cc44', 'de.kaiserpfalz-edv', 'D10006', 'Roland T. Lichti',
        'Roland Thomas', 'Lichti',
        parsedatetime('2018-05-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
INSERT INTO BASE.NATURAL_PERSONS (id, _UUID, _TENANT, _KEY, _DISPLAYNAME,
                                  _GIVENNAME, _SURNAME,
                                  _CREATED, _MODIFIED,
                                  _GIVENNAME_POSTFIX, _SURNAME_PREFIX)
VALUES (17, 'e099521e-0c71-452c-8740-27e59e7b2d2b', 'de.kaiserpfalz-edv', 'D10007', 'Roland T. Jr. auf und zu Lichti',
        'Roland Thomas', 'Lichti',
        parsedatetime('2018-03-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        'Jr.', 'auf und zu');
INSERT INTO BASE.NATURAL_PERSONS (id, _UUID, _TENANT, _KEY, _DISPLAYNAME, _GIVENNAME, _SURNAME, _CREATED, _MODIFIED,
                                  _HONORIFIC_TITLE_POSTFIX)
VALUES (18, '6b5697c9-e40c-4606-8a63-a7a02908cb0c', 'de.kaiserpfalz-edv', 'D10008', 'Roland T. Lichti no PD',
        'Roland Thomas', 'Lichti', parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'), 'no PD');
INSERT INTO BASE.NATURAL_PERSONS (id, _UUID, _TENANT, _KEY, _DISPLAYNAME, _GIVENNAME, _SURNAME, _CREATED, _MODIFIED,
                                  _HERALDIC_TITLE_PREFIX)
VALUES (19, 'f422fcf1-ebe1-4b1e-adc2-0a0cfdeb0d81', 'de.kaiserpfalz-edv', 'D10009', 'Kardinal Roland T. Lichti',
        'Roland Thomas', 'Lichti', parsedatetime('2019-01-06 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'), 'Kardinal');
