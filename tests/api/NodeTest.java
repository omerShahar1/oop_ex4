package api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    Node n = new Node (1,1,1,1);

    @Test
    void getKey()
    {
        assertEquals(n.getKey(),1);
    }

    @Test
    void getLocation()
    {
        assertEquals(n.getLocation().x(),1);
        assertEquals(n.getLocation().y(),1);
        assertEquals(n.getLocation().z(),1);
    }

    @Test
    void setLocation()
    {
        Location p = new Location(2,1,1);
        n.setLocation(p);
        assertEquals(n.getLocation().x(),2);
        Location p2 = new Location(1,1,1);
        n.setLocation(p2);
        assertEquals(n.getLocation().x(),1);
    }

    @Test
    void getWeight()
    {
        assertEquals(n.getWeight(),0);
    }

    @Test
    void setWeight()
    {
        n.setWeight(2);
        assertEquals(n.getWeight(),2);

    }
}