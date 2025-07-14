package cat.itacademy.s05.t01.n01.S05T01N01.repositories;

import cat.itacademy.s05.t01.n01.S05T01N01.models.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository <Game, Long>{
}
