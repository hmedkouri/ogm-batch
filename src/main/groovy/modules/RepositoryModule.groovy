package modules

import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import entity.FRA
import entity.IRS
import entity.Trade
import services.persist.TradeService
import services.persist.TradeServiceImpl

class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {

        install(new Neo4jPersistModule())

        bind(String.class).annotatedWith(Names.named("neo4j.ogm.packages")).toInstance("entity")

        bind(new TypeLiteral<TradeService<Trade>>(){}).to(new TypeLiteral<TradeServiceImpl<Trade>>(){})
        bind(new TypeLiteral<TradeService<IRS>>(){}).to(new TypeLiteral<TradeServiceImpl<IRS>>(){})
        bind(new TypeLiteral<TradeService<FRA>>(){}).to(new TypeLiteral<TradeServiceImpl<FRA>>(){})
    }

}