package mis283.service;

import mis283.model.Article;

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
import static mis283.repository.ArticleRepository.getArticleRepository;
import static mis283.repository.Datastore.getDatastore;

@Path("/article")
public class ArticleService {
    private static final Logger logger = Logger.getLogger("service");

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Article create(final Article article) {
        if (article.getId() != null && article.getId() > 0) throw new BadRequestException("Article id not allowed in create");

        final EntityManager entityManager = getDatastore().getManager();
        try {
            return getArticleRepository().save(article, entityManager);
        } catch (final Throwable t) {
            logger.log(SEVERE, "Error creating article", t);
            throw new InternalServerErrorException(t.getMessage());
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Article get(@PathParam("id") final Integer id) {
        if (id == null || id <= 0) throw new BadRequestException(format("Invalid article id: (%d) specified",id));

        final EntityManager entityManager = getDatastore().getManager();
        try {
            final Optional<Article> option = getArticleRepository().retrieve(id, entityManager);
            if (option.isPresent()) return option.get();
            throw new NotFoundException(format("No article with id: (%d) found.", id));
        } catch (final Throwable t) {
            logger.log(SEVERE, format("Error retrieving article with id: %d", id), t);
            throw new InternalServerErrorException(t.getMessage());
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Article update(@PathParam("id") final Integer id, final Article article) {
        if (article.getId() == null || article.getId() <= 0) throw new BadRequestException(format("Invalid article id: (%d) specified", article.getId()));
        if (id == null || id <= 0 || !Objects.equals(id, article.getId())) throw new BadRequestException(format("Resource path id: (%d) and resource id: (%d) do not match.", id,article.getId()));

        final EntityManager entityManager = getDatastore().getManager();
        try {
            return getArticleRepository().save(article, entityManager);
        } catch (final Throwable t) {
            logger.log(SEVERE, "Error updating article", t);
            throw new InternalServerErrorException(t.getMessage());
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(@PathParam("id") final Integer id) {
        if (id == null || id <= 0) throw new BadRequestException(format("Invalid article id: (%d) specified",id));

        final EntityManager entityManager = getDatastore().getManager();
        try {
            getArticleRepository().delete(id, entityManager);
            return Response.ok(format("Deleted article with id: %d", id)).build();
        } catch (final Throwable t) {
            logger.log(SEVERE, format("Error deleting article with id: %d", id), t);
            throw new InternalServerErrorException(t.getMessage());
        } finally {
            if (entityManager != null) entityManager.close();
        }


    }
}