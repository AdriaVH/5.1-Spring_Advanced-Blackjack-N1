package cat.itacademy.s05.t01.n01.S05T01N01.controllers;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerRankingResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerUpdateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    public Mono<PlayerCreateResponseDTO> createPlayer(@RequestBody PlayerCreateRequestDTO request) {
       return playerService.createPlayer(request);
    }
    // Update player name (PUT /player/{playerId})
    @PutMapping("/{playerId}")
    public Mono<PlayerResponseDTO> updatePlayerName(@PathVariable Long playerId,
                                                    @RequestBody PlayerUpdateRequestDTO request) {
        return playerService.updatePlayerName(playerId, request);
    }

    // Get ranking of players (GET /ranking)
    @GetMapping("/ranking")
    public Mono<PlayerRankingResponseDTO> getPlayerRanking(@PathVariable Long id) {
        return playerService.getPlayerRanking(id);
    }
}
