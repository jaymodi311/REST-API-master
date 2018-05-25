package mis283;

import mis283.repository.Datastore;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger("mis283");

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        Datastore.getDatastore();
        logger.info("Servlet context initialized");
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        Datastore.shutdown();
        logger.info("JPA EMF shutdown");
    }
}