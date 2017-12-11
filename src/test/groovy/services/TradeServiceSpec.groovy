package services

import entity.FRA
import ids.TradeId
import modules.InProcessNeo4jServerModule
import modules.RepositoryModule
import services.TradeService
import spock.guice.UseModules
import spock.lang.Specification

import javax.inject.Inject

@UseModules([
        InProcessNeo4jServerModule,
        RepositoryModule
])
class TradeServiceSpec extends Specification {

    @Inject
    TradeService<FRA> fraTradeService

    def "simple save and find trade"() {

        given:
        def tradeId = TradeId.fromString("1")
        def fra = new FRA()
        fra.tradeId = tradeId
        fraTradeService.save(fra)

        when:
        def find = fraTradeService.find(tradeId)

        then:
        find != null
    }
}
