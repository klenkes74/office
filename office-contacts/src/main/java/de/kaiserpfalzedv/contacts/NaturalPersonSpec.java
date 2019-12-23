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
import de.kaiserpfalzedv.base.api.Spec;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.Optional;

/*
 *
 *
 * @author rlichti@kaiserpfalz-edv.de
 * @since 2019-12-15T10:20Z
 */
@Value.Immutable
@JsonSerialize(as = ImmutableNaturalPersonSpec.class)
@JsonDeserialize(builder = ImmutableNaturalPersonSpec.Builder.class)
public interface NaturalPersonSpec extends Spec<Serializable>, Serializable {
    String KIND = "de.kaiserpfalzedv.contacts.NaturalPersonSpec";
    String VERSION = "1.0.0";

    @Value.Default
    default String getKind() {
        return KIND;
    }

    @Value.Default
    default String getVersion() {
        return VERSION;
    }


    Optional<String> getGivennamePrefix();

    String getGivenname();

    Optional<String> getGivennamePostfix();

    Optional<String> getSurnamePrefix();

    String getSurname();

    Optional<String> getSurnamePostfix();

    /**
     * Honorific prefix titles are titles like Dr., Prof, that prefix the name.
     *
     * @return the prefixed honorific titles of the person.
     */
    Optional<String> getHonorificPrefixTitle();

    /**
     * Honorific postfix titles are titles like MD or PD, that are postfixed to the name.
     *
     * @return the postfixed honorific titles of the person.
     */
    Optional<String> getHonorificPostfixTitle();

    /**
     * Heraldic titles are from feudal systems and kingdoms. Since I live in Germany I don't have much experience about
     * them but may be important for countries like the Netherlands, Belgium, ...
     *
     * @return the prefixed heraldic titles of the person.
     */
    Optional<String> getHeraldicPrefixTitle();

    /**
     * Heraldic titles are from feudal systems and kingdoms. Since I live in Germany I don't have much experience about
     * them but may be important for countries like the Netherlands, Belgium, ...
     * <p>
     * May be used for thinks like "... the 4th".
     *
     * @return the postfixed heraldic titles of the person.
     */
    Optional<String> getHeraldicPostfixTitle();
}
