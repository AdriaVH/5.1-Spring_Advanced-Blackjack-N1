package cat.itacademy.s05.t01.n01.S05T01N01.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public Mono<Map<String, Object>> handleGameNotFound(GameNotFoundException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), exchange);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public Mono<Map<String, Object>> handlePlayerNotFound(PlayerNotFoundException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), exchange);
    }

    @ExceptionHandler(InvalidMoveException.class)
    public Mono<Map<String, Object>> handleInvalidMove(InvalidMoveException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), exchange);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public Mono<Map<String, Object>> handleInsufficientBalance(InsufficientBalanceException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getReason(), exchange);
    }

    @ExceptionHandler(Exception.class)
    public Mono<Map<String, Object>> handleGeneralException(Exception ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", exchange, ex.getMessage());
    }

    private Mono<Map<String, Object>> buildErrorResponse(HttpStatus status, String message, ServerWebExchange exchange) {
        return buildErrorResponse(status, message, exchange, null);
    }

    private Mono<Map<String, Object>> buildErrorResponse(HttpStatus status, String message, ServerWebExchange exchange, String detail) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        error.put("path", exchange.getRequest().getPath().value());

        if (detail != null && !detail.equals(message)) {
            error.put("detail", detail);
        }

        exchange.getResponse().setStatusCode(status);
        return Mono.just(error);
    }
}
