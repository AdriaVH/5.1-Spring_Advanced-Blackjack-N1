package cat.itacademy.s05.t01.n01.S05T01N01.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", String.valueOf(ex.getStatusCode().value()));
        error.put("error", ex.getReason());
        return Mono.just(error);
    }

    @ExceptionHandler(Exception.class)
    public Mono<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", "500");
        error.put("error", "Internal Server Error: " + ex.getMessage());
        return Mono.just(error);
    }
}
