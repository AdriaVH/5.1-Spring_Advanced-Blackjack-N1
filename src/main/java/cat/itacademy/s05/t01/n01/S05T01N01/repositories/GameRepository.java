package cat.itacademy.s05.t01.n01.S05T01N01.repositories;

import cat.itacademy.s05.t01.n01.S05T01N01.models.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, String> {
    Mono<Game> findByPlayerId(Long playerId);
}
