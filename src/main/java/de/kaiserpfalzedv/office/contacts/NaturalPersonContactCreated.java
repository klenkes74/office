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

package de.kaiserpfalzedv.office.contacts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.office.contacts.api.NaturalPersonContactResultWithSpec;
import de.kaiserpfalzedv.office.folders.ImmutableFolderCreated;
import org.immutables.value.Value;

/*
 *
 *
 * @author rlichti@kaiserpfalz-edv.de
 * @since 2019-12-15T10:20Z
 */
@Value.Immutable
@JsonSerialize(as = ImmutableFolderCreated.class)
@JsonDeserialize(builder = ImmutableFolderCreated.Builder.class)
public interface NaturalPersonContactCreated extends NaturalPersonContactResultWithSpec<CreateNaturalPerson> {
    String KIND = "de.kaiserpfalzedv.office.contacts.NaturalPersonContactCreated";
    String VERSION = "1.0.0";

    @Override
    default String getKind() {
        return KIND;
    }

    @Override
    default String getVersion() {
        return VERSION;
    }
}
