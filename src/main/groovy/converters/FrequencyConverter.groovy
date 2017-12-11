package converters

import com.opengamma.strata.basics.schedule.Frequency
import groovy.util.logging.Slf4j
import org.neo4j.ogm.typeconversion.AttributeConverter

@Slf4j
class FrequencyConverter implements AttributeConverter<Frequency, String> {
    @Override
    String toGraphProperty(Frequency value) {
        if (Objects.isNull(value)) return null
        return value.toString()
    }

    @Override
    Frequency toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null
        try
        {
            return Frequency.parse(value)
        }
        catch (Exception e)
        {
            log.error("error in FrequencyConverter", e)
            return  null
        }

    }
}
