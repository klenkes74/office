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

package de.kaiserpfalzedv.commons;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListSet;

@Value.Immutable
@JsonSerialize(as = ImmutableObjectList.class)
@JsonDeserialize(builder = ImmutableObjectList.Builder.class)
public interface ObjectList<T extends BaseObject<? extends Serializable> & Comparable<T>> extends BaseObject<ConcurrentSkipListSet<T>>, Comparable<ObjectList<?>> {
    String KIND = "de.kaiserpfalz.base.ObjectList";
    String VERSION = "1.0.0";

    @Value.Default
    default String getKind() {
        return KIND;
    }

    @Value.Default
    default String getVersion() {
        return VERSION;
    }

    @Value.Default
    default ConcurrentSkipListSet<T> getSpec() {
        return new ConcurrentSkipListSet<>();
    }

    @Value.Default
    @Override
    default int compareTo(@NotNull ObjectList<?> other) {
        return getMetadata().getIdentity().compareTo(other.getMetadata().getIdentity());
    }
}
