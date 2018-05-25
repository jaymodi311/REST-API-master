package mis283.repository;

import mis283.model.Site;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Optional;

public class SiteRepository extends BaseRepository<Site> {
    private static final SiteRepository singleton = new SiteRepository();
    public static SiteRepository getSiteRepository() { return singleton; }

    public Collection<Site> retrieveAll(final EntityManager entityManager) {
        return retrieveAll( Site.class, entityManager );
    }

    public Optional<Site> retrieve(final int id, final EntityManager entityManager) {
        final Site site = new Site();
        site.setId(id);
        return retrieve(site, entityManager);
    }

    public Optional<Site> retrieve(final String name, final EntityManager em) {
        try {
            em.getTransaction().begin();
            final Query query = em.createQuery( "select s from Site s where s.name = :name" );
            query.setParameter( "name", name );
            final Site site = (Site) query.getSingleResult();
            return Optional.ofNullable( site );
        }
        finally {
            em.getTransaction().commit();
        }
    }

    public Site save(final Site entity, final EntityManager em) {
        return super.save( entity, em );
    }

    public void delete(final Integer id, final EntityManager entityManager) {
        final Optional<Site> option = retrieve(id, entityManager);
        if (option.isPresent()) delete(option.get(), entityManager);
    }

    @Override
    public void delete(final Site entity, final EntityManager entityManager) {
        super.delete(entity, entityManager);
    }
}
