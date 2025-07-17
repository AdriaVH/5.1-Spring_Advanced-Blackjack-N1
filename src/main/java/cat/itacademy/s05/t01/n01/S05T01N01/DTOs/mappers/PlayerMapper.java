package cat.itacademy.s05.t01.n01.S05T01N01.DTOs.mappers;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerUpdateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Player;

public class PlayerMapper {

    // Map Player entity to PlayerResponseDTO
    public static PlayerResponseDTO toPlayerResponseDTO(Player player) {
        if (player == null) return null;
        return new PlayerResponseDTO(
                player.getId(),
                player.getName()
        );
    }

    // Map PlayerUpdateRequestDTO to Player entity (for update)
    public static Player toPlayer(PlayerUpdateRequestDTO dto, Player existingPlayer) {
        if (existingPlayer == null) return null;
        existingPlayer.setName(dto.newName());
        return existingPlayer;
    }
}
