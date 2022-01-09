package api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdjListNodeTest {

    int v = 5;
    double w  = 10.3;

    AdjListNode a = new AdjListNode(v, w);

//    int getVertex() { return vertex; }
//    double getWeight() { return weight; }
    @Test
    void getVertex() {
        assertEquals(a.getVertex(), v);
    }

    @Test
    void getWeight() {
        assertEquals(a.getWeight(), w);
    }
}