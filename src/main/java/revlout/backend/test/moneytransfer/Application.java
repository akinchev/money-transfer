package revlout.backend.test.moneytransfer;

import revlout.backend.test.moneytransfer.ioc.ApplicationComponent;
import revlout.backend.test.moneytransfer.ioc.DaggerApplicationComponent;

public class Application {

    private ApplicationComponent applicationComponent;

    private void start(){
        initializeDagger();
        runMigrations();
        registerRoutes();
    }
    private void initializeDagger() {
        applicationComponent = DaggerApplicationComponent.create();
    }

    private void runMigrations() {
        applicationComponent.flyway().migrate();
    }

    private void registerRoutes(){
        applicationComponent.routeRegistry().registerRoutes();
    }

    public static void main(String[] args) {
        new Application().start();
    }
}
