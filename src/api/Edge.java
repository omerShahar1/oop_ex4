package api;

/**
 * This class implements EdgeData interface
 */
public class Edge
{
    private final int src;
    private final int dest;
    private final double weight;

    public Edge(int src, int dest, double weight)
    {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    /**
     * get the id of the node the edge coming from
     * @return int
     */
    public int getSrc() {
        return src;
    }

    /**
     * get the id of the node the edge go to
     * @return int
     */
    public int getDest() {
        return dest;
    }

    /**
     * get the weight of the edge
     * @return double
     */
    public double getWeight() {
        return weight;
    }
}
