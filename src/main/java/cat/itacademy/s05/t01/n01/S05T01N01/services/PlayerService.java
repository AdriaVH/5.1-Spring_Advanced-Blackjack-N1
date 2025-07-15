package cat.itacademy.s05.t01.n01.S05T01N01.services;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.mappers.GameMapper;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.mappers.PlayerMapper;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.GameCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerUpdateRequestDTO;
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

    public Mono<PlayerResponseDTO> updatePlayerName(Long playerId, PlayerUpdateRequestDTO updateRequest) {
        return playerRepository.findById(playerId)
                .map(player -> PlayerMapper.toPlayer(updateRequest, player))
                .flatMap(playerRepository::save)
                .map(PlayerMapper::toPlayerResponseDTO);
    }

    public Mono<PlayerResponseDTO> getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .map(PlayerMapper::toPlayerResponseDTO);
    }

    // Other player-related business logic can be added here
}
