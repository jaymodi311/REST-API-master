package mis283.repository;

import mis283.model.Site;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;

import static java.lang.String.format;
import static mis283.repository.Datastore.getDatastore;
import static mis283.repository.SiteRepository.getSiteRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SiteRepositoryTest {
    private static final String name = "Unit Test Site";
    private static final URL url;
    private static int id = 0;
    private EntityManager entityManager;

    static {
        URL temp = null;

        try {
            temp = new URL("http://test.com/");
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }

        url = temp;
    }

    @BeforeClass
    public static void create() {
        final EntityManager entityManager = getDatastore().getManager();
        try {
            create(entityManager);
        } finally {
            entityManager.close();
        }
    }

    static void create(final EntityManager entityManager) {
        final Site site = new Site();
        site.setName(name);
        site.setUrl(url);

        final Site result = getSiteRepository().save(site, entityManager);
        assertTrue(result.getId() > 0);
        id = result.getId();
    }

    @Before
    public void before() {
        entityManager = getDatastore().getManager();
    }

    @Test
    public void retrieveAll() {
        final Collection<Site> results = getSiteRepository().retrieveAll(entityManager);
        assertFalse(results.isEmpty());

        boolean found = false;
        final Site test = defaultSite();

        for (final Site site : results) {
            if (test.equals(site)) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    public void retrieve() {
        final Optional<Site> optional = getSiteRepository().retrieve(id, entityManager);
        assertTrue(optional.isPresent());
        assertEquals(defaultSite(), optional.get());
    }

    @Test
    public void retrieveByName() {
        final Optional<Site> optional = getSiteRepository().retrieve(name, entityManager);
        assertTrue(optional.isPresent());
        assertEquals(defaultSite(), optional.get());
    }

    @Test
    public void save() {
        final Site site = defaultSite();
        final String modified = format("%s modified", name);
        site.setName(modified);

        getSiteRepository().save(site, entityManager);
        Optional<Site> optional = getSiteRepository().retrieve(id, entityManager);
        assertTrue(optional.isPresent());
        assertEquals(modified, optional.get().getName());

        site.setName(name);
        getSiteRepository().save(site, entityManager);
        optional = getSiteRepository().retrieve(id, entityManager);
        assertTrue(optional.isPresent());
        assertEquals(name, optional.get().getName());
    }

    @After
    public void after() {
        entityManager.close();
    }

    @AfterClass
    public static void delete() {
        final EntityManager entityManager = getDatastore().getManager();
        try {
            delete(entityManager);
        } finally {
            entityManager.close();
        }
    }

    static void delete(final EntityManager entityManager) {
        getSiteRepository().delete(id, entityManager);
        final Optional<Site> optional = getSiteRepository().retrieve(id, entityManager);
        assertFalse(optional.isPresent());
    }

    static Site defaultSite() {
        final Site site = new Site();
        site.setId(id);
        site.setName(name);
        site.setUrl(url);
        return site;
    }
}