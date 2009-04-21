package teamJA_ND.comm;


import teamJA_ND.Point;


public class FringeInfo extends SubMessageBody{
	private Point[] points;
	private boolean[] groundTraversable;
	private int[] heights;
	public static final int ID = SubMessageBody.FRINGE_ID;
	public static final FringeInfo PARSER = new FringeInfo(null, null, null);


	public FringeInfo (Point[] points, boolean[] groundTraversable, int[] heights) {
		this.points = points;
		this.groundTraversable = groundTraversable;
		this.heights = heights;
	}

	public int getLength() {
		return  (2 * points.length)+ (1 * groundTraversable.length)+ (1 * heights.length)+5;
	}
	public int getID() { return ID; }

	public int[] toIntArray() {
		final int LENGTH = getLength();
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		array[2] = points.length;
		for (int i = 0; i < points.length; i++) {
			int startIndex =  (2 * i) +3;
			Point tmppoints = points[i];
			array[startIndex] = tmppoints.x;
			array[startIndex + 1] = tmppoints.y;
		}
		array[ (2 * points.length)+3] = groundTraversable.length;
		for (int i = 0; i < groundTraversable.length; i++) {
			int startIndex =  (2 * points.length)+ (1 * i) +4;
			boolean tmpgroundTraversable = groundTraversable[i];
			array[startIndex+0] = tmpgroundTraversable ? 1 : 0;
		}
		array[ (2 * points.length) + (1 * groundTraversable.length)+4] = heights.length;
		for (int i = 0; i < heights.length; i++) {
			int startIndex =  (2 * points.length) + (1 * groundTraversable.length)+ (1 * i) +5;
			int tmpheights = heights[i];
			array[startIndex+0] = tmpheights;
		}
		return array;
	}
	public FringeInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int pointssize = array[counter+0];
		Point[] points = new Point[pointssize];
		for (int i = 0; i < points.length; i++) {
			int startIndex = counter+ (2 * i) +1;
			Point tmppoints = new Point(array[startIndex], array[startIndex+1]);
			points[i] = tmppoints;
		}
		int groundTraversablesize = array[counter + (2 * points.length)+1];
		boolean[] groundTraversable = new boolean[groundTraversablesize];
		for (int i = 0; i < groundTraversable.length; i++) {
			int startIndex = counter + (2 * points.length)+ (1 * i) +2;
			boolean tmpgroundTraversable = (array[startIndex+0] == 1);
			groundTraversable[i] = tmpgroundTraversable;
		}
		int heightssize = array[counter + (2 * points.length) + (1 * groundTraversable.length)+2];
		int[] heights = new int[heightssize];
		for (int i = 0; i < heights.length; i++) {
			int startIndex = counter + (2 * points.length) + (1 * groundTraversable.length)+ (1 * i) +3;
			int tmpheights = array[startIndex+0];
			heights[i] = tmpheights;
		}
		return new FringeInfo(points, groundTraversable, heights);
	}
	public Point[] getPoints() {
		return points;
	}
	public boolean[] getGroundTraversable() {
		return groundTraversable;
	}
	public int[] getHeights() {
		return heights;
	}
}