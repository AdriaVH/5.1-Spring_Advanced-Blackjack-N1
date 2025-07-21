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
                // By default cards are face up when initialized in the deck (just a template)
                cards.add(new Card(rank, suit, true));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws a card and returns it as face up by default.
     * @return Card drawn face up.
     */
    public Card drawCard() {
        return drawCard(true);
    }

    /**
     * Draws a card specifying if it should be face up or down.
     * @param faceUp true if card is face up, false otherwise.
     * @return Card drawn with specified visibility.
     */
    public Card drawCard(boolean faceUp) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        Card card = cards.remove(0);
        // Return new Card instance with the same rank and suit, but specified faceUp visibility
        return new Card(card.rank(), card.suit(), faceUp);
    }
}
