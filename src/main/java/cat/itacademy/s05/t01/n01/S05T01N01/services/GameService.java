package cat.itacademy.s05.t01.n01.S05T01N01.services;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.*;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.*;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.mappers.GameMapper;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Game.MoveType;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.GameRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public Mono<GameCreateResponseDTO> createGame(GameCreateRequestDTO request) {
        return playerRepository.findById(request.playerId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Player not found with ID: " + request.playerId())))
                .flatMap(player -> {
                    Game game = new Game();
                    game.setPlayerId(request.playerId());
                    game.setStatus(Game.GameStatus.PLAYER_TURN);
                    game.setCreatedAt(Instant.now());
                    game.setUpdatedAt(Instant.now());
                    game.setMoveHistory(new ArrayList<>());
                    game.getDeck().initialize();
                    game.getDeck().shuffle();
                    game.setPlayerHand(new ArrayList<>());
                    game.setDealerHand(new ArrayList<>());
                    game.setBetAmount(0);

                    return gameRepository.save(game)
                            .map(GameMapper::toGameCreateResponse);
                });
    }




    public Mono<GameDetailsResponseDTO> getGameDetails(String gameId) {
        return gameRepository.findById(gameId)
                .map(GameMapper::toGameDetailsResponse);
    }

    public Mono<GamePlayResponseDTO> playMove(String gameId, PlayRequestDTO request) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    // Update game based on move type and bet amount
                    // Simplified example: add move to history, update status, draw cards, etc.
                    List<MoveType> history = game.getMoveHistory();
                    history.add(request.move());

                    game.setUpdatedAt(Instant.now());
                    game.setMoveHistory(history);
                    game.setBetAmount(request.betAmount());

                    // TODO: Implement actual blackjack game logic here (hit/stand etc.)

                    return gameRepository.save(game);
                })
                .map(game -> GameMapper.toGamePlayResponse(game, "Move played"));
    }

    public Mono<Void> deleteGame(String gameId) {
        return gameRepository.deleteById(gameId);
    }

}

