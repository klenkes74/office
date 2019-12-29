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
import de.kaiserpfalzedv.commons.ObjectReference;
import de.kaiserpfalzedv.commons.api.Spec;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;

/**
 * @author rlichti
 * @since 2019-12-29T14:48
 */
@Value.Immutable
@JsonSerialize(as = ImmutableJobScheduleSpec.class)
@JsonDeserialize(builder = ImmutableJobScheduleSpec.Builder.class)
public interface JobScheduleSpec extends Spec<JobScheduleSpec>, Comparable<JobScheduleSpec> {
    String KIND = "de.kaiserpfalzedv.conventions.organisation.Job";
    String VERSION = "1.0.0";

    Job getJob();

    OffsetDateTime getStarting();

    OffsetDateTime getEnding();

    ObjectReference getResponsible();

    @Override
    @Value.Default
    @Value.Lazy
    default int compareTo(@NotNull final JobScheduleSpec other) {
        if (getJob().compareTo(other.getJob()) != 0) {
            return getJob().compareTo(other.getJob());
        }

        if (getStarting().compareTo(other.getStarting()) != 0) {
            return getStarting().compareTo(other.getStarting());
        }

        return getResponsible().compareTo(other.getResponsible());
    }

}
