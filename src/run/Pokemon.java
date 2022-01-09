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

    public double getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public GeoLocation getPos() {
        return pos;
    }

    public EdgeData getEdge() {
        return edge;
    }

    public void setEdge(EdgeData edge) {
        this.edge = edge;
    }

}
