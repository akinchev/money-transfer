package revlout.backend.test.moneytransfer.dao;

import revlout.backend.test.moneytransfer.model.Account;

import java.util.Optional;

public interface AccountDao {

    void update(Account account);

    Optional<Account> findById(String id);
}
