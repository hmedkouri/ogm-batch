package converters

import org.neo4j.ogm.typeconversion.AttributeConverter

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Objects

class LocalTimeConverter implements AttributeConverter<LocalTime, String> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME

    @Override
    String toGraphProperty(LocalTime value) {
        if (Objects.isNull(value)) return null
        return value.format(formatter)
    }

    @Override
    LocalTime toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null
        return LocalTime.parse(value)
    }
}