package cat.itacademy.s05.t01.n01.S05T01N01.repositories;

import cat.itacademy.s05.t01.n01.S05T01N01.models.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {
}
