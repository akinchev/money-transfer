package revlout.backend.test.moneytransfer.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import revlout.backend.test.moneytransfer.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DefaultAccountDaoTest {

    private Connection conMock;

    private PreparedStatement psMock;

    private Account accountMock;

    private ResultSet rsMock;

    private DefaultAccountDao accountDao;

    @Before
    public void beforeEach() {
        conMock = mock(Connection.class);
        psMock = mock(PreparedStatement.class);
        accountMock = mock(Account.class);
        rsMock = mock(ResultSet.class);
        accountDao = new DefaultAccountDao(conMock);
    }

    @Test
    public void when_update_correct_sql_should_be_executed() throws Exception {
        String correctSql = "UPDATE accounts SET balance = ?, is_blocked = ?, version = version + 1  WHERE id = ? AND version = ?";
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        accountDao.update(accountMock);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(conMock).prepareStatement(sqlCaptor.capture());
        assertEquals(correctSql, sqlCaptor.getValue());
    }

    @Test
    public void when_update_account_balance_should_be_first_replace_parameter_value() throws Exception {
        Integer expectedAccountBalance = 100;
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(accountMock.getBalance()).thenReturn(expectedAccountBalance);
        accountDao.update(accountMock);
        ArgumentCaptor<Integer> balanceCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(psMock).setInt(eq(1), balanceCaptor.capture());
        assertEquals(expectedAccountBalance, balanceCaptor.getValue());
    }

    @Test
    public void when_update_account_is_blocked_should_be_second_replace_parameter_value() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(accountMock.isBlocked()).thenReturn(false);
        accountDao.update(accountMock);
        ArgumentCaptor<Boolean> isBlockedCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(psMock).setBoolean(eq(2), isBlockedCaptor.capture());
        assertEquals(false, isBlockedCaptor.getValue());
    }

    @Test
    public void when_update_account_id_should_be_third_replace_parameter_value() throws Exception {
        String expectedAccountId = "1";
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(accountMock.getId()).thenReturn(expectedAccountId);
        accountDao.update(accountMock);
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        verify(psMock).setString(eq(3), idCaptor.capture());
        assertEquals(expectedAccountId, idCaptor.getValue());
    }

    @Test
    public void when_update_account_id_should_be_fourth_replace_parameter_value() throws Exception {
        Integer expectedAccountVersion = 1;
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(accountMock.getVersion()).thenReturn(expectedAccountVersion);
        accountDao.update(accountMock);
        ArgumentCaptor<Integer> versionCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(psMock).setInt(eq(4), versionCaptor.capture());
        assertEquals(expectedAccountVersion, versionCaptor.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void when_update_runtime_exception_should_be_thrown_if_prepare_statement_throws_sql_exception() throws Exception {
        when(conMock.prepareStatement(anyString())).thenThrow(SQLException.class);
        accountDao.update(accountMock);
    }

    @Test(expected = RuntimeException.class)
    public void when_update_runtime_exception_should_be_thrown_if_execute_update_throws_sql_exception() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeUpdate()).thenThrow(SQLException.class);
        accountDao.update(accountMock);
    }

    @Test
    public void when_update_connection_prepare_statement_method_should_be_called_once() throws Exception{
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        accountDao.update(accountMock);
        verify(conMock, times(1)).prepareStatement(anyString());
    }

    @Test
    public void when_update_prepared_statement_set_int_method_should_be_called_twice() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        accountDao.update(accountMock);
        verify(psMock, times(2)).setInt(anyInt(), anyInt());
    }

    @Test
    public void when_update_prepared_statement_set_string_method_should_be_called_once() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(accountMock.getId()).thenReturn(anyString());
        accountDao.update(accountMock);
        verify(psMock).setString(anyInt(), anyString());
    }

    @Test
    public void when_update_prepared_statement_set_boolean_method_should_be_called_once() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        accountDao.update(accountMock);
        verify(psMock).setBoolean(anyInt(), anyBoolean());
    }

    @Test
    public void when_update_prepared_statement_execute_update_method_should_be_called_once() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        accountDao.update(accountMock);
        verify(psMock).executeUpdate();
    }

    @Test
    public void when_update_prepared_statement_close_method_should_be_called_once() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        accountDao.update(accountMock);
        verify(psMock).close();
    }

    @Test
    public void when_update_prepared_statement_should_not_have_unexpected_method_invocations() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(accountMock.getId()).thenReturn(anyString());
        accountDao.update(accountMock);
        verify(psMock, times(2)).setInt(anyInt(), anyInt());
        verify(psMock).setString(anyInt(), anyString());
        verify(psMock).setBoolean(anyInt(), anyBoolean());
        verify(psMock).executeUpdate();
        verify(psMock).close();
        verifyNoMoreInteractions(psMock);
    }

    @Test
    public void when_update_account_get_balance_method_should_be_called_once() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        accountDao.update(accountMock);
        verify(accountMock).getBalance();
    }

    @Test
    public void when_update_account_is_blocked_method_should_be_called_once() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        accountDao.update(accountMock);
        verify(accountMock).isBlocked();
    }

    @Test
    public void when_update_account_get_id_method_should_be_called_once() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        accountDao.update(accountMock);
        verify(accountMock).getId();
    }

    @Test
    public void when_update_account_get_version_method_should_be_called_once() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        accountDao.update(accountMock);
        verify(accountMock).getVersion();
    }

    @Test
    public void when_update_account_should_not_have_unexpected_method_invocations() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        accountDao.update(accountMock);
        verify(accountMock).getBalance();
        verify(accountMock).isBlocked();
        verify(accountMock).getId();
        verify(accountMock).getVersion();
        verifyNoMoreInteractions(accountMock);
    }

    @Test
    public void when_find_by_id_correct_sql_should_be_executed() throws Exception {
        String correctSql = "SELECT balance, is_blocked, version FROM accounts WHERE id = ?";
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeQuery()).thenReturn(rsMock);
        accountDao.findById(anyString());
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(conMock).prepareStatement(sqlCaptor.capture());
        assertEquals(correctSql, sqlCaptor.getValue());
    }

    @Test
    public void when_find_by_id_result_set_is_empty_result_should_be_empty() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeQuery()).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(false);
        Optional<Account> result = accountDao.findById(anyString());
        assertFalse(result.isPresent());
    }

    @Test
    public void when_find_by_id_result_set_is_empty_result_should_not_be_empty() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeQuery()).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(true);
        Optional<Account> result = accountDao.findById(anyString());
        assertTrue(result.isPresent());
    }

    @Test
    public void when_find_by_id_result_account_id_should_be_equal_to_passed_id() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeQuery()).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(true);
        String expectedAccountId = "1";
        Optional<Account> result = accountDao.findById(expectedAccountId);
        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), expectedAccountId);
    }

    @Test
    public void when_find_by_id_result_balance_should_be_equal_to_rs_get_int_first_param() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeQuery()).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(true);
        int expectedBalance = 100;
        when(rsMock.getInt(eq(1))).thenReturn(expectedBalance);
        Optional<Account> result = accountDao.findById(anyString());
        assertTrue(result.isPresent());
        assertEquals(result.get().getBalance(), expectedBalance);
    }

    @Test
    public void when_find_by_id_result_is_blocked_should_be_equal_to_rs_get_boolean_second_param() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeQuery()).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(true);
        when(rsMock.getBoolean(eq(2))).thenReturn(false);
        Optional<Account> result = accountDao.findById(anyString());
        assertTrue(result.isPresent());
        assertFalse(result.get().isBlocked());
    }

    @Test
    public void when_find_by_id_result_is_blocked_should_be_equal_to_rs_get_int_third_param() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeQuery()).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(true);
        int expectedVersion = 5;
        when(rsMock.getInt(eq(3))).thenReturn(expectedVersion);
        Optional<Account> result = accountDao.findById(anyString());
        assertTrue(result.isPresent());
        assertEquals(result.get().getVersion(), expectedVersion);
    }

    @Test(expected = RuntimeException.class)
    public void when_find_by_id_runtime_exception_should_be_thrown_if_prepare_statement_throws_sql_exception() throws Exception {
        when(conMock.prepareStatement(anyString())).thenThrow(SQLException.class);
        accountDao.findById(anyString());
    }

    @Test(expected = RuntimeException.class)
    public void when_find_by_id_runtime_exception_should_be_thrown_if_execute_query_throws_sql_exception() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeQuery()).thenThrow(SQLException.class);
        accountDao.findById(anyString());
    }

    @Test(expected = RuntimeException.class)
    public void when_find_by_id_runtime_exception_should_be_thrown_if_result_set_next_throws_sql_exception() throws Exception {
        when(conMock.prepareStatement(anyString())).thenReturn(psMock);
        when(rsMock.next()).thenThrow(SQLException.class);
        accountDao.findById(anyString());
    }
}
