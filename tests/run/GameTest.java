package run;

import api.Edge;
import api.Location;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class GameTest
{
    Game game = new Game(new Client());

    @Test
    void isStop_the_game()
    {
        assertFalse(game.isStop_the_game());
    }

    @Test
    void setStop_the_game()
    {
        assertFalse(game.isStop_the_game());
        game.setStop_the_game(true);
        assertTrue(game.isStop_the_game());
        game.setStop_the_game(false);
        assertFalse(game.isStop_the_game());
    }

    @Test
    void getAlgo()
    {
        assertNull(game.getAlgo());
    }

    @Test
    void getPokemons()
    {
        assertEquals(game.getPokemons().size(), 0);
    }

    @Test
    void getAgents()
    {
        assertEquals(game.getAgents().size(), 0);
    }

    @Test
    void setGraph() throws IOException
    {
        String file = "data/A0";
        String json = new String(Files.readAllBytes(Paths.get(file)));
        game.setGraph(json);
        assertNotEquals(game.getAlgo(), null);
        assertEquals(game.getAlgo().getGraph().nodeSize(),11);
    }

    @Test
    void getClient()
    {
        assertNotEquals(game.getAgents(), null);
    }

    @Test
    void findEdgeOfPokemon() throws IOException {
        String file = "data/A0";
        String json = new String(Files.readAllBytes(Paths.get(file)));
        game.setGraph(json);
        Pokemon pokemon1 = new Pokemon(3.0, 1, new Location(35.197656770719604,32.10191878639921,0.0));
        Pokemon pokemon2 = new Pokemon(3.0, -1, new Location(35.197656770719604,32.10191878639921,0.0));
        Edge edge1 = game.findEdgeOfPokemon(pokemon1.getPos(), pokemon1.getType());
        Edge edge2 = game.findEdgeOfPokemon(pokemon2.getPos(), pokemon2.getType());
        assertNotEquals(edge1.getSrc(), edge2.getSrc());
        assertNotEquals(edge1.getDest(), edge2.getDest());
    }
}