package revlout.backend.test.moneytransfer.strategy;

public interface TransferMoneyStrategy {

    void transferMoney(String from, String to, int amount);
}
