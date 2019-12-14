package de.kaiserpfalzedv.folders.store.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.UUID;

/*
 *
 *
 * @author rlichti
 * @since 2019-12-14 21:33
 */
@Converter(autoApply = true)
public class UuidConverter implements AttributeConverter<UUID, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UuidConverter.class);

    @Override
    public String convertToDatabaseColumn(final UUID entityValue) {
        return Optional.ofNullable(entityValue)
                .map(entityUuid -> entityUuid.toString())
                .orElse(null);
    }

    @Override
    public UUID convertToEntityAttribute(final String databaseValue) {
        return Optional.ofNullable(databaseValue)
                .map(databaseUuid -> UUID.fromString(databaseUuid))
                .orElse(null);
    }
}