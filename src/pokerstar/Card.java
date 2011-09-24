package pokerstar;

/**
 *
 * @author Oyvind Selmer
 */
public class Card implements Comparable {
    private SuitEnum suit;
    private ValueEnum value;
    //ctor
    public Card(SuitEnum suit, ValueEnum value) {
        this.suit = suit;
        this.value = value;
    }
    
    public SuitEnum getSuit() {
        return suit;
    }

    public ValueEnum getValue() {
        return value;
    }

    public int getIntValue() {
        return value.ordinal();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
	} else if (!(obj instanceof Card)) {
            return false;
        } else {
            Card card2 = (Card) obj;
            return value.equals(card2.getValue()) && suit.equals(card2.getSuit());
	}
    }

    @Override
    public String toString() {
        return "Suit: " + suit.toString() + ", Value :" + value.toString();
    }

    public int compareTo(Object o) {
         return (this.value.ordinal()+2) - (((Card) o).getValue().ordinal()+2);
    }
}
