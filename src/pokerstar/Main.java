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
        boolean DEBUG = true;
        // TODO code application logic here
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("Selmer", 1000.0));
        players.add(new Player("Hanna", 1000.0));
        players.add(new Player("Svein", 1000.0));
        players.add(new Player("Kjell", 1000.0));
        players.add(new Player("Hilde", 1000.0));
       
        Game game = new Game(players, 10);
        //Start new round
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
        for(Player winner : winners) {
            System.out.println("Winner: "+winner.getName()+ " with "+winner.getRank());
        }
        //System.out.println("Selmers highCard: "+HandRanker.getHighCard(players.get(0), null));
    }

}
