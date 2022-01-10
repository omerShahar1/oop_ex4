package api;

/**
 * This class implements GeoLocation interface
 */
public class Location
{
    private final double x;
    private final double y;
    private final double z;

    public Location(double x, double y, double z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
    }

    /**
     * getters for x value
     * @return double
     */
    public double x() {
        return x;
    }

    /**
     * getters for y value
     * @return double
     */
    public double y() {
        return y;
    }

    /**
     * getters for z value
     * @return double
     */
    public double z() {
        return z;
    }
}
