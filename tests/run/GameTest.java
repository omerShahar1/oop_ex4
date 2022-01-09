package run;

import api.Algo;
import api.DirectedWeightedGraph;
import api.Graph;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Client client = new Client();
    Game game = new Game(client);


    @Test
    void isStop_the_game() {
        assertEquals(game.isStop_the_game(), false);
    }

    @Test
    void setStop_the_game() {
        game.setStop_the_game(true);
        assertEquals(game.isStop_the_game(), true);
    }

    @Test
    void getAlgo() {
        DirectedWeightedGraph g = new Graph("data/A0");
        Algo algo = null;
        algo.init(g);
        System.out.println("dfwvf");
//        assertEquals(game.getAlgo(), algo);
    }

    @Test
    void getPokemons() {
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        assertEquals(game.getPokemons(), pokemons);
    }

    @Test
    void getAgents() {
        HashMap<Integer, Agent> agents = new HashMap<>();
        assertEquals(game.getAgents(), agents);
    }



    @Test
    void getClient() {
        assertEquals(game.getClient(), client);
    }



    @Test
    void findEdgeOfPokemon() {
    }//fo

    @Test
    void chooseAgent() {
//        Agent agent =
    }//gg

    @Test
    void planNext() {
    }//hjbhb
}