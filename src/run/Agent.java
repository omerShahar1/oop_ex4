package run;

import api.*;

public class Agent {
    private final int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private Location pos;
    private double timeToNext;

    public Agent(int id, int src, int dest, double speed, Location pos) {
        this.id = id;
        this.value = 0;
        this.src = src;
        this.dest = dest;
        this.speed = speed;
        this.pos = pos;
        this.timeToNext = Double.MAX_VALUE;
    }

    /**
     * This function return the id of the agent
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * This function return the value of the agent
     * @return
     */
    public double getValue() {
        return value;
    }

    /**
     * This function get the src of the agent
     * @return
     */
    public int getSrc() {
        return src;
    }

    /**
     * This function set the src of the agent
     * @return
     */
    public void setSrc(int src) {
        this.src = src;
    }

    /**
     * This function get the dest of the agent
     * @return
     */
    public int getDest() {
        return dest;
    }

    /**
     * This function set the dest of the agent
     * @return
     */
    public void setDest(int dest) {
        this.dest = dest;
    }

    /**
     * This function get the speed of the agent
     * @return
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * This function get the position of the agent
     * @return
     */
    public Location getPos() {
        return pos;
    }

    /**
     * This function set the position of the agent
     * @return
     */
    public void setPos(Location pos) {
        this.pos = pos;
    }

    /**
     * This function get the timeToNext of the agent
     * @return
     */
    public double getTimeToNext() {
        return timeToNext;
    }

    /**
     * This function set the timeToNext of the agent
     * @return
     */
    public void setTimeToNext(double timeToNext) {
        this.timeToNext = timeToNext;
    }
}

