package gameClient;

import api.EdgeData;
import gameClient.util.Point3D;

public class Pokemon {
    private EdgeData edges;
    private final double value;
    private final int type;
    private final Point3D position;
    private double distance;

    /**
     * a parametric constructor.
     *
     * @param p - a Point3D type.
     * @param t - (type - going up or down), int type.
     * @param v - (the value of this Pokemon), double type.
     * @param e - (the edge the Pokemon is on), edge_data type.
     */
    public Pokemon(Point3D p, int t, double v, EdgeData e) {
        type = t;
        value = v;
        position = p;
        setEdges(e);
    }

    /**
     * @return the edge that the Pokemon is standing on.
     */
    public EdgeData getEdges() {
        return edges;
    }

    /**
     * allows to change the edge in each update from the server.
     *
     * @param edges - an edge_data type.
     */
    public void setEdges(EdgeData edges) {
        this.edges = edges;
    }

    /**
     * @return a Point3D location of where the Pokemon is seen.
     */
    public Point3D getLocation() {
        return position;
    }

    /**
     * @return -1 if the Pokemon is doing down or 1 is its going up.
     */
    public int getType() {
        return type;
    }

    /**
     * @return return the value of this Pokemon.
     */
    public double getValue() {
        return value;
    }

    /**
     * @return the destination of the Pokemon move.
     */
    public int getDest() {
        return edges.getDest();
    }

    /**
     * allows setting the distance from a specific Agent to this Pokemon (in each update).
     *
     * @param dis - a double type.
     */
    public void setDistance(double dis) {
        distance = dis;
    }

    /**
     * @return the distance from a specific Agent to this Pokemon.
     */
    public Double getDistance() {
        return distance;
    }
}