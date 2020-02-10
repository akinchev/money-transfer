package revlout.backend.test.moneytransfer.ioc;

import dagger.Component;
import org.flywaydb.core.Flyway;
import revlout.backend.test.moneytransfer.web.RouteRegistry;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    RouteRegistry routeRegistry();

    Flyway flyway();
}
