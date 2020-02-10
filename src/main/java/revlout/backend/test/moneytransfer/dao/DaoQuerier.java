package revlout.backend.test.moneytransfer.dao;

@FunctionalInterface
public interface DaoQuerier<T> {

    T execute(DaoFactory daoFactory);
}
