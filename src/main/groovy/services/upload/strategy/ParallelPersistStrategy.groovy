package services.upload.strategy

import entity.Trade
import services.persist.TradeService

import javax.inject.Inject
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.locks.ReentrantLock

class ParallelPersistStrategy implements PersistStrategy {

    private final ReentrantLock lock = new ReentrantLock();
    private final TradeService<Trade> tradeService

    @Inject
    ParallelPersistStrategy(TradeService<Trade> tradeService) {
        this.tradeService = tradeService
    }

    @Override
    Iterable<Trade> persist(List<Trade> trades) {
        ConcurrentLinkedDeque<Trade> queue = new ConcurrentLinkedDeque<>()
        trades.groupBy {
            it.tradeType
        }
        .values()
        .parallelStream()
        .forEach{ list ->
                queue.addAll(tradeService.createOrUpdate(list))
        }
        return queue
    }
}
