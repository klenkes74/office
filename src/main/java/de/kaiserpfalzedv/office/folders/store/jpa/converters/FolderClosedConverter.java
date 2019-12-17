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

package de.kaiserpfalzedv.office.folders.store.jpa.converters;

import de.kaiserpfalzedv.base.api.Metadata;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import de.kaiserpfalzedv.office.folders.FolderClosed;
import de.kaiserpfalzedv.office.folders.ImmutableFolderClosed;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolderClose;
import de.kaiserpfalzedv.office.folders.store.jpa.converters.base.ConverterForResultsWithoutFolderSpec;

import javax.enterprise.context.Dependent;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-15 13:01
 */
@Dependent
public class FolderClosedConverter extends ConverterForResultsWithoutFolderSpec<FolderClosed, JPAFolderClose> {
    @Override
    public JPAFolderClose convertFromAPI(FolderClosed data) {
        JPAFolderClose result = new JPAFolderClose();

        convert(data, result);

        return result;
    }

    @Override
    public FolderClosed convertToAPI(JPAFolderClose data) {
        ObjectIdentifier identity = createSpecIdentity(data.identity, FolderClosed.KIND, FolderClosed.VERSION);
        Metadata metadata = createMetadata(data, identity);

        return ImmutableFolderClosed.builder()
                .metadata(metadata)
                .build();
    }
}
