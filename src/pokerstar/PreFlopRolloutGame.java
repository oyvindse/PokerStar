package pokerstar;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Oyvind Selmer
 */
public class PreFlopRolloutGame {
    private ArrayList<Player> players;
    private Deck deck;
    private ArrayList<Card> communityCards;

    public PreFlopRolloutGame(ArrayList<Player> players) {
        this.players = players;
        communityCards = new ArrayList<Card>();
    }

    public void playRound(Card card1, Card card2) {
        deck = new Deck();
        deck.removeCards(card1, card2);
        dealPreflop(card1, card2);
        dealCommunityCards();
    }

    public void dealCommunityCards() {
        deck.dealCard();//muck one card
        for(int i=0;i < 3;i++) {
            communityCards.add(deck.dealCard());
        }
        deck.dealCard();//muck one card
        communityCards.add(deck.dealCard());
        deck.dealCard();//muck one card
        communityCards.add(deck.dealCard());
    }

    public void dealPreflop(Card card1, Card card2) {
        for(Player player : players) {
            if(player.getName().equalsIgnoreCase("selmer")) {
                player.getCards()[0] = card1;
                player.getCards()[1] = card2;
            }
            else {
                player.getCards()[0] = deck.dealCard();
                player.getCards()[1] = deck.dealCard();
            }
        }
    }

    public ArrayList<Player> getWinner() {
	getHandRankings();
	ArrayList<Player> winnerList = new ArrayList<Player>();
	Player winner = players.get(0);
	int winnerRank = winner.getRank().ordinal();
	winnerList.add(winner);
	for (int i = 1; i < players.size(); i++) {
            Player player = players.get(i);
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

    private void getHandRankings() {
        for (Player player : players) {
            HandRanker.checkRanking(player, communityCards);
            //System.out.println(player.getRank());
        }
    }

}//class PreFlopRolloutGame
