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

package de.kaiserpfalzedv.conventions.location;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.commons.ObjectList;
import de.kaiserpfalzedv.commons.ObjectReference;
import de.kaiserpfalzedv.commons.api.Spec;
import de.kaiserpfalzedv.conventions.organisation.ResponsibleJobHolding;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author rlichti
 * @since 2019-12-29T11:50
 */
@Value.Immutable
@JsonSerialize(as = ImmutableLocationSpec.class)
@JsonDeserialize(builder = ImmutableLocationSpec.Builder.class)
public interface LocationSpec extends Spec<LocationSpec>, Comparable<LocationSpec>, ResponsibleJobHolding {
    String KIND = Location.KIND;
    String VERSION = Location.VERSION;

    Optional<ObjectReference> getContainingLocation();

    ObjectList<Location> getSubLocations();

    ObjectList<ObjectReference> getSlots();

    /**
     * Compares different events. It tries to group events following this priority:
     * <ol>
     *     <li>{@link #getDisplayname()} of the event.</li>
     * </ol>
     *
     * @param other The event to be compared with.
     * @return The order of the two elements.
     */
    @Override
    @Value.Default
    @Value.Lazy
    default int compareTo(@NotNull LocationSpec other) {
        if (getContainingLocation().isPresent() && other.getContainingLocation().isPresent()) {
            if (getContainingLocation().get().compareTo(other.getContainingLocation().get()) != 0) {
                return getContainingLocation().get().compareTo(other.getContainingLocation().get());
            }
        }

        if (getContainingLocation().isPresent() && !other.getContainingLocation().isPresent()) {
            return 1; // a location in a structure is always preferred.
        }

        return getDisplayname().compareTo(other.getDisplayname());
    }
}
