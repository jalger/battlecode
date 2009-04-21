package teamJA_ND.comm;


import teamJA_ND.Point;


public class PathInfo2 extends SubMessageBody{
	private Point[] points;
	public static final int ID = SubMessageBody.PATH_ID;
	public static final PathInfo2 PARSER = new PathInfo2(null);


	public PathInfo2 (Point[] points) {
		this.points = points;
	}

	public int getLength() {
		return  (2 * points.length)+3;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		array[offset +2] = points.length;
		for (int i = 0; i < points.length; i++) {
			int startIndex = offset + (2 * i) +3;
			Point tmppoints = points[i];
			array[startIndex] = tmppoints.x;
			array[startIndex + 1] = tmppoints.y;
		}
	}
	public PathInfo2 fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int pointssize = array[counter+0];
		Point[] points = new Point[pointssize];
		for (int i = 0; i < points.length; i++) {
			int startIndex = counter+ (2 * i) +1;
			Point tmppoints = new Point(array[startIndex], array[startIndex+1]);
			points[i] = tmppoints;
		}
		return new PathInfo2(points);
	}
	public Point[] getPoints() {
		return points;
	}
}