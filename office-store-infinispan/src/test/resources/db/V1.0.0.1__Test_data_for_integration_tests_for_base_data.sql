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
CREATE SEQUENCE PUBLIC.HIBERNATE_SEQUENCE START 11;

INSERT INTO BASE.FOLDERS (id, _UUID, _TENANT, _KEY, _NAME, _DISPLAYNAME, _DESCRIPTION, _CLOSED, _CREATED, _MODIFIED)
VALUES (1, '3ca1aa42-4ae0-4066-ae5b-1ab2d1eab7f8', 'de.kaiserpfalz-edv', 'I-19-0001', 'Softwaretest Akte 1',
        'SW Test 1', 'Einfache Akte für Softwaretests', null,
        parsedatetime('2018-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'),
        parsedatetime('2019-12-17 18:12:00', 'yyyy-MM-dd hh:mm:ss'));
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
