package revlout.backend.test.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Account {

    private final String id;

    private final int balance;

    private final boolean isBlocked;

    @JsonIgnore
    private final int version;

    public Account withBalance(int amount) {
        return new Account(
            id,
            amount,
            isBlocked,
            version
        );
    }
}
