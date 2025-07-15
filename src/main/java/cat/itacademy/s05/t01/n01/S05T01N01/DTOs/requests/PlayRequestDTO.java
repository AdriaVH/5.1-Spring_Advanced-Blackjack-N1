package cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests;

import cat.itacademy.s05.t01.n01.S05T01N01.models.Game;

public record PlayRequestDTO(Game.MoveType move, Integer betAmount) {}