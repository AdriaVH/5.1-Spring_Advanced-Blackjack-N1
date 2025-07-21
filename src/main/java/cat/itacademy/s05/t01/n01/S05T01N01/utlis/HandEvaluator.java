package cat.itacademy.s05.t01.n01.S05T01N01.utlis;


import cat.itacademy.s05.t01.n01.S05T01N01.models.Card;
import cat.itacademy.s05.t01.n01.S05T01N01.models.Card.Rank;

import java.util.List;

public class HandEvaluator {

    public static int calculateHandValue(List<Card> hand) {
        int totalValue = 0;
        int aceCount = 0;

        for (Card card : hand) {
            Rank rank = card.rank();
            totalValue += rank.getValue();
            if (rank == Rank.ACE) {
                aceCount++;
            }
        }

        while (totalValue > 21 && aceCount > 0) {
            totalValue -= 10;
            aceCount--;
        }

        return totalValue;
    }
}
