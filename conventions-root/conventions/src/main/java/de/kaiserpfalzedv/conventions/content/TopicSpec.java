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

/**
 * @author rlichti
 * @since 2019-12-29T11:50
 */
@Value.Immutable
@JsonSerialize(as = ImmutableTopicSpec.class)
@JsonDeserialize(builder = ImmutableTopicSpec.Builder.class)
public interface TopicSpec extends Spec<TopicSpec>, Comparable<TopicSpec> {
    String KIND = Topic.KIND;
    String VERSION = Topic.VERSION;

    ObjectReference getMasterTopic();

    ObjectList<ObjectReference> getSubTopics();

    String getAbstract();

    String getDescription();

    @Override
    @Value.Default
    @Value.Lazy
    default int compareTo(@NotNull TopicSpec other) {
        if (getMasterTopic().compareTo(other.getMasterTopic()) != 0) {
            return getMasterTopic().compareTo(other.getMasterTopic());
        }

        return getDisplayname().compareTo(other.getDisplayname());
    }
}
