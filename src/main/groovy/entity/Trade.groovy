package entity

import com.opengamma.strata.basics.currency.Currency
import converters.CurrencyConverter
import converters.LocalDateConverter
import converters.LocalTimeConverter
import converters.TypedStringConverter
import groovy.transform.Canonical
import ids.TradeId
import org.neo4j.ogm.annotation.Index
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.typeconversion.Convert

import java.time.LocalDate
import java.time.LocalTime
import java.util.function.Function


@NodeEntity
@Canonical
abstract class Trade<T extends Trade> extends Entity<T> {

    Function<T, String> findQuery() { return { swap -> "" } }

    @Property(name = "id")
    @Index(primary = true)
    @Convert(TypedStringConverter.TradeIdConverter.class)
    TradeId tradeId

    String underlyingAssetId

    Double notional

    String buySellProtection

    Double couponRate

    @Convert(LocalDateConverter.class)
    LocalDate tradeDate

    @Convert(LocalTimeConverter.class)
    LocalTime tradeTime

    @Convert(LocalDateConverter.class)
    LocalDate maturity

    @Convert(LocalDateConverter.class)
    LocalDate clearingDate

    @Convert(CurrencyConverter.class)
    Currency currency

    String underlyingEntity

    Double factor

    String seniority

    @Relationship(type = "PRICING_SOURCE")
    PricingSource pricingSource

    @Relationship(type = "BELONGS_TO")
    Portfolio portfolio

    @Relationship(type = "POSITIONS_ON", direction = Relationship.INCOMING)
    TradingAccount account
}
