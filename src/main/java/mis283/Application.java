package mis283;

import mis283.service.CORSResponseFilter;
import mis283.service.SiteService;
import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {
    public Application() {
        packages(SiteService.class.getPackage().getName());
        register(CORSResponseFilter.class);
    }
}