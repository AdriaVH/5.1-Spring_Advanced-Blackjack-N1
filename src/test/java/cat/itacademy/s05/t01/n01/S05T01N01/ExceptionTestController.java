package cat.itacademy.s05.t01.n01.S05T01N01;

import cat.itacademy.s05.t01.n01.S05T01N01.exceptions.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ExceptionTestController {

    @GetMapping("/test/game-not-found")
    public Mono<Void> gameNotFound() {
        return Mono.error(new GameNotFoundException("Game not found"));
    }

    @GetMapping("/test/player-not-found")
    public Mono<Void> playerNotFound() {
        return Mono.error(new PlayerNotFoundException("Player not found"));
    }

    @GetMapping("/test/invalid-move")
    public Mono<Void> invalidMove() {
        return Mono.error(new InvalidMoveException("Invalid move"));
    }

    @GetMapping("/test/insufficient-balance")
    public Mono<Void> insufficientBalance() {
        return Mono.error(new InsufficientBalanceException(10.0, 20));
    }

    @GetMapping("/test/general-exception")
    public Mono<Void> generalException() {
        return Mono.error(new RuntimeException("Something went wrong"));
    }
}
