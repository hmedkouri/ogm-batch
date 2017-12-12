package services.upload.strategy

import entity.Trade
import groovy.util.logging.Slf4j
import services.persist.TradeService

import javax.inject.Inject

@Slf4j
class SequentialPersistStrategy implements PersistStrategy {

    private final TradeService<Trade> tradeService

    @Inject
    SequentialPersistStrategy(TradeService<Trade> tradeService) {
        this.tradeService = tradeService
    }

    @Override
    Iterable<Trade> persist(List<Trade> trades) {
        log.info("persisting the trades")
        return tradeService.createOrUpdate(trades)
    }
}
