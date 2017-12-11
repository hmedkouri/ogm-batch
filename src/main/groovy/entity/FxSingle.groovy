package entity

import com.acuo.common.model.AdjustableDate
import com.acuo.common.model.BusinessDayAdjustment
import com.opengamma.strata.basics.currency.CurrencyAmount
import com.opengamma.strata.basics.date.BusinessDayConvention
import com.opengamma.strata.basics.date.HolidayCalendarId
import converters.BusinessDayConventionConverter
import converters.CurrencyAmountConverter
import converters.LocalDateConverter
import org.neo4j.ogm.annotation.typeconversion.Convert

import java.time.LocalDate

import static java.util.stream.Collectors.toSet

class FxSingle extends Entity<FxSingle> {

    @Convert(LocalDateConverter)
    LocalDate payDate

    @Convert(BusinessDayConventionConverter)
    BusinessDayConvention payConvention

    @Convert(CurrencyAmountConverter)
    CurrencyAmount baseCurrencyAmount

    @Convert(CurrencyAmountConverter)
    CurrencyAmount counterCurrencyAmount

    Set<String> payHolidays

    @Convert(LocalDateConverter)
    LocalDate fixingDate

    @Convert(BusinessDayConventionConverter)
    BusinessDayConvention fixingConvention

    Set<String> fixingHolidays

    double rate

    FxSingle(com.acuo.common.model.product.fx.FxSingle leg) {
        setBaseCurrencyAmount(leg.getBaseCurrencyAmount())
        setCounterCurrencyAmount(leg.getCounterCurrencyAmount())
        //setPaymentDate(new AdjustableDate(leg.getPaymentDate()));
        setPayDate(leg.getPaymentDate().getDate())
        setPayConvention(leg.getPaymentDate().getAdjustment().getBusinessDayConvention())
        setPayHolidays(leg.getPaymentDate().getAdjustment()
                .getHolidays()
                .stream()
                .map{ it.toString() }
                .collect(toSet()))
        //setFixingDate(new AdjustableDate(leg.getFixingDate()));
        setFixingDate(leg.getFixingDate().getDate())
        setFixingConvention(leg.getFixingDate().getAdjustment().getBusinessDayConvention())
        setFixingHolidays(leg.getFixingDate().getAdjustment()
                .getHolidays()
                .stream()
                .map{ it.toString() }
                .collect(toSet()))
        setNonDeliverable(leg.isNonDeliverable())
        setRate(leg.getRate())
    }

    com.acuo.common.model.product.fx.FxSingle model() {
        com.acuo.common.model.product.fx.FxSingle model = new com.acuo.common.model.product.fx.FxSingle()
        model.setBaseCurrencyAmount(baseCurrencyAmount)
        model.setCounterCurrencyAmount(counterCurrencyAmount)
        //model.setPaymentDate(paymentDate.model());
        AdjustableDate paymentDate = new AdjustableDate()
        paymentDate.setDate(payDate)
        BusinessDayAdjustment payAdjustment = new BusinessDayAdjustment()
        payAdjustment.setBusinessDayConvention(payConvention)
        payAdjustment.setHolidays(payHolidays.stream().map{ HolidayCalendarId.of(it) }.collect(toSet()))
        paymentDate.setAdjustment(payAdjustment)
        model.setPaymentDate(paymentDate)
        //model.setFixingDate(fixingDate.model());
        AdjustableDate fixingDate = new AdjustableDate()
        fixingDate.setDate(this.fixingDate)
        BusinessDayAdjustment fixingAdjustment = new BusinessDayAdjustment()
        fixingAdjustment.setBusinessDayConvention(fixingConvention)
        fixingAdjustment.setHolidays(fixingHolidays.stream().map{ HolidayCalendarId.of(it) }.collect(toSet()))
        fixingDate.setAdjustment(payAdjustment)
        model.setFixingDate(fixingDate)
        model.setNonDeliverable(nonDeliverable)
        model.setRate(rate)
        return model
    }

    boolean nonDeliverable
}
