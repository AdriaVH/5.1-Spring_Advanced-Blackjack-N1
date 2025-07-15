package cat.itacademy.s05.t01.n01.S05T01N01.controllers;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerUpdateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.mappers.PlayerMapper;
import cat.itacademy.s05.t01.n01.S05T01N01.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    // Update player name (PUT /player/{playerId})
    @PutMapping("/{playerId}")
    public Mono<PlayerResponseDTO> updatePlayerName(@PathVariable Long playerId,
                                                    @RequestBody PlayerUpdateRequestDTO request) {
        return playerService.updatePlayerName(playerId, request)
                .map(PlayerMapper::toPlayerResponseDTO);
    }

    // Get ranking of players (GET /ranking)
    @GetMapping("/ranking")
    public Flux<PlayerResponseDTO> getPlayerRanking() {
        return playerService.getPlayerRanking()
                .map(PlayerMapper::toPlayerResponseDTO);
    }
}
