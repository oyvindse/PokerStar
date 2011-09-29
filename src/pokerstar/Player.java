package pokerstar;

import java.util.ArrayList;
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
    private DBManager db;
    //Hand properties
    private Card[] cards = new Card[2];
    private RankEnum rank;
    private List<Card> rankList;
    private Card highCard;
    private double bet = 0;

    //ctor
    public Player() {
    }
    
    public Player(String name, double credits) {
        this.name = name;
        this.credits = credits;
    }

    public Player(String name, double credits, int playerType) {
        this.name = name;
        this.credits = credits;
        this.playerType = playerType;
        db = new DBManager();
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
//        this.credits += getBet(); //Is 0 if not blind, or have betted earlier this round.
//        this.credits -= bet;
        this.bet = bet;
    }
    
    /*round:        |   action:
     * 0 = preflop  |   0 = fold
     * 1 = flop     |   1 = call
     * 2 = turn     |   2 = raise
     * 3 = river    |
     */
    public int makeAction(int round, int raises, double highestBet, boolean lastBetter, int players) {
        if(playerType == 1) {
            Random randomGen = new Random();
            int action = 0;
            //Preflop is totally random
            if(round == 0) {
                action = randomGen.nextInt(3);
            }
            //Flop is pretty random, but at least based on the hand rating
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
            //If there has been two raises allready, player calls -could be removed after i figured more than two raises is allowed ;>
            if(raises == 2 && action == 2) action = 1;
            return action;
        }
        else {
            int rateForRaise = 0;
            int rateForCall = 0;
            ArrayList<String> rollout;
            if(round == 0) {
                //Is blind
                if(getBet() > 0) {
                     rateForRaise = 2000;
                     rateForCall = 900;
                    //Preflop rollout
                    rollout = db.getRating(""+cards[0].getValue().name(), ""+cards[1].getValue().name(), ""+players);
                    for(int j=0;j<rollout.size();j++) {
                        if(rank.ordinal()>0) {
                            if(Double.parseDouble(rollout.get(j)) > rateForRaise) {
                                //Raises with a pocket pair with rate > 2500/10000
                                return 2;
                            }
                        }
                        else {
                            if(cards[0].getSuit().equals(cards[1].getSuit())) {
                                if(Double.parseDouble(rollout.get(j+1)) > rateForCall) {
                                    //Suited pocket, but nothing yet => call
                                    //System.out.println(name+" calls with rating: "+Double.parseDouble(rollout.get(j+1)));
                                    return 1;
                                }
                                else {
                                    //Below rateForCall
                                    return 0;
                                }
                            }
                            else {
                                //Good enough to call
                                if(Double.parseDouble(rollout.get(j)) > rateForCall) {
                                    //System.out.println(name+" calls with rating: "+Double.parseDouble(rollout.get(j+1)));
                                    return 1;
                                }
                                else {
                                    //Too bad
                                    return 0;
                                }
                            }
                        }
                    }
                }
                else {
                    rateForRaise = 3000;
                    rateForCall = 1100;
                    rollout = db.getRating(""+cards[0].getValue().name(), ""+cards[1].getValue().name(), ""+players);
                    for(int j=0;j<rollout.size();j++) {
                        if(rank.ordinal() > 0) {
                            if(Double.parseDouble(rollout.get(j)) > rateForRaise) {
                                return 2;
                            }
                        }
                        else {
                            if(cards[0].getSuit().equals(cards[1].getSuit())) {
                                if(Double.parseDouble(rollout.get(j+1)) > rateForRaise) {
                                    return 1;
                                }
                                else return 0;
                            }
                            else {
                                if(Double.parseDouble(rollout.get(j)) > rateForCall) {
                                    return 1;
                                }
                                else return 0;
                            }
                        }
                    }
                }
            }
            return 1;
        }
    }
}//class Player