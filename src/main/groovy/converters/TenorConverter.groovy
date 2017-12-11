package converters

import com.opengamma.strata.basics.date.Tenor
import org.neo4j.ogm.typeconversion.AttributeConverter

import java.util.Objects

class TenorConverter implements AttributeConverter<Tenor, String> {
    @Override
    String toGraphProperty(Tenor value) {
        if (Objects.isNull(value)) return null
        return value.toString()
    }

    @Override
    Tenor toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null
        return Tenor.parse(value)
    }
}
