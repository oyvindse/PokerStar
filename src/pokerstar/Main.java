package pokerstar;

import java.util.ArrayList;

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
        players.add(new Player("Selmer", 1000.0));
        players.add(new Player("Hanna", 1000.0));
        players.add(new Player("Svein", 1000.0));
        players.add(new Player("Kjell", 1000.0));
        players.add(new Player("Hilde", 1000.0));

        Game game = new Game(players, 8);
        game.initGame();
        game.placeBlinds();
        game.dealPreFlop();
        game.dealFlop();

        System.out.println("Selmers highCard: "+HandRanker.getHighCard(players.get(0), null));
    }

}
