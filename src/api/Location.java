package api;

/**
 * This class implements GeoLocation interface
 */
public class Location implements GeoLocation
{
    private double x,y,z;
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
    @Override
    public double x() {
        return x;
    }

    /**
     * getters for y value
     * @return double
     */
    @Override
    public double y() {
        return y;
    }

    /**
     * getters for z value
     * @return double
     */
    @Override
    public double z() {
        return z;
    }

    /**
     * This function measures the distance between the current location and a given location
     * @param location
     * @return double
     */
    @Override
    public double distance(GeoLocation location)
    {
        double answer = Math.pow(this.x - location.x(), 2); //(x1 - x2)^2
        answer += Math.pow(this.y - location.y(), 2); //(y1 - y2)^2
        answer += Math.pow(this.z - location.z(), 2); //(z1 - z2)^2
        answer = Math.sqrt(answer);
        return answer;
    }

    /**
     * This function return a string represent the location
     * @return
     */
    @Override
    public String toString()
    {
        return ("" + x + "," + y + "," + z);
    }
}
