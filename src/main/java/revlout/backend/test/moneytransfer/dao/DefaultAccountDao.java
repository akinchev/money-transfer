package revlout.backend.test.moneytransfer.dao;

import lombok.RequiredArgsConstructor;
import revlout.backend.test.moneytransfer.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class DefaultAccountDao implements AccountDao {

    private final Connection con;

    @Override
    public void update(Account account) {
        String sql = "UPDATE accounts SET balance = ?, is_blocked = ?, version = version + 1  WHERE id = ? AND version = ?";
        try (PreparedStatement updateStatement = con.prepareStatement(sql)){
            updateStatement.setInt(1, account.getBalance());
            updateStatement.setBoolean(2, account.isBlocked());
            updateStatement.setString(3, account.getId());
            updateStatement.setInt(4, account.getVersion());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Account> findById(String id) {
        String sql = "SELECT balance, is_blocked, version FROM accounts WHERE id = ?";
        try (PreparedStatement queryStatement = con.prepareStatement(sql)){
            queryStatement.setString(1, id);
            try (ResultSet rs = queryStatement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Account(id, rs.getInt(1), rs.getBoolean(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
