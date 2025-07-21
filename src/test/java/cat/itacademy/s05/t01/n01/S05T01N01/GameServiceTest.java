package cat.itacademy.s05.t01.n01.S05T01N01;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.GameCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameDetailsResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GamePlayResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.models.*;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Game.MoveType;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.GameRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.PlayerRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGame_ShouldReturnGameCreateResponse() {
        Long playerId = 123L;

        Player player = new Player();
        player.setId(playerId);

        Game game = new Game();
        game.setId("game123");
        game.setPlayerId(playerId);
        game.setCreatedAt(Instant.now());
        game.setUpdatedAt(Instant.now());
        game.setMoveHistory(new ArrayList<>());
        game.setStatus(Game.GameStatus.PLAYER_TURN);
        game.setBetAmount(0);

        when(playerRepository.findById(playerId)).thenReturn(Mono.just(player));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(game));

        GameCreateRequestDTO request = new GameCreateRequestDTO(playerId);

        Mono<GameCreateResponseDTO> result = gameService.createGame(request);

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.playerId().equals(playerId))
                .verifyComplete();

        verify(playerRepository).findById(playerId);
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void getGameDetails_ShouldReturnGameDetailsResponse() {
        String gameId = "game123";
        Game game = new Game();
        game.setId(gameId);
        game.setPlayerId(123L);

        when(gameRepository.findById(gameId)).thenReturn(Mono.just(game));

        Mono<GameDetailsResponseDTO> result = gameService.getGameDetails(gameId);

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.gameId().equals(gameId))
                .verifyComplete();

        verify(gameRepository).findById(gameId);
    }

    @Test
    void playMove_Hit_ShouldAddCardAndUpdateStatus() {
        String gameId = "game123";
        Game game = new Game();
        game.setId(gameId);
        Deck deck = new Deck();
        deck.initialize();
        deck.shuffle();
        game.setDeck(deck);
        game.setPlayerHand(new ArrayList<>());
        game.setMoveHistory(new ArrayList<>());
        game.setStatus(Game.GameStatus.PLAYER_TURN);

        PlayRequestDTO request = new PlayRequestDTO(MoveType.HIT, 10);

        when(gameRepository.findById(gameId)).thenReturn(Mono.just(game));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(game));

        Mono<GamePlayResponseDTO> result = gameService.playMove(gameId, request);

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.message().equals("Move played"))
                .verifyComplete();

        verify(gameRepository).findById(gameId);
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void playMove_Stand_ShouldPlayDealerTurnAndUpdateStatus() {
        String gameId = "game123";
        Game game = new Game();
        game.setId(gameId);
        Deck deck = new Deck();
        deck.initialize();
        deck.shuffle();
        game.setDeck(deck);
        game.setPlayerHand(new ArrayList<>());
        game.setDealerHand(new ArrayList<>());
        game.setMoveHistory(new ArrayList<>());
        game.setStatus(Game.GameStatus.PLAYER_TURN);

        PlayRequestDTO request = new PlayRequestDTO(MoveType.STAND, 20);

        when(gameRepository.findById(gameId)).thenReturn(Mono.just(game));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(game));

        Mono<GamePlayResponseDTO> result = gameService.playMove(gameId, request);

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.message().equals("Move played"))
                .verifyComplete();

        verify(gameRepository).findById(gameId);
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void deleteGame_ShouldCallDeleteById() {
        String gameId = "game123";

        when(gameRepository.deleteById(gameId)).thenReturn(Mono.empty());

        Mono<Void> result = gameService.deleteGame(gameId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(gameRepository).deleteById(gameId);
    }
}
