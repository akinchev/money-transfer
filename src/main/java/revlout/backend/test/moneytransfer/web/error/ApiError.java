package revlout.backend.test.moneytransfer.web.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiError {

    private final int status;

    private final String developerMessage;

    private final String userMessage;

    private final int errorCode;
}
