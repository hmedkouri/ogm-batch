package converters

import com.opengamma.strata.basics.currency.CurrencyAmount
import org.neo4j.ogm.typeconversion.AttributeConverter

import java.util.Objects

class CurrencyAmountConverter implements AttributeConverter<CurrencyAmount, String> {
    @Override
    String toGraphProperty(CurrencyAmount value) {
        if (Objects.isNull(value)) return null
        return value.toString()
    }

    @Override
    CurrencyAmount toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null
        return CurrencyAmount.parse(value)
    }
}
