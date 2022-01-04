package run;

import api.GeoLocation;

public class Agent
{
    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private GeoLocation pos;


    public Agent(int id, int src, int dest, double speed, GeoLocation pos)
    {
        this.id = id;
        this.value = 0;
        this.src = src;
        this.dest = dest;
        this.speed = speed;
        this.pos = pos;
    }

    public int id(){ return this.id;}

    public double value(){ return this.value;}

    public int src(){ return this.src;}

    public int dest(){ return this.dest;}

    public double speed(){ return this.speed;}

    public GeoLocation pos(){ return this.pos;}

    public void setSrc(int newSrc){ this.src = newSrc;}

    public void setDest(int newDest){ this.dest = newDest;}

    public void setPos(GeoLocation newPos){this.pos = newPos;}

    public void setValue(int points){ this.value += points;}




}
