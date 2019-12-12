package de.kaiserpfalzedv.office.folders;

import de.kaiserpfalzedv.office.folders.store.Folder;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.persistence.NoResultException;
import java.util.TimeZone;

@ApplicationScoped
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @ConfigProperty(name = "office-folders.name")
    String name;
    @ConfigProperty(name = "office-folders.version")
    String version;

    @ConfigProperty(name = "office-folders.tz", defaultValue = "UTC")
    String tz;

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
        long folders = 0;
        try {
            folders = Folder.count();
        } catch (NoResultException e) {
            // need nothing to do. Only logging will log wrong number (unless the database is empty, then 0 is correct!
        }

        LOGGER.info("log watchdog. folder count={}", folders);
    }
}
