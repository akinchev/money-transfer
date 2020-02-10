package revlout.backend.test.moneytransfer.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import revlout.backend.test.moneytransfer.exception.*;
import revlout.backend.test.moneytransfer.model.Account;
import revlout.backend.test.moneytransfer.service.AccountService;
import revlout.backend.test.moneytransfer.web.error.ApiError;
import revlout.backend.test.moneytransfer.web.request.TransferRequest;
import spark.Response;

import static spark.Spark.*;

@Slf4j
@RequiredArgsConstructor
public class DefaultRouteRegistry implements RouteRegistry {

    private final ObjectMapper objectMapper;

    private final AccountService accountService;

    @Override
    public void registerRoutes() {
        get("api/v1/accounts/:id", "application/json", (req, res) -> {
            Account account = accountService.getById(req.params(":id")).orElseThrow(ResourceNotFoundException::new);
            res.type("application/json");
            return account;
        }, objectMapper::writeValueAsString);

        post("api/v1/transfers", "application/json", (req, res) -> {
            TransferRequest transfer = objectMapper.readValue(req.body(), TransferRequest.class);
            accountService.transferMoney(transfer.getFrom(), transfer.getTo(), transfer.getAmount());
            res.status(201);
            res.type("application/json");
            return "";
        });

        exception(ResourceNotFoundException.class, (e, req, res) -> {
            handleError(res, new ApiError(404, "Account is not found", "Account is not found!", 100504));
        });

        exception(MoneySenderNotFoundException.class, (e, req, res) -> {
            handleError(res, new ApiError(400, "Sender is not found.", "Sender is not found!", 100500));
        });

        exception(MoneyReceiverNotFoundException.class, (e, req, res) -> {
            handleError(res, new ApiError(400, "Receiver is not found.", "Receiver is not found!", 100501));
        });

        exception(NotEnoughMoneyToTransferException.class, (e, req, res) -> {
            handleError(res, new ApiError(400, "Not enough money.", "Not enough money!", 100502));
        });

        exception(MoneySenderBlockedException.class, (e, req, res) -> {
            handleError(res, new ApiError(403, "Sender is blocked.", "Sender is blocked!", 100503));
        });

        exception(MoneyReceiverBlockedException.class, (e, req, res) -> {
            handleError(res, new ApiError(403, "Receiver is blocked.", "Receiver is blocked!", 100504));
        });

    }

    private void handleError(Response res, ApiError error) {
        res.type("application/json");
        res.status(error.getStatus());
        try {
            res.body(objectMapper.writeValueAsString(error));
        } catch (JsonProcessingException jpe) {
            log.error("Couldn't serialize api error.");
            res.status(500);
            res.body("");
        }
    }
}
