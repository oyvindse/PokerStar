package pokerstar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static pokerstar.RankEnum.*;
import static pokerstar.ValueEnum.*;

/**
 *
 * @author Oyvind Selmer
 */
public class HandRanker {

    public static void checkRanking(Player player, List<Card> communityCards) {

		//HIGH_CARD
		Card highCard = getHighCard(player, communityCards);
		player.setHighCard(highCard);

		//ROYAL_FLUSH
		List<Card> rankingList = getRoyalFlush(player, communityCards);
		if (rankingList != null) {
			setRankEnumAndList(player, ROYAL_FLUSH, rankingList);
			return;
		}
		//STRAIGHT_FLUSH
		rankingList = getStraightFlush(player, communityCards);
		if (rankingList != null) {
			setRankEnumAndList(player, STRAIGHT_FLUSH,
					rankingList);
			return;
		}
		//FOUR_OF_A_KIND
		rankingList = getFourOfAKind(player, communityCards);
		if (rankingList != null) {
			setRankEnumAndList(player, FOUR_OF_A_KIND,
					rankingList);
			return;
		}
		//FULL_HOUSE
		rankingList = getFullHouse(player, communityCards);
		if (rankingList != null) {
			setRankEnumAndList(player, FULL_HOUSE, rankingList);
			return;
		}
		//FLUSH
		rankingList = getFlush(player, communityCards);
		if (rankingList != null) {
			setRankEnumAndList(player, FLUSH, rankingList);
			return;
		}
		//STRAIGHT
		rankingList = getStraight(player, communityCards);
		if (rankingList != null) {
			setRankEnumAndList(player, STRAIGHT, rankingList);
			return;
		}
		//THREE_OF_A_KIND
		rankingList = getThreeOfAKind(player, communityCards);
		if (rankingList != null) {
			setRankEnumAndList(player, THREE_OF_A_KIND,
					rankingList);
			return;
		}
		//TWO_PAIR
		rankingList = getTwoPair(player, communityCards);
		if (rankingList != null) {
			setRankEnumAndList(player, TWO_PAIR, rankingList);
			return;
		}
		//ONE_PAIR
		rankingList = getOnePair(player, communityCards);
		if (rankingList != null) {
			setRankEnumAndList(player, ONE_PAIR, rankingList);
			return;
		}
		//HIGH_CARD
		player.setHandRank(HIGH_CARD);
		List<Card> highCardRankingList = new ArrayList<Card>();
		highCardRankingList.add(highCard);
		player.setRankList(highCardRankingList);
		return;
    }

    public static Card getHighCard(Player player, List<Card> communityCards) {
	List<Card> allCards = new ArrayList<Card>();
	if(communityCards != null)allCards.addAll(communityCards);
	allCards.add(player.getCards()[0]);
	allCards.add(player.getCards()[1]);
        //Sorts cards in ascending value
	Collections.sort(allCards);
	return allCards.get(allCards.size()-1);
    }

    public static List<Card> getRoyalFlush(Player player, List<Card> communityCards) {
	if (!isSameSuit(player, communityCards)) {
            return null;
	}
	List<ValueEnum> valueEnumList = toValueEnumList(player, communityCards);
	if (valueEnumList.contains(TEN) && valueEnumList.contains(JACK)
                                           && valueEnumList.contains(QUEEN)
                                           && valueEnumList.contains(KING)
				           && valueEnumList.contains(ACE)) {
                                               return getMergedCardList(player, communityCards);
	}
        return null;
    }

    public static List<Card> getStraightFlush(Player player, List<Card> communityCards) {
	return getSequence(player, communityCards, 5, true);
    }

    public static List<Card> getFourOfAKind(Player player, List<Card> communityCards) {
	List<Card> mergedList = getMergedCardList(player, communityCards);
	return checkPair(mergedList, 4);
    }

    public static List<Card> getFullHouse(Player player, List<Card> communityCards) {
	List<Card> mergedList = getMergedCardList(player, communityCards);
	List<Card> threeList = checkPair(mergedList, 3);
	if (threeList != null) {
            mergedList.removeAll(threeList);
            List<Card> twoList = checkPair(mergedList, 2);
            if (twoList != null) {
                threeList.addAll(twoList);
		return threeList;
            }
	}
	return null;
    }

    public static List<Card> getFlush(Player player, List<Card> communityCards) {
	List<Card> mergedList = getMergedCardList(player, communityCards);
	List<Card> flushList = new ArrayList<Card>();
	for (Card card1 : mergedList) {
            for (Card card2 : mergedList) {
                if (card1.getSuit().equals(card2.getSuit())) {
                    if (!flushList.contains(card1)) {
                        flushList.add(card1);
                    }
                    if (!flushList.contains(card2)) {
                        flushList.add(card2);
                    }
                }
            }
            if (flushList.size() == 5) {
                return flushList;
            }
            flushList.clear();
        }
        return null;
    }

    public static List<Card> getStraight(Player player, List<Card> tableCards) {
        return getSequence(player, tableCards, 5, false);
    }

    public static List<Card> getThreeOfAKind(Player player, List<Card> communityCards) {
		List<Card> mergedList = getMergedCardList(player, communityCards);
		return checkPair(mergedList, 3);
    }

    public static List<Card> getTwoPair(Player player, List<Card> communityCards) {
        List<Card> mergedList = getMergedCardList(player, communityCards);
	List<Card> twoPair1 = checkPair(mergedList, 2);
	if (twoPair1 != null) {
            mergedList.removeAll(twoPair1);
            List<Card> twoPair2 = checkPair(mergedList, 2);
            if (twoPair2 != null) {
                twoPair1.addAll(twoPair2);
                return twoPair1;
            }
        }
	return null;
    }

    public static List<Card> getOnePair(Player player, List<Card> communityCards) {
        List<Card> mergedList = getMergedCardList(player, communityCards);
	return checkPair(mergedList, 2);
    }

    private static List<Card> checkPair(List<Card> mergedList, Integer pairSize) {
	List<Card> checkedPair = new ArrayList<Card>();
	for (Card card1 : mergedList) {
            checkedPair.add(card1);
            for (Card card2 : mergedList) {
                if (!card1.equals(card2) && card1.getValue().equals(card2.getValue())) {
                    checkedPair.add(card2);
                }
            }
            if (checkedPair.size() == pairSize) {
                return checkedPair;
            }
            checkedPair.clear();
        }
        return null;
    }

//    public static ArrayList<Card> getPair(Player player, ArrayList<Card> communityCards) {
//        ArrayList<Card> allCards = new ArrayList<Card>();
//        if(communityCards != null)allCards.addAll(communityCards);
//        for(int i=0;i<allCards.size();i++) {
//            if(i+1 > allCards.size()-1) {
//                if(allCards.get(i).getIntValue() == allCards.get(i+1).getIntValue()) {
//                    return
//                }
//            }
//        }
//    }
    //Checks for straight and straight flush if compareSuit == true
    private static List<Card> getSequence(Player player, List<Card> communityCards, Integer sequenceSize, Boolean compareSuit) {
	List<Card> orderedList = getOrderedCardList(player, communityCards);
	List<Card> sequenceList = new ArrayList<Card>();
	Card cardPrevious = null;
	for (Card card : orderedList) {
            if (cardPrevious != null) {
                if ((card.getIntValue() - cardPrevious.getIntValue()) == 1) {
                    if (!compareSuit || cardPrevious.getSuit().equals(card.getSuit())) {
			if (sequenceList.size() == 0) {
                            sequenceList.add(cardPrevious);
			}
			sequenceList.add(card);
                    }
		} else {
                    if (sequenceList.size() == sequenceSize) {
                        return sequenceList;
                    }
                    sequenceList.clear();
                }
            }
            cardPrevious = card;
	}
	return (sequenceList.size() == sequenceSize) ? sequenceList : null;
    }
    
    private static List<Card> getMergedCardList(Player player, List<Card> communityCards) {
	List<Card> merged = new ArrayList<Card>();
	merged.addAll(communityCards);
	merged.add(player.getCards()[0]);
	merged.add(player.getCards()[1]);
	return merged;
    }

    private static List<Card> getOrderedCardList(Player player, List<Card> communityCards) {
	List<Card> ordered = getMergedCardList(player, communityCards);
	Collections.sort(ordered);
	return ordered;
    }

    private static void setRankEnumAndList(Player player, RankEnum rankEnum, List<Card> rankList) {
        player.setRank(rankEnum);
        player.setRankList(rankList);
    }

    private static List<ValueEnum> toValueEnumList(Player player, List<Card> communityCards) {
	List<ValueEnum> valueEnumList = new ArrayList<ValueEnum>();
	for (Card card : communityCards) {
            valueEnumList.add(card.getValue());
	}
	valueEnumList.add(player.getCards()[0].getValue());
	valueEnumList.add(player.getCards()[1].getValue());
	return valueEnumList;
    }

    private static Boolean isSameSuit(Player player, List<Card> communityCards) {
	SuitEnum suit = player.getCards()[0].getSuit();
	if (!suit.equals(player.getCards()[1].getSuit())) {
            return false;
	}
        for (Card card : communityCards) {
            if (!card.getSuit().equals(suit)) {
		return false;
            }
	}
	return true;
    }


}//Class HandRanker
