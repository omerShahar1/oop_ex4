package api;

public class AdjListNode
{
    int vertex;
    double weight;

    public AdjListNode(int v, double w)
    {
        vertex = v;
        weight = w;
    }

    /**
     * This function get the vertex
     * @return
     */
    int getVertex() { return vertex; }

    /**
     * This function get the weight
     * @return
     */
    double getWeight() { return weight; }
}
