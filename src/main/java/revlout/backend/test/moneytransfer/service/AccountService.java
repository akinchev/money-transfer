package revlout.backend.test.moneytransfer.service;

import revlout.backend.test.moneytransfer.model.Account;

import java.util.Optional;

public interface AccountService {

    Optional<Account> getById(String id);

    void transferMoney(String from, String to, int amount);
}
