package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    Location l = new Location(1,2,3);


    @Test
    void x()
    {
        assertEquals(l.x(), 1);
    }

    @Test
    void y()
    {
        assertEquals(l.y(),2);
    }

    @Test
    void z()
    {
        assertEquals(l.z(),3);
    }
}