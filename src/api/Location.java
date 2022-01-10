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

    /**
     * This function measures the distance between the current location and a given location
     * @param location the location of another point in space
     * @return double
     */
    public double distance(Location location)
    {
        double answer = Math.pow(this.x - location.x(), 2); //(x1 - x2)^2
        answer += Math.pow(this.y - location.y(), 2); //(y1 - y2)^2
        answer += Math.pow(this.z - location.z(), 2); //(z1 - z2)^2
        answer = Math.sqrt(answer);
        return answer;
    }

    /**
     * This function return a string represent the location
     * @return string represent
     */
    public String toString()
    {
        return ("" + x + "," + y + "," + z);
    }
}
