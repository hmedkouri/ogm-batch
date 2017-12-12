package entity

import com.acuo.common.model.trade.FxSwapTrade
import com.acuo.common.model.trade.TradeInfo
import ids.TradeId
import org.neo4j.ogm.annotation.Relationship


class FxSwap extends Trade<FxSwap> {

    FxSwap() {
    }

    FxSwap(FxSwapTrade model) {
        final TradeInfo info = model.getInfo()
        setTradeType("FXSWAP")
        setTradeId(TradeId.fromString(info.getTradeId()))
        setTradeDate(info.getTradeDate())
        setTradeTime(info.getTradeTime() != null ? info.getTradeTime().toLocalTime() : null)
        setMaturity(info.getMaturityDate())
        setClearingDate(info.getClearedTradeDate())

        PricingSource pricingSource = new PricingSource()
        pricingSource.setName(PricingProvider.Markit)
        setPricingSource(pricingSource)

        final com.acuo.common.model.product.fx.FxSwap product = model.getProduct()
        this.nearLeg = new FxSingle(product.getNearLeg())
        this.farLeg = new FxSingle(product.getFarLeg())
    }

    FxSwapTrade model() {
        TradeInfo info = new TradeInfo()
        info.setTradeId(getTradeId().toString())
        info.setClearedTradeId(getTradeId().toString())
        info.setClearedTradeDate(getClearingDate())
        info.setTradeDate(getTradeDate())
        info.setBook(getAccount() != null ? getAccount().getAccountId() : null)
        info.setPortfolio(getPortfolio() != null ? getPortfolio().getPortfolioId().toString() : null)

        com.acuo.common.model.product.fx.FxSwap product = new com.acuo.common.model.product.fx.FxSwap()
        product.setNearLeg(nearLeg.model())
        product.setFarLeg(farLeg.model())
        final FxSwapTrade fxSwapTrade = new FxSwapTrade()
        fxSwapTrade.setInfo(info)
        fxSwapTrade.setProduct(product)
        return fxSwapTrade
    }

    @Relationship(type = "NEAR")
    FxSingle nearLeg

    @Relationship(type = "FAR")
    FxSingle farLeg
}
