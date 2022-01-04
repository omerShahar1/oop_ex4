package api;

public class Edge implements EdgeData
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
    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

}
