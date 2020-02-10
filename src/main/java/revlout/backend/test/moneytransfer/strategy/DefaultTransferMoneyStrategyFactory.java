package revlout.backend.test.moneytransfer.strategy;

import revlout.backend.test.moneytransfer.dao.DaoFactory;

public class DefaultTransferMoneyStrategyFactory implements TransferMoneyStrategyFactory {

    @Override
    public TransferMoneyStrategy createStrategy(DaoFactory daoFactory) {
        return new DefaultTransferMoneyStrategy(daoFactory);
    }
}
