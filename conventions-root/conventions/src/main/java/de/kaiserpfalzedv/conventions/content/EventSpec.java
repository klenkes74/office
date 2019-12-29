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

import de.kaiserpfalzedv.commons.ObjectList;
import de.kaiserpfalzedv.commons.ObjectReference;
import de.kaiserpfalzedv.commons.api.Spec;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

/**
 * @author rlichti
 * @since 2019-12-29T11:50
 */
public interface EventSpec<T extends EventSpec<?>> extends Spec<T>, Comparable<T> {
    String KIND = "de.kaiserpfalzedv.conventions.content.TopicSpec";
    String VERSION = Topic.VERSION;

    /**
     * The topic of the event.
     *
     * @return a reference to the topic.
     */
    Topic getTopic();

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
     *     <li>Type of talk ({@link Group}, {@link Talk], {@link Workshop}}.</li>
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
    default int compareTo(@NotNull T other) {
        if (getClass().getSimpleName().equals(other.getClass().getSimpleName())) {
            if (getTopic().getKind().equals(other.getTopic().getKind())) {
                return getTopic().compareTo(other.getTopic());
            }
            return getDisplayname().compareTo(other.getDisplayname());
        }

        return getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}
