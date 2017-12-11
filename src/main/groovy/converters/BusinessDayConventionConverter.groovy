package converters

import com.opengamma.strata.basics.date.BusinessDayConvention
import groovy.util.logging.Slf4j
import org.neo4j.ogm.typeconversion.AttributeConverter

import java.util.Objects

@Slf4j
class BusinessDayConventionConverter implements AttributeConverter<BusinessDayConvention, String> {

    @Override
    String toGraphProperty(BusinessDayConvention value) {
        if (Objects.isNull(value)) return null
        return value.getName()
    }

    @Override
    BusinessDayConvention toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null
        try {
            return BusinessDayConvention.of(value)
        }
        catch (Exception e) {
            log.error("error in BusinessDayConvention:", e)
            return null
        }
    }
}
