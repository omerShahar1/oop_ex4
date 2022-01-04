package util;
/**
 * This class represents a simple 1D range of shape [min,max]
 * @author boaz_benmoshe
 *
 */
public class Range
{
	private double min, max;
	public Range(double min, double max)
	{
		set_min(min);
		set_max(max);
	}
	public Range(Range x) {
		this(x.min, x.max);
	}
	
	public boolean isIn(double d)
	{
		boolean inSide = false;
		if(d >= this.get_min() && d <= this.get_max())
			inSide=true;
		return inSide;
	}
	public String toString()
	{
		String ans = "["+this.get_min()+","+this.get_max()+"]";
		if(this.isEmpty()) {ans = "Empty Range";}
		return ans;
	}
	public boolean isEmpty() {
		return this.get_min()>this.get_max();
	}
	public double get_max() {
		return max;
	}
	public double get_length() {
		return max-min;
	}

	private void set_max(double _max) {
		this.max = _max;
	}
	public double get_min() {
		return min;
	}
	private void set_min(double _min) {
		this.min = _min;
	}
	public double getPortion(double d) {
		double d1 = d-min;
		double ans = d1/get_length();
		return ans;
	}
	public double fromPortion(double p) {
		return min+p* get_length();
	}
}
