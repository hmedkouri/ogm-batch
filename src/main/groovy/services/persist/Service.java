package services.persist;

import java.io.Serializable;

interface Service<T, ID extends Serializable> {

    public <S extends T> S save(S entity);

    public <S extends T> Iterable<S> save(Iterable<S> entities);

    void delete(ID id);

    void delete(T entity);

    void delete(Iterable<? extends T> entities);

    void deleteAll();

    public <S extends T> S save(S entity, int depth);

    public <S extends T> Iterable<S> save(Iterable<S> entities, int depth);

    public <S extends T> S createOrUpdate(S entity);

    Iterable<T> findAll();

    Iterable<T> findAll(int depth);

    T find(ID id);

    T find(ID id, int depth);
}