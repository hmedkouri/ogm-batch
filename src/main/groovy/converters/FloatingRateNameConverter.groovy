package converters

import com.opengamma.strata.basics.index.FloatingRateName
import com.opengamma.strata.basics.index.FloatingRateType
import com.opengamma.strata.basics.index.ImmutableFloatingRateName
import groovy.util.logging.Slf4j
import org.neo4j.ogm.typeconversion.AttributeConverter

@Slf4j
class FloatingRateNameConverter implements AttributeConverter<FloatingRateName, String> {
    @Override
    String toGraphProperty(FloatingRateName value) {
        if (Objects.isNull(value)) return null
        return value.getName()
    }

    @Override
    FloatingRateName toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null
        try{
            return FloatingRateName.of(value)
        } catch (Exception e) {
            log.warn(e.getMessage())
            return ImmutableFloatingRateName.of(value,value, FloatingRateType.IBOR)
        }
    }
}
