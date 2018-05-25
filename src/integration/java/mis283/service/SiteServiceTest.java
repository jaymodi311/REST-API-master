package mis283.service;

import mis283.model.Site;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
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

    private static final ClientConfig config = new ClientConfig();
    private static final Client client = ClientBuilder.newClient(config);
    private static final URI baseUri = UriBuilder.fromUri("http://localhost:8080/lab10").build();

    @BeforeClass
    public static void create() {
        final WebTarget target = client.target(baseUri);
        site = target
                .path("site")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(site, MediaType.APPLICATION_JSON),Site.class);
        assertNotNull(site.getId());
        assertTrue(site.getId() > 0);
    }

    @Test
    public void retrieve() {
        final Site s = get();
        assertNotNull(s.getId());
        assertEquals(site.getId(), s.getId());
        assertEquals(site.getName(), s.getName());
        assertEquals(site.getUrl(), s.getUrl());
    }

    @Test
    public void update() {
        final String modified = format("%s modified", site.getName());

        Site s = get();
        assertNotNull(s.getId());
        s.setName(modified);

        s = update(s);
        assertEquals(modified, s.getName());

        s = get();
        assertEquals(modified, s.getName());

        s.setName(site.getName());
        s = update(s);
        assertEquals(site.getName(), s.getName());

        s = get();
        assertEquals(site.getName(), s.getName());
    }

    @AfterClass
    public static void delete() {
        final WebTarget target = client.target(baseUri);
        final Response response = target
                .path("site")
                .path(site.getId().toString())
                .request(MediaType.TEXT_PLAIN)
                .delete(Response.class);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        try {
            get();
            fail("Retrieving deleted site did not throw exception");
        } catch (final Throwable ignored) {}
    }

    private static Site get() {
        final WebTarget target = client.target(baseUri);
        return target
                .path("site")
                .path(site.getId().toString())
                .request(MediaType.APPLICATION_JSON)
                .get(Site.class);
    }

    private Site update(final Site s) {
        final WebTarget target = client.target(baseUri);
        return target
                .path("site")
                .path(site.getId().toString())
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON), Site.class);
    }
}