package run;

import api.*;
import java.util.LinkedList;

public class Agent
{
    public int id;
    public double value;
    public int src;
    public int dest;
    public double speed;
    public Pokemon target;
    public LinkedList<NodeData> path;
    public boolean flag_stop_move;


    public Agent(int id, int src, int dest, double speed)
    {
        this.id = id;
        this.value = 0;
        this.src = src;
        this.dest = dest;
        this.speed = speed;
        target = null;
        path = null;
        flag_stop_move = false;
    }

    public void update()
    {
        if(target!=null && this.src == target.edge.getSrc())
        {
            this.value += target.value;
            this.src = this.dest;
            this.dest = -1;
            this.target = null;
            this.path = null;
        }
        else
        {
            path.removeFirst();
            this.src = path.getFirst().getKey();
            if(path.size() == 1)
                this.dest = target.edge.getDest();
            else
                this.dest = path.get(1).getKey();
        }
    }
}

