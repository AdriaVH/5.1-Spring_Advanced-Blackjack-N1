package cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses;

public record PlayerCreateResponseDTO(
        long id,
        String name,
        Integer balance,
        int gamesWon,
        int gamesPlayed
) {
}
