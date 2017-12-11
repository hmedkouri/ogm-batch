package services

import com.google.inject.persist.Transactional
import com.opengamma.strata.collect.ArgChecker
import groovy.util.logging.Slf4j
import org.neo4j.ogm.session.Session

import javax.inject.Inject
import javax.inject.Provider

import static com.opengamma.strata.collect.ArgChecker.notEmpty

@Slf4j
@Transactional
final class GenericService<T, ID extends Serializable> implements Service<T, ID> {

    private static final int DEPTH_LIST = 0
    private static final int DEPTH_ENTITY = 1

    private final Provider<Session> sessionProvider
    private final Class<T> type

    @Inject
    GenericService(Provider<Session> sessionProvider, Class<T> type) {
        this.sessionProvider = sessionProvider
        this.type = type
    }

    private Class<T> getEntityType() {
        return type
    }

    @Override
    <S extends T> S save(S entity) {
        entity = ArgChecker.notNull(entity, "entity")
        if (log.isDebugEnabled()) {
            log.debug("save {}",entity)
        }
        getSession().save(entity)
        return entity
    }

    @Override
    <S extends T> Iterable<S> save(Iterable<S> entities) {
        entities = notEmpty(entities, "entities")
        if (log.isDebugEnabled()) {
            log.debug("save {}",entities)
        }
        getSession().save(entities)
        return entities
    }

    @Override
    void delete(ID id) {
        id = notNull(id, "id")
        if (log.isDebugEnabled()) {
            log.debug("delete {}", id)
        }
        getSession().delete(getSession().load(getEntityType(), id))
    }

    @Override
    void delete(T entity) {
        entity = notNull(entity, "entity")
        if (log.isDebugEnabled()) {
            log.debug("delete {}", entity)
        }
        getSession().delete(entity)
    }

    @Override
    void delete(Iterable<? extends T> entities) {
        entities = notEmpty(entities, "entities")
        if (log.isDebugEnabled()) {
            log.debug("delete {}", entities)
        }
        getSession().delete(entities)
    }

    @Override
    void deleteAll() {
        getSession().deleteAll(getEntityType())
    }

    @Override
    <S extends T> S save(S entity, int depth) {
        entity = notNull(entity, "entity")
        if (log.isDebugEnabled()) {
            log.debug("save {} depth {}", entity, depth)
        }
        getSession().save(entity, depth)
        return entity
    }

    @Override
    <S extends T> Iterable<S> save(Iterable<S> entities, int depth) {
        entities = notEmpty(entities, "entities")
        if (log.isDebugEnabled()) {
            log.debug("save {} depth {}", entities, depth)
        }
        getSession().save(entities, depth)
        return entities
    }

    @Override
    <S extends T> S createOrUpdate(S entity) {
        entity = notNull(entity, "entity")
        if (log.isDebugEnabled()) {
            log.debug("createOrUpdate {}", entity)
        }
        getSession().save(entity)
        return entity
    }

    @Override
    Iterable<T> findAll() {
        return findAll(DEPTH_LIST)
    }

    @Override
    Iterable<T> findAll(int depth) {
        if (log.isDebugEnabled()) {
            log.debug("findAll depth [{}]", depth)
        }
        return getSession().loadAll(getEntityType(), depth)
    }

    @Override
    T find(ID id) {
        id = notNull(id, "id")
        if (log.isDebugEnabled()) {
            log.debug("find {}", id)
        }
        return getSession().load(getEntityType(), id, DEPTH_ENTITY)
    }

    @Override
    T find(ID id, int depth) {
        id = notNull(id, "id")
        if (log.isDebugEnabled()) {
            log.debug("find {} depth {}", id, depth)
        }
        return getSession().load(getEntityType(), id, depth)
    }

    Session getSession() {
        final Session session = sessionProvider.get()
        if (log.isDebugEnabled()) {
            log.debug("session {}", session.hashCode())
        }
        return session
    }
}