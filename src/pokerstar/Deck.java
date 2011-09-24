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
}
