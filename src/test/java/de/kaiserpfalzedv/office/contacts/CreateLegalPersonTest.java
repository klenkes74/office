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

package de.kaiserpfalzedv.office.contacts;

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.api.Metadata;
import de.kaiserpfalzedv.base.api.ObjectIdentifier;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 10:42
 */
public class CreateLegalPersonTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateLegalPersonTest.class);

    static private final UUID ID = UUID.randomUUID();
    private static final String TENANT = "tenant";
    private static final String KEY = "key";
    private static final OffsetDateTime CREATED = OffsetDateTime.now();
    private static final OffsetDateTime MODIFIED = CREATED;

    private static final LegalPersonContactSpec LEGAL_PERSON = new LegalPersonContactSpec() {
        @Override
        public ObjectIdentifier getIdentity() {
            return ImmutableObjectIdentifier.builder()
                    .kind(KIND)
                    .version(VERSION)
                    .uuid(ID)
                    .tenant(TENANT)
                    .name(KEY)
                    .build();
        }

        @Override
        public String getDisplayname() {
            return "displayname";
        }

        @Override
        public OffsetDateTime getCreated() {
            return CREATED;
        }

        @Override
        public OffsetDateTime getModified() {
            return MODIFIED;
        }
    };


    private static final CreateLegalPerson SERVICE = new CreateLegalPerson() {
        @Override
        public Metadata getMetadata() {
            return ImmutableMetadata.builder()
                    .identity(ImmutableObjectIdentifier.builder()
                            .kind(KIND)
                            .version(VERSION)
                            .uuid(ID)
                            .tenant(TENANT)
                            .name(KEY)
                            .build()
                    )
                    .build();
        }

        @Override
        public LegalPersonContactSpec getSpec() {
            return LEGAL_PERSON;
        }
    };


    @Test
    public void shouldReturnCorrectKindOfFolder() {
        assert CreateLegalPerson.KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfFolder() {
        assert CreateLegalPerson.VERSION.equals(SERVICE.getVersion());
    }

    @Test
    public void shouldApplyTheCommandCorrectly() {
        assert LEGAL_PERSON.equals(SERVICE.apply(LEGAL_PERSON));
    }

    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", CreateLegalPerson.class.getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", CreateLegalPerson.class.getCanonicalName());
    }
}
