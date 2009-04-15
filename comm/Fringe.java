package teamJA_ND.comm;




public class Fringe extends SubMessageBody{
	private Point[] points;
	private boolean[] groundTraversable;
	private int[] heights;
	public static final int ID = SubMessageBody.SubMessageBodyType.FRINGE.getID();
	public static final Fringe PARSER = new Fringe(null, null, null);


	public Fringe (Point[] points, boolean[] groundTraversable, int[] heights) {
		this.points = points;
		this.groundTraversable = groundTraversable;
		this.heights = heights;
	}

	public int getLength() {
		return  (2 * points.length)+ (1 * groundTraversable.length)+ (1 * heights.length)+5;
	}
	public int getID() { return ID; }

	public int[] toIntArray() {
		final int LENGTH =  (2 * points.length)+ (1 * groundTraversable.length)+ (1 * heights.length)+5;
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		array[2] = points.length;
		for (int i = 0; i < points.length; i++) {
			Point tmppoints = points[i];
			array[ (2 * i) +3] = tmppoints.x;
			array[ (2 * i) +3 + 1] = tmppoints.y;
		}
		array[ (2 * points.length)+3] = groundTraversable.length;
		for (int i = 0; i < groundTraversable.length; i++) {
			boolean tmpgroundTraversable = groundTraversable[i];
			array[ (2 * points.length)+ (1 * i) +4] = tmpgroundTraversable ? 1 : 0;
		}
		array[ (2 * points.length) + (1 * groundTraversable.length)+4] = heights.length;
		for (int i = 0; i < heights.length; i++) {
			int tmpheights = heights[i];
			array[ (2 * points.length) + (1 * groundTraversable.length)+ (1 * i) +5] = tmpheights;
		}
		return array;
	}
	public Fringe fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int pointssize = array[counter+0];
		Point[] points = new Point[pointssize];
		for (int i = 0; i < points.length; i++) {
			Point tmppoints = new Point(array[counter+ (2 * i) +1], array[counter+ (2 * i) +2]);
			points[i] = tmppoints;
		}
		int groundTraversablesize = array[counter + (2 * points.length)+1];
		boolean[] groundTraversable = new boolean[groundTraversablesize];
		for (int i = 0; i < groundTraversable.length; i++) {
			boolean tmpgroundTraversable = (array[counter + (2 * points.length)+ (1 * i) +2] == 1);
			groundTraversable[i] = tmpgroundTraversable;
		}
		int heightssize = array[counter + (2 * points.length) + (1 * groundTraversable.length)+2];
		int[] heights = new int[heightssize];
		for (int i = 0; i < heights.length; i++) {
			int tmpheights = array[counter + (2 * points.length) + (1 * groundTraversable.length)+ (1 * i) +3];
			heights[i] = tmpheights;
		}
		return new Fringe(points, groundTraversable, heights);
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