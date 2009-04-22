package teamJA_ND.comm;


import teamJA_ND.Point;
import battlecode.common.Clock;

public class PathInfo2 extends SubMessageBody{
	private Point[] points;
	public static final int ID = SubMessageBody.PATH_ID;
	public static final PathInfo2 PARSER = new PathInfo2(null);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

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

    public void debug_tick() {
        clockTurnNum = Clock.getRoundNum();
        clockByteNum = Clock.getBytecodeNum();
    }

    public void debug_tock() {
        int turnFinal = Clock.getRoundNum();
        int bytesFinal = Clock.getBytecodeNum() - 1; //The -1 accounts for the cost of calling debug_tock().
        int delta = bytesFinal - clockByteNum + BYTES_PER_ROUND*(turnFinal - clockTurnNum);
        System.out.println(delta + " bytecodes used since calling debug_tick().");
    }
	public String toString() {
		 return "PathInfo2\n"+		"points	:" + java.util.Arrays.toString(points);
	}
}