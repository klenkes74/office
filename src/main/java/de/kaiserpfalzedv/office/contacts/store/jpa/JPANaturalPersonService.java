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

package de.kaiserpfalzedv.office.contacts.store.jpa;

import de.kaiserpfalzedv.base.WrappingException;
import de.kaiserpfalzedv.base.cdi.EventLogged;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.CreationFailedException;
import de.kaiserpfalzedv.base.store.KeyAlreadyExistsException;
import de.kaiserpfalzedv.base.store.UuidAlreadyExistsException;
import de.kaiserpfalzedv.office.contacts.NaturalPersonCreated;
import de.kaiserpfalzedv.office.contacts.NaturalPersonSpec;
import de.kaiserpfalzedv.office.contacts.api.NaturalPersonResultService;
import de.kaiserpfalzedv.office.folders.store.jpa.JPAFolder;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

@JPA
@EventLogged
@Dependent
public class JPANaturalPersonService implements NaturalPersonResultService<NaturalPersonCreated> {
    @Transactional
    public void observe(@Observes final NaturalPersonCreated event) {
        NaturalPersonSpec spec = event.getSpec();

        if (JPAFolder.find("identity.uuid", spec.getIdentity().getUuid()).count() != 0) {
            throw new WrappingException(new UuidAlreadyExistsException(spec.getIdentity()));
        }

        if (!spec.getIdentity().getTenant().orElse("./").isEmpty() && spec.getIdentity().getName().isPresent()) {
            if (JPAFolder.find("identity.scope = ?1 and identity.key = ?2", spec.getIdentity().getTenant().orElse("./."), spec.getIdentity().getName().orElse(null)).count() != 0) {
                throw new WrappingException(new KeyAlreadyExistsException(spec.getIdentity()));
            }
        }

        try {
            JPANaturalPerson jpa = new JPANaturalPerson().fromModel(event);
            jpa.persist();
        } catch (IllegalArgumentException e) {
            throw new WrappingException(new CreationFailedException(event.getMetadata().getIdentity(), e));
        }
    }
}
