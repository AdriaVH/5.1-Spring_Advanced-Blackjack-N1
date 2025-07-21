package cat.itacademy.s05.t01.n01.S05T01N01.models;

public record Card(Card.Rank rank, Card.Suit suit, boolean faceUp) {

    public Card(Card.Rank rank, Card.Suit suit) {
        this(rank, suit, true); // default faceUp = true
    }

    @Override
    public String toString() {
        if (faceUp) {
            return rank + " of " + suit;
        } else {
            return "Face Down Card";
        }
    }

    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
        EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10), ACE(11);

        private final int value;

        Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
