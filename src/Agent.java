import api.DirectedWeightedGraph;
import api.GeoLocation;
import api.Graph;
import api.NodeData;

import java.util.Iterator;

public class Agent
{
    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private GeoLocation pos;
    private DirectedWeightedGraph graph;


    public Agent(Graph graph, int id, double speed, GeoLocation pos)
    {
        this.graph = graph;
        this.id = id;
        this.value = 0;
        this.src = findNode();
        this.dest = -1;
        this.speed = speed;
        this.pos = pos;
    }

    public int id(){ return this.id;}

    public double value(){ return this.value;}

    public int src(){ return this.src;}

    public int dest(){ return this.dest;}

    public double speed(){ return this.speed;}

    public GeoLocation pos(){ return this.pos;}

    public void setSrc(int newSrc){ this.src = newSrc;}

    public void setDest(int newDest){ this.dest = newDest;}

    public void setPos(GeoLocation newPos){this.pos = newPos;}

    public void setValue(int points){ this.value += points;}


    private int findNode()
    {
        NodeData temp = null;
        Iterator<NodeData> nodesIter = graph.nodeIter();
        while (nodesIter.hasNext())
        {
            temp = nodesIter.next();
            if(temp.getLocation().x() == pos.x() && temp.getLocation().y() == pos.y() && temp.getLocation().z() == pos.z())
                return temp.getKey();
        }

        return -1;
    }




}
