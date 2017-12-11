package services.upload

import com.acuo.collateral.transform.Transformer
import com.acuo.collateral.transform.TransformerOutput
import entity.Trade
import groovy.util.logging.Slf4j
import org.apache.commons.io.IOUtils
import services.persist.TradeService

import javax.inject.Inject
import javax.inject.Named

import static java.util.stream.Collectors.toList

@Slf4j
class TradeUploadServiceImpl implements TradeUploadService {

    private final TradeService<Trade> tradeService
    private final Transformer<Void, com.acuo.common.model.trade.Trade> portfolioTransformer

    @Inject
    TradeUploadServiceImpl(TradeService<Trade> tradeService,
                           @Named("portfolio") Transformer<Void, com.acuo.common.model.trade.Trade> portfolioTransformer) {
        this.tradeService = tradeService
        this.portfolioTransformer = portfolioTransformer
    }

    @Override
    List<String> fromExcel(InputStream fis) {
        try {
            List<Trade> trades = buildTrades(fis)
            return trades.stream()
                    .map{ trade -> trade.getTradeId() }
                    .map{ id -> id.toString() }
                    .collect(toList())
        } catch (Exception e) {
            log.error(e.getMessage(), e)
            return Collections.emptyList()
        }
    }

    private List<Trade> buildTrades(InputStream fis) throws IOException {
        TransformerOutput<com.acuo.common.model.trade.Trade> output = portfolioTransformer.deserialise(IOUtils.toByteArray(fis))
        List<com.acuo.common.model.trade.Trade> list = output.results()
        List<Trade> tradeIdList = list.stream()
                .map{ trade -> buildTradeNew(trade) }
                .collect(toList())
        if (tradeIdList != null && tradeIdList.size() > 0) {
            tradeService.createOrUpdate(tradeIdList)
        }
        return tradeIdList
    }

    private Trade buildTradeNew(com.acuo.common.model.trade.Trade t) {
        Trade trade = TradeConverter.build(t)
        //linkPortfolio(trade, t.getInfo().getPortfolio())
        //linkAccount(trade, t.getInfo().getTradingAccountId())
        return trade
    }

//    void linkPortfolio(Trade trade, String portfolioId) {
//        if(log.isDebugEnabled()) {
//            log.debug("linking to portfolioId: {}", portfolioId)
//        }
//        Portfolio portfolio = portfolio(PortfolioId.fromString(portfolioId))
//        trade.setPortfolio(portfolio)
//    }
//
//    void linkAccount(Trade trade, String accountId) {
//        if(log.isDebugEnabled()) {
//            log.debug("linking to accountId: {}", accountId)
//        }
//        TradingAccount account = account(accountId)
//        trade.setAccount(account)
//    }
}
