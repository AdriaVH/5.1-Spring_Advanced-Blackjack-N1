package cat.itacademy.s05.t01.n01.S05T01N01.services;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.mappers.PlayerMapper;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerUpdateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerRankingResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repositories.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
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
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Player not found with ID: " + playerId)))
                .map(player -> PlayerMapper.toPlayer(updateRequest, player))
                .flatMap(playerRepository::save)
                .map(PlayerMapper::toPlayerResponseDTO);
    }

    public Mono<PlayerResponseDTO> getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .map(PlayerMapper::toPlayerResponseDTO);
    }
    public Mono<PlayerRankingResponseDTO> getPlayerRanking(Long id) {
        return playerRepository.findAll()
                .sort((p1, p2) -> {
                    double ratio1 = p1.getGamesPlayed() == 0 ? 0 : (double) p1.getGamesWon() / p1.getGamesPlayed();
                    double ratio2 = p2.getGamesPlayed() == 0 ? 0 : (double) p2.getGamesWon() / p2.getGamesPlayed();
                    return Double.compare(ratio2, ratio1); // Descending: best win ratio first
                })
                .index()
                .filter(t -> t.getT2().getId() == id)
                .next()
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Player not found: " + id)))
                .map(t -> {
                    Player player = t.getT2();
                    int rank = t.getT1().intValue() + 1;
                    return new PlayerRankingResponseDTO(
                            player.getId(),
                            player.getName(),
                            player.getGamesWon(),
                            player.getGamesPlayed(),
                            rank
                    );
                });
    }

}




    // Other player-related business logic can be added here
