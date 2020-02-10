package revlout.backend.test.moneytransfer.ioc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dagger.Module;
import dagger.Provides;
import org.flywaydb.core.Flyway;
import revlout.backend.test.moneytransfer.dao.DaoManager;
import revlout.backend.test.moneytransfer.dao.DefaultDaoManager;
import revlout.backend.test.moneytransfer.service.AccountService;
import revlout.backend.test.moneytransfer.service.DefaultAccountService;
import revlout.backend.test.moneytransfer.strategy.DefaultTransferMoneyStrategyFactory;
import revlout.backend.test.moneytransfer.strategy.TransferMoneyStrategyFactory;
import revlout.backend.test.moneytransfer.web.DefaultRouteRegistry;
import revlout.backend.test.moneytransfer.web.RouteRegistry;

import javax.inject.Singleton;
import javax.sql.DataSource;

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    public ObjectMapper provideGson(){
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    public RouteRegistry provideRouteRegistry(ObjectMapper objectMapper, AccountService accountService) {
        return new DefaultRouteRegistry(objectMapper, accountService);
    }

    @Provides
    @Singleton
    public Flyway provideFlyway(DataSource dataSource) {
        return Flyway.configure().dataSource(dataSource).locations("classpath:db/migration").load();
    }

    @Provides
    @Singleton
    public DataSource provideDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:test");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        return new HikariDataSource(config);
    }

    @Provides
    @Singleton
    public DaoManager provideDaoManager(DataSource dataSource) {
        return new DefaultDaoManager(dataSource);
    }

    @Provides
    @Singleton
    public TransferMoneyStrategyFactory provideTransferMoneyStrategyFactory() {
        return new DefaultTransferMoneyStrategyFactory();
    }

    @Provides
    @Singleton
    public AccountService provideAccountService(DaoManager daoManager, TransferMoneyStrategyFactory transferMoneyStrategyFactory) {
        return new DefaultAccountService(daoManager, transferMoneyStrategyFactory);
    }
}
