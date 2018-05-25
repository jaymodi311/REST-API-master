package mis283.repository;

import mis283.model.Article;
import mis283.model.Site;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static mis283.repository.ArticleRepository.getArticleRepository;
import static mis283.repository.Datastore.getDatastore;
import static mis283.repository.SiteRepository.getSiteRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ArticleRepositoryTest {
    private static final String title = "Unit Test Title";
    private static final String subtitle = "Unit Test Sub Title";
    private static final String description = "Unit Test Description for article.";
    private static int id = 0;
    private static Site site;
    private EntityManager entityManager;

    @BeforeClass
    public static void create() {
        final EntityManager entityManager = getDatastore().getManager();
        try {
            SiteRepositoryTest.create(entityManager);
            site = SiteRepositoryTest.defaultSite();
            final Optional<Site> optional = getSiteRepository().retrieve(site.getId(), entityManager);
            assertTrue(optional.isPresent());
            site = optional.get();

            final Article article = new Article();
            article.setTitle(title);
            article.setSubtitle(subtitle);
            article.setDescription(description);
            article.setSite(Optional.of(site));

            final Article result = getArticleRepository().save(article, entityManager);
            assertTrue(result.getId() > 0);
            id = result.getId();
        } finally {
            entityManager.close();
        }
    }

    @Before
    public void before() {
        entityManager = getDatastore().getManager();
    }

    @Test
    public void retrieveAll() {
        final Collection<Article> results = getArticleRepository().retrieveAll(entityManager);
        assertFalse(results.isEmpty());
        check(results);
    }

    @Test
    public void retrieve() {
        final Optional<Article> optional = getArticleRepository().retrieve(id, entityManager);
        assertTrue(optional.isPresent());
        assertEquals(defaultArticle(), optional.get());
        assertEquals(site, optional.get().getSite().get());
    }

    @Test
    public void retrieveBySite() {
        final List<Article> list = getArticleRepository().retrieve(site, entityManager);
        assertFalse(list.isEmpty());
        check(list);
    }

    @Test
    public void retrieveByTitle() {
        final List<Article> list = getArticleRepository().retrieve(title, entityManager);
        assertFalse(list.isEmpty());
        check(list);
    }

    @Test
    public void save() {
        final Article article = defaultArticle();
        final String modified = format("%s modified", subtitle);
        article.setSubtitle(modified);

        getArticleRepository().save(article, entityManager);
        Optional<Article> optional = getArticleRepository().retrieve(id, entityManager);
        assertTrue(optional.isPresent());
        assertEquals(modified, optional.get().getSubtitle());

        article.setSubtitle(subtitle);
        getArticleRepository().save(article, entityManager);
        optional = getArticleRepository().retrieve(id, entityManager);
        assertTrue(optional.isPresent());
        assertEquals(subtitle, optional.get().getSubtitle());
    }

    @After
    public void after() {
        entityManager.close();
    }

    @AfterClass
    public static void delete() {
        final EntityManager entityManager = getDatastore().getManager();
        try {
            getArticleRepository().delete(id, entityManager);
            final Optional<Article> optional = getArticleRepository().retrieve(id, entityManager);
            assertFalse(optional.isPresent());
            SiteRepositoryTest.delete();
        } finally {
            entityManager.close();
        }
    }

    private void check(final Collection<Article> collection) {
        boolean found = false;
        final Article test = defaultArticle();

        for (final Article article : collection) {
            if (test.getId().equals(article.getId())) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    private static Article defaultArticle() {
        final Article article = new Article();
        article.setId(id);
        article.setTitle(title);
        article.setSubtitle(subtitle);
        article.setDescription(description);
        article.setSite(Optional.of(site));
        return article;
    }
}