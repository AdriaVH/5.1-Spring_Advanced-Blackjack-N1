package cat.itacademy.s05.t01.n01.S05T01N01.repositories;

import cat.itacademy.s05.t01.n01.S05T01N01.models.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository <Player, Long> {
}
