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

package de.kaiserpfalzedv.base.store;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.io.Serializable;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-15 17:11
 */
@Value.Immutable
@JsonSerialize(as = ImmutablePage.class)
@JsonDeserialize(builder = ImmutablePage.Builder.class)
public interface Page extends Serializable {
    long getIndex();

    int getSize();

    default Page first() {
        return ImmutablePage.builder()
                .index(0)
                .size(getSize())
                .build();
    }

    default Page previous() {
        if (getIndex() - getSize() < 0) {
            return first();
        }

        return ImmutablePage.builder()
                .index(getIndex() - getSize())
                .size(getSize())
                .build();
    }

    default Page next() {
        return ImmutablePage.builder()
                .index(getIndex() + getSize())
                .size(getSize())
                .build();
    }
}
