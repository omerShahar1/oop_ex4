package api;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * This class implements DirectedWeightedGraph interface
 */
public class Graph
{
    private HashMap<Integer, Node> nodes; //the key is the node id.
    private HashMap<String, Edge> edges; // for example, if src=2 and dest=4 then the key is the string: "2,4"
    private int MC; //count changes in the graph.
    private HashMap<Integer, HashMap<Integer, Edge>> outEdges;
    private HashMap<Integer, HashMap<Integer, Edge>> inEdges;
    public HashMap<Integer, Integer> changes; //save amount of changes to a specific node outEdges


    public Graph(String jsonStr)
    {//constructor from json file string
        nodes = new HashMap<>();
        edges = new HashMap<>();
        outEdges = new HashMap<>();
        inEdges = new HashMap<>();
        changes = new HashMap<>();
        MC = 0;

        JSONObject j = new JSONObject(jsonStr);
        JSONArray jEdges = j.getJSONArray("Edges");
        JSONArray jNodes = j.getJSONArray("Nodes");
        for(int i = 0; i < jNodes.length(); i++)
        {
            String pos=jNodes.getJSONObject(i).getString("pos");
            int id=jNodes.getJSONObject(i).getInt("id");

            String[] loc_of_node = pos.split(",");
            double x = Double.parseDouble(loc_of_node[0]);
            double y = Double.parseDouble(loc_of_node[1]);
            double z = Double.parseDouble(loc_of_node[2]);
            Node node = new Node(id, x, y, z);
            addNode(node);
            MC = 0;
        }
        for(int i = 0; i < jEdges.length(); i++)
        {
            int src = jEdges.getJSONObject(i).getInt("src");
            double w = jEdges.getJSONObject(i).getDouble("w");
            int dest = jEdges.getJSONObject(i).getInt("dest");
            connect(src, dest, w);
            changes.put(dest, 0);
            MC = 0;
        }
    }


    /**
     * get the node by the node_id,
     * @param key - the node_id
     * @return Node, null if none
     */
    public Node getNode(int key) { return nodes.get(key); }

    /**
     * get the edge by two nodes represent source and destination
     * @param src - the id of the node the edge coming from
     * @param dest - the id of the node the edge go to
     * @return Edge
     */
    public Edge getEdge(int src, int dest) {
        String str = src + "," + dest; //every edge will be saved by string representing the edge src and dest with "," between them.
        return edges.get(str);
    }

    /**
     * add a node to the graph
     * @param n the new node
     */
    public void addNode(Node n)
    {// hash map complexity of put is o(1) so the toal complexity of adding new node is o(1).
        nodes.put(n.getKey() , new Node(n.getKey(), n.getLocation().x(), n.getLocation().y(), n.getLocation().z()));
        changes.put(n.getKey(), 0);
        MC++;
    }

    /**
     * connect two nodes -> adding an edge to the graph
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    public void connect(int src, int dest, double w)
    {//hash map complexity of put is o(1) so total complexity would be o(1).
        inEdges.put(dest, new HashMap<>());
        outEdges.put(src, new HashMap<>());
        Edge e = new Edge(src, dest, w);
        String str = src + "," + dest;
        edges.put(str, e);
        inEdges.get(dest).put(src, e);
        outEdges.get(src).put(dest, e);
        changes.put(src, changes.get(src)+1);
        MC++;
    }

    public Iterator<Node> nodeIter()
    {
        return new NodeIterator();
    }

    public Iterator<Edge> edgeIter()
    {
        return new EdgeIterator();
    }

    public Iterator<Edge> edgeIter(int node_id)
    {
        return new SpecificNodesIterator(node_id);
    }

    /**
     * remove the node with the given key (=id)
     * @param key the node id we want to remove
     * @return  erased Node
     */
    public Node removeNode(int key)
    {
        Iterator<Edge> edgesIter = edgeIter();
        while (edgesIter.hasNext()) //go over all the graph edges. o(E)
        {
            Edge e = edgesIter.next();
            if (e.getSrc() == key || e.getDest() == key) //check if the edge comes from or to the deleted node, and then remove it.
            {
                edgesIter.remove();
                MC++;
            }
        }
        changes.remove(key);
        MC++;
        return nodes.remove(key); //o(1)
    }

    /**
     * remove the edge with the given two id of nodes representing source and destination
     * @param src - the id of the node the edge coming from
     * @param dest - the id of the node the edge go to
     * @return erased Edge
     */
    public Edge removeEdge(int src, int dest)
    {
        outEdges.get(src).remove(dest);
        inEdges.get(dest).remove(src);
        String str = src + "," + dest;
        changes.put(src, changes.get(src)+1);
        MC++;
        return edges.remove(str);
    }

    /**
     * returns the number of vertices (nodes) in the graph
     * @return int
     */
    public int nodeSize() {
        return nodes.size();
    }

    /**
     * returns the number of edges in the graph
     * @return int
     */
    public int edgeSize() {
        return edges.size();
    }

    /**
     * returns the Mode Count - for testing changes in the graph
     * @return the mc
     */
    public int getMC() {
        return MC;
    }


    private class NodeIterator implements Iterator<Node>
    {
        int mc;
        private final Iterator<Node> iter;

        public NodeIterator()
        {
            iter = nodes.values().iterator();
            mc = getMC();
        }
        @Override
        public boolean hasNext()
        {
            if (mc == getMC())
                return iter.hasNext();
            else
                throw new RuntimeException();
        }
        @Override
        public Node next()
        {
            if (mc == getMC())
                return iter.next();
            else
                throw new RuntimeException();
        }
        @Override
        public void remove()
        {
            if (mc == getMC())
            {
                iter.remove();
                mc++;
            }
            else
                throw new RuntimeException();
        }
        @Override
        public void forEachRemaining(Consumer<? super Node> action)
        {
            iter.forEachRemaining(action);
        }
    }

    private class EdgeIterator implements Iterator<Edge>
    {
        int mc;
        private final Iterator<Edge> iter;

        public EdgeIterator()
        {
            iter = edges.values().iterator();
            mc = getMC();
        }
        @Override
        public boolean hasNext()
        {
            if (mc == getMC())
                return iter.hasNext();
            else
                throw new RuntimeException();
        }
        @Override
        public Edge next()
        {
            if (mc == getMC())
                return iter.next();
            else
                throw new RuntimeException();
        }
        @Override
        public void remove()
        {
            if (mc == getMC())
            {
                iter.remove();
                mc++;
            }
            else
                throw new RuntimeException();
        }

        @Override
        public void forEachRemaining(Consumer<? super Edge> action)
        {
            iter.forEachRemaining(action);
        }
    }

    private class SpecificNodesIterator implements Iterator<Edge>
    {
        int mc;
        int id;
        private final Iterator<Edge> iter;

        public SpecificNodesIterator(int node_id)
        {
            id = node_id;
            iter = outEdges.get(node_id).values().iterator();
            mc = changes.get(node_id);
        }
        @Override
        public boolean hasNext()
        {
            if (mc == changes.get(id))
                return iter.hasNext();
            else
                throw new RuntimeException();
        }
        @Override
        public Edge next()
        {
            if (mc == changes.get(id))
                return iter.next();
            else
                throw new RuntimeException();
        }
        @Override
        public void remove()
        {
            if (mc == changes.get(id))
            {
                iter.remove();
                mc++;
            }
            else
                throw new RuntimeException();
        }

        @Override
        public void forEachRemaining(Consumer<? super Edge> action)
        {
            iter.forEachRemaining(action);
        }
    }
}
