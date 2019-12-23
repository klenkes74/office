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

import de.kaiserpfalzedv.base.api.ImmutableMetadata;
import de.kaiserpfalzedv.base.api.ImmutableObjectIdentifier;
import de.kaiserpfalzedv.base.api.Metadata;
import de.kaiserpfalzedv.base.api.ObjectIdentity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-22 11:19
 */
public class CreateNaturalPersonTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNaturalPersonTest.class);

    static private final UUID ID = UUID.randomUUID();
    private static final String TENANT = "tenant";
    private static final String KEY = "key";
    private static final OffsetDateTime CREATED = OffsetDateTime.now();
    private static final OffsetDateTime MODIFIED = CREATED;

    private static final NaturalPersonSpec CONTACT_SPEC = new NaturalPersonSpec() {
        @Override
        public Optional<String> getGivennamePrefix() {
            return Optional.empty();
        }

        @Override
        public String getGivenname() {
            return null;
        }

        @Override
        public Optional<String> getGivennamePostfix() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getSurnamePrefix() {
            return Optional.empty();
        }

        @Override
        public String getSurname() {
            return null;
        }

        @Override
        public Optional<String> getSurnamePostfix() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getHonorificPrefixTitle() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getHonorificPostfixTitle() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getHeraldicPrefixTitle() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getHeraldicPostfixTitle() {
            return Optional.empty();
        }

        @Override
        public ObjectIdentity getIdentity() {
            return ImmutableObjectIdentifier.builder()
                    .kind(NaturalPersonSpec.KIND)
                    .version(NaturalPersonSpec.VERSION)
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


    private static final CreateNaturalPerson SERVICE = new CreateNaturalPerson() {
        @Override
        public Metadata getMetadata() {
            return ImmutableMetadata.builder()
                    .identity(ImmutableObjectIdentifier.builder()
                            .kind(CreateNaturalPerson.KIND)
                            .version(CreateNaturalPerson.VERSION)
                            .uuid(ID)
                            .tenant(TENANT)
                            .name(KEY)
                            .build()
                    )
                    .build();
        }

        @Override
        public NaturalPersonSpec getSpec() {
            return CONTACT_SPEC;
        }
    };


    @Test
    public void shouldReturnCorrectKindOfFolder() {
        assert CreateNaturalPerson.KIND.equals(SERVICE.getKind());
    }

    @Test
    public void shouldReturnCorrectVersionOfFolder() {
        assert CreateNaturalPerson.VERSION.equals(SERVICE.getVersion());
    }

    @Test
    public void shouldApplyTheCommandCorrectly() {
        assert CONTACT_SPEC.equals(SERVICE.apply(CONTACT_SPEC));
    }

    @BeforeAll
    public static void logStart() {
        LOGGER.trace("Started tests for: {}", CreateNaturalPerson.class.getCanonicalName());
    }

    @AfterAll
    public static void logEnd() {
        LOGGER.trace("Ended tests for: {}", CreateNaturalPerson.class.getCanonicalName());
    }
}
