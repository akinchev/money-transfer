package revlout.backend.test.moneytransfer.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import revlout.backend.test.moneytransfer.exception.MoneyTransferException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class DefaultDaoManagerTest {

    private DataSource dataSourceMock;

    private Connection conMock;

    private DefaultDaoManager daoManager;

    @Before
    public void beforeEach() {
        dataSourceMock = mock(DataSource.class);
        conMock = mock(Connection.class);
        daoManager = new DefaultDaoManager(dataSourceMock);
    }

    @Test
    public void when_doQueryInTransaction_datasource_get_connection_method_should_be_invoked_once() throws Exception{
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        daoManager.queryInTransaction(daoFactory -> null);
        verify(dataSourceMock).getConnection();
    }

    @Test
    public void when_doQueryInTransaction_connection_set_autocommit_method_should_be_invoked_twice() throws Exception{
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        daoManager.queryInTransaction(daoFactory -> null);
        verify(conMock, times(2)).setAutoCommit(anyBoolean());
    }

    @Test
    public void when_doQueryInTransaction_connection_commit_method_should_be_invoked_once() throws Exception{
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        daoManager.queryInTransaction(daoFactory -> null);
        verify(conMock).commit();
    }

    @Test
    public void when_doQueryInTransaction_connection_close_method_should_be_invoked_once() throws Exception{
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        daoManager.queryInTransaction(daoFactory -> null);
        verify(conMock).close();
    }

    @Test
    public void when_doQueryInTransaction_dao_querier_execute_method_should_be_invoked_once() throws Exception{
        DaoQuerier<Void> querierMock = mock(DaoQuerier.class);
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        daoManager.queryInTransaction(querierMock);
        verify(querierMock).execute(any());
    }

    @Test
    public void when_doQueryInTransaction_connection_autocommit_should_be_first_set_false_and_then_true() throws Exception{
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        daoManager.queryInTransaction(daoFactory -> null);
        InOrder inOrder = inOrder(conMock);
        inOrder.verify(conMock).setAutoCommit(eq(false));
        inOrder.verify(conMock).setAutoCommit(eq(true));
    }

    @Test
    public void when_doQueryInTransaction_connection_rollback_method_should_be_invoked_once_if_execute_method_throws_exception() throws Exception{
        DaoQuerier<Void> querierMock = mock(DaoQuerier.class);
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        when(querierMock.execute(any())).thenThrow(RuntimeException.class);
        try {
            daoManager.queryInTransaction(querierMock);
        } catch (Exception ignored) {}
        verify(conMock).rollback();
    }

    @Test
    public void when_doQueryInTransaction_connection_close_method_should_be_invoked_once_if_execute_method_throws_exception() throws Exception{
        DaoQuerier<Void> querierMock = mock(DaoQuerier.class);
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        when(querierMock.execute(any())).thenThrow(RuntimeException.class);
        try {
            daoManager.queryInTransaction(querierMock);
        } catch (Exception ignored) {}
        verify(conMock).close();
    }

    @Test
    public void when_doQueryInTransaction_connection_rollback_method_should_be_invoked_once_if_connection_commit_method_throws_exception() throws Exception{
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        doThrow(RuntimeException.class).when(conMock).commit();
        try {
            daoManager.queryInTransaction(daoFactory -> null);
        } catch (Exception ignored) {}
        verify(conMock).rollback();
    }

    @Test
    public void when_doQueryInTransaction_connection_close_method_should_be_invoked_once_if_connection_commit_method_throws_exception() throws Exception{
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        doThrow(RuntimeException.class).when(conMock).commit();
        try {
            daoManager.queryInTransaction(daoFactory -> null);
        } catch (Exception ignored) {}
        verify(conMock).close();
    }

    @Test(expected = RuntimeException.class)
    public void when_doQueryInTransaction_runtime_exception_should_be_thrown_if_connection_commit_throws_sql_exception() throws Exception {
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        doThrow(SQLException.class).when(conMock).commit();
        daoManager.queryInTransaction(daoFactory -> null);
    }

    @Test(expected = MoneyTransferException.class)
    public void when_doQueryInTransaction_money_transfer_exception_should_be_thrown_if_connection_commit_throws_sql_exception() throws Exception {
        DaoQuerier<Void> querierMock = mock(DaoQuerier.class);
        when(dataSourceMock.getConnection()).thenReturn(conMock);
        when(querierMock.execute(any())).thenThrow(MoneyTransferException.class);
        daoManager.queryInTransaction(querierMock);
    }
}
