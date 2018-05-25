package mis283.service;

import mis283.model.Site;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SiteServiceTest {
    private static Site site;
    static {
        site = new Site();
        site.setName("Unit Test Site");
        try {
            site.setUrl(new URL("http://test.com/"));
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static final SiteService service = new SiteService();

    @BeforeClass
    public static void create() {
        site = service.create(site);
        assertNotNull(site.getId());
        assertTrue(site.getId() > 0);
    }

    @Test
    public void retrieve() {
        final Site s = service.get(site.getId());
        assertNotNull(s.getId());
        assertEquals(site.getId(), s.getId());
        assertEquals(site.getName(), s.getName());
        assertEquals(site.getUrl(), s.getUrl());
    }

    @Test
    public void update() {
        final String modified = format("%s modified", site.getName());
        Site s = service.get(site.getId());
        assertNotNull(s.getId());
        s.setName(modified);
        s = service.update(site.getId(), s);
        assertEquals(modified, s.getName());

        s = service.get(s.getId());
        assertEquals(modified, s.getName());

        s.setName(site.getName());
        s = service.update(site.getId(), s);
        assertEquals(site.getName(), s.getName());

        s = service.get(s.getId());
        assertEquals(site.getName(), s.getName());
    }

    @AfterClass
    public static void delete() {
        final Integer id = site.getId();
        service.delete(id);

        try {
            service.get(id);
            fail("Retrieving deleted site did not throw exception");
        } catch (final Throwable ignored) {}
    }
}