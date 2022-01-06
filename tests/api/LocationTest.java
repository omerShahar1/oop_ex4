package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    Location l = new Location(1,2,3);
    Location l2 = new Location(2,4,9);
    Location l3 = new Location(1,4,3);


    @Test
    void distance()
    {
        l.distance(l);
        assertEquals(l.distance(l2),6.4031242374328485);
        assertEquals(l.distance(l3),2);
    }
}