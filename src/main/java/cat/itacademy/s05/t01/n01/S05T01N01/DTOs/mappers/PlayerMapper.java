package cat.itacademy.s05.t01.n01.S05T01N01.DTOs.mappers;

import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.responses.PlayerResponseDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.DTOs.requests.PlayerUpdateRequestDTO;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Player;

public class PlayerMapper {

    public static PlayerResponseDTO toPlayerResponseDTO(Player player) {
        if (player == null) return null;
        return new PlayerResponseDTO(
                player.getId(),
                player.getName()
        );
    }

    public static Player toPlayer(PlayerUpdateRequestDTO dto, Player existingPlayer) {
        if (existingPlayer == null) return null;
        existingPlayer.setName(dto.newName());
        return existingPlayer;
    }
}
