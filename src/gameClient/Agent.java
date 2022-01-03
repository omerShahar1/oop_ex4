package gameClient;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class Agent {

    private int key;
    private GeoLocation position;
    private double speed;
    private EdgeData current_edge;
    private NodeData current_node;
    private final DirectedWeightedGraph graph;
    private double points;

    /**
     * a constructor.
     *
     * @param g          a directed_weighted_graph type.
     * @param start_node the first spot the agent to start with.
     */
    public Agent(DirectedWeightedGraph g, int start_node) {
        graph = g;
        setPoints(0);
        current_node = graph.getNode(start_node);
        position = current_node.getLocation();
        key = -1;
        setSpeed(0);
    }

    /**
     * updates the agent in every run-step.
     *
     * @param json a given JSON string from the server.
     */
    public void update(String json) {
        JSONObject line;
        try {
            line = new JSONObject(json);
            JSONObject ttt = line.getJSONObject("Agent");
            int id = ttt.getInt("id");
            if (id == this.getID() || this.getID() == -1) {
                if (this.getID() == -1) {
                    key = id;
                }
                double speed = ttt.getDouble("speed");
                String p = ttt.getString("pos");
                Point3D pp = new Point3D(p);
                int src = ttt.getInt("src");
                int dest = ttt.getInt("dest");
                double value = ttt.getDouble("value");
                this.position = pp;
                this.setCurrNode(src);
                this.setSpeed(speed);
                this.setNextNode(dest);
                this.setPoints(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the key node of where the agent is standing.
     */
    public int getSrcNode() {
        return current_node.getKey();
    }

    /**
     * a point setter.
     *
     * @param p is given from the server.
     */
    private void setPoints(double p) {
        points = p;
    }

    /**
     * a next node setter.
     *
     * @param dest is given from the server.
     */
    public void setNextNode(int dest) {
        int src = this.current_node.getKey();
        if(graph.getEdge(src, dest) != null) {
            current_edge = graph.getEdge(src, dest);
        }
    }

    /**
     * the current node setter, where the agent is now standing on.
     *
     * @param src is given from the server.
     */
    public void setCurrNode(int src) {
        this.current_node = graph.getNode(src);
    }

    /**
     * @return the agent unique ID.
     */
    public int getID() {
        return this.key;
    }

    /**
     * @return the agent's location.
     */
    public GeoLocation getLocation() {
        return position;
    }

    /**
     * @return the points of this agent.
     */
    public double getPoints() {
        return this.points;
    }

    /**
     * @return the next move key-node.
     */
    public int getNextNode() {
        return this.current_edge == null ? -1 : this.current_edge.getDest();
    }

    /**
     * @return the agent's speed.
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * a speed setter given from the server.
     *
     * @param v a double type.
     */
    public void setSpeed(double v) {
        this.speed = v;
    }

}
