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

package de.kaiserpfalzedv.contacts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.base.actions.commands.CreateCommand;
import de.kaiserpfalzedv.contacts.api.NaturalPersonCommandWithSpec;
import org.immutables.value.Value;

/**
 * A new legal person contact needs to be created.
 *
 * @author rlichti@kaiserpfalz-edv.de
 * @since 22.12.2019 10:36
 */
@Value.Immutable
@JsonSerialize(as = ImmutableModifyNaturalPerson.class)
@JsonDeserialize(builder = ImmutableModifyNaturalPerson.Builder.class)
public interface ModifyNaturalPerson extends NaturalPersonCommandWithSpec, CreateCommand<NaturalPersonSpec> {
    String KIND = "de.kaiserpfalzedv.contacts.ModifyNaturalPerson";
    String VERSION = "1.0.0";

    @Override
    @Value.Default
    default String getKind() {
        return KIND;
    }

    @Override
    @Value.Default
    default String getVersion() {
        return VERSION;
    }

    @Override
    @Value.Default
    default NaturalPersonSpec apply(final NaturalPersonSpec orig) {
        return ImmutableNaturalPersonSpec.copyOf(orig)
                .withDisplayname(getSpec().getDisplayname())
                .withModified(getSpec().getModified())
                .withData(getSpec().getData());
    }
}
