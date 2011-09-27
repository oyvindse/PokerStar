package pokerstar;

import java.util.List;
import java.util.Random;

/**
 *
 * @author Oyvind Selmer
 */
public class Player {
    private double credits;
    private String name;
    private boolean hasFolded;
    private int playerType;
    //Hand properties
    private Card[] cards = new Card[2];
    private RankEnum rank;
    private List<Card> rankList;
    private Card highCard;
    private double bet = 0;

    //ctor
    public Player(String name, double credits) {
        this.name = name;
        this.credits = credits;
    }

    public Player(String name, double credits, int playerType) {
        this.name = name;
        this.credits = credits;
        this.playerType = playerType;
    }

    public String getName() {
        return name;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public Card[] getCards() {
	return cards;
    }

    public void setCards(Card[] cards) {
	this.cards = cards;
    }

    public Card getHighCard() {
        return highCard;
    }

    public void setHighCard(Card highCard) {
        this.highCard = highCard;
    }

    public RankEnum getRank() {
        return rank;
    }

    public void setRank(RankEnum rank) {
        this.rank = rank;
    }

    public List<Card> getRankList() {
        return rankList;
    }

    public void setRankList(List<Card> rankList) {
        this.rankList = rankList;
    }

    public void setHandRank(RankEnum rank) {
        this.rank = rank;
    }

    public void setFolded(boolean hasFolded) {
        this.hasFolded = hasFolded;
    }
    
    public boolean getFolded() {
        return hasFolded;
    }

    public double getBet() {
        return bet;
    }

    public void setBet(double bet) {
        this.credits += getBet();
        this.credits -= bet;
        this.bet = bet;
    }
    
    /*round:        |   action:
     * 0 = preflop  |   0 = fold
     * 1 = flop     |   1 = call
     * 2 = turn     |   2 = raise
     * 3 = river    |
     */
    public int makeAction(int round, int raises, double highestBet, boolean lastBetter) {
        Random randomGen = new Random();
        int action = 0;
        //Preflop is totally random
        if(round == 0) {
            action = randomGen.nextInt(3);
        }
        //Flop is pretty random, but based on the hand rating
        if(round > 0) {
            int callOrRaise = randomGen.nextInt(2);
            switch(rank.ordinal()) {
                case 0: action = 0;
                case 1: action = randomGen.nextInt(2);break;
                case 2: action = callOrRaise+1;break;
                case 3: action = callOrRaise+1;break; //call or raise
                case 4: action = callOrRaise+1;break;
                case 5: action = callOrRaise+1;break;
                case 6: action = callOrRaise+1;break;
                case 7: action = callOrRaise+1;break;
                case 8: action = callOrRaise+1;break;
                case 9: action = callOrRaise+1;break;
            }
            //System.out.println(getName()+" chooses to "+action+" based on the hand: "+rank.ordinal());
        }
        if(lastBetter && highestBet == 0) action = 2;
        //If there has been two raises allready, player calls
        if(raises == 2 && action == 2) action = 1;
        return action;
    }

}