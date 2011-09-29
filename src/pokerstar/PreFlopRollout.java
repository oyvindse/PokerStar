package pokerstar;

import java.util.ArrayList;
/**
 *
 * @author Oyvind Selmer
 */
public class PreFlopRollout {
    private static ArrayList<EquivalentClass> equivListUnsuited = new ArrayList();
    private static ArrayList<EquivalentClass> equivListSuited = new ArrayList();
    private static ArrayList<EquivalentClass> equivListPairs = new ArrayList();
    private static ValueEnum[] values = ValueEnum.values();
    private static SuitEnum[] suits = SuitEnum.values();
    public static ArrayList<EquivalentClass> getEquivListPairs() {
        return equivListPairs;
    }

    public static void setEquivListPairs(ArrayList<EquivalentClass> equivListPairs) {
        PreFlopRollout.equivListPairs = equivListPairs;
    }

    public static ArrayList<EquivalentClass> getEquivListSuited() {
        return equivListSuited;
    }

    public static void setEquivListSuited(ArrayList<EquivalentClass> equivListSuited) {
        PreFlopRollout.equivListSuited = equivListSuited;
    }

    public static ArrayList<EquivalentClass> getEquivListUnsuited() {
        return equivListUnsuited;
    }

    public static void setEquivListUnsuited(ArrayList<EquivalentClass> equivListUnsuited) {
        PreFlopRollout.equivListUnsuited = equivListUnsuited;
    }

        public static void generateEquivLists() {
        //Create card deck
        ArrayList<Card> sortedList = new ArrayList();
        for (int i = 0; i < SuitEnum.values().length; i++) {
            for (int j = 0; j < ValueEnum.values().length; j++) {
                sortedList.add(new Card(suits[i],values[j]));
            }
        }
        //Equivalence class for !=suit && !=value pocket cards, saves only 1 hand from each class
        for (int i = 0; i < 13; i++) {
            for (int j = 13; j < 26; j++) {
                if (sortedList.get(i).getValue() != sortedList.get(j).getValue()) {
                    boolean exists = false;
                    for (int k = 0; k < equivListUnsuited.size(); k++) {
                        if (sortedList.get(j).getValue()==equivListUnsuited.get(k).getCard1().getValue()) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        equivListUnsuited.add(new EquivalentClass(sortedList.get(i), sortedList.get(j)));
                    }
                }
            }
        }
        System.out.println("Unsuited Generated");
        // Adds suited class
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                 if (sortedList.get(i).getValue() != sortedList.get(j).getValue()) {
                    boolean exists = false;
                    for (int k = 0; k < equivListSuited.size(); k++) {
                        if (sortedList.get(j).getValue() == equivListSuited.get(k).getCard1().getValue()) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        equivListSuited.add(new EquivalentClass(sortedList.get(i), sortedList.get(j)));
                    }
                }
            }
        }
        System.out.println("Suited Generated");
        //Add pairs
        for (int i = 0; i < 13; i++) {
            equivListPairs.add(new EquivalentClass(sortedList.get(i), sortedList.get(i + 13)));
        }
        System.out.println("Pairs generated");
    }

public static void main(String[] args) {

    DBManager db = new DBManager();
    //System.out.println("RATING" + db.getRating( "2", "2", "4"));

    ArrayList<Player> allPlayers = new ArrayList<Player>();
        allPlayers.add(new Player("Selmer", 3000.0));
        allPlayers.add(new Player("Hanna", 3000.0));
        allPlayers.add(new Player("Paal", 3000.0));
        allPlayers.add(new Player("Stian", 3000.0));
        allPlayers.add(new Player("Tommy", 3000.0));
        allPlayers.add(new Player("Roar", 3000.0));
        allPlayers.add(new Player("Martin", 3000.0));
        allPlayers.add(new Player("Mikael", 3000.0));
        allPlayers.add(new Player("Dag", 3000.0));
        allPlayers.add(new Player("Jostein", 3000.0));

        generateEquivLists();

        int playerCount =2;
        int rounds = 10000;

    //2-10 players.. Cancels iteration when playerCount reaches 11
    while(playerCount<11) {
        System.out.println("Playing with: "+playerCount+ " players");
        ArrayList<Player> players = new ArrayList<Player>(allPlayers.subList(0, playerCount));

        for (int i = 0; i < getEquivListUnsuited().size(); i++) {
            int counter =0;
            double winCounter =0;
            double tieCounter =0;

            while(counter<rounds){

            PreFlopRolloutGame pfl = new PreFlopRolloutGame(players);
            //pfl.getRandom52Deck();

              pfl.playRound(getEquivListUnsuited().get(i).getCard1(),getEquivListUnsuited().get(i).getCard2());
              ArrayList<Player> winners = new ArrayList();
              winners = pfl.getWinner();

                for (int j = 0; j < winners.size(); j++) {
                    if(winners.get(j).getName().equalsIgnoreCase("selmer") && winners.size()<2){
                        winCounter++;
                    }else if(winners.get(j).getName().equalsIgnoreCase("selmer") && winners.size()>1){
                        tieCounter++;
                    }
                }
            counter++;
            winners.clear();
            pfl = null;
            }

  System.out.println("\nTo DB: With "+playerCount+" players and the Unsuited cards "+getEquivListUnsuited().get(i) +"winner chanse is: "+ winCounter+ " of " +rounds+" and Tie chance is: "+ tieCounter  );
  db.addRates("unsuited", getEquivListUnsuited().get(i).getCard1().getValue().name(), getEquivListUnsuited().get(i).getCard2().getValue().name(),
                playerCount, winCounter,tieCounter,rounds-winCounter-tieCounter);
        }
         for (int i = 0; i < getEquivListSuited().size(); i++) {
            int counter =0;
            int winCounter =0;
            int tieCounter =0;

            while(counter<rounds){

            PreFlopRolloutGame pfl = new PreFlopRolloutGame(players);
             //pfl.getRandom52Deck();

              pfl.playRound(getEquivListSuited().get(i).getCard1(),getEquivListSuited().get(i).getCard2());

              ArrayList<Player> winners = new ArrayList();

              winners = pfl.getWinner();

                for (int j = 0; j < winners.size(); j++) {
                    if(winners.get(j).getName().equalsIgnoreCase("selmer")&& winners.size()<2){
                        winCounter++;
                    }else if(winners.get(j).getName().equalsIgnoreCase("selmer") && winners.size()>1){
                        tieCounter++;
                    }
                }

            counter++;
            winners.clear();
            pfl = null;

            }
  System.out.println("\nTo DB: With "+playerCount+" players and the Suited cards "+getEquivListSuited().get(i) +"winner chanse is: "+ winCounter+ " of " +rounds+" and Tie chance is: "+ tieCounter  );
db.addRates("S", getEquivListSuited().get(i).getCard1().getValue().name(), getEquivListSuited().get(i).getCard2().getValue().name(),
            playerCount,winCounter,tieCounter,rounds-winCounter-tieCounter);
        }
        for (int i = 0; i < getEquivListPairs().size(); i++) {
            int counter =0;
            int winCounter =0;
            int tieCounter =0;

            while(counter<rounds){

            PreFlopRolloutGame pfl = new PreFlopRolloutGame(players);
             //pfl.getRandom52Deck();
              pfl.playRound(getEquivListPairs().get(i).getCard1(),getEquivListPairs().get(i).getCard2());
              ArrayList<Player> winners = new ArrayList();
              winners = pfl.getWinner();

                for (int j = 0; j < winners.size(); j++) {
                    if(winners.get(j).getName().equalsIgnoreCase("selmer")&& winners.size()<2){
                        winCounter++;
                    }else if(winners.get(j).getName().equalsIgnoreCase("selmer") && winners.size()>1){
                        tieCounter++;
                    }
                }
            counter++;
            winners.clear();
            pfl = null;

            }
  System.out.println("\nTo DB: With "+playerCount+" players and the Paired cards "+getEquivListPairs().get(i) +"winner chanse is: "+ winCounter+ " of " +rounds+" and Tie chance is: "+ tieCounter  );
db.addRates("pair", getEquivListPairs().get(i).getCard1().getValue().name(), getEquivListPairs().get(i).getCard2().getValue().name(),
            playerCount,winCounter, tieCounter, rounds-winCounter-tieCounter);
        }
        playerCount++;
    }
    }
}
