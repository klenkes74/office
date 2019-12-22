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

package de.kaiserpfalzedv.office.folders.store.jpa;

import de.kaiserpfalzedv.base.store.jpa.JPAIdentity;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-17 09:47
 */
@Entity
@Table(name = "FOLDERS", schema = "BASE")
public class JPAFolder extends PanacheEntity {
    @Embedded
    public JPAIdentity identity;

    @Embedded
    public JPAFolderSpec spec;
}
