
package pokerstar;

/**
 *
 * @author Bertie
 */
public class EquivalentClass {

    private Card card1;
    private Card card2;

    public EquivalentClass(Card card1, Card card2) {
        this.card1 = card1;
        this.card2 = card2;
    }

    public Card getCard1() {
        return card1;
    }

    public void setCard1(Card card1) {
        this.card1 = card1;
    }

    public Card getCard2() {
        return card2;
    }

    public void setCard2(Card card2) {
        this.card2 = card2;
    }

    @Override
    public String toString(){
        return "[("+card1.getSuit()+":"+card1.getValue()+")("+card2.getSuit()+":"+card2.getValue()+")]" ;
    }
}

