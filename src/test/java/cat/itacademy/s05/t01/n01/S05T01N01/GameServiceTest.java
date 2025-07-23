package cat.itacademy.s05.t01.n01.S05T01N01;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.GameCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameDetailsResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GamePlayResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.models.*;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.GameRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.services.GameMoveService;
import cat.itacademy.s05.t01.n01.S05T01N01.services.GameService;
import cat.itacademy.s05.t01.n01.S05T01N01.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock private GameRepository gameRepository;
    @Mock private PlayerService playerService;
    @Mock private GameMoveService gameMoveService;

    @InjectMocks private GameService gameService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGame_ShouldReturnGameCreateResponse() {
        Long playerId = 123L;

        PlayerResponseDTO mockPlayer = new PlayerResponseDTO(playerId, "Test");

        when(playerService.getPlayerById(playerId)).thenReturn(Mono.just(mockPlayer));
        when(gameRepository.save(any(Game.class)))
                .thenAnswer(invocation -> {
                    Game g = invocation.getArgument(0);
                    g.setId("gameId123");
                    return Mono.just(g);
                });

        GameCreateRequestDTO request = new GameCreateRequestDTO(playerId);

        Mono<GameCreateResponseDTO> result = gameService.createGame(request);

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.playerId().equals(playerId) && resp.gameId() != null)
                .verifyComplete();

        verify(playerService).getPlayerById(playerId);
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void getGameDetails_ShouldReturnDetails() {
        String gameId = "game123";
        Game game = new Game();
        game.setId(gameId);
        game.setPlayerId(321L);

        when(gameRepository.findById(gameId)).thenReturn(Mono.just(game));

        Mono<GameDetailsResponseDTO> result = gameService.getGameDetails(gameId);

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.gameId().equals(gameId))
                .verifyComplete();

        verify(gameRepository).findById(gameId);
    }

    @Test
    void playMove_Hit_ShouldApplyMoveAndUpdateGame() {
        String gameId = "gameHit";
        Game game = new Game();
        game.setId(gameId);
        game.setDeck(new Deck()); game.getDeck().initialize();
        game.setPlayerHand(new ArrayList<>());
        game.setMoveHistory(new ArrayList<>());
        game.setStatus(Game.GameStatus.PLAYER_TURN);
        game.setBetAmount(0);
        game.setPlayerId(123L);

        PlayRequestDTO request = new PlayRequestDTO(Game.MoveType.HIT, 10);

        when(gameRepository.findById(gameId)).thenReturn(Mono.just(game));
        when(playerService.deductBetIfNeeded(any(), eq(10))).thenReturn(Mono.empty());
        doAnswer(invocation -> {
            game.setStatus(Game.GameStatus.PLAYER_TURN);
            return null;
        }).when(gameMoveService).applyMove(eq(game), eq(Game.MoveType.HIT));

        when(gameMoveService.isGameOver(game)).thenReturn(false);
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(game));

        Mono<GamePlayResponseDTO> result = gameService.playMove(gameId, request);

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.message().equals("Move played"))
                .verifyComplete();

        verify(gameRepository).save(any(Game.class));
        verify(playerService).deductBetIfNeeded(any(), eq(10));
        verify(gameMoveService).applyMove(eq(game), eq(Game.MoveType.HIT));
    }

    @Test
    void playMove_Stand_GameOver_ShouldUpdatePlayer() {
        String gameId = "gameStand";
        Game game = new Game();
        game.setId(gameId);
        game.setStatus(Game.GameStatus.PLAYER_TURN);
        game.setPlayerHand(new ArrayList<>());
        game.setDealerHand(new ArrayList<>());
        game.setMoveHistory(new ArrayList<>());
        game.setBetAmount(20);

        PlayRequestDTO request = new PlayRequestDTO(Game.MoveType.STAND, 20);

        // Mock fetching game by ID
        when(gameRepository.findById(gameId)).thenReturn(Mono.just(game));

        // Mock deductBetIfNeeded to return Mono.empty() (fix NPE here)
        when(playerService.deductBetIfNeeded(any(Game.class), anyInt())).thenReturn(Mono.empty());

        // Dynamic isGameOver: returns true if game status != PLAYER_TURN
        when(gameMoveService.isGameOver(any(Game.class)))
                .thenAnswer(invocation -> {
                    Game g = invocation.getArgument(0);
                    return g.getStatus() != Game.GameStatus.PLAYER_TURN;
                });

        // When applyMove is called, update game status to simulate game over
        doAnswer(invocation -> {
            Game gameArg = invocation.getArgument(0);
            // Set to PLAYER_WIN (simulate player won after STAND)
            gameArg.setStatus(Game.GameStatus.PLAYER_WIN);
            return null;
        }).when(gameMoveService).applyMove(eq(game), eq(Game.MoveType.STAND));

        // Mock repository save returning the updated game
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> {
            Game savedGame = invocation.getArgument(0);
            return Mono.just(savedGame);
        });

        // Mock playerService update called when game is over
        when(playerService.updatePlayerAfterGame(any(Game.class))).thenReturn(Mono.empty());

        Mono<GamePlayResponseDTO> result = gameService.playMove(gameId, request);

        StepVerifier.create(result)
                .expectNextMatches(resp ->
                        resp.gameId().equals(gameId) &&
                                resp.status() == Game.GameStatus.PLAYER_WIN &&
                                resp.message().equals("Move played, stats and balance updated")
                )
                .verifyComplete();

        // Verify interactions
        verify(gameRepository).findById(gameId);
        verify(playerService).deductBetIfNeeded(any(Game.class), anyInt());
        verify(gameMoveService, atLeastOnce()).isGameOver(any(Game.class));
        verify(gameMoveService).applyMove(game, Game.MoveType.STAND);
        verify(gameRepository).save(any(Game.class));
        verify(playerService).updatePlayerAfterGame(any(Game.class));
    }

    @Test
    void deleteGame_ShouldDeleteSuccessfully() {
        String gameId = "toDelete";

        when(gameRepository.existsById(gameId)).thenReturn(Mono.just(true));
        when(gameRepository.deleteById(gameId)).thenReturn(Mono.empty());

        StepVerifier.create(gameService.deleteGame(gameId))
                .verifyComplete();

        verify(gameRepository).deleteById(gameId);
    }
}
