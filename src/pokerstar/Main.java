package pokerstar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oyvind Selmer
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("Selmer", 3000.0));
        players.add(new Player("Hanna", 3000.0));
        players.add(new Player("Paal", 3000.0));
        players.add(new Player("Stian", 3000.0));
        players.add(new Player("Tommy", 3000.0));
        players.add(new Player("Roar", 3000.0));
        players.add(new Player("Martin", 3000.0));
        players.add(new Player("Mikael", 3000.0));
        players.add(new Player("Dag", 3000.0));
        players.add(new Player("Jostein", 3000.0));

        boolean DEBUG = true;
        int playerCount = 10;
        Game game = new Game(new ArrayList<Player>(players.subList(0, playerCount)), 10);
        //Start new round
        int round = 0;
        while(round < 1) {
            game.initGame();
            game.placeBlinds();

            if(DEBUG)System.out.println("\n-Dealing preflop- ");
            //Start dealing
            game.dealPreFlop();
            game.doBetting();
            game.betsToPot();

            if(DEBUG)System.out.println("\n-Dealing flop- ");
            game.dealFlop();
            game.doBetting();
            game.betsToPot();

            if(DEBUG)System.out.println("\n-Dealing turn- ");
            game.dealTurn();
            game.doBetting();
            game.betsToPot();

            if(DEBUG)System.out.println("\n-Dealing river- ");
            game.dealRiver();
            game.doBetting();
            game.betsToPot();

            List<Player> winners = game.getWinner();
            if(DEBUG) {
                for(Player winner : winners) {
                    System.out.println("Winner: "+winner.getName()+ " with "+winner.getRank()+"\n");
                }
                for(Player player : players) {
                    System.out.println(player.getName()+" credits: "+player.getCredits());
                }
            }
            round++;
        }
        if(!DEBUG) {
            for(Player player : players) {
                System.out.println(player.getName()+" credits: "+player.getCredits());
            }
        }
    }

}
