package cat.itacademy.s05.t01.n01.S05T01N01.controllers;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.GameCreateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameCreateResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GameDetailsResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.GamePlayResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.services.GameService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
@Tag(name = "Game API", description = "Endpoints for managing Blackjack games")
public class GameController {

    private final GameService gameService;

    @Operation(summary = "Create a new game",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Game created successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GameCreateResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameCreateResponseDTO> createGame(@RequestBody GameCreateRequestDTO request) {
        return gameService.createGame(request)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create game")));
    }

    @Operation(summary = "Get game details by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Game details retrieved",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GameDetailsResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<GameDetailsResponseDTO> getGameDetails(@PathVariable String id) {
        return gameService.getGameDetails(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found")));
    }

    @Operation(summary = "Play a move on the game",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Move played successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GamePlayResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Game not found or invalid move")
            }
    )
    @PostMapping(value = "/{id}/play",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<GamePlayResponseDTO> playMove(@PathVariable String id,
                                              @RequestBody PlayRequestDTO playRequestDTO) {
        return gameService.playMove(id, playRequestDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not play move - game not found or invalid")));
    }

    @Operation(summary = "Delete a game by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Game deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .then();  // just return completion signal
    }

}
