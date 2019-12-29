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

package de.kaiserpfalzedv.conventions.content;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.commons.ObjectList;
import de.kaiserpfalzedv.commons.ObjectReference;
import de.kaiserpfalzedv.commons.api.Spec;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author rlichti
 * @since 2019-12-29T11:50
 */
@Value.Immutable
@JsonSerialize(as = ImmutableEventSpec.class)
@JsonDeserialize(builder = ImmutableEventSpec.Builder.class)
public interface EventSpec extends Spec<EventSpec>, Comparable<EventSpec> {
    String KIND = Event.KIND;
    String VERSION = Event.VERSION;

    ObjectReference getContainingEvent();

    ObjectList<ObjectReference> getSubEvents();

    ObjectList<ObjectReference> getSlots();

    EventType getType();

    String getAbstract();

    String getDescription();

    Optional<Integer> getMinumAttendees();

    Optional<Integer> getMaximumAttendees();

    /**
     * The topic of the event.
     *
     * @return a reference to the topic.
     */
    Optional<Topic> getTopic();

    /**
     * The speaker or moderator of this event.
     *
     * @return a reference to the person moderating or speaking at the event.
     */
    ObjectReference getSpeaker();

    /**
     * The attendees of the event.
     *
     * @return a list of references to all attendees of the event.
     */
    ObjectList<ObjectReference> getAttendees();

    /**
     * Compares different events. It tries to group events following this priority:
     * <ol>
     *     <li>{@link Topic} of the event.</li>
     *     <li>{@link #getDisplayname()} of the event.</li>
     * </ol>
     *
     * @param other The event to be compared with.
     * @return The order of the two elements.
     */
    @Override
    @Value.Default
    @Value.Lazy
    default int compareTo(@NotNull EventSpec other) {
        if (getContainingEvent().compareTo(other.getContainingEvent()) != 0) {
            return getContainingEvent().compareTo(other.getContainingEvent());
        }

        if (getTopic().isPresent() && other.getTopic().isPresent()) {
            if (getTopic().get().compareTo(other.getTopic().get()) != 0) {
                return getTopic().get().compareTo(other.getTopic().get());
            }
        }

        if (getTopic().isPresent() && !other.getTopic().isPresent()) {
            return 1;
        } else if (!getTopic().isPresent() && other.getTopic().isPresent()) {
            return -1;
        }

        if (getType().compareTo(other.getType()) != 0) {
            return getType().compareTo(other.getType());
        }

        return getDisplayname().compareTo(other.getDisplayname());
    }
}
