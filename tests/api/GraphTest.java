package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

class GraphTest
{
    private static DirectedWeightedGraph graph;

    @BeforeEach
    void init() throws IOException
    {
        String file = "data/A0";
        String json = new String(Files.readAllBytes(Paths.get(file)));
        graph = new Graph(json);
    }

    @Test
    void getNode()
    {
        Iterator<NodeData> nodeIter = graph.nodeIter();
        while (nodeIter.hasNext())
        {
            NodeData node = nodeIter.next();
            NodeData test = graph.getNode(node.getKey());
            assert (node.equals(test));
        }
    }

    @Test
    void getEdge()
    {
        Iterator<EdgeData> edgeIter = graph.edgeIter();
        while (edgeIter.hasNext())
        {
            EdgeData edge = edgeIter.next();
            EdgeData test = graph.getEdge(edge.getSrc(), edge.getDest());
            assert (edge.equals(test));
        }
    }

    @Test
    void addNode()
    {
        int mc = graph.getMC();
        NodeData test = new Node(500, 74, 65, 88);
        graph.addNode(test);
        NodeData node = new Node(500, 12.0, 1.0, 0.0);
        graph.addNode(node);
        assert (!graph.getNode(500).equals(test));
        assert (graph.getMC() == mc+2);
    }

    @Test
    void connect()
    {
        int mc = graph.getMC();
        graph.connect(0,2, 3.2);
        assert (graph.getEdge(0, 2) != null);
        assert (graph.getEdge(0, 2).getWeight() == 3.2);
        assert (graph.getMC() == mc+1);
    }

    @Test
    void removeNode()
    {
        int mc = graph.getMC();
        NodeData node = graph.getNode(0);
        int nodeSize = graph.nodeSize();

        graph.removeNode(node.getKey());
        assert (graph.nodeSize() == nodeSize-1);
        Iterator<EdgeData> edgeIter = graph.edgeIter();
        int count=0;
        while (edgeIter.hasNext())
        {
            EdgeData edge = edgeIter.next();
            if (edge.getSrc() == node.getKey() || edge.getDest() == node.getKey())
                count++;
        }
        assert (count == 0);
    }

    @Test
    void removeEdge()
    {
        int mc = graph.getMC();
        int edgeSize = graph.edgeSize();
        EdgeData edge = graph.removeEdge(0, 1);
        assert (graph.edgeSize() == edgeSize - 1);
        assert (graph.getMC() == mc+1);
    }
}