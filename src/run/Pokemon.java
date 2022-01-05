package run;

import api.Edge;
import api.EdgeData;
import api.GeoLocation;

public class Pokemon
{
    private double value;
    private int type;
    private GeoLocation pos;
    private EdgeData edge;

    public Pokemon(double value, int type, GeoLocation pos)
    {
        this.value = value;
        this.type = type;
        this.pos = pos;
    }

    public GeoLocation getPos() {
        return pos;
    }

    public void setPos(GeoLocation pos) {
        this.pos = pos;
    }

    public EdgeData getEdge() {
        return edge;
    }

    public void setEdge(EdgeData edge) {
        this.edge = edge;
    }
}
