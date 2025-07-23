package cat.itacademy.s05.t01.n01.S05T01N01.services;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.GameCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameDetailsResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GamePlayResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.mappers.GameMapper;
import cat.itacademy.s05.t01.n01.S05T01N01.exceptions.GameNotFoundException;
import cat.itacademy.s05.t01.n01.S05T01N01.exceptions.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.S05T01N01.models.*;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final GameMoveService gameMoveService;

    public Mono<GameCreateResponseDTO> createGame(GameCreateRequestDTO request) {
        return playerService.getPlayerById(request.playerId())
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player with ID " + request.playerId() + " not found")))

                .flatMap(player -> {
                    Game game = buildNewGame(player.id());
                    return gameRepository.save(game);
                })
                .map(GameMapper::toGameCreateResponse);
    }

    public Mono<GameDetailsResponseDTO> getGameDetails(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with ID: " + gameId)))
                .map(GameMapper::toGameDetailsResponse);
    }


    public Mono<GamePlayResponseDTO> playMove(String gameId, PlayRequestDTO request) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found")))
                .flatMap(game -> {

                    if (gameMoveService.isGameOver(game)) {
                        // Game is over, just return game without applying moves
                        return Mono.just(game);
                    }

                    return playerService.deductBetIfNeeded(game, request.betAmount())
                            .thenReturn(game);
                })
                .flatMap(game -> {

                    if (gameMoveService.isGameOver(game)) {
                        return Mono.just(GameMapper.toGamePlayResponse(game,
                                "Game is already over with status: " + game.getStatus()));
                    }

                    addMoveToHistory(game, request.move());
                    game.setUpdatedAt(Instant.now());
                    game.setBetAmount(request.betAmount());
                    gameMoveService.applyMove(game, request.move());

                    return gameRepository.save(game)
                            .flatMap(savedGame -> {
                                if (gameMoveService.isGameOver(savedGame)) {
                                    return playerService.updatePlayerAfterGame(savedGame)
                                            .thenReturn(GameMapper.toGamePlayResponse(savedGame, "Move played, stats and balance updated"));
                                } else {
                                    return Mono.just(GameMapper.toGamePlayResponse(savedGame, "Move played"));
                                }
                            });
                });
    }

// ---------------------HELPERS------------------------


    public Mono<Void> deleteGame(String gameId) {
        return gameRepository.existsById(gameId)
                .flatMap(exists -> exists
                        ? gameRepository.deleteById(gameId)
                        : Mono.error(new GameNotFoundException("Game with ID " + gameId + " not found")));
    }

    private void addMoveToHistory(Game game, Game.MoveType move) {
        game.getMoveHistory().add(move);
    }

    private Game buildNewGame(Long playerId) {
        Game game = new Game();
        game.setPlayerId(playerId);
        game.setCreatedAt(Instant.now());
        game.setUpdatedAt(Instant.now());

        Deck deck = new Deck();
        deck.initialize();
        deck.shuffle();

        setupInitialHands(game, deck);

        game.setDeck(deck);
        game.setMoveHistory(new ArrayList<>());
        game.setStatus(Game.GameStatus.PLAYER_TURN);
        game.setBetAmount(0);
        return game;
    }

    private void setupInitialHands(Game game, Deck deck) {
        List<Card> playerHand = new ArrayList<>();
        List<Card> dealerHand = new ArrayList<>();

        playerHand.add(deck.drawCard(true));
        playerHand.add(deck.drawCard(true));

        dealerHand.add(deck.drawCard(true));
        dealerHand.add(deck.drawCard(false));

        game.setPlayerHand(playerHand);
        game.setDealerHand(dealerHand);
    }
}
