package cat.itacademy.s05.t01.n01.S05T01N01;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.GameCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameDetailsResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GamePlayResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.controllers.GameController;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Card;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class GameControllerTest {

    private GameService gameService;
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        gameService = Mockito.mock(GameService.class);
        GameController controller = new GameController(gameService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    public void createGame_ReturnsCreatedGame() {
        GameCreateRequestDTO requestDTO = new GameCreateRequestDTO(123L);
        GameCreateResponseDTO responseDTO = new GameCreateResponseDTO(
                "game123",
                123L,
                Game.GameStatus.PLAYER_TURN,
                new ArrayList<>(),          // playerHand
                new ArrayList<>(),          // dealerHand
                new ArrayList<>(),          // moveHistory
                1000,                       // betAmount
                "Game created successfully" // message
        );

        when(gameService.createGame(any(GameCreateRequestDTO.class))).thenReturn(Mono.just(responseDTO));

        webTestClient.post()
                .uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(GameCreateResponseDTO.class)
                .isEqualTo(responseDTO);
    }

    @Test
    public void getGameDetails_ReturnsGameDetails() {
        String gameId = "game123";

        List<Card> playerHand = List.of(
                new Card(Card.Rank.ACE, Card.Suit.SPADES, true),
                new Card(Card.Rank.TEN, Card.Suit.HEARTS, true)
        );

        List<Card> dealerHand = List.of(
                new Card(Card.Rank.KING,  Card.Suit.CLUBS, true),
                new Card(Card.Rank.FIVE,  Card.Suit.DIAMONDS, true)
        );

        List<Game.MoveType> moveHistory = List.of(Game.MoveType.HIT, Game.MoveType.STAND);

        Game.GameStatus status = Game.GameStatus.PLAYER_TURN;
        Integer betAmount = 20;

        GameDetailsResponseDTO detailsDTO = new GameDetailsResponseDTO(
                gameId,
                123L,
                playerHand,
                dealerHand,
                moveHistory,
                status,
                betAmount
        );

        when(gameService.getGameDetails(eq(gameId))).thenReturn(Mono.just(detailsDTO));

        webTestClient.get()
                .uri("/games/{id}", gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameDetailsResponseDTO.class)
                .isEqualTo(detailsDTO);
    }

    @Test
    public void playMove_ReturnsGamePlayResponse() {
        String gameId = "game123";

        PlayRequestDTO playRequestDTO = new PlayRequestDTO(Game.MoveType.HIT, 10);

        List<Card> playerHand = List.of(
                new Card(Card.Rank.ACE, Card.Suit.SPADES, true),
                new Card(Card.Rank.TEN, Card.Suit.HEARTS, true)
        );

        List<Card> dealerHand = List.of(
                new Card(Card.Rank.KING,  Card.Suit.CLUBS, true),
                new Card(Card.Rank.FIVE,  Card.Suit.DIAMONDS, true)
        );

        List<Game.MoveType> moveHistory = List.of(Game.MoveType.HIT);

        GamePlayResponseDTO playResponseDTO = new GamePlayResponseDTO(
                gameId,
                Game.GameStatus.PLAYER_TURN,
                playerHand,
                dealerHand,
                moveHistory,
                10,
                "Move played"
        );

        when(gameService.playMove(eq(gameId), any(PlayRequestDTO.class))).thenReturn(Mono.just(playResponseDTO));

        webTestClient.post()
                .uri("/games/{id}/play", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(playRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GamePlayResponseDTO.class)
                .isEqualTo(playResponseDTO);
    }

    @Test
    public void deleteGame_ReturnsNoContent() {
        String gameId = "game123";

        // Simulate game exists and is deleted successfully (Mono<Void> completes normally)
        when(gameService.deleteGame(eq(gameId))).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/games/{id}", gameId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void deleteGame_ReturnsNotFound() {
        String gameId = "nonexistent";

        // Simulate game does NOT exist, so deleteGame returns Mono.error with 404
        when(gameService.deleteGame(eq(gameId))).thenReturn(
                Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"))
        );

        webTestClient.delete()
                .uri("/games/{id}", gameId)
                .exchange()
                .expectStatus().isNotFound();
    }

}
