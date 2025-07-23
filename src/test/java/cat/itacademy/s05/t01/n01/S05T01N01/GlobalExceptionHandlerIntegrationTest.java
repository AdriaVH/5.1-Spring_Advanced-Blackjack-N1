package cat.itacademy.s05.t01.n01.S05T01N01;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGameNotFoundException() {
        webTestClient.get().uri("/test/game-not-found")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.error").isEqualTo("Not Found")
                .jsonPath("$.message").isEqualTo("Game not found")
                .jsonPath("$.path").isEqualTo("/test/game-not-found");
    }

    @Test
    void testPlayerNotFoundException() {
        webTestClient.get().uri("/test/player-not-found")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Player not found");
    }

    @Test
    void testInvalidMoveException() {
        webTestClient.get().uri("/test/invalid-move")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid move");
    }

    @Test
    void testInsufficientBalanceException() {
        webTestClient.get().uri("/test/insufficient-balance")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").value(msg -> {
                    // Should contain "Insufficient balance"
                    assert msg.toString().contains("Insufficient balance");
                });
    }

    @Test
    void testGeneralException() {
        webTestClient.get().uri("/test/general-exception")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Internal Server Error");
    }
}
