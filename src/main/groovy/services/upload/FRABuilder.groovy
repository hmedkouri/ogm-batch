package services.upload

import com.acuo.common.model.AdjustableDate
import com.acuo.common.model.AdjustableSchedule
import com.acuo.common.model.BusinessDayAdjustment
import com.acuo.common.model.product.FRA
import com.acuo.common.model.trade.FRATrade
import com.acuo.common.model.trade.ProductType
import com.acuo.common.model.trade.TradeInfo
import com.google.common.collect.ImmutableSet
import com.opengamma.strata.basics.date.HolidayCalendarId
import com.opengamma.strata.basics.date.Tenor
import com.opengamma.strata.basics.schedule.RollConvention
import com.opengamma.strata.product.common.PayReceive
import com.opengamma.strata.product.swap.FixingRelativeTo
import entity.Leg
import entity.PricingProvider
import entity.PricingSource
import entity.Trade

class FRABuilder extends TradeBuilder {

    Trade build(FRATrade fraTrade) {
        FRA fra = fraTrade.getProduct()
        TradeInfo tradeInfo = fraTrade.getInfo()

        Set<Leg> payLegs = new HashSet<>()
        Set<Leg> receiveLegs = new HashSet<>()

        entity.FRA entity = new entity.FRA()
        setTradeDetails(fraTrade, entity)
        entity.tradeType = tradeInfo.getDerivativeType()
        entity.payLegs = payLegs
        entity.receiveLegs = receiveLegs

        if (TradeBuilder.TRADE_TYPE_BILATERAL.equalsIgnoreCase(tradeInfo.getDerivativeType())) {
            PricingSource pricingSource = new PricingSource(name: PricingProvider.Markit)
            entity.pricingSource = pricingSource
        } else {
            PricingSource pricingSource = new PricingSource(name: PricingProvider.Clarus)
            pricingSource.setName(PricingProvider.Clarus)
            entity.setPricingSource(pricingSource)
        }

        //leg
        int legId = 0
        for (FRA.FRALeg fraLeg : fra.getLegs()) {
            Leg leg = new Leg()
            if (PayReceive.PAY.equals(fraLeg.getPayReceive()))
                payLegs.add(leg)
            else
                receiveLegs.add(leg)

            legId++
            leg.setLegId(tradeInfo.getTradeId() + "-" + legId)
            leg.setType(fraLeg.getType())
            leg.setCurrency(fraLeg.getCurrency())
            leg.setPayStart(fraLeg.getStartDate().getDate())
            leg.setPayEnd(fraLeg.getMaturityDate().getDate())
            leg.setDayCount(fraLeg.getDaycount())
            leg.setNotional(fraLeg.getNotional())
            if (fraLeg.getRate() != null)
                leg.setFixedRate(fraLeg.getRate() / 100)
            if (fraLeg.getPaymentDate().getAdjustment() != null) {
                if (fraLeg.getPaymentDate().getAdjustment().getHolidays() != null &&
                    fraLeg.getPaymentDate().getAdjustment().getHolidays().size() > 0) {
                    final String name = fraLeg.getPaymentDate().getAdjustment().getHolidays().iterator().next().getName()
                    leg.setRefCalendar(name)
                }
                leg.setBusinessDayConvention(fraLeg.getPaymentDate().getAdjustment().getBusinessDayConvention())
            }

            if (fraLeg.getFixing() != null) {
                FRA.FRALegFixing fixing = fraLeg.getFixing()
                leg.setIndex(fixing.getFloatingRateName())
                leg.setIndexTenor(fixing.getTenor())
            }
        }
        return entity
    }

    FRATrade buildTrade(entity.FRA trade) {
        FRATrade fraTrade = new FRATrade()
        FRA fra = new FRA()
        fraTrade.setProduct(fra)
        fraTrade.setType(ProductType.FRA)

        TradeInfo info = buildTradeInfo(trade)
        info.setTradeId(trade.getTradeId().toString())
        info.setClearedTradeId(trade.getTradeId().toString())
        info.setPortfolio(trade.getPortfolio().getPortfolioId().toString())
        info.setDiscountMethod("ISDA")
        fraTrade.setInfo(info)

        Set<Leg> receiveLegs = trade.getReceiveLegs()
        if(receiveLegs != null)
            for (Leg receiveLeg : receiveLegs) {
                FRA.FRALeg leg = buildFRALeg(1,receiveLeg)
                leg.setPayReceive(PayReceive.RECEIVE)
                leg.setRollConvention(RollConvention.ofDayOfMonth(10))
                fra.addLeg(leg)
            }

        Set<Leg> payLegs = trade.getPayLegs()
        if(payLegs != null)
            for (Leg payLeg : payLegs) {
                FRA.FRALeg leg = buildFRALeg(2, payLeg)
                leg.setPayReceive(PayReceive.PAY)
                leg.setRollConvention(RollConvention.ofDayOfMonth(10))
                leg.setNotional( -1 * leg.getNotional())
                fra.addLeg(leg)
            }
        return fraTrade
    }

    private FRA.FRALeg buildFRALeg(int id, Leg leg) {
        FRA.FRALeg  result = new FRA.FRALeg ()

        result.setId(id)
        result.setCurrency(leg.getCurrency())
        result.setNotional(leg.getNotional())
        result.setRate(leg.getFixedRate())
        result.setDaycount(leg.getDayCount())
        result.setType(leg.getType())

        AdjustableDate adjustableDate = new AdjustableDate()
        adjustableDate.setDate(leg.getPayStart())
        BusinessDayAdjustment adjustment = new BusinessDayAdjustment()
        adjustment.setBusinessDayConvention(leg.getBusinessDayConvention())

        HolidayCalendarId holidays = holidays(leg)

        adjustment.setHolidays(ImmutableSet.of(holidays))
        adjustableDate.setAdjustment(adjustment)
        result.setStartDate(adjustableDate)

        adjustableDate = new AdjustableDate()
        adjustableDate.setDate(leg.getPayEnd())
        adjustableDate.setAdjustment(adjustment)
        result.setMaturityDate(adjustableDate)
        result.setPaymentDate(adjustableDate)

        AdjustableSchedule adjustableSchedule = new AdjustableSchedule()
        adjustableSchedule.setFrequency(leg.getPaymentFrequency())
        adjustableSchedule.setAdjustment(adjustment)

        if ("FLOAT".equals(leg.getType())) {
            FRA.FRALegFixing swapLegFixing = new FRA.FRALegFixing ()
            result.setFixing(swapLegFixing)

            swapLegFixing.setTenor(leg.getIndexTenor())
            if(swapLegFixing.getTenor() == null)
                swapLegFixing.setTenor(Tenor.TENOR_1D)

            swapLegFixing.setFloatingRateName(leg.getIndex())

            swapLegFixing.setFixingRelativeTo(FixingRelativeTo.PERIOD_START)
        }
        return result
    }
}
