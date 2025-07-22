package cat.itacademy.s05.t01.n01.S05T01N01.services;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.mappers.PlayerMapper;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerUpdateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerRankingResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.exceptions.InsufficientBalanceException;
import cat.itacademy.s05.t01.n01.S05T01N01.exceptions.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Mono<PlayerCreateResponseDTO> createPlayer(PlayerCreateRequestDTO request) {
        Player player = new Player();
        player.setName(request.name());
        player.setBalance(request.balance());
        player.setGamesPlayed(0);
        player.setGamesWon(0);

        return playerRepository.save(player)
                .map(saved -> new PlayerCreateResponseDTO(
                        saved.getId(),
                        saved.getName(),
                        saved.getBalance(),
                        saved.getGamesWon(),
                        saved.getGamesPlayed()
                ));
    }

    public Mono<PlayerResponseDTO> updatePlayerName(Long playerId, PlayerUpdateRequestDTO updateRequest) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found with ID: " + playerId)))

                .map(player -> {
                    player.setName(updateRequest.newName());
                    return player;
                })
                .flatMap(playerRepository::save)
                .map(PlayerMapper::toPlayerResponseDTO);
    }

    public Mono<PlayerResponseDTO> getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .map(PlayerMapper::toPlayerResponseDTO);
    }

    public Mono<Void> deductBetIfNeeded(Game game, int betAmount) {
        if (betAmount <= 0 || game.getBetAmount() > 0) return Mono.empty();

        return playerRepository.findById(game.getPlayerId())
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found with ID: " + game.getPlayerId())))
                .flatMap(player -> {
                    int balance = safeBalance(player);
                    if (balance < betAmount)
                        return Mono.error(new InsufficientBalanceException(balance, betAmount));

                    player.setBalance(balance - betAmount);
                    return playerRepository.save(player).then();
                });
    }

    public Mono<Void> updatePlayerAfterGame(Game game) {
        return playerRepository.findById(game.getPlayerId())
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found with ID: " + game.getPlayerId())))
                .flatMap(player -> {
                    player.setGamesPlayed(player.getGamesPlayed() + 1);

                    if (wonGame(game)) {
                        player.setGamesWon(player.getGamesWon() + 1);
                        player.setBalance(safeBalance(player) + game.getBetAmount() * 2);
                    } else if (game.getStatus() == Game.GameStatus.PUSH) {
                        player.setBalance(safeBalance(player) + game.getBetAmount());
                    }

                    return playerRepository.save(player).then();
                });
    }

    public Flux<PlayerRankingResponseDTO> getAllPlayersRanking() {
        return playerRepository.findAll()
                .filter(player -> player.getGamesPlayed() > 0)
                .map(this::mapToRankingDTO)
                .collectList()
                .flatMapMany(this::assignRankingPositions);
    }

    // ----------------- Helpers ------------------

    private PlayerRankingResponseDTO mapToRankingDTO(Player player) {
        double winRate = (double) player.getGamesWon() / player.getGamesPlayed();
        String percentage = String.format("%.0f%%", winRate * 100);
        return new PlayerRankingResponseDTO(player.getName(), percentage, null);
    }

    private Flux<PlayerRankingResponseDTO> assignRankingPositions(List<PlayerRankingResponseDTO> list) {
        list.sort(Comparator.comparingDouble(p -> -parsePercentage(p.winPercentage())));

        for (int i = 0; i < list.size(); i++) {
            String label = switch (i + 1) {
                case 1 -> "1st";
                case 2 -> "2nd";
                case 3 -> "3rd";
                default -> (i + 1 == list.size()) ? "Last" : (i + 1) + "th";
            };
            PlayerRankingResponseDTO p = list.get(i);
            list.set(i, new PlayerRankingResponseDTO(p.playerName(), p.winPercentage(), label));
        }

        return Flux.fromIterable(list);
    }

    private double parsePercentage(String percentStr) {
        try {
            return Double.parseDouble(percentStr.replace("%", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private boolean wonGame(Game game) {
        return game.getStatus() == Game.GameStatus.PLAYER_WIN
                || game.getStatus() == Game.GameStatus.DEALER_BUST;
    }

    private int safeBalance(Player player) {
        return player.getBalance() != null ? player.getBalance() : 0;
    }

}
