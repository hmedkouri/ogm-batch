package modules

import groovy.util.logging.Slf4j
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory

import javax.inject.Inject
import javax.inject.Provider

@Slf4j
class SingleNeo4jSessionFactory implements Provider<SessionFactory> {

    private final SessionFactory factory

    @Inject
    SingleNeo4jSessionFactory(Configuration configuration, Packages packages) {
        log.info("Creating a Neo4j Session Factory using config [{}] and packages [{}]", configuration, packages)
        this.factory = new SessionFactory(configuration, packages.value())
    }

    @Override
    SessionFactory get() {
        return this.factory
    }
}
