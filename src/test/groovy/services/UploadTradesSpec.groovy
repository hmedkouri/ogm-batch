package services

import modules.InProcessNeo4jServerModule
import modules.MappingModule
import modules.RepositoryModule
import org.junit.Rule
import services.upload.TradeUploadService
import services.upload.UploadModule
import services.upload.strategy.PersistStrategy
import services.upload.strategy.StrategyModule
import spock.guice.UseModules
import spock.lang.Specification
import spock.lang.Subject
import util.ResourceFile

import javax.inject.Inject
import javax.inject.Named

@UseModules([
        InProcessNeo4jServerModule,
        RepositoryModule,
        MappingModule,
        UploadModule,
        StrategyModule
])
class UploadTradesSpec extends Specification {

    @Rule
    public ResourceFile all = new ResourceFile("/excel/TradePortfolio.xlsx")

    @Inject
    @Named("parallel")
    PersistStrategy parallel

    @Inject
    @Named("sequential")
    PersistStrategy sequential

    @Subject
    @Inject
    TradeUploadService uploadService

    void "load all trades sequentially"() {
        when:
        def ids = uploadService.fromExcel(all.createInputStream(), sequential)

        then:
        with(ids){
            !isEmpty()
            size() == 1200
        }

    }

    void "load all trades in parallel"() {
        when:
        def ids = uploadService.fromExcel(all.createInputStream(), parallel)

        then:
        with(ids){
            !isEmpty()
            size() == 1200
        }

    }
}
