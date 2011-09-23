package pokerstar;

import java.util.ArrayList;

/**
 *
 * @author Oyvind Selmer
 */
public class Game {
    private ArrayList<Player> players;
    private ArrayList<Card> communityCards; //Shared cards
    private int bigBlindPlayer = 0; //Player to post big blind
    private double bigBlind; //Price for posing big blind
    private double smallBlind;
    private double pot = 0.0;
    private Deck deck;
    //ctor
    public Game(ArrayList<Player> players, double bigBlind) {
        this.players = players;
        this.bigBlind = bigBlind;
        this.smallBlind = bigBlind / 2;
    }
    
    public void initGame() {
        deck = new Deck();
        communityCards = new ArrayList<Card>();
    }
    
    public void dealPreFlop() {
        for(Player player : players) {
            player.getCards()[0] = deck.dealCard();
            player.getCards()[1] = deck.dealCard();
            System.out.println(player.getName() + " , Hand: "+player.getCards()[0] + ", " + player.getCards()[1]);
        }
    }

    public void dealFlop() {
        deck.dealCard(); //Muck
        for(int i=0;i < 3;i++) communityCards.add(deck.dealCard());
        //System.out.println("Community cards: "+communityCards.get(0) + ", " + communityCards.get(1) +", " + communityCards.get(2)); 
    }

    public void dealTurn() {
        deck.dealCard(); //Muck
        communityCards.add(deck.dealCard());
    }

    public void dealRiver() {
        deck.dealCard(); //Muck
        communityCards.add(deck.dealCard());
        for(Card commCard: communityCards) {
            System.out.println("Community cards: "+commCard+ ", ");
        }
        //Checking hand ranks
        for(Player player : players) {
            HandRanker.checkRanking(player, communityCards);
            System.out.println(player.getRank());
        }
    }

    /* For now, we allow players to have a negative credit - later we might wanna add a credit check */
    public void placeBlinds() {
        Player bigP = players.get(bigBlindPlayer);
        Player smallP;
        if (bigBlindPlayer < players.size()-1)smallP = players.get(bigBlindPlayer+1);
        else smallP = players.get(0);
        bigP.setCredits(bigP.getCredits() - bigBlind);
        smallP.setCredits(smallP.getCredits() - smallBlind);
        pot += (bigBlind + smallBlind);
        bigBlindPlayer++;
    }
}
