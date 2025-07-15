package cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses;

import cat.itacademy.s05.t01.n01.S05T01N01.models.Game;

public record PlayResponseDTO(
        Game.MoveType move,
        String resultMessage
) {}