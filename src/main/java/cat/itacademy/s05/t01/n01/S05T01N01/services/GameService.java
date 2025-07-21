package cat.itacademy.s05.t01.n01.S05T01N01.services;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.GameCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameDetailsResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GamePlayResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.mappers.GameMapper;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Deck;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Card;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Game.MoveType;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.GameRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.PlayerRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.utlis.HandEvaluator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
                    addMoveToHistory(game, request.move());
                    game.setUpdatedAt(Instant.now());
                    game.setBetAmount(request.betAmount());

                    switch (request.move()) {
                        case HIT -> handleHit(game);
                        case STAND -> handleStand(game);
                    }

                    return gameRepository.save(game);
                })
                .map(game -> GameMapper.toGamePlayResponse(game, "Move played"));
    }

    public Mono<Void> deleteGame(String id) {
        return gameRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return gameRepository.deleteById(id);
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
                    }
                });
    }




    // ---------- Helpers ----------

    private void setupInitialHands(Game game, Deck deck) {
        List<Card> playerHand = new ArrayList<>();
        List<Card> dealerHand = new ArrayList<>();

        playerHand.add(deck.drawCard(true));
        playerHand.add(deck.drawCard(true));

        dealerHand.add(deck.drawCard(true));   // face up
        dealerHand.add(deck.drawCard(false));  // face down (hole card)

        game.setPlayerHand(playerHand);
        game.setDealerHand(dealerHand);
    }

    private void addMoveToHistory(Game game, MoveType move) {
        List<MoveType> history = game.getMoveHistory();
        history.add(move);
        game.setMoveHistory(history);
    }

    private void handleHit(Game game) {
        Deck deck = game.getDeck();
        List<Card> playerHand = game.getPlayerHand();

        playerHand.add(deck.drawCard(true));

        int playerScore = HandEvaluator.calculateHandValue(playerHand);
        game.setStatus(playerScore > 21 ? Game.GameStatus.PLAYER_BUST : Game.GameStatus.PLAYER_TURN);

        game.setPlayerHand(playerHand);
        game.setDeck(deck);
    }

    private void handleStand(Game game) {
        Deck deck = game.getDeck();
        List<Card> dealerHand = game.getDealerHand();
        List<Card> playerHand = game.getPlayerHand();

        game.setStatus(Game.GameStatus.DEALER_TURN);
        flipDealerHoleCard(dealerHand);

        int dealerScore = HandEvaluator.calculateHandValue(dealerHand);
        int playerScore = HandEvaluator.calculateHandValue(playerHand);

        while (dealerScore < 17) {
            dealerHand.add(deck.drawCard(true));
            dealerScore = HandEvaluator.calculateHandValue(dealerHand);
        }

        determineFinalGameStatus(game, playerScore, dealerScore);

        game.setDealerHand(dealerHand);
        game.setDeck(deck);
    }

    private void flipDealerHoleCard(List<Card> dealerHand) {
        for (int i = 0; i < dealerHand.size(); i++) {
            Card card = dealerHand.get(i);
            if (!card.faceUp()) {
                dealerHand.set(i, new Card(card.rank(), card.suit(), true));
                return;
            }
        }
    }

    private void determineFinalGameStatus(Game game, int playerScore, int dealerScore) {
        if (dealerScore > 21) {
            game.setStatus(Game.GameStatus.DEALER_BUST);
        } else if (playerScore > dealerScore) {
            game.setStatus(Game.GameStatus.PLAYER_WIN);
        } else if (playerScore < dealerScore) {
            game.setStatus(Game.GameStatus.DEALER_WIN);
        } else {
            game.setStatus(Game.GameStatus.PUSH);
        }

        game.setStatus(Game.GameStatus.FINISHED);
    }
}
