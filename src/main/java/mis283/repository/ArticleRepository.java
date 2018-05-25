package mis283.repository;

import mis283.model.Article;
import mis283.model.Site;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ArticleRepository extends BaseRepository<Article> {
    private static final ArticleRepository singleton = new ArticleRepository();
    public static ArticleRepository getArticleRepository() { return singleton; }

    public Collection<Article> retrieveAll(final EntityManager entityManager) {
        return retrieveAll(Article.class, entityManager);
    }

    public Optional<Article> retrieve(final int id, final EntityManager entityManager) {
        final Article article = new Article();
        article.setId( id );
        return retrieve(article, entityManager);
    }

    public List<Article> retrieve(final Site site, final EntityManager em) {
        try {
            em.getTransaction().begin();
            final Query query = em.createQuery( "select a from Article a where a.site = :site order by a.title" );
            query.setParameter( "site", site );
            return query.getResultList();
        } finally {
            em.getTransaction().commit();
        }
    }

    public List<Article> retrieve(final String title, final EntityManager em) {
        try {
            em.getTransaction().begin();
            final Query query = em.createQuery( "select a from Article a where a.title = :title" );
            query.setParameter( "title", title );
            return query.getResultList();
        } finally {
            em.getTransaction().commit();
        }
    }

    public Article save(final Article entity, final EntityManager em) {
        return super.save(entity, em);
    }

    public void delete(final Integer id, final EntityManager em) {
        final Optional<Article> option = retrieve(id, em);
        if (option.isPresent()) delete(option.get(), em);
    }

    @Override
    public void delete(final Article article, final EntityManager em) {
        super.delete(article, em);
    }
}