package revlout.backend.test.moneytransfer.strategy;

import org.junit.Before;
import org.junit.Test;
import revlout.backend.test.moneytransfer.dao.AccountDao;
import revlout.backend.test.moneytransfer.dao.DaoFactory;
import revlout.backend.test.moneytransfer.exception.MoneyReceiverBlockedException;
import revlout.backend.test.moneytransfer.exception.MoneySenderBlockedException;
import revlout.backend.test.moneytransfer.exception.MoneySenderNotFoundException;
import revlout.backend.test.moneytransfer.exception.NotEnoughMoneyToTransferException;
import revlout.backend.test.moneytransfer.model.Account;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class DefaultTransferMoneyStrategyTest {

    private DaoFactory daoFactoryMock;

    private AccountDao accountDaoMock;

    private Account fromAccountMock;

    private Account toAccountMock;

    private DefaultTransferMoneyStrategy strategy;

    private final static String existingFromId = "1";

    private final static String existingToId = "2";

    @Before
    public void beforeEach() {
        daoFactoryMock = mock(DaoFactory.class);
        accountDaoMock = mock(AccountDao.class);
        fromAccountMock = mock(Account.class);
        toAccountMock = mock(Account.class);
        strategy = new DefaultTransferMoneyStrategy(daoFactoryMock);
        when(daoFactoryMock.createAccountDao()).thenReturn(accountDaoMock);
        when(accountDaoMock.findById(eq(existingFromId))).thenReturn(Optional.of(fromAccountMock));
        when(accountDaoMock.findById(eq(existingToId))).thenReturn(Optional.of(toAccountMock));
    }

    @Test
    public void create_account_dao_should_be_called_once() {
        strategy.transferMoney(existingFromId, existingToId, 0);
        verify(daoFactoryMock).createAccountDao();
    }

    @Test
    public void account_dao_should_have_only_one_interaction() {
        strategy.transferMoney(existingFromId, existingToId, 0);
        verify(daoFactoryMock).createAccountDao();
        verifyNoMoreInteractions(daoFactoryMock);
    }

    @Test(expected = MoneySenderNotFoundException.class)
    public void MoneySenderNotFoundException_should_be_thrown_if_from_account_doesnt_exist() {
        String nonExistentFromId = "3";
        when(accountDaoMock.findById(nonExistentFromId)).thenReturn(Optional.empty());
        strategy.transferMoney(nonExistentFromId, existingToId, 0);
    }

    @Test(expected = MoneySenderBlockedException.class)
    public void MoneySenderBlockedException_should_be_thrown_if_from_account_is_blocked() {
        when(fromAccountMock.isBlocked()).thenReturn(true);
        strategy.transferMoney(existingFromId, existingToId, 0);
    }

    @Test(expected = NotEnoughMoneyToTransferException.class)
    public void NotEnoughMoneyToTransferException_should_be_thrown_if_from_account_doesnt_have_enough_money() {
        when(fromAccountMock.getBalance()).thenReturn(50);
        strategy.transferMoney(existingFromId, existingToId, 100);
    }

    @Test(expected = MoneyReceiverBlockedException.class)
    public void MoneyReceiverBlockedException_should_be_thrown_if_from_account_is_blocked() {
        when(toAccountMock.isBlocked()).thenReturn(true);
        strategy.transferMoney(existingFromId, existingToId, 0);
    }

    @Test
    public void account_dao_should_be_called_twice() {
        strategy.transferMoney(existingFromId, existingToId, 0);
        verify(accountDaoMock, times(2)).update(any());
    }

    @Test
    public void from_account_balance_should_be_decreased_by_amount() {
        when(fromAccountMock.getBalance()).thenReturn(100);
        strategy.transferMoney(existingFromId, existingToId, 50);
        verify(fromAccountMock).withBalance(50);
    }

    @Test
    public void to_account_balance_should_be_increased_by_amount() {
        when(fromAccountMock.getBalance()).thenReturn(100);
        when(toAccountMock.getBalance()).thenReturn(100);
        strategy.transferMoney(existingFromId, existingToId, 50);
        verify(toAccountMock).withBalance(150);
    }
}
