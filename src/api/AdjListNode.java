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
    int getVertex() { return vertex; }
    double getWeight() { return weight; }
}
