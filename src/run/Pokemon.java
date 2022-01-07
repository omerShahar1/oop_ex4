package run;

import api.EdgeData;
import api.GeoLocation;

public class Pokemon
{
    private double value;
    private int type;
    private GeoLocation pos;
    private EdgeData edge;
    private boolean targeted;

    public Pokemon(double value, int type, GeoLocation pos)
    {
        this.value = value;
        this.type = type;
        this.pos = pos;
        targeted = false;
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

    public boolean isTargeted() {
        return targeted;
    }

    public void setTargeted(boolean targeted) {
        this.targeted = targeted;
    }

    public Boolean equals(Pokemon pokemon){
        return (this.value==pokemon.getValue() && this.pos.x() == pokemon.pos.x() && this.edge.getSrc() == pokemon.edge.getSrc() && this.type==pokemon.type);
    }

}
