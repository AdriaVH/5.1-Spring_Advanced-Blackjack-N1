package cat.itacademy.s05.t01.n01.S05T01N01.controllers;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.GameCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameDetailsResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GamePlayResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    // Create new game with player name (POST /game/new)
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameCreateResponseDTO> createGame(@RequestBody GameCreateRequestDTO request) {
        return gameService.createGame(request);
    }

    // Get game details (GET /game/{id})
    @GetMapping("/{id}")
    public Mono<GameDetailsResponseDTO> getGameDetails(@PathVariable String id) {
        return gameService.getGameDetails(id);
    }

    // Play a move (POST /game/{id}/play)
    @PostMapping("/{id}/play")
    public Mono<GamePlayResponseDTO> playMove(@PathVariable String id,
                                              @RequestBody PlayRequestDTO playRequestDTO) {
        return gameService.playMove(id, playRequestDTO);
    }

    // Delete a game (DELETE /game/{id}/delete)
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id);
    }
}
