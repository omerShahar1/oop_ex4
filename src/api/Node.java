package api;

/**
 * This class implements NodeData interface
 */
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

    /**
     * get the key(=id) of the node
     * @return int
     */
    @Override
    public int getKey() {
        return id;
    }

    /**
     * get the location of the current node
     * @return GeoLocation
     */
    @Override
    public GeoLocation getLocation() {
        return location;
    }

    /**
     * set the location of the current node
     * @return
     */
    @Override
    public void setLocation(GeoLocation p) {
        location = new Location(p.x(), p.y(), p.z());
    }

    /**
     * get the weight of the current node
     * @return double
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * set the weight of the current node
     * @return
     */
    @Override
    public void setWeight(double weight) {
        this.weight=weight;
    }
}
