package cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses;

import cat.itacademy.s05.t01.n01.S05T01N01.models.Card;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Game.GameStatus;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Game.MoveType;
import java.util.List;

public record GameCreateResponseDTO(
        String gameId,
        Long playerId,
        GameStatus status,
        List<Card> playerHand,
        List<Card> dealerHand,
        List<MoveType> moveHistory,
        Integer betAmount,
        String message
) {}
