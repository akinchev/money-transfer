package revlout.backend.test.moneytransfer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import revlout.backend.test.moneytransfer.dao.AccountDao;
import revlout.backend.test.moneytransfer.dao.DaoManager;
import revlout.backend.test.moneytransfer.exception.*;
import revlout.backend.test.moneytransfer.model.Account;
import revlout.backend.test.moneytransfer.strategy.TransferMoneyStrategyFactory;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class DefaultAccountService implements AccountService {

    private final DaoManager daoManager;

    private final TransferMoneyStrategyFactory transferMoneyStrategyFactory;

    @Override
    public Optional<Account> getById(String id) {
        return daoManager.queryInTransaction(daoFactory -> {
            AccountDao accountDao = daoFactory.createAccountDao();
            return accountDao.findById(id);
        });
    }

    @Override
    public void transferMoney(String from, String to, int amount) {
        int attemptsLeft = 3;
        while(true) {
            try {
                daoManager.executeInTransaction(daoFactory -> {
                    transferMoneyStrategyFactory.createStrategy(daoFactory).transferMoney(from, to, amount);
                });
                return;
            } catch (RuntimeException e) {
                if (e instanceof NotRetryableException) {
                    throw e;
                }
                attemptsLeft -= 1;
                if (attemptsLeft == 0) {
                    throw e;
                }
                log.warn("Error occurred when transferring: {}. Attempts left: {}", e, attemptsLeft);
            }
        }
    }
}
