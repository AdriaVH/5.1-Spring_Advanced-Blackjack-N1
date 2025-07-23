package cat.itacademy.s05.t01.n01.S05T01N01;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.GameCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameDetailsResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GamePlayResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.PlayerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PlayerRepository playerRepository;

    private Long playerId;

    @BeforeEach
    void setupPlayer() {
        // Clear all players (optional)
        playerRepository.deleteAll().block();

        // Create a new player before each test
        Player player = new Player();
        player.setName("Test Player");
        player.setBalance(1000.0);
        player.setGamesPlayed(0);
        player.setGamesWon(0);

        Player savedPlayer = playerRepository.save(player).block();
        playerId = savedPlayer.getId();
    }

    @Test
    void testFullGameFlow() {
        // 1. Create a game using the dynamically created player's ID
        GameCreateRequestDTO createRequest = new GameCreateRequestDTO(playerId);

        GameCreateResponseDTO createResponse = webTestClient.post()
                .uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(GameCreateResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(createResponse).as("Game creation response").isNotNull();
        assertThat(createResponse.gameId()).as("Game ID").isNotNull();

        String gameId = createResponse.gameId();

        // 2. Get game details
        GameDetailsResponseDTO detailsResponse = webTestClient.get()
                .uri("/games/{id}", gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameDetailsResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(detailsResponse).as("Game details").isNotNull();
        assertThat(detailsResponse.gameId()).isEqualTo(gameId);

        // 3. Play a move (HIT)
        PlayRequestDTO playRequest = new PlayRequestDTO(Game.MoveType.HIT, 50);

        GamePlayResponseDTO playResponse = webTestClient.post()
                .uri("/games/{id}/play", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(playRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GamePlayResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(playResponse).as("Game play response").isNotNull();
        assertThat(playResponse.gameId()).isEqualTo(gameId);

        // 4. Delete the game
        webTestClient.delete()
                .uri("/games/{id}", gameId)
                .exchange()
                .expectStatus().isNoContent();
    }
}
