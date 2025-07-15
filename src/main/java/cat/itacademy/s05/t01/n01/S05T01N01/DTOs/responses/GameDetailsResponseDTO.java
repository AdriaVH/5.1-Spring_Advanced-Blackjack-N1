package cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses;

import cat.itacademy.s05.t01.n01.S05T01N01.models.Card;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Game;

import java.util.List;

public record GameDetailsResponseDTO(
        String gameId,
        Long playerId,
        List<Card> playerHand,
        List<Card> dealerHand,
        List<Game.MoveType> moveHistory,
        Game.GameStatus status,
        Integer betAmount
) {}