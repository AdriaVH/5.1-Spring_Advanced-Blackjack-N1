package cat.itacademy.s05.t01.n01.S05T01N01.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InsufficientBalanceException extends ResponseStatusException {
    public InsufficientBalanceException(int currentBalance, int attemptedBet) {
        super(HttpStatus.BAD_REQUEST,
                "Insufficient balance. Current: " + currentBalance + ", Required: " + attemptedBet);
    }
}
