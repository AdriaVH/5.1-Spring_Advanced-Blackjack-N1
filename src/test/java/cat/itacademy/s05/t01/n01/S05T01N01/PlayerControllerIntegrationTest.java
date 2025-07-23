package cat.itacademy.s05.t01.n01.S05T01N01;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerUpdateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerRankingResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.PlayerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayerControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PlayerRepository playerRepository;

    private static Long createdPlayerId;

    @Test
    @Order(1)
    void testCreatePlayer() {
        PlayerCreateRequestDTO request = new PlayerCreateRequestDTO("TestPlayer", 100.0);

        PlayerCreateResponseDTO response = webTestClient.post()
                .uri("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PlayerCreateResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("TestPlayer");
        assertThat(response.balance()).isEqualTo(100);
        assertThat(response.gamesPlayed()).isEqualTo(0);
        assertThat(response.gamesWon()).isEqualTo(0);

        createdPlayerId = response.id();
    }

    @Test
    @Order(2)
    void testUpdatePlayerName() {
        PlayerUpdateRequestDTO updateRequest = new PlayerUpdateRequestDTO("UpdatedPlayerName");

        PlayerResponseDTO response = webTestClient.put()
                .uri("/player/{id}", createdPlayerId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(createdPlayerId);
        assertThat(response.name()).isEqualTo("UpdatedPlayerName");
    }

    @Test
    @Order(3)
    void preparePlayerForRanking() {
        // Directly update player stats to simulate games played and won
        Player player = playerRepository.findById(createdPlayerId).block();
        assertThat(player).isNotNull();

        player.setGamesPlayed(10);
        player.setGamesWon(6);
        playerRepository.save(player).block();
    }

    @Test
    @Order(4)
    void testGetPlayersRanking() {
        PlayerRankingResponseDTO[] ranking = webTestClient.get()
                .uri("/ranking")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerRankingResponseDTO[].class)
                .returnResult()
                .getResponseBody();

        assertThat(ranking).isNotNull();
        assertThat(ranking.length).isGreaterThan(0);

        PlayerRankingResponseDTO first = ranking[0];
        assertThat(first.playerName()).isNotEmpty();
        assertThat(first.winPercentage()).matches("\\d+%");
        assertThat(first.position()).isNotEmpty();
    }
}
