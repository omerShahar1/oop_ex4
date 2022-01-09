package api;

/**
 * This interface represents the set of operations applicable on a 
 * node (vertex) in a (directional) weighted graph.
 * @author boaz.benmoshe
 */
public interface NodeData {
    /**
	 * Returns the key (id) associated with this node.
	 * @return
	 */
	public int getKey();
	/** Returns the location of this node, if none return null.
	 * @return
	 */
	public GeoLocation getLocation();
	/** Allows changing this node's location.
	 * @param p - new new location  (position) of this node.
	 */
	public void setLocation(GeoLocation p);
	/**
	 * Returns the weight associated with this node.
	 * @return
	 */
	public double getWeight();
	/**
	 * Allows changing this node's weight.
	 * @param w - the new weight
	 */
	public void setWeight(double w);
}
