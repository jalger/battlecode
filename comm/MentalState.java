package teamJA_ND.comm;


import battlecode.common.MapLocation;
import teamJA_ND.Point;


public class MentalState extends SubMessageBody{
	private int robotID;
	private Point points;
	private boolean testing;
	private MapLocation testing2;
	public static final int ID = SubMessageBody.SubMessageBodyType.MENTAL_STATE.getID();
	public static final int LENGTH = 2;
	public static final MentalState PARSER = new MentalState(0, null, false, null);


	public MentalState (int robotID, Point points, boolean testing, MapLocation testing2) {
		this.robotID = robotID;
		this.points = points;
		this.testing = testing;
		this.testing2 = testing2;
	}

	public int getLength() {
		return LENGTH;
	}
	public int getID() { return ID; }

	public int[] toIntArray() {
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		array[2] = robotID;
		array[3] = points.x;
		array[4] = points.y;
		array[5] = testing ? 1 : 0;
		array[6] = testing2.getX();
		array[7] = testing2.getY();
		return array;
	}
	public MentalState fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int robotID = array[counter];
		Point points = new Point(array[counter + 1], array[counter + 1 + 1]);
		boolean testing = (array[counter + 1 + 2] == 1);
		MapLocation testing2 = new MapLocation(array[counter + 1 + 2 + 1], array[counter + 1 + 2 + 1 + 1]);
		return new MentalState(robotID, points, testing, testing2);
	}
	public int getRobotID() {
		return robotID;
	}
	public Point getPoints() {
		return points;
	}
	public boolean getTesting() {
		return testing;
	}
	public MapLocation getTesting2() {
		return testing2;
	}
}