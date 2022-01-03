package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 *
 * @author boaz.benmoshe
 */
public class Arena {
    public static final double EPS1 = 0.001, EPS2 = EPS1 * EPS1;
    private static DirectedWeightedGraph graph;
    private List<Agent> agents;
    private List<Pokemon> pokemons;
    private static game_service game;
    static HashMap<Integer, LinkedList<Pokemon>> edges;
    static PriorityQueue<Integer> strongestEdge;
    static HashMap<Integer, HashMap<EdgeData, Integer>> stuckProblem;

    /**
     * an empty constructor.
     */
    public Arena() {
        stuckProblem = new HashMap<>();
    }

    /**
     * set the Pokemons this Arena.
     *
     * @param p a list of Pokemon.
     */
    public void setPokemons(List<Pokemon> p) {
        pokemons = p;
    }

    /**
     * set the Agents in this Arena.
     *
     * @param a a list of Agent.
     */
    public void setAgents(List<Agent> a) {
        agents = a;
    }

    /**
     * set the Graph-data in this Arena.
     *
     * @param g a directed_weighted_graph.
     */
    public void setGraph(DirectedWeightedGraph g) {
        graph = g;
    }

    /**
     * @return a list of Agents from this Arena.
     */
    public List<Agent> getAgents() {
        return agents;
    }

    /**
     * @return a list of Pokemons from this Arena.
     */
    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    /**
     * @return a Graph of this Arena.
     */
    public DirectedWeightedGraph getGraph() {
        return graph;
    }

    /**
     * set the server game at this Arena.
     *
     * @param g a game_service type.
     */
    public void setGame(game_service g) {
        game = g;
    }

    /**
     * @return game_service type.
     */
    public game_service getGame() {
        return game;
    }

    /**
     * This method return a collection of agents objects by taking them from JSON file
     * that represent the actual status of them.
     *
     * @param status string that representing status of all the agents
     * @param graph  the graph that the agents will insert
     * @return collection of all the agents objects
     */
    public static List<Agent> getAgents(String status, DirectedWeightedGraph graph) {
        ArrayList<Agent> agents_arraylist = new ArrayList<>();
        try {
            JSONObject agents_status = new JSONObject(status);
            JSONArray agents_JSONArray = agents_status.getJSONArray("Agents");
            for (int i = 0; i < agents_JSONArray.length(); i++) {
                Agent a = new Agent(graph, i);
                a.update(agents_JSONArray.get(i).toString());
                agents_arraylist.add(a);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return agents_arraylist;
    }

    /**
     * This method return a collection of pokemon's objects by taking them from a json file
     *
     * @param actual string that represent all the pokemon's
     * @return collection of all pokemon's objects
     */
    public static ArrayList<Pokemon> getPokemons(String actual) {
        ArrayList<Pokemon> pokemons_arraylist = new ArrayList<>();
        edges = new HashMap<>();
        strongestEdge = new PriorityQueue<>(new ComparatorValue());
        try {
            JSONObject pokemons_status = new JSONObject(actual);
            JSONArray actual_pokemons = pokemons_status.getJSONArray("Pokemons");
            for (NodeData node : graph.getV()) {
                edges.put(node.getKey(), new LinkedList<>());
            }
            for (int i = 0; i < actual_pokemons.length(); i++) {
                JSONObject pokemon = actual_pokemons.getJSONObject(i);
                JSONObject pk = pokemon.getJSONObject("Pokemon");
                int t = pk.getInt("type");
                double v = pk.getDouble("value");
                String p = pk.getString("pos");
                Pokemon f = new Pokemon(new Point3D(p), t, v, null);
                updateEdge(f, graph);
                pokemons_arraylist.add(f);
                edges.get(f.getEdges().getDest()).add(f);
            }

            for (Pokemon p : pokemons_arraylist) {
                strongestEdge.add(p.getEdges().getDest());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pokemons_arraylist;
    }

    /**
     * This method update the edges by status of the pokemon's
     *
     * @param pk    pokemon object.
     * @param graph directed_weighted_graph type.
     */
    public static void updateEdge(Pokemon pk, DirectedWeightedGraph graph) {
        for (NodeData runner : graph.getV()) {
            for (EdgeData edge : graph.getE(runner.getKey())) {
                boolean flag = isOnEdge(pk.getLocation(), edge, pk.getType(), graph);
                if (flag) {
                    pk.setEdges(edge);
                }
            }
        }
    }

    /**
     * This method checks whether pokemon object is on the edge
     *
     * @param p    the location of the pokemon by coordinate
     * @param e    the edge that the pokemon stays on
     * @param type the status of the location of the pokemon (if his moving up or down)
     * @param g    the graph
     * @return true if and only if this pokemon object are in the edge
     */
    private static boolean isOnEdge(GeoLocation p, EdgeData e, int type, DirectedWeightedGraph g) {
        int src = g.getNode(e.getSrc()).getKey();
        int dest = g.getNode(e.getDest()).getKey();
        if (type < 0 && dest > src) {
            return false;
        }
        if (type > 0 && src > dest) {
            return false;
        }
        GeoLocation srcLocation = g.getNode(src).getLocation();
        GeoLocation destLocation = g.getNode(dest).getLocation();
        boolean ans = false;
        double dist = srcLocation.distance(destLocation);
        double d1 = srcLocation.distance(p) + p.distance(destLocation);
        if (dist > d1 - EPS2) {
            ans = true;
        }
        return ans;
    }

    /**
     * @param g     directed_weighted_graph type.
     * @param frame Range2D type.
     * @return a coordinate system of this graph (Range2Range type).
     */
    public static Range2Range w2f(DirectedWeightedGraph g, Range2D frame) {
        return new Range2Range(GraphRange(g), frame);
    }

    /**
     * generate the coordinates of this graph range.
     *
     * @param g directed_weighted_graph type.
     * @return Range2D type.
     */
    private static Range2D GraphRange(DirectedWeightedGraph g) {
        double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
        boolean first = true;
        for (NodeData runner : g.getV()) {
            GeoLocation p = runner.getLocation();
            if (first) {
                x0 = p.x();
                x1 = x0;
                y0 = p.y();
                y1 = y0;
                first = false;
            } else {
                if (p.x() < x0) x0 = p.x();
                if (p.x() > x1) x1 = p.x();
                if (p.y() < y0) y0 = p.y();
                if (p.y() > y1) y1 = p.y();
            }
        }
        Range xr = new Range(x0, x1);
        Range yr = new Range(y0, y1);
        return new Range2D(xr, yr);
    }

    /**
     * calculate the value of pokemons that counting to this current destination on the same edge.
     *
     * @param key a key of a node.
     * @return the value of pokemon's values on this specific edge.
     */
    public static int valueOfEdge(int key) {
        int value = 0;
        for (Pokemon pokemon : edges.get(key)) {
            value += pokemon.getValue();
        }
        return value;
    }

    /**
     * @return a LinkedList with a priority of the most evaluated edge with most pokemon values.
     */
    public LinkedList<Integer> getBestValue() {
        return new LinkedList<>(strongestEdge);
    }

    /**
     * @param key a key of a node destination.
     * @return a LinkedList of Pokemon that are standing on this edge.
     */
    public LinkedList<Pokemon> getPokemonsOnEdge(int key) {
        return edges.getOrDefault(key, null);
    }

    /**
     * used especially for fixing the dt fps problem which is happening when the agents are on speed 5.
     * when it happens, the agents are faster than the current data positions, so they can't catch the pokemons and get stuck.
     * this method is used to fix this problem, it counts how many times an Agent is standing on the same edge in row.
     * if the Agent is standing more than 8 time (int statistically), the dt becomes 30 for a short time just to set it free.
     *
     * @param bond   an Agent.
     * @param target a Pokemon.
     * @return the number of this this Agent is standing on the same edge in a row.
     */
    public int computeStuckProblem(Agent bond, Pokemon target) {
        if (stuckProblem.containsKey(bond.getID()) && stuckProblem.get(bond.getID()).containsKey(target.getEdges())) {
            int last = stuckProblem.get(bond.getID()).get(target.getEdges());
            stuckProblem.get(bond.getID()).put(target.getEdges(), ++last);
            return last;
        } else {
            stuckProblem.put(bond.getID(), new HashMap<>());
            stuckProblem.get(bond.getID()).put(target.getEdges(), 1);
            return 1;
        }
    }

    /**
     * compares between edges, the value of each edge is evaluated by the number of pokemon and their values in each edge.
     */
    public static class ComparatorValue implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return Integer.compare(valueOfEdge(o2), valueOfEdge(o1));
        }
    }

}