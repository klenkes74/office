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

package de.kaiserpfalzedv.base.store.jpa.contacts;

import de.kaiserpfalzedv.base.WrappingException;
import de.kaiserpfalzedv.base.cdi.EventLogged;
import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.base.store.*;
import de.kaiserpfalzedv.contacts.NaturalPersonCreated;
import de.kaiserpfalzedv.contacts.NaturalPersonDeleted;
import de.kaiserpfalzedv.contacts.NaturalPersonSpec;
import de.kaiserpfalzedv.contacts.api.NaturalPersonResultService;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.UUID;

@JPA
@EventLogged
@Dependent
public class JPANaturalPersonService implements NaturalPersonResultService<NaturalPersonCreated> {
    @Transactional
    public void observe(@Observes final NaturalPersonCreated event) {
        NaturalPersonSpec spec = event.getSpec();

        if (JPANaturalPerson.findByUuid(spec.getIdentity().getTenant(), spec.getIdentity().getUuid()).count() != 0) {
            throw new WrappingException(new UuidAlreadyExistsException(spec.getIdentity()));
        }

        if (spec.getIdentity().getName().isPresent()) {
            if (JPANaturalPerson.findByTenantAndKey(spec.getIdentity().getTenant(), spec.getIdentity().getName().orElse(null)).count() != 0) {
                throw new WrappingException(new KeyAlreadyExistsException(spec.getIdentity()));
            }
        }

        try {
            JPANaturalPerson jpa = new JPANaturalPerson().fromModel(event.getSpec());
            jpa.persist();
        } catch (IllegalArgumentException e) {
            throw new WrappingException(new CreationFailedException(event.getMetadata().getIdentity(), e));
        }
    }

    @Transactional
    public void observe(@Observes final NaturalPersonDeleted event) {
        String tenant = event.getMetadata().getIdentity().getTenant();
        UUID uuid = event.getMetadata().getIdentity().getUuid();

        PanacheQuery<JPANaturalPerson> query = JPANaturalPerson.findByUuid(tenant, uuid);
        if (query.count() != 1) {
            throw new WrappingException(new NoModifiableDataFoundException(event.getMetadata().getIdentity()));
        }

        try {
            JPANaturalPerson jpa = query.firstResult();
            jpa.delete();
        } catch (PersistenceException e) {
            throw new WrappingException(new DeletionFailedException(event.getMetadata().getIdentity(), e));
        }
    }
}
