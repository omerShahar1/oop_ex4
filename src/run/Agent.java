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
    public GeoLocation pos;
    public Pokemon target;
    public LinkedList<NodeData> path;
    public boolean flag_stop_move;


    public Agent(int id, int src, int dest, double speed, GeoLocation pos)
    {
        this.id = id;
        this.value = 0;
        this.src = src;
        this.dest = dest;
        this.speed = speed;
        this.pos = pos;
        target = null;
        path = null;
        flag_stop_move = false;
    }
}

