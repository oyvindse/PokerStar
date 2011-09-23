package pokerstar;

import java.util.List;

/**
 *
 * @author Oyvind Selmer
 */
public class Player {
    private double credits;
    private String name;
    //Hand properties
    private Card[] cards = new Card[2];
    private RankEnum rank;
    private List<Card> rankList;
    private Card highCard;

    //ctor
    public Player(String name, double credits) {
        this.name = name;
        this.credits = credits;
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

    void setHandRank(RankEnum rank) {
        this.rank = rank;
    }
}