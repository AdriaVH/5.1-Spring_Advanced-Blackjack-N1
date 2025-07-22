package cat.itacademy.s05.t01.n01.S05T01N01.controllers;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerUpdateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerRankingResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/player")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PlayerCreateResponseDTO> createPlayer(@RequestBody PlayerCreateRequestDTO request) {
        return playerService.createPlayer(request)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create player")));
    }

    @PutMapping("/player/{playerId}")
    public Mono<PlayerResponseDTO> updatePlayerName(@PathVariable Long playerId,
                                                    @RequestBody PlayerUpdateRequestDTO request) {
        return playerService.updatePlayerName(playerId, request)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found")));
    }

    @GetMapping("/ranking")
    public Flux<PlayerRankingResponseDTO> getAllPlayersRanking() {
        return playerService.getAllPlayersRanking()
                .switchIfEmpty(Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No players found")));
    }

}
