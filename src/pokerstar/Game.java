package pokerstar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        //For testing purposes
        for(Card commCard: communityCards) {
            System.out.println("Community cards: "+commCard+ ", ");
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

    public List<Player> getWinner() {
        boolean dirty = false;
        getHandRankings();
        List<Player> winners = new ArrayList<Player>();
        winners.add(players.get(0));
        int winnerRank = winners.get(0).getRank().ordinal();
        for(int i=1;i < players.size();i++) {
            dirty = false;
            //System.out.println("Spiller: " + players.get(i).getName());
            Player player = players.get(i);
            int playerRank = player.getRank().ordinal();
            //if draw
            if(winnerRank == playerRank) {
               List<Card> p1Cards = HandRanker.getMergedCardList(winners.get(0), communityCards);
               List<Card> p2Cards = HandRanker.getMergedCardList(player, communityCards);
               Collections.sort(p1Cards);
               Collections.sort(p2Cards);
               Collections.reverse(p1Cards);
               Collections.reverse(p2Cards);
               for(int j=0;j < p1Cards.size();j++) {
                   if(p1Cards.get(j).getIntValue() < p2Cards.get(j).getIntValue()) {
                       winners.clear();
                       winners.add(player);
                       winnerRank = player.getRank().ordinal();
                       dirty = true;
                   }
                   if(!dirty && j==p1Cards.size()-1 && p1Cards.get(j).getIntValue() == p2Cards.get(j).getIntValue()) {
                       winners.add(player);
                   }
               }
            }
            else if(!winners.contains(player) && playerRank > winnerRank) {
                winners.clear();
                winners.add(player);
                winnerRank = player.getRank().ordinal();
            }
        }
        return winners;
    }

    private void getHandRankings() {
        for (Player player : players) {
            HandRanker.checkRanking(player, communityCards);
            System.out.println(player.getRank());
        }
    }
}
