package revlout.backend.test.moneytransfer.dao;

@FunctionalInterface
public interface DaoExecutor {

    void execute(DaoFactory daoFactory);
}
