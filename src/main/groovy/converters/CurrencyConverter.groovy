package converters

import com.opengamma.strata.basics.currency.Currency
import org.neo4j.ogm.typeconversion.AttributeConverter

import java.util.Objects

class CurrencyConverter implements AttributeConverter<Currency, String> {
    @Override
    String toGraphProperty(Currency value) {
        if (Objects.isNull(value)) return null
        return value.getCode()
    }

    @Override
    Currency toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null
        return Currency.parse(value)
    }
}
