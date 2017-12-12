package services.upload.strategy

import com.google.inject.AbstractModule
import com.google.inject.name.Names

class StrategyModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(PersistStrategy).annotatedWith(Names.named("sequential")).to(SequentialPersistStrategy)
        bind(PersistStrategy).annotatedWith(Names.named("parallel")).to(ParallelPersistStrategy)
    }
}
