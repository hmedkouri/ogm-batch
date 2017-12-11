package modules

import com.acuo.collateral.transform.Transformer
import com.acuo.collateral.transform.modules.TransformerModule
import com.acuo.collateral.transform.services.PortfolioImportTransformer
import com.acuo.common.model.trade.Trade
import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral
import com.google.inject.name.Names

class MappingModule extends AbstractModule {

    @Override
    protected void configure() {

        install(new TransformerModule())

        bind(new TypeLiteral<Transformer<Void, Trade>>() {}).annotatedWith(Names.named("portfolio"))
                .to(new TypeLiteral<PortfolioImportTransformer<Void, Trade>>(){})

    }
}