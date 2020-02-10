package revlout.backend.test.moneytransfer.dao;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;

@RequiredArgsConstructor
public class DefaultDaoFactory implements DaoFactory {

    private final Connection con;

    @Override
    public AccountDao createAccountDao() {
        return new DefaultAccountDao(con);
    }
}
