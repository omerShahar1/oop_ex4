package util;

import api.GeoLocation;

/**
 * This class represents a simple world 2 frame conversion (both ways).
 * @author boaz.benmoshe
 *
 */

public class Range2Range
{
	private Range2D world, frame;
	
	public Range2Range(Range2D w, Range2D f)
	{
		world = new Range2D(w);
		frame = new Range2D(f);
	}
	public GeoLocation world2frame(GeoLocation p)
	{
		Point3D d = world.getPortion(p);
		Point3D ans = frame.fromPortion(d);
		return ans;
	}
	public GeoLocation frame2world(GeoLocation p)
	{
		Point3D d = frame.getPortion(p);
		Point3D ans = world.fromPortion(d);
		return ans;
	}
	public Range2D getWorld() {
		return world;
	}
	public Range2D getFrame() {
		return frame;
	}
}
