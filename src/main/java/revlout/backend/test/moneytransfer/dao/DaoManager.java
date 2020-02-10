package revlout.backend.test.moneytransfer.dao;

public interface DaoManager {

    void executeInTransaction(DaoExecutor daoExecutor);

    <T> T queryInTransaction(DaoQuerier<T> daoQuerier);
}
