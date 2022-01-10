package run;

import api.Edge;
import api.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTest
{
    Pokemon pokemon = new Pokemon(5.0, 1, new Location(2.0,2.0,2.0));

    @Test
    void getValue()
    {
        assertEquals(pokemon.getValue(),5.0);
    }

    @Test
    void getType()
    {
        assertEquals(pokemon.getType(), 1);
    }

    @Test
    void getPos()
    {
        assertEquals(pokemon.getPos().x(), 2.0);
        assertEquals(pokemon.getPos().y(), 2.0);
        assertEquals(pokemon.getPos().z(), 2.0);
    }

    @Test
    void getEdge()
    {
        assertNull(pokemon.getEdge());
        Edge edge = new Edge(1,2,3.0);
        pokemon.setEdge(edge);
        assertEquals(pokemon.getEdge(), edge);
    }

    @Test
    void setEdge()
    {
        Edge edge = new Edge(4,33,3.6);
        pokemon.setEdge(edge);
        assertEquals(pokemon.getEdge(), edge);
    }
}