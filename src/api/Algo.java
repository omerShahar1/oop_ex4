package api;
import java.util.*;

public class Algo implements DirectedWeightedGraphAlgorithms
{
    private DirectedWeightedGraph graph;

    public Algo()
    {
        this.graph = null;
    }

    public Algo(String jsonString)
    {
        Graph g = new Graph(jsonString);
        init(g);
    }

    public Algo(DirectedWeightedGraph graph) { init(graph); }


    @Override
    public void init(DirectedWeightedGraph g) {graph = g;}

    @Override
    public DirectedWeightedGraph getGraph()
    {
        return graph;
    }

    @Override
    public DirectedWeightedGraph copy()
    {
        return new Graph(graph);
    }


    @Override
    public boolean isConnected() //worst case: o(V*(V+E))
    {
        HashMap<Integer, List<Integer>> adjList = new HashMap<>(); //sore for every node (key is node id) the list of adjust nodes.
        HashMap<Integer, Boolean> visited = new HashMap<>(); //store for every run how many nodes we visited
        HashMap<Integer, Boolean> falseVisit = new HashMap<>(); //used to zero the visited after every run.
        // (if one of them is false in the end of each run then the graph is not connected).
        Iterator<NodeData> nodeIter = graph.nodeIter();
        while (nodeIter.hasNext())
        {
            NodeData node = nodeIter.next();
            adjList.put(node.getKey(), new ArrayList<>());
            visited.put(node.getKey(), false);
            falseVisit.put(node.getKey(), false);
        }

        // add edges to the directed graph
        Iterator<EdgeData> iter = graph.edgeIter();
        while(iter.hasNext())
        {
            EdgeData e = iter.next();
            adjList.get(e.getSrc()).add(e.getDest());
        }

        nodeIter = graph.nodeIter();
        while (nodeIter.hasNext()) // do for every node
        {
            DFS(adjList, nodeIter.next().getKey(), visited);
            if (visited.containsValue(false))
                return false;
            visited.putAll(falseVisit);
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest)
    {// final complexity is: o(ElogV)
        HashMap<Integer, ArrayList<AdjListNode>> ew = new HashMap<>(); //sore for every node (key is node id) the list of adjust nodes
        //in the form of AdjlistNode object (look up in the static class for explanation).
        HashMap<Integer, Double> dist = new HashMap<>(); //store distance from the src to every other node.
        Iterator<NodeData> nodeIter = graph.nodeIter();
        while (nodeIter.hasNext())
        {
            NodeData node = nodeIter.next();
            dist.put(node.getKey(), Double.MAX_VALUE);
            ew.put(node.getKey(), new ArrayList<>());
        }
        dist.put(src, 0.0);

        Iterator<EdgeData> edgeIter = graph.edgeIter();
        while (edgeIter.hasNext())
        {
            EdgeData edge = edgeIter.next();
            ew.get(edge.getSrc()).add(new AdjListNode(edge.getDest(), edge.getWeight()));
        }

        PriorityQueue<AdjListNode> pq = new PriorityQueue<>(Comparator.comparingDouble(AdjListNode::getWeight));
        pq.add(new AdjListNode(src, 0.0));

        while (pq.size() > 0)
        {
            AdjListNode current = pq.poll();
            for (AdjListNode n : ew.get(current.getVertex()))
            {
                if (dist.get(current.vertex) + n.weight < dist.get(n.vertex))
                {
                    dist.put(n.vertex, n.weight + dist.get(current.vertex));
                    pq.add(new AdjListNode(n.getVertex(), dist.get(n.vertex)));
                }
            }
        }
        return dist.get(dest);
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest)
    {// final complexity is: o(ElogV)
        HashMap<Integer, ArrayList<AdjListNode>> ew = new HashMap<>();
        HashMap<Integer, Double> dist = new HashMap<>();
        Iterator<NodeData> nodeIter = graph.nodeIter();
        HashMap<Integer, Integer> preNode = new HashMap<>();
        while (nodeIter.hasNext())
        {
            NodeData node = nodeIter.next();
            dist.put(node.getKey(), Double.MAX_VALUE);
            ew.put(node.getKey(), new ArrayList<>());
        }
        dist.put(src, 0.0);

        Iterator<EdgeData> edgeIter = graph.edgeIter();
        while (edgeIter.hasNext())
        {
            EdgeData edge = edgeIter.next();
            ew.get(edge.getSrc()).add(new AdjListNode(edge.getDest(), edge.getWeight()));
        }

        PriorityQueue<AdjListNode> pq = new PriorityQueue<>(Comparator.comparingDouble(AdjListNode::getWeight));
        pq.add(new AdjListNode(src, 0.0));

        while (pq.size() > 0)
        {
            AdjListNode current = pq.poll();
            for (AdjListNode n : ew.get(current.getVertex()))
                if (dist.get(current.vertex) + n.weight < dist.get(n.vertex))
                {
                    dist.put(n.vertex, n.weight + dist.get(current.vertex));
                    pq.add(new AdjListNode(n.getVertex(), dist.get(n.vertex)));
                    preNode.put(n.vertex, current.vertex);
                    if (n.vertex == dest)
                        return checkPath(preNode, src, dest);
                }
        }
        return null;
    }

    @Override
    public NodeData center()
    {
        HashMap<Integer, ArrayList<AdjListNode>> ew = new HashMap<>(); //save all the edges weights

        Iterator<NodeData> nodeIter = graph.nodeIter();
        while (nodeIter.hasNext())
            ew.put(nodeIter.next().getKey(), new ArrayList<>());

        Iterator<EdgeData> edgeIter = graph.edgeIter();
        while (edgeIter.hasNext())
        {
            EdgeData edge = edgeIter.next();
            ew.get(edge.getSrc()).add(new AdjListNode(edge.getDest(), edge.getWeight()));
        }

        int id=0; //represent the center node id.
        double lowestWeight= Double.MAX_VALUE;
        Iterator<NodeData> nodesIter = graph.nodeIter();
        while (nodesIter.hasNext())
        { //go over all the nodes and find the lowest value.
            NodeData node = nodesIter.next();
            double newWeight = dijkstra(node.getKey(), ew);
            if(newWeight == -1)
                return null;
            if (lowestWeight > newWeight)
            {
                id = node.getKey();
                lowestWeight = newWeight;
            }
        }
        return graph.getNode(id);
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities)
    {
        HashMap<Integer, Boolean> visited = new HashMap<>();
        List<NodeData> path = new LinkedList<>();
        double weight = Double.MAX_VALUE;

        for (NodeData node: cities) //find the best path when the src is different in each loop run.
        {
            for (NodeData n: cities)
                visited.put(n.getKey(), false);

            int src = node.getKey(); //change the src every loop run
            visited.put(src, true); //we start in the src so we will mark it
            List<NodeData> newPath = new LinkedList<>();
            newPath.add(graph.getNode(src));
            NodeData newNode = null;
            while (visited.containsValue(false)) //for every src find the best path from it to the rest.
            {
                newNode = findNextNode(src, visited, cities);
                if (newNode == null) //in case we don't have a path return null.
                    return null;
                List<NodeData> tempList = shortestPath(src, newNode.getKey());
                tempList.remove(graph.getNode(src));
                newPath.addAll(tempList);
                visited.put(newNode.getKey(), true);
                src = newNode.getKey();
            }
            double newWeight = listWeight(newPath);
            if(newWeight < weight)
            {
                weight = newWeight;
                path = newPath;
            }
        }

        return path;
    }

    private List<NodeData> checkPath(HashMap<Integer, Integer> preNode, int src, int dest)
    {//the value of each cell in the hash map is the node id we need to reach before going to the node id represented
        // by its key (therefore we start from the dest id).
        LinkedList<NodeData> list = new LinkedList<>();
        while (dest != src)
        {
            list.addFirst(graph.getNode(dest));
            dest = preNode.get(dest);
        }
        list.addFirst(graph.getNode(src));
        return list;
    }

    private static void DFS(HashMap<Integer, List<Integer>> adjList, int v, HashMap<Integer, Boolean> visited)
    {//non-recursive DFS for a given src id. would finish when we don't have any nodes to go to. (complexity of o(E+V))
        Stack<Integer> stack = new Stack<>();
        stack.push(v);
        while (!stack.empty())
        {
            v = stack.pop();
            if (visited.get(v))
                continue;

            visited.put(v, true);
            for (Integer i : adjList.get(v))
                if (!visited.get(i))
                    stack.push(i);
        }
    }

    private double dijkstra(int src, HashMap<Integer, ArrayList<AdjListNode>> ew)
    {
        // the function is for the center algorithm. its build like the previous versions but here we
        // will return the highest result instead of result for a given destination value.

        HashMap<Integer, Double> dist = new HashMap<>();
        Iterator<NodeData> nodeIter = graph.nodeIter();
        while (nodeIter.hasNext())
        {
            NodeData node = nodeIter.next();
            dist.put(node.getKey(), Double.MAX_VALUE);
        }
        dist.put(src, 0.0);

        PriorityQueue<AdjListNode> pq = new PriorityQueue<>(Comparator.comparingDouble(AdjListNode::getWeight));
        pq.add(new AdjListNode(src, 0.0));

        while (pq.size() > 0)
        {
            AdjListNode current = pq.poll();
            for (AdjListNode n : ew.get(current.getVertex()))
            {
                if (dist.get(current.vertex) + n.weight < dist.get(n.vertex))
                {
                    dist.put(n.vertex, n.weight + dist.get(current.vertex));
                    pq.add(new AdjListNode(n.getVertex(), dist.get(n.vertex)));
                }
            }
        }
        double max = -1;
        for (Double weight: dist.values()) //check for the max distance value.
        {
            if(weight > max)
                max = weight;
            if(weight == Double.MAX_VALUE) //represent that the graph is not connected. so we will return -1 because
                // all nodes id are positive integers.
                return -1;
        }
        return max;
    }

    private NodeData findNextNode(int src, HashMap<Integer, Boolean> visited, List<NodeData> list)
    {
        double weight = Double.MAX_VALUE;
        int id = 0 ;

        for (NodeData node: list) //go over all the nodes and check for the one with the shortest path from src.
        {
            if (visited.get(node.getKey()))
                continue;
            double newWeight = shortestPathDist(src, node.getKey());

            if (newWeight == Double.MAX_VALUE) //in case we don't have a path return null.
                return null;

            if(newWeight < weight)
            {
                weight = newWeight;
                id = node.getKey();
            }
        }
        return graph.getNode(id);
    }

    private double listWeight(List<NodeData> list)
    { //accept list of connected nodes and return the total weight.
        double answer = 0;
        boolean firstTime = true;
        NodeData preNode = null;

        for(NodeData node : list)
        {
            if (firstTime)
            {
                firstTime = false;
                preNode = node;
                continue;
            }

            answer += graph.getEdge(preNode.getKey(), node.getKey()).getWeight();
            preNode = node;
        }
        return answer;
    }
}
