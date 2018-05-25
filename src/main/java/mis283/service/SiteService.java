package mis283.service;

import mis283.model.Site;

import javax.persistence.EntityManager;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;
import static mis283.repository.Datastore.getDatastore;
import static mis283.repository.SiteRepository.getSiteRepository;

@Path("/site")
public class SiteService {
    private static final Logger logger = Logger.getLogger("service");

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Site create(final Site site) {
        if (site.getId() != null && site.getId() > 0) throw new BadRequestException("Site id not allowed in create");

        final EntityManager entityManager = getDatastore().getManager();
        try {
            return getSiteRepository().save(site, entityManager);
        } catch (final Throwable t) {
            logger.log(SEVERE, "Error creating site", t);
            throw new InternalServerErrorException(t.getMessage());
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Site get(@PathParam("id") final Integer id) {
        if (id == null || id <= 0) throw new BadRequestException(format("Invalid site id: (%d) specified", id));

        final EntityManager entityManager = getDatastore().getManager();
        try {
            final Optional<Site> option = getSiteRepository().retrieve(id, entityManager);
            if (option.isPresent()) return option.get();
            throw new NotFoundException(format("No site with id: (%d) found.", id));
        } catch (final Throwable t) {
            logger.log(SEVERE, format("Error retrieving site with id: %d", id), t);
            throw new InternalServerErrorException(t.getMessage());
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Site update(@PathParam("id") final Integer id, final Site site) {
        if (site.getId() == null || site.getId() <= 0) throw new BadRequestException(format("Invalid site id: (%d) specified", site.getId()));
        if (id == null || id <= 0 || !Objects.equals(id, site.getId())) throw new BadRequestException(format("Resource path id: (%d) and resource id: (%d) do not match.", id, site.getId()));

        final EntityManager entityManager = getDatastore().getManager();
        try {
            return getSiteRepository().save(site, entityManager);
        } catch (final Throwable t) {
            logger.log(SEVERE, "Error updating site", t);
            throw new InternalServerErrorException(t.getMessage());
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(@PathParam("id") final Integer id) {
        if (id == null || id <= 0) throw new BadRequestException(format("Invalid site id: (%d) specified", id));

        final EntityManager entityManager = getDatastore().getManager();
        try {
            getSiteRepository().delete(id, entityManager);
            return Response.ok(format("Deleted site with id: %d", id)).build();
        } catch (final Throwable t) {
            logger.log(SEVERE, format("Error deleting site with id: %d", id), t);
            throw new InternalServerErrorException(t.getMessage());
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
}