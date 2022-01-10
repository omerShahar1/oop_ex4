package api;

/**
 * This class implements NodeData interface
 */
public class Node
{
    private final int id;
    private double weight;
    private Location location;


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
    public int getKey() {
        return id;
    }

    /**
     * get the location of the current node
     * @return Location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * set the location of the current node
     */
    public void setLocation(Location p) {
        location = new Location(p.x(), p.y(), p.z());
    }

    /**
     * get the weight of the current node
     * @return double
     */
    public double getWeight() {
        return weight;
    }

    /**
     * set the weight of the current node
     */
    public void setWeight(double weight) {
        this.weight=weight;
    }
}
