package cat.itacademy.s05.t01.n01.S05T01N01.models;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deck {
    private List<Card> cards;

    public void initialize() {
        cards = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {  ////// cambiar a excepcio personalitzada
        if (cards == null || cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        return cards.remove(0);
    }
}

