package cat.itacademy.s05.t01.n01.S05T01N01.DTOs.mappers;


import cat.itacademy.s05.t01.n01.S05T01N01.models.*;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.*;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.*;

public class GameMapper {

    public static GameCreateResponseDTO toGameCreateResponse(Game game) {
        return new GameCreateResponseDTO(
                game.getId(),
                game.getPlayerId(),
                game.getStatus(),
                game.getPlayerHand(),
                game.getDealerHand(),
                game.getMoveHistory(),
                game.getBetAmount(),
                "Game created successfully"
        );
    }

    public static GameDetailsResponseDTO toGameDetailsResponse(Game game) {
        return new GameDetailsResponseDTO(
                game.getId(),
                game.getPlayerId(),
                game.getPlayerHand(),
                game.getDealerHand(),
                game.getMoveHistory(),
                game.getStatus(),
                game.getBetAmount()
        );
    }

    public static GamePlayResponseDTO toGamePlayResponse(Game game, String message) {
        return new GamePlayResponseDTO(
                game.getId(),
                game.getStatus(),
                game.getPlayerHand(),
                game.getDealerHand(),
                game.getMoveHistory(),
                game.getBetAmount(),
                message
        );
    }

    public static Game.MoveType toMoveType(PlayRequestDTO dto) {
        return dto.move();
    }

    public static Integer toBetAmount(PlayRequestDTO dto) {
        return dto.betAmount();
    }
}
