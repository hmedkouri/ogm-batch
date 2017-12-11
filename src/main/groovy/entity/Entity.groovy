package entity

import org.neo4j.ogm.annotation.GraphId

abstract class Entity<T extends Entity> {

    @GraphId
    private Long id

    final Long getId() {
        return id
    }

    @Override
    boolean equals(Object o) {
        if (this == o) return true
        if (o == null || id == null || getClass() != o.getClass()) return false

        T entity = (T) o

        return id.equals(entity.getId())
    }

    @Override
    int hashCode() {
        return (id == null) ? -1 : id.hashCode()
    }
}