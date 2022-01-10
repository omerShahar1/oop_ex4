package run;

import api.Edge;
import api.Location;

public class Pokemon
{
    private final double value; //points value of the pokemon
    private final int type;
    private final Location pos;
    private Edge edge;


    public Pokemon(double value, int type, Location pos)
    {
        this.value = value;
        this.type = type;
        this.pos = pos;
    }

    /**
     * This function return the value of the pockemon
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * This function return the type of the pockemon
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * This function get the position of the pockemon
     * @return the location
     */
    public Location getPos() {
        return pos;
    }

    /**
     * This function get the edge of the pockemon
     * @return the edge
     */
    public Edge getEdge() {
        return edge;
    }

    /**
     * This function set the edge of the pockemon
     */
    public void setEdge(Edge edge) {
        this.edge = edge;
    }

}
