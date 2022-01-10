package run;

import api.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest
{
    Agent agent = new Agent(0, 0,-1,1.0,new Location(10.0,10.0,10.0));

    @Test
    void getId()
    {
        assertEquals(agent.getId(), 0);
    }

    @Test
    void getSrc()
    {
        assertEquals(agent.getSrc(), 0);
    }

    @Test
    void setSrc()
    {
        assertEquals(agent.getSrc(), 0);
        agent.setSrc(1);
        assertEquals(agent.getSrc(), 1);
        agent.setSrc(0);
        assertEquals(agent.getSrc(), 0);
    }

    @Test
    void getDest()
    {
        assertEquals(agent.getDest(), -1);
    }

    @Test
    void setDest()
    {
        assertEquals(agent.getDest(), -1);
        agent.setDest(1);
        assertEquals(agent.getDest(), 1);
        agent.setDest(-1);
        assertEquals(agent.getDest(), -1);
    }

    @Test
    void getSpeed()
    {
        assertEquals(agent.getSpeed(), 1.0);
    }

    @Test
    void getPos()
    {
        assertEquals(agent.getPos().x(), 10.0);
        assertEquals(agent.getPos().y(), 10.0);
        assertEquals(agent.getPos().z(), 10.0);
    }

    @Test
    void setPos()
    {
        assertEquals(agent.getPos().x(), 10.0);
        assertEquals(agent.getPos().y(), 10.0);
        assertEquals(agent.getPos().z(), 10.0);
        agent.setPos(new Location(1.0,1.0,1.0));
        assertEquals(agent.getPos().x(), 1.0);
        assertEquals(agent.getPos().y(), 1.0);
        assertEquals(agent.getPos().z(), 1.0);
    }
}