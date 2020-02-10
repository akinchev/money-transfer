package revlout.backend.test.moneytransfer.web.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TransferRequest {

    private final String from;

    private final String to;

    private final int amount;

    @JsonCreator
    public TransferRequest(
            @JsonProperty("from") String from,
            @JsonProperty("to") String to,
            @JsonProperty("amount") int amount
    ) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
}
