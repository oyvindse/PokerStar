package pokerstar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Oyvind Selmer
 */
public class Game {
    private final boolean DEBUG = true;
    private ArrayList<Player> players;
    private ArrayList<Player> playersInRound;
    private ArrayList<Card> communityCards; //Shared cards
    private Deck deck;
    private int round;
    //Betting
    private int bigBlindPlayer = 0; //Player to post big blind
    private double bigBlind; //Price for posing big blind
    private double smallBlind;
    private double pot = 0.0;
    private int raises = 0;
    private double highestBet = 0.0;
    private final double MAX_BET = 100.0;
    //ctor
    public Game(ArrayList<Player> players, double bigBlind) {
        this.players = players;
        this.bigBlind = bigBlind;
        this.smallBlind = bigBlind / 2;
        communityCards = new ArrayList<Card>();
    }
    
    public void initGame() {
        deck = new Deck();
        communityCards.clear();
        round = 0;
        pot = 0;
        highestBet = 0;
        raises = 0;
        playersInRound = new ArrayList<Player>();
        for(Player player : players) {
            player.setFolded(false);
        }
    }

    /* For now, we allow players to have a negative credit - later we might wanna add a credit check */
    public void placeBlinds() {
        Collections.rotate(players, 1);
        players.get(1).setBet(bigBlind);
        highestBet = bigBlind;
        players.get(0).setBet(smallBlind);
        playersInRound = players;
        if(DEBUG) {
            System.out.println(players.get(0).getName() + " posts Smallblind: " + smallBlind + " credits");
            System.out.println(players.get(1).getName() + " posts Bigblind: " + bigBlind + " credits");
        }
        
    }
    
    public void dealPreFlop() {
        for(Player player : players) {
            player.getCards()[0] = deck.dealCard();
            player.getCards()[1] = deck.dealCard();
            System.out.println(player.getName() + " , Hand: "+player.getCards()[0] + ", " + player.getCards()[1]);
        }
    }

    public void doBetting() {
        getHandRankings();
        boolean bettingDone = false;
        boolean lastBetter = false;
        Player player;
        int action = -1;
        int counter = 0;
        int raiser = 1;
        //No blinds after preflop
        if(round > 0){
            highestBet = 0;
            raiser = 0;
        }
        while(!bettingDone) {
        //int highBidder = 0; //Used to make sure that the bidding continues one more round after a raise..
            if(round == 0) {
                for(int i=0; i<playersInRound.size();i++) {
                    player = playersInRound.get(i);
                    if(!player.getFolded() && counter > 1) {
                        action = player.makeAction(round, raises, highestBet, lastBetter);
                        //if everyone else called/folded the betting ends
                        if(raiser == i) {
                            bettingDone=true;
                            round++;
                            //System.out.println("Players still in game after betting: ");
                            return;
                        }
//                        if((i==0 || i ==1) && round == 0 && playersInRound.get(playersInRound.size()-1).getBet() == 0
//                                        && !playersInRound.get(playersInRound.size()-1).getFolded()); //System.out.println(player.getName()+" has posted blind");//do nothing
                        else if(action == 2) {
                            if(highestBet == MAX_BET) action = 1;
                            else {
                                raises++;
                                highestBet = highestBet + bigBlind;
                                raiser = i;
                                //highBidder = i;
                                player.setBet(highestBet);
                            }
                        }
                        else if(action == 1) {
                            player.setBet(highestBet);
                        }
                        else if(action == 0) {
                            player.setFolded(true);
                        }
                        if(DEBUG){
                            switch(action) {
                                case 0: System.out.println(player.getName() + " folded");break;
                                case 1: System.out.println(player.getName() + " calls: " + player.getBet()+" credits");break;
                                case 2: System.out.println(player.getName() + " raises/bets with " + bigBlind + " which totals: " + player.getBet()+" credits");break;
                            }
                        }
                    }
                    counter++;
                }
            }//if(round == 0)
            //else = flop or later
            else {
                for(int i=0; i<playersInRound.size();i++) { 
                    player = playersInRound.get(i);
                    if(i == playersInRound.size()-1)lastBetter = true;
                    action = player.makeAction(round, raises, highestBet, lastBetter);
                    if((raiser == i && counter > 0) || playersInRound.size() == 1) {
                        bettingDone=true;
                        round++;
                        //System.out.println("Players still in game after betting: ");
                       return;
                    }
                     else if(action == 2 && !player.getFolded()) {
                        if(highestBet == MAX_BET) action = 1;
                        else {
                            raises++;
                            highestBet = highestBet + bigBlind;
                            raiser = i;
                            player.setBet(highestBet);
                        }
                    }
                    else if(action == 1) {
                        player.setBet(highestBet);
                    }
                    else if(action == 0) {
                        //Checks in stead of calling
                        if(highestBet == player.getBet()) {
                            action = 1;
                        }
                        else player.setFolded(true);
                    }
                    if(DEBUG) {
                        switch(action) {
                            case 0: System.out.println(player.getName() + " folded");break;
                            case 1: System.out.println(player.getName() + " calls: " + player.getBet()+" credits");break;
                            case 2: System.out.println(player.getName() + " raises/bets with " + bigBlind + " which totals: " + player.getBet()+" credits");break;
                        }
                    }
                    counter++;
                }
            }
        }
        round++;
    }

    public void updateBalance() {
        for(Player player : players) {
            player.setCredits(player.getCredits() - player.getBet());
        }
    }

    public void betsToPot() {
        updateBalance();
        for(Player player : players) {
            double pBet = player.getBet();
            pot +=pBet;
            player.setBet(0);
            raises = 0;
            highestBet = 0;
        }
        removeFoldedPlayers();
        if(DEBUG)System.out.println("The pot total: "+pot+" credits");
    }

    public void dealFlop() {
        deck.dealCard(); //Muck
        for(int i=0;i < 3;i++) communityCards.add(deck.dealCard());
        //System.out.println("Community cards: "+communityCards.get(0) + ", " + communityCards.get(1) +", " + communityCards.get(2)); 
    }

    public void dealTurn() {
        //If more than one player left
        if(playersInRound.size()>1) {
            deck.dealCard(); //Muck
            communityCards.add(deck.dealCard());
        }
    }

    public void dealRiver() {
        //If more than one player left
        if(playersInRound.size()>1) {
            deck.dealCard(); //Muck
            communityCards.add(deck.dealCard());
        }
        if(DEBUG) {
            for(Card commCard: communityCards) {
                System.out.println("Community cards: "+commCard+ ", ");
            }
        }
    }

    public void removeFoldedPlayers() {
        playersInRound = new ArrayList<Player>();
        //playersInRound.add(players.get(0));
        for(int i=0;i<players.size();i++) {
            if(!players.get(i).getFolded()) {
                playersInRound.add(players.get(i));
                if(DEBUG)System.out.println("Still in game(round "+round+"): "+players.get(i).getName());
            }
        }
        //if(playersInRound.get(0).getFolded())playersInRound.remove(0);
    }

   //TODO: Doesn't calculate draw right...
   public List<Player> getWinner() {
	getHandRankings();
	List<Player> winnerList = new ArrayList<Player>();
	Player winner = playersInRound.get(0);
	int winnerRank = winner.getRank().ordinal();
	winnerList.add(winner);
	for (int i = 1; i < playersInRound.size(); i++) {
            Player player = playersInRound.get(i);
            int playerRank = player.getRank().ordinal();
            //Draw game
            if (winnerRank == playerRank) {
		Player highHandPlayer = checkHighSequence(winner, player);
		//Draw checkHighSequence
		if (highHandPlayer == null) {
                    highHandPlayer = checkHighCardWinner(winner, player);
		}
		//Not draw in checkHighSequence or checkHighCardWinner
		if (highHandPlayer != null && !winner.equals(highHandPlayer)) {
                    winner = highHandPlayer;
                    winnerList.clear();
                    winnerList.add(winner);
		} else if (highHandPlayer == null) {
                    //Draw in checkHighSequence and checkHighCardWinner
                    winnerList.add(winner);
		}
            } else if (winnerRank < playerRank) {
		winner = player;
		winnerList.clear();
		winnerList.add(winner);
            }
            winnerRank = winner.getRank().ordinal();
        }
        double winnings = pot / winnerList.size();
        for(Player player : winnerList) {
            player.setCredits(player.getCredits() + winnings);
        }
        return winnerList;
    }

    private Player checkHighSequence(Player player1, Player player2) {
        int player1Rank = sumRankingList(player1);
	int player2Rank = sumRankingList(player2);
	if (player1Rank > player2Rank) {
            return player1;
	} else if (player1Rank < player2Rank) {
            return player2;
        }
        return null;
    }

    private int sumRankingList(Player player) {
        Integer sum = 0;
	for (Card card : player.getRankList()) {
            sum += card.getIntValue();
	}
        return sum;
    }
    
    @SuppressWarnings("unchecked")
    private Player checkHighCardWinner(Player player1, Player player2) {
	Player winner = compareHighCard(player1, player1.getHighCard(), player2, player2.getHighCard());
	if (winner == null) {
            Card player1Card = HandRanker.getHighCard(player1, Collections.EMPTY_LIST);
            Card player2Card = HandRanker.getHighCard(player2, Collections.EMPTY_LIST);
            winner = compareHighCard(player1, player1Card, player2, player2Card);
            if (winner != null) {
		player1.setHighCard(player1Card);
		player2.setHighCard(player2Card);
            } else if (winner == null) {
                player1Card = getSecondHighCard(player1, player1Card);
                player2Card = getSecondHighCard(player2, player2Card);
                winner = compareHighCard(player1, player1Card, player2, player2Card);
                if (winner != null) {
                    player1.setHighCard(player1Card);
                    player2.setHighCard(player2Card);
                }
            }
        }
        return winner;
    }

    private Player compareHighCard(Player player1, Card player1HighCard, Player player2, Card player2HighCard) {
        if (player1HighCard.getIntValue() > player2HighCard.getIntValue()) {
            return player1;
	} else if (player1HighCard.getIntValue() < player2HighCard.getIntValue()) {
            return player2;
	}
        return null;
    }

    private Card getSecondHighCard(Player player, Card card) {
        if (player.getCards()[0].equals(card)) {
            return player.getCards()[1];
	}
	return player.getCards()[0];
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    private void getHandRankings() {
        for (Player player : playersInRound) {
            HandRanker.checkRanking(player, communityCards);
            //System.out.println(player.getRank());
        }
    }
}
