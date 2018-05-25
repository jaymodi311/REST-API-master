package mis283.repository;

import mis283.model.EntityWithId;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Optional;

import static java.lang.String.format;

public class BaseRepository<T extends EntityWithId> {

    Collection<T> retrieveAll(final Class<T> t, final EntityManager em) {
        try {
            em.getTransaction().begin();
            return em.createQuery(format("select c from %s c", t.getSimpleName())).getResultList();
        } finally {
            em.getTransaction().commit();
        }
    }

    Optional<T> retrieve(final T entity, final EntityManager em) {
        try {
            em.getTransaction().begin();
            final T t = em.find((Class<T>) entity.getClass(), entity.getId());
            return Optional.ofNullable(t);
        } finally {
            em.getTransaction().commit();
        }
    }

    T save(final T entity, final EntityManager em) {
        try {
            em.getTransaction().begin();
            if (entity.getId() == null) {
                em.persist(entity);
                return entity;
            } else return em.merge(entity);
        } catch (final Throwable t) {
            em.getTransaction().rollback();
            throw t;
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().commit();
        }
    }

    void delete(final T entity, final EntityManager em) {
        try {
            em.getTransaction().begin();
            em.remove(entity);
        } catch (final Throwable t) {
            em.getTransaction().rollback();
            throw t;
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().commit();
        }
    }

    public static boolean notEmpty(final String value) {
        return ((value != null) && !value.isEmpty());
    }
}