package run;

import api.EdgeData;
import api.GeoLocation;

public class Pokemon
{
    private double value; //points value of the pokemon
    private int type;
    private GeoLocation pos;
    private EdgeData edge;


    public Pokemon(double value, int type, GeoLocation pos)
    {
        this.value = value;
        this.type = type;
        this.pos = pos;
    }

    /**
     * This function return the value of the pockemon
     * @return
     */
    public double getValue() {
        return value;
    }

    /**
     * This function return the type of the pockemon
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     * This function get the position of the pockemon
     * @return
     */
    public GeoLocation getPos() {
        return pos;
    }

    /**
     * This function get the edge of the pockemon
     * @return
     */
    public EdgeData getEdge() {
        return edge;
    }

    /**
     * This function set the edge of the pockemon
     * @return
     */
    public void setEdge(EdgeData edge) {
        this.edge = edge;
    }

}
