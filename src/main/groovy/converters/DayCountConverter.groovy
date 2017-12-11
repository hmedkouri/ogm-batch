package converters

import com.opengamma.strata.basics.date.DayCount
import groovy.util.logging.Slf4j
import org.neo4j.ogm.typeconversion.AttributeConverter

@Slf4j
class DayCountConverter implements AttributeConverter<DayCount, String> {
    @Override
    String toGraphProperty(DayCount value) {
        if (Objects.isNull(value)) return null
        return value.getName()
    }

    @Override
    DayCount toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null
        try
        {
            return DayCount.of(value)
        }
        catch (Exception e)
        {
            log.error("error in DayCountConverter", e)
            return null
        }
    }
}
