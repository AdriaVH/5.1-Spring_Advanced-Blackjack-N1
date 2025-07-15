package cat.itacademy.s05.t01.n01.S05T01N01.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "games")
public class Game {

    @Id
    private String id;

    private Long playerId; // References Player in MySQL

    private Deck deck;

    private List<Card> playerHand;
    private List<Card> dealerHand;

    private List<MoveType> moveHistory;

    private GameStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    private Integer betAmount;

    public enum GameStatus {
        IN_PROGRESS,
        PLAYER_TURN,
        DEALER_TURN,
        PLAYER_BUST,
        DEALER_BUST,
        PLAYER_WIN,
        DEALER_WIN,
        PUSH,
        CANCELLED,
        FINISHED
    }

    public enum MoveType {
        HIT,
        STAND
    }
}
