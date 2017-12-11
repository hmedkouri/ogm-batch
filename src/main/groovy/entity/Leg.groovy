package entity

import com.opengamma.strata.basics.currency.Currency
import com.opengamma.strata.basics.date.BusinessDayConvention
import com.opengamma.strata.basics.date.DayCount
import com.opengamma.strata.basics.date.Tenor
import com.opengamma.strata.basics.index.FloatingRateName
import com.opengamma.strata.basics.schedule.Frequency
import converters.*
import org.neo4j.ogm.annotation.Index
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert

import java.time.LocalDate

@NodeEntity
class Leg extends Entity<Leg> {

    Double notional

    @Convert(DayCountConverter.class)
    DayCount dayCount

    @Convert(BusinessDayConventionConverter.class)
    BusinessDayConvention businessDayConvention

    Double fixedRate

    @Convert(LocalDateConverter.class)
    LocalDate payStart

    @Property(name = "id")
    @Index(primary = true)
    String legId

    String legNumber

    @Convert(FrequencyConverter.class)
    Frequency paymentFrequency

    String type

    @Convert(LocalDateConverter.class)
    LocalDate nextCouponPaymentDate

    @Convert(LocalDateConverter.class)
    LocalDate payEnd

    String refCalendar

    @Convert(TenorConverter.class)
    Tenor indexTenor

    @Convert(FloatingRateNameConverter.class)
    FloatingRateName index

    @Convert(FrequencyConverter.class)
    Frequency resetFrequency

    @Convert(CurrencyConverter.class)
    Currency currency

    Boolean variableCurrency
}
