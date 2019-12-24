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

package de.kaiserpfalzedv.office.application;

import de.kaiserpfalzedv.base.cdi.JPA;
import de.kaiserpfalzedv.contacts.store.NaturalPersonReadAdapter;
import de.kaiserpfalzedv.folders.store.FolderReadAdapter;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.TimeZone;

@ApplicationScoped
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @ConfigProperty(name = "office.baseservice.name", defaultValue = "office-base")
    String name;
    @ConfigProperty(name = "office.baseservice.version", defaultValue = "0.0.0.failure-in-intialization")
    String version;

    @ConfigProperty(name = "office.tz", defaultValue = "UTC")
    String tz;

    @Inject
    @JPA
    FolderReadAdapter folderStore;

    @Inject
    @JPA
    NaturalPersonReadAdapter naturalPersonStore;


    void onStart(@Observes StartupEvent event) {
        LOGGER.info("Started: {} (v{})", name, version);

        TimeZone.setDefault(TimeZone.getTimeZone(tz));
        LOGGER.trace("Default Timezone is: {}", TimeZone.getDefault());
    }

    void onStop(@Observes ShutdownEvent event) {
        LOGGER.info("Application shutdown.");
    }


    @Scheduled(every = "10s")
    void logWatchdog() {
        long folders = folderStore.count();
        long naturalPersons = naturalPersonStore.count();
        LOGGER.info("log watchdog. folders={}, natural persons={}", folders, naturalPersons);
    }
}
