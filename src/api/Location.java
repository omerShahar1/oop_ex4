package api;

public class Location implements GeoLocation
{
    private double x,y,z;
    public Location(double x, double y, double z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
    }
    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(GeoLocation location)
    {
        double answer = Math.pow(this.x - location.x(), 2); //(x1 - x2)^2
        answer += Math.pow(this.y - location.y(), 2); //(y1 - y2)^2
        answer += Math.pow(this.z - location.z(), 2); //(z1 - z2)^2
        answer = Math.sqrt(answer);
        return answer;
    }

    @Override
    public String toString()
    {
        return ("" + x + "," + y + "," + z);
    }
}
