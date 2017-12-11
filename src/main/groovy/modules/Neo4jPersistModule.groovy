package modules

import com.google.common.util.concurrent.Service
import com.google.inject.multibindings.Multibinder
import com.google.inject.persist.PersistModule
import com.google.inject.persist.PersistService
import com.google.inject.persist.UnitOfWork
import org.aopalliance.intercept.MethodInterceptor
import org.neo4j.ogm.session.Session

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

class Neo4jPersistModule extends PersistModule {

    private final MethodInterceptor transactionInterceptor = new TransactionInterceptor()

    @Override
    protected void configurePersistence() {

        bind(Packages.class).toProvider(PackagesProvider.class)

        bind(Neo4jPersistService.class).in(Singleton.class)
        bind(PersistService.class).to(Neo4jPersistService.class)
        bind(UnitOfWork.class).to(Neo4jPersistService.class)
        bind(Session.class).toProvider(Neo4jPersistService.class)

        requestInjection(transactionInterceptor)

        Multibinder<Service> services = Multibinder.newSetBinder(binder(), Service.class)
        services.addBinding().to(Neo4jPersistService.class)
    }

    @Override
    protected MethodInterceptor getTransactionInterceptor() {
        return this.transactionInterceptor
    }

    static class PackagesProvider implements Provider<Packages> {

        private final String[] packages

        @Inject
        PackagesProvider(@Named("neo4j.ogm.packages") String packages) {
            this.packages = packages.split(",")
        }

        @Override
        Packages get() {
            return { -> packages } as Packages
        }
    }
}
