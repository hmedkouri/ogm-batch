package services.upload

import com.acuo.common.model.trade.FRATrade
import com.acuo.common.model.trade.FxSwapTrade
import com.acuo.common.model.trade.ProductTrade
import com.acuo.common.model.trade.SwapTrade
import entity.FRA
import entity.FxSwap
import entity.IRS
import entity.Trade
import groovy.util.logging.Slf4j

@Slf4j
class TradeConverter {

    private static SwapBuilder swapBuilder = new SwapBuilder()
    private static FRABuilder fraBuilder = new FRABuilder()

    static Trade build(com.acuo.common.model.trade.Trade trade) {

        if (trade instanceof FRATrade) {
            return fraBuilder.build((FRATrade) trade)
        }

        if (trade instanceof SwapTrade) {
            return swapBuilder.build((SwapTrade) trade)
        }

        if(trade instanceof FxSwapTrade) {
            return new FxSwap((FxSwapTrade) trade)
        }

        throw new UnsupportedOperationException("trade " + trade + " not supported")
    }

    static ProductTrade buildTrade(Trade trade) {

        if (trade instanceof FRA) {
            return fraBuilder.buildTrade((FRA)trade)
        }

        if (trade instanceof IRS) {
            return swapBuilder.buildTrade((IRS)trade)
        }

        if (trade instanceof FxSwap) {
            return ((FxSwap)trade).model()
        }

        throw new UnsupportedOperationException("trade " + trade + " not supported")
    }
}