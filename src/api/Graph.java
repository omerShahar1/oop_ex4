package api;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;


public class Graph implements DirectedWeightedGraph
{
    private HashMap<Integer, NodeData> nodes; //the key is the node id.
    private HashMap<String, EdgeData> edges; // for example, if src=2 and dest=4 then the key is the string: "2,4"
    private int MC; //count changes in the graph.
    private HashMap<Integer, HashMap<Integer, EdgeData>> outEdges;
    private HashMap<Integer, HashMap<Integer, EdgeData>> inEdges;
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


    @Override
    public NodeData getNode(int key) { return nodes.get(key); }

    @Override
    public EdgeData getEdge(int src, int dest) {
        String str = src + "," + dest; //every edge will be saved by string representing the edge src and dest with "," between them.
        return edges.get(str);
    }

    @Override
    public void addNode(NodeData n)
    {// hash map complexity of put is o(1) so the toal complexity of adding new node is o(1).
        nodes.put(n.getKey() , new Node(n.getKey(), n.getLocation().x(), n.getLocation().y(), n.getLocation().z()));
        changes.put(n.getKey(), 0);
        MC++;
    }

    @Override
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

    @Override
    public Iterator<NodeData> nodeIter()
    {
        return new NodeIterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter()
    {
        return new EdgeIterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id)
    {
        return new SpecificNodesIterator(node_id);
    }

    @Override
    public NodeData removeNode(int key)
    {
        Iterator<EdgeData> edgesIter = edgeIter();
        while (edgesIter.hasNext()) //go over all the graph edges. o(E)
        {
            EdgeData e = edgesIter.next();
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

    @Override
    public EdgeData removeEdge(int src, int dest)
    {
        outEdges.get(src).remove(dest);
        inEdges.get(dest).remove(src);
        String str = src + "," + dest;
        changes.put(src, changes.get(src)+1);
        MC++;
        return edges.remove(str);
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edges.size();
    }

    @Override
    public int getMC() {
        return MC;
    }


    private class NodeIterator implements Iterator<NodeData>
    {
        int mc;
        private final Iterator<NodeData> iter;

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
        public NodeData next()
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
        public void forEachRemaining(Consumer<? super NodeData> action)
        {
            iter.forEachRemaining(action);
        }
    }

    private class EdgeIterator implements Iterator<EdgeData>
    {
        int mc;
        private final Iterator<EdgeData> iter;

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
        public EdgeData next()
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
        public void forEachRemaining(Consumer<? super EdgeData> action)
        {
            iter.forEachRemaining(action);
        }
    }

    private class SpecificNodesIterator implements Iterator<EdgeData>
    {
        int mc;
        int id;
        private final Iterator<EdgeData> iter;

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
        public EdgeData next()
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
        public void forEachRemaining(Consumer<? super EdgeData> action)
        {
            iter.forEachRemaining(action);
        }
    }
}
