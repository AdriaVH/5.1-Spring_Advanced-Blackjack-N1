package cat.itacademy.s05.t01.n01.S05T01N01.services;

import cat.itacademy.s05.t01.n01.S05T01N01.models.*;
import cat.itacademy.s05.t01.n01.S05T01N01.utlis.HandEvaluator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameMoveService {

    public void applyMove(Game game, Game.MoveType move) {
        switch (move) {
            case HIT -> playerHit(game);
            case STAND -> playerStand(game);
            default -> throw new IllegalArgumentException("Unsupported move: " + move);
        }
    }

    private void playerHit(Game game) {
        List<Card> playerHand = game.getPlayerHand();
        Card newCard = game.getDeck().drawCard(true);
        playerHand.add(newCard);

        int playerScore = HandEvaluator.calculateHandValue(playerHand);

        if (playerScore > 21) {
            game.setStatus(Game.GameStatus.PLAYER_BUST);
        } else {
            game.setStatus(Game.GameStatus.PLAYER_TURN);
        }

        game.setPlayerHand(playerHand);
    }

    private void playerStand(Game game) {
        List<Card> dealerHand = game.getDealerHand();
        Deck deck = game.getDeck();

        revealDealerHoleCard(dealerHand);

        int dealerScore = HandEvaluator.calculateHandValue(dealerHand);
        int playerScore = HandEvaluator.calculateHandValue(game.getPlayerHand());

        while (dealerScore < 17) {
            dealerHand.add(deck.drawCard(true));
            dealerScore = HandEvaluator.calculateHandValue(dealerHand);
        }

        if (dealerScore > 21) {
            game.setStatus(Game.GameStatus.DEALER_BUST);
        } else if (dealerScore > playerScore) {
            game.setStatus(Game.GameStatus.DEALER_WIN);
        } else if (dealerScore < playerScore) {
            game.setStatus(Game.GameStatus.PLAYER_WIN);
        } else {
            game.setStatus(Game.GameStatus.PUSH);
        }

        game.setDealerHand(dealerHand);
    }

    private void revealDealerHoleCard(List<Card> dealerHand) {
        for (int i = 0; i < dealerHand.size(); i++) {
            Card card = dealerHand.get(i);
            if (!card.faceUp()) {
                dealerHand.set(i, new Card(card.rank(), card.suit(), true));
                break;
            }
        }
    }

    public boolean isGameOver(Game game) {
        return switch (game.getStatus()) {
            case PLAYER_BUST, PLAYER_WIN, DEALER_BUST, DEALER_WIN, PUSH, FINISHED -> true;
            default -> false;
        };
    }
}
