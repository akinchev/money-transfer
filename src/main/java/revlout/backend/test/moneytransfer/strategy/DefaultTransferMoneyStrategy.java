package revlout.backend.test.moneytransfer.strategy;

import lombok.RequiredArgsConstructor;
import revlout.backend.test.moneytransfer.dao.AccountDao;
import revlout.backend.test.moneytransfer.dao.DaoFactory;
import revlout.backend.test.moneytransfer.exception.*;
import revlout.backend.test.moneytransfer.model.Account;

@RequiredArgsConstructor
public class DefaultTransferMoneyStrategy implements TransferMoneyStrategy {

    private final DaoFactory daoFactory;

    @Override
    public void transferMoney(String from, String to, int amount) {
        AccountDao accountDao = daoFactory.createAccountDao();

        Account fromAccount = accountDao.findById(from).orElseThrow(MoneySenderNotFoundException::new);
        if (fromAccount.isBlocked()) {
            throw new MoneySenderBlockedException();
        }
        if (fromAccount.getBalance() < amount) {
            throw new NotEnoughMoneyToTransferException();
        }

        Account toAccount = accountDao.findById(to).orElseThrow(MoneyReceiverNotFoundException::new);
        if (toAccount.isBlocked()) {
            throw new MoneyReceiverBlockedException();
        }

        accountDao.update(fromAccount.withBalance(fromAccount.getBalance() - amount));
        accountDao.update(toAccount.withBalance(toAccount.getBalance() + amount));
    }
}
