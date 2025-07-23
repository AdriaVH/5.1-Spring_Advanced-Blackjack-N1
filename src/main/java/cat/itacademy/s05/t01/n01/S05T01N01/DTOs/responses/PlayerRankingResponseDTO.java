package cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses;


public record PlayerRankingResponseDTO(
        String playerName,
        String winPercentage,
        String position
) {}
