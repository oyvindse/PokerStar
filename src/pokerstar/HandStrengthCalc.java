package pokerstar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
/**
 *
 * @author Oyvind Selmer
 */
public class HandStrengthCalc
{

    private ArrayList<Card> deck;
    private static ArrayList<EquivalentClass> cardList = new ArrayList();
    private Player player1 = new Player();
    private Player player2 = new Player();
    Random r = new Random();

    public ArrayList<EquivalentClass> getCardList()
    {
        return cardList;
    }

    public HandStrengthCalc()
    {
        deck = new ArrayList<Card>();
    }

    public void generateDeck()
    {
        deck = new ArrayList();
        for (SuitEnum suit : SuitEnum.values()) {
            for (ValueEnum rank : ValueEnum.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        // For phun ^^
        Collections.shuffle(deck);
    }

    public double getHandStrengthCalc(ArrayList<Card> holeCards, ArrayList<Card> sharedCards, int playersLeft)
    {
        // Generates a fresh deck
        generateDeck();

        for (int i = 0; i < holeCards.size(); i++) {
            for (int j = 0; j < deck.size(); j++) {
                if (holeCards.get(i).equals(deck.get(j))) {
                    deck.remove(deck.get(j));
                }
            }
        }
        // Removes community cards from deck
        for (int i = 0; i < sharedCards.size(); i++) {
            for (int j = 0; j < deck.size(); j++) {
                if (sharedCards.get(i).getSuit().equals(deck.get(j).getSuit()) && sharedCards.get(i).getValue().equals(deck.get(j).getValue())) {
                    deck.remove(deck.get(j));
                }
            }
        }
        // Removes duplicates
        boolean exists = false;
        for (int i = 0; i < deck.size(); i++) {
            for (int j = 0; j < deck.size(); j++) {
                if (!deck.get(i).equals(deck.get(j))) {
                    exists = false;
                    for (int k = 0; k < cardList.size(); k++) {
                        if (cardList.get(k).getCard1().equals(deck.get(j))) {
                        exists = true;
                        }
                    }
                    if (!exists) {
                        cardList.add(new EquivalentClass(deck.get(i), deck.get(j)));
                    }
                }
            }
        }

        // Contains the player's cards
        ArrayList<Card> myCards = new ArrayList();
        for (int i = 0; i < sharedCards.size(); i++)
        {
            myCards.add(sharedCards.get(i));

        }
        for (int i = 0; i < holeCards.size(); i++)
        {
            myCards.add(holeCards.get(i));

        }
        player2.getCards()[0] = holeCards.get(0);
        player2.getCards()[1] = holeCards.get(1);
        HandRanker.checkRanking(player2, sharedCards);
        int winCounter = 0;
        int tieCounter = 0;
        int tempPlayer2Power = player2.getRank().ordinal();

        for (int i = 0; i < cardList.size(); i++)
        {
            // Contains possible hand combinations
            ArrayList<Card> checkList = new ArrayList<Card>();
            checkList.add(cardList.get(i).getCard1());
            checkList.add(cardList.get(i).getCard2());
            //System.out.println("jaa" + checkList.get(i));
            for (int j = 0; j < sharedCards.size(); j++)
            {
                checkList.add(sharedCards.get(j));
            }
            player1.getCards()[0] = checkList.get(0);
            player1.getCards()[1] = checkList.get(1);
            HandRanker.checkRanking(player1, sharedCards);
            if (player1.getRank().ordinal() < tempPlayer2Power )
            {
                winCounter++;
            } else if (player1.getRank().ordinal() == tempPlayer2Power)
            {
                tieCounter++;
            }

                checkList.clear();

        }

        deck.clear();
        holeCards.clear();
        sharedCards.clear();
        double temp = Math.pow(((winCounter + (tieCounter / 2.0)) / cardList.size()), playersLeft);
        cardList.clear();
        myCards.clear();
        // Returns hand strength based on formula
        return temp;


    }

    public static void main(String[] args)
    {
        HandStrengthCalc hs = new HandStrengthCalc();
        ArrayList<Card> holeCards = new ArrayList<Card>();
        ArrayList<Card> sharedCards = new ArrayList<Card>();

        holeCards.add(new Card(SuitEnum.SPADE, ValueEnum.THREE));
        holeCards.add(new Card(SuitEnum.SPADE, ValueEnum.SEVEN));

        sharedCards.add(new Card(SuitEnum.SPADE, ValueEnum.FOUR));
        sharedCards.add(new Card(SuitEnum.SPADE, ValueEnum.FIVE));
        sharedCards.add(new Card(SuitEnum.SPADE, ValueEnum.SIX));

//        holeCards.add(new Card(3, "S"));
//        holeCards.add(new Card(7, "S"));
//
//        sharedCards.add(new Card(6, "S"));
//        sharedCards.add(new Card(5, "S"));
//        sharedCards.add(new Card(4, "S"));




        System.out.println(hs.getHandStrengthCalc(holeCards, sharedCards, 4));


    }
}
