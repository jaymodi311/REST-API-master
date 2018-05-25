package mis283.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Datastore {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("lab10" );
    private static final Datastore singleton = new Datastore();
    public static Datastore getDatastore() { return singleton; }

    private Datastore() {
        Runtime.getRuntime().addShutdownHook( new Thread( () -> shutdown() ) );
    }

    public EntityManager getManager() {
        return emf.createEntityManager();
    }

    public static void shutdown() {
        if (emf.isOpen()) emf.close();
    }
}