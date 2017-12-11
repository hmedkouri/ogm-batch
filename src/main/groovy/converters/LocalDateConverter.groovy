package converters

import org.neo4j.ogm.typeconversion.AttributeConverter

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Objects

class LocalDateConverter implements AttributeConverter<LocalDate, String> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    @Override
    String toGraphProperty(LocalDate value) {
        if (Objects.isNull(value)) return null
        return value.format(formatter)
    }

    @Override
    LocalDate toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null
        return LocalDate.parse(value,formatter)
    }
}