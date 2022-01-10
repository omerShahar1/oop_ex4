package api;

/**
 * This class implements EdgeData interface
 */
public class Edge
{
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    public Edge(int src, int dest, double weight)
    {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.info = "";
        this.tag = 0;
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

    /**
     * get the info of the edge
     * @return String
     */
    public String getInfo() {
        return info;
    }

    /**
     * set the info of the edge
     * @param s
     */
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * get the tag of the edge
     * @return int
     */
    public int getTag() {
        return tag;
    }

    /**
     * set the tag of the edge
     * @param t
     */
    public void setTag(int t) {
        this.tag = t;
    }

}
