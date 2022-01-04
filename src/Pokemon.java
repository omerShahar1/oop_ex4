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

}
