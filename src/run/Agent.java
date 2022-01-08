package run;

import api.*;

public class Agent {
    private final int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private GeoLocation pos;

    public Agent(int id, int src, int dest, double speed, GeoLocation pos) {
        this.id = id;
        this.value = 0;
        this.src = src;
        this.dest = dest;
        this.speed = speed;
        this.pos = pos;

    }

    public int getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed){ this.speed = speed;}

    public GeoLocation getPos() {
        return pos;
    }

    public void setPos(GeoLocation pos) {
        this.pos = pos;
    }
}

