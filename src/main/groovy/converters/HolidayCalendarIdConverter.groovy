package converters

import com.opengamma.strata.basics.date.HolidayCalendarId
import org.neo4j.ogm.typeconversion.AttributeConverter

class HolidayCalendarIdConverter implements AttributeConverter<HolidayCalendarId, String> {
    @Override
    String toGraphProperty(HolidayCalendarId value) {
        return value.getName()
    }

    @Override
    HolidayCalendarId toEntityAttribute(String value) {
        return HolidayCalendarId.of(value)
    }
}
