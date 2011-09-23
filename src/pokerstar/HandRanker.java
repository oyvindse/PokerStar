package pokerstar;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Oyvind Selmer
 */
public class HandRanker {

    public void checkRanking(Player player, ArrayList<Card> communityCards) {

    }

    public static Card getHighCard(Player player, ArrayList<Card> communityCards) {
	ArrayList<Card> allCards = new ArrayList<Card>();
	if(communityCards != null)allCards.addAll(communityCards);
	allCards.add(player.getCards()[0]);
	allCards.add(player.getCards()[1]);
        //Sorts cards in ascending value
	Collections.sort(allCards);
	return allCards.get(allCards.size()-1);
    }

    public static ArrayList<Card> getPair(Player player, ArrayList<Card> communityCards) {
        ArrayList<Card> allCards = new ArrayList<Card>();
        if(communityCards != null)allCards.addAll(communityCards);
        for(int i=0;i<allCards.size();i++) {
            if(i+1 > allCards.size()-1) {
                if(allCards.get(i).getIntValue() == allCards.get(i+1).getIntValue()) {
                    return 
                }
            }
        }
    }

    private static void setRankEnumAndList(Player player, RankEnum rankEnum, ArrayList<Card> rankList) {
        player.setRank(rankEnum);
        player.setRankList(rankList);
    }


}
