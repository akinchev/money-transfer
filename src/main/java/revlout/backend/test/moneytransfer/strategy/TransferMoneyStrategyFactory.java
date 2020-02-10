package revlout.backend.test.moneytransfer.strategy;

import revlout.backend.test.moneytransfer.dao.DaoFactory;

public interface TransferMoneyStrategyFactory {

    TransferMoneyStrategy createStrategy(DaoFactory daoFactory);
}
