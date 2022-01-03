package api;

public class Node implements NodeData
{
    private int id;
    private double weight;
    private GeoLocation location;
    private String info;
    private int tag;


    public Node(int id, double x, double y, double z)
    {
        this.id = id;
        location = new Location(x, y, z);
        weight = 0;
        info = "";
        tag = 0;
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

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        this.info =s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        this.tag=t;
    }
}
