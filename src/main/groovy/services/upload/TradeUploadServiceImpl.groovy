package services.upload

import com.acuo.collateral.transform.Transformer
import entity.Trade
import groovy.util.logging.Slf4j
import org.apache.commons.io.IOUtils
import services.upload.strategy.PersistStrategy

import javax.inject.Inject
import javax.inject.Named
import java.util.concurrent.TimeUnit

import static java.util.stream.Collectors.toList

@Slf4j
class TradeUploadServiceImpl implements TradeUploadService {

    private final Transformer<Void, com.acuo.common.model.trade.Trade> portfolioTransformer

    @Inject
    TradeUploadServiceImpl(@Named("portfolio") Transformer<Void, com.acuo.common.model.trade.Trade> portfolioTransformer) {
        this.portfolioTransformer = portfolioTransformer
    }

    @Override
    Iterable<Trade> fromExcel(InputStream fis, PersistStrategy strategy) {
        log.info("start portfolio upload ...")
        long start = System.nanoTime()
        try {
            log.info("parse and buid the trades")
            List<Trade> trades = parseFile(fis)
                    .stream()
                    .map{ trade ->  TradeConverter.build(trade) }
                    .collect(toList())
            return strategy.persist(trades)
        } catch (Exception e) {
            log.error(e.getMessage(), e)
            return Collections.emptyList()
        } finally {
            long end = System.nanoTime()
            log.info("... portfolio upload completed in {}", TimeUnit.NANOSECONDS.toSeconds(end - start))
        }
    }

    def parseFile(InputStream fis) {
        def output = portfolioTransformer.deserialise(IOUtils.toByteArray(fis))
        return output.results()
    }
}
