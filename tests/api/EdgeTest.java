package api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest
{
    Edge et = new Edge(1,2,3.0);
    @Test
    void getSrc()
    {
        assertEquals(et.getSrc(),1);
    }

    @Test
    void getDest()
    {
        assertEquals(et.getDest(),2);
    }

    @Test
    void getWeight()
    {
        assertEquals(et.getWeight() ,3);
    }
}