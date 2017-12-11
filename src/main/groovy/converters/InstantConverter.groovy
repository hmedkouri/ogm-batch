package converters

import org.neo4j.ogm.typeconversion.AttributeConverter

import java.time.Instant

class InstantConverter implements AttributeConverter<Instant, Long> {

    @Override
    Long toGraphProperty(Instant value) {
        return value.toEpochMilli()
    }

    @Override
    Instant toEntityAttribute(Long value) {
        return Instant.ofEpochMilli(value)
    }
}
