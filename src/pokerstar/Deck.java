package pokerstar;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Oyvind Selmer
 */
public class Deck {
    private ArrayList<Card> cards;
    //ctor
    public Deck() {
        createDeck();
    }

    private void createDeck() {
        cards = new ArrayList<Card>();
        for (SuitEnum suit : SuitEnum.values()) {
            for (ValueEnum rank : ValueEnum.values()) {
                cards.add(new Card(suit, rank));
            }
	}
    }

    public Card dealCard() {
        return cards.remove(new Random().nextInt(cards.size()));
    }

    //used to remove pocket cards when generating preflop rollout tables
    public void removeCards(Card card1, Card card2) {
//        for(int i = 0;i<cards.size();i++) {
//            if(cards.get(i).equals(card1) || cards.get(i).equals(card2))cards.remove(i);
//        }
    }
}
