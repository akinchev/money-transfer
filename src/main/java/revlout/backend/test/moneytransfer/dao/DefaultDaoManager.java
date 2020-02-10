package revlout.backend.test.moneytransfer.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class DefaultDaoManager implements DaoManager {

    private final DataSource dataSource;

    @Override
    public void executeInTransaction(DaoExecutor daoExecutor) {
        doQueryInTransaction(daoFactory -> {
            daoExecutor.execute(daoFactory);
            return null;
        });
    }

    @Override
    public <T> T queryInTransaction(DaoQuerier<T> daoQuerier) {
        return doQueryInTransaction(daoQuerier);
    }

    private  <T> T doQueryInTransaction(DaoQuerier<T> daoQuerier) {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            T result = daoQuerier.execute(new DefaultDaoFactory(con));
            con.commit();
            return result;
        } catch (RuntimeException e) {
            rollback(con);
            throw e;
        } catch (Exception e) {
            rollback(con);
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    log.error("Error occurred when restoring autocommit flag: ", e);
                }
            }
        }
    }

    private void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException rollbackException) {
                log.error("Error occurred during transaction rollback: ", rollbackException);
            }
        }
    }
}
