package api;

public class Node implements NodeData
{
    private int id;
    private double weight;
    private GeoLocation location;


    public Node(int id, double x, double y, double z)
    {
        this.id = id;
        location = new Location(x, y, z);
        weight = 0;
    }

    @Override
    public int getKey() {
        return id;
    }

    @Override
    public GeoLocation getLocation() {
        return location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        location = new Location(p.x(), p.y(), p.z());
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight=weight;
    }
}
