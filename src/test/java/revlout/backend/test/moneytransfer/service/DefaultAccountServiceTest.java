package revlout.backend.test.moneytransfer.service;

import org.junit.Before;
import org.junit.Test;
import revlout.backend.test.moneytransfer.dao.DaoManager;
import revlout.backend.test.moneytransfer.strategy.TransferMoneyStrategy;
import revlout.backend.test.moneytransfer.strategy.TransferMoneyStrategyFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultAccountServiceTest {

    private TransferMoneyStrategyFactory transferMoneyStrategyFactoryMock;

    private TransferMoneyStrategy transferMoneyStrategyMock;

    private DaoManager daoManagerMock;

    private DefaultAccountService accountService;

    @Before
    public void beforeEach() {
        transferMoneyStrategyFactoryMock = mock(TransferMoneyStrategyFactory.class);
        transferMoneyStrategyMock = mock(TransferMoneyStrategy.class);
        daoManagerMock = mock(DaoManager.class);
        when(transferMoneyStrategyFactoryMock.createStrategy(any())).thenReturn(transferMoneyStrategyMock);
        accountService = new DefaultAccountService(daoManagerMock, transferMoneyStrategyFactoryMock);
    }

    @Test
    public void when_transfer_money_dao_manager_execute_in_transaction_method_is_called_at_least_once() {
        accountService.transferMoney("", "", 0);
        verify(daoManagerMock, atLeast(1)).executeInTransaction(any());
    }

    @Test
    public void when_getting_by_id_dao_manager_query_in_transaction_method_is_called_at_least_once() {
        accountService.getById("");
        verify(daoManagerMock, atLeast(1)).queryInTransaction(any());
    }
}
