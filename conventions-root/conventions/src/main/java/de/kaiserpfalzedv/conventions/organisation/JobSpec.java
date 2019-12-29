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

package de.kaiserpfalzedv.conventions.organisation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.commons.ObjectList;
import de.kaiserpfalzedv.commons.ObjectReference;
import de.kaiserpfalzedv.commons.api.Spec;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

/**
 * @author rlichti
 * @since 2019-12-29T14:48
 */
@Value.Immutable
@JsonSerialize(as = ImmutableJobSpec.class)
@JsonDeserialize(builder = ImmutableJobSpec.Builder.class)
public interface JobSpec extends Spec<JobSpec>, Comparable<JobSpec> {
    String KIND = "de.kaiserpfalzedv.conventions.organisation.Job";
    String VERSION = "1.0.0";

    ObjectReference getSupervisor();

    ObjectList<Job> getReports();

    @Override
    @Value.Default
    @Value.Lazy
    default int compareTo(@NotNull final JobSpec other) {
        //noinspection unchecked
        if (getSupervisor().compareTo(other.getSupervisor()) != 0) {
            //noinspection unchecked
            return getSupervisor().compareTo(other.getSupervisor());
        }

        return getDisplayname().compareTo(other.getDisplayname());
    }

}
