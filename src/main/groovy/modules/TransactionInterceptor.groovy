package modules

import com.google.inject.persist.Transactional
import groovy.util.logging.Slf4j
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.neo4j.ogm.session.Session
import org.neo4j.ogm.transaction.Transaction

import javax.inject.Inject
import java.sql.SQLException

/**
 * Interceptor for {@code @Transactional} methods.
 *
 * @author cgdecker@gmail.com (Colin Decker)
 */
@Slf4j
class TransactionInterceptor implements MethodInterceptor {

    @Inject
    Neo4jPersistService persistService

    private final ThreadLocal<Boolean> workStarted = new ThreadLocal<Boolean>()

    Object invoke(MethodInvocation invocation) throws Throwable {
        startWorkIfNecessary()

        Session session = persistService.get()
        Transaction transaction = session.getTransaction()

        if (transaction != null) {
            if (transaction.status().equals(Transaction.Status.OPEN) || transaction.status().equals(Transaction.Status.PENDING)) {
                return invocation.proceed()
            }
        }

        transaction = session.beginTransaction()

        try {
            Object result = invocation.proceed()
            transaction.commit()
            return result
        } catch (Exception e) {
            throw rollbackOrCommit(transaction, e, invocation)
        } finally {
            endWorkIfNecessary()
        }
    }

    /**
     * Starts a unit of work if one isn't currently active.
     */

    private void startWorkIfNecessary() {
        if (!persistService.isWorking()) {
            persistService.begin()
            workStarted.set(true)
        }
    }

    /**
     * Ends the current unit of work if it was started by this invocation.
     */
    private void endWorkIfNecessary() {
        if (workStarted.get() != null) {
            workStarted.remove()
            persistService.end()
        }
    }

    /**
     * Rolls back or commits the transaction depending on the type of exception that occurred and what
     * types of exceptions the metadata specifies should cause rollbacks.
     *
     * @param transaction the active transaction
     * @param e           the exception that occurred
     * @param invocation  the method invocation
     * @return the given exception, for re-throwing
     */
    private static Exception rollbackOrCommit(Transaction transaction, Exception e,
                                              MethodInvocation invocation) {
        Transactional metadata = getTransactionalAnnotation(invocation)
        try {
            if (shouldRollback(e, metadata))
                transaction.rollback()
            else
                transaction.commit()
        } catch (Exception exp) {
            log.error("fail to rollback or commit, invocation {}, exception {}, cause {}",
                    invocation.getMethod(), e.getMessage(), exp.getMessage())
        }
        return e
    }

    private static final Transactional DEFAULT_METADATA = AnnotationHolder.class.getAnnotation(Transactional.class)

    /**
     * Gets the {@code @Transactional} metadata for the given method invocation. Attempts to read it
     * first from the method that is being invoked, then from the class of the object the method is
     * being invoked on. If it doesn't find it either of those places, the default is used.
     *
     * @param invocation the method invocation
     * @return transactional metadata for the invocation
     */
    private static Transactional getTransactionalAnnotation(MethodInvocation invocation) {
        Transactional result = invocation.getMethod().getAnnotation(Transactional.class)
        if (result == null)
            result = invocation.getThis().getClass().getAnnotation(Transactional.class)
        if (result == null)
            result = DEFAULT_METADATA
        return result
    }

    /**
     * Checks if a rollback should occur based on the type of exception and the given metadata. A
     * rollback should only occur if the exception is both a type that should be rolled back on and
     * not a type that should be ignored. In addition to any types defined in the metadata, we always
     * roll back on {@link SQLException}s unless the metadata specifies that it should be ignored.
     *
     * @param e        the exception that occurred
     * @param metadata the metadata telling what types of exceptions should cause rollbacks and what
     *                 types should not
     * @return {@code true} if the transaction should be rolled back; {@code false} if it should be
     * committed
     */
    private static boolean shouldRollback(Exception e, Transactional metadata) {
        return (isInstance(e, metadata.rollbackOn()) || SQLException.class.isInstance(e)) &&
                !isInstance(e, metadata.ignore())
    }

    /**
     * Checks if the given object is an instance of one of the given types.
     *
     * @param obj   the object to check
     * @param types the types to check for
     * @return {@code true} if the object is an instance of one of the given types;
     * {@code false} otherwise.
     */
    private static boolean isInstance(Object obj, Class<?>[] types) {
        for (Class<?> type : types) {
            if (type.isInstance(obj))
                return true
        }
        return false
    }

    @Transactional
    private static class AnnotationHolder {
    }
}