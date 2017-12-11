package entity

import com.opengamma.strata.basics.currency.Currency
import converters.CurrencyConverter
import converters.LocalDateConverter
import converters.LocalTimeConverter
import converters.TypedStringConverter
import ids.TradeId
import org.neo4j.ogm.annotation.Index
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert

import java.time.LocalDate
import java.time.LocalTime
import java.util.function.Function


@NodeEntity
abstract class Trade<T extends Trade> extends Entity<T> {

    Function<T, String> findQuery() { return { swap -> "" } }

    @Property(name = "id")
    @Index(primary = true)
    @Convert(TypedStringConverter.TradeIdConverter.class)
    public TradeId tradeId

    private String underlyingAssetId

    private Double notional

    private String buySellProtection

    private Double couponRate

    @Convert(LocalDateConverter.class)
    private LocalDate tradeDate

    @Convert(LocalTimeConverter.class)
    private LocalTime tradeTime

    @Convert(LocalDateConverter.class)
    private LocalDate maturity

    @Convert(LocalDateConverter.class)
    private LocalDate clearingDate

    @Convert(CurrencyConverter.class)
    private Currency currency

    private String underlyingEntity

    private Double factor

    private String seniority
}
