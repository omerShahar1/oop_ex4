package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlgoTest
{
    private static DirectedWeightedGraphAlgorithms algo;

    @BeforeEach
    void init() throws IOException
    {
        String file = "data/A0";
        String json = new String(Files.readAllBytes(Paths.get(file)));
        algo = new Algo(json);
    }

    @Test
    void copy()
    {
        DirectedWeightedGraph test = algo.copy();
        assert (!test.equals(algo.getGraph()));
        Iterator<NodeData> nodeIter = algo.getGraph().nodeIter();
        while (nodeIter.hasNext())
        {
            int id = nodeIter.next().getKey();
            assert (!algo.getGraph().getNode(id).equals(test.getNode(id)));
        }
        Iterator<EdgeData> edgeIter = algo.getGraph().edgeIter();
        while (edgeIter.hasNext())
        {
            EdgeData e = edgeIter.next();
            assert (!algo.getGraph().getEdge(e.getSrc(), e.getDest()).equals(test.getEdge(e.getSrc(), e.getDest())));
        }
    }

    @Test
    void shortestPathDist()
    {
        double test1 = algo.shortestPathDist(0,5);
        algo.getGraph().connect(0,5,0.0004);
        double test2 = algo.shortestPathDist(0,5);
        assert (test1 > test2);
    }

    @Test
    void shortestPath()
    {
        List<NodeData> test1 = algo.shortestPath(0,5);
        algo.getGraph().connect(0,5,0.0004);
        List<NodeData> test2 = algo.shortestPath(0,5);
        assert (test1.size() > test2.size());
        assert (test1.contains(algo.getGraph().getNode(0)) && test1.contains(algo.getGraph().getNode(5)));
        assert (test2.contains(algo.getGraph().getNode(0)) && test2.contains(algo.getGraph().getNode(5)));
    }

}