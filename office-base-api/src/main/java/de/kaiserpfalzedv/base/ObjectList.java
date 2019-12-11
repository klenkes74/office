/*
 *  Copyright Kaiserpfalz EDV-Service, Roland T. Lichti , 2019. All rights reserved.
 *
 *  This file is part of Kaiserpfalz EDV-Service Office.
 *
 *  This is free software: you can redistribute it and/or modify it under the terms of
 *   the GNU Lesser General Public License as published by the Free Software
 *   Foundation, either version 3 of the License.
 *
 *  This file is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *  License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along
 *  with this file. If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package de.kaiserpfalzedv.base;

import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
@Value.Modifiable
public interface ObjectList<T extends SingleObject<?>>  extends BaseAPI<ObjectList<T>> {
    static final String KIND = "de.kaiserpfalz.base.list";

    @Value.Default
    default String getKind() {
        return KIND;
    }

    @Value.Default
    default String getVersion() {
        return "1.0.0";
    }

    @Value.Default
    default Optional<String> getScope() {
        return Optional.of(getClass().getPackage().getName());
    }

    List<? extends T> getSpec();
}
