package teamJA_ND.comm;


import battlecode.common.MapLocation;
import teamJA_ND.Point;


public class MentalStateInfo extends SubMessageBody{
	private int robotID;
	private Point points;
	private boolean testing;
	private MapLocation testing2;
	private int anotherInt;
	private int[] test;
	private MapLocation[] testing3;
	private Point[] testing4;
	private boolean[] boolArray;
	 int[] ints;
	public static final int ID = SubMessageBody.SubMessageBodyType.MENTAL_STATE.getID();
	public static final MentalStateInfo PARSER = new MentalStateInfo(0, null, false, null, 0, null, null, null, null, null);


	public MentalStateInfo (int robotID, Point points, boolean testing, MapLocation testing2, int anotherInt, int[] test, MapLocation[] testing3, Point[] testing4, boolean[] boolArray, int[] ints) {
		this.robotID = robotID;
		this.points = points;
		this.testing = testing;
		this.testing2 = testing2;
		this.anotherInt = anotherInt;
		this.test = test;
		this.testing3 = testing3;
		this.testing4 = testing4;
		this.boolArray = boolArray;
		this.ints = ints;
	}

	public int getLength() {
		return  (1 * test.length)+ (2 * testing3.length)+ (2 * testing4.length)+ (1 * boolArray.length)+ (1 * ints.length)+14;
	}
	public int getID() { return ID; }

	public int[] toIntArray() {
		final int LENGTH = getLength();
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		array[2] = robotID;
		array[3] = points.x;
		array[4] = points.y;
		array[5] = testing ? 1 : 0;
		array[6] = testing2.getX();
		array[7] = testing2.getY();
		array[8] = anotherInt;
		array[9] = test.length;
		for (int i = 0; i < test.length; i++) {
			int startIndex =  (1 * i) +10;
			int tmptest = test[i];
			array[startIndex+0] = tmptest;
		}
		array[ (1 * test.length)+10] = testing3.length;
		for (int i = 0; i < testing3.length; i++) {
			int startIndex =  (1 * test.length)+ (2 * i) +11;
			MapLocation tmptesting3 = testing3[i];
			array[startIndex+0] = tmptesting3.getX();
			array[startIndex +1] = tmptesting3.getY();
		}
		array[ (1 * test.length) + (2 * testing3.length)+11] = testing4.length;
		for (int i = 0; i < testing4.length; i++) {
			int startIndex =  (1 * test.length) + (2 * testing3.length)+ (2 * i) +12;
			Point tmptesting4 = testing4[i];
			array[startIndex] = tmptesting4.x;
			array[startIndex + 1] = tmptesting4.y;
		}
		array[ (1 * test.length) + (2 * testing3.length) + (2 * testing4.length)+12] = boolArray.length;
		for (int i = 0; i < boolArray.length; i++) {
			int startIndex =  (1 * test.length) + (2 * testing3.length) + (2 * testing4.length)+ (1 * i) +13;
			boolean tmpboolArray = boolArray[i];
			array[startIndex+0] = tmpboolArray ? 1 : 0;
		}
		array[ (1 * test.length) + (2 * testing3.length) + (2 * testing4.length) + (1 * boolArray.length)+13] = ints.length;
		for (int i = 0; i < ints.length; i++) {
			int startIndex =  (1 * test.length) + (2 * testing3.length) + (2 * testing4.length) + (1 * boolArray.length)+ (1 * i) +14;
			int tmpints = ints[i];
			array[startIndex+0] = tmpints;
		}
		return array;
	}
	public MentalStateInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int robotID = array[counter+0];
		Point points = new Point(array[counter + 1], array[counter +2]);
		boolean testing = (array[counter +3] == 1);
		MapLocation testing2 = new MapLocation(array[counter +4], array[counter +5]);
		int anotherInt = array[counter +6];
		int testsize = array[counter +7];
		int[] test = new int[testsize];
		for (int i = 0; i < test.length; i++) {
			int startIndex = counter + (1 * i) +8;
			int tmptest = array[startIndex+0];
			test[i] = tmptest;
		}
		int testing3size = array[counter + (1 * test.length)+8];
		MapLocation[] testing3 = new MapLocation[testing3size];
		for (int i = 0; i < testing3.length; i++) {
			int startIndex = counter + (1 * test.length)+ (2 * i) +9;
			MapLocation tmptesting3 = new MapLocation(array[startIndex+0], array[startIndex+1]);
			testing3[i] = tmptesting3;
		}
		int testing4size = array[counter + (1 * test.length) + (2 * testing3.length)+9];
		Point[] testing4 = new Point[testing4size];
		for (int i = 0; i < testing4.length; i++) {
			int startIndex = counter + (1 * test.length) + (2 * testing3.length)+ (2 * i) +10;
			Point tmptesting4 = new Point(array[startIndex], array[startIndex+1]);
			testing4[i] = tmptesting4;
		}
		int boolArraysize = array[counter + (1 * test.length) + (2 * testing3.length) + (2 * testing4.length)+10];
		boolean[] boolArray = new boolean[boolArraysize];
		for (int i = 0; i < boolArray.length; i++) {
			int startIndex = counter + (1 * test.length) + (2 * testing3.length) + (2 * testing4.length)+ (1 * i) +11;
			boolean tmpboolArray = (array[startIndex+0] == 1);
			boolArray[i] = tmpboolArray;
		}
		int intssize = array[counter + (1 * test.length) + (2 * testing3.length) + (2 * testing4.length) + (1 * boolArray.length)+11];
		int[] ints = new int[intssize];
		for (int i = 0; i < ints.length; i++) {
			int startIndex = counter + (1 * test.length) + (2 * testing3.length) + (2 * testing4.length) + (1 * boolArray.length)+ (1 * i) +12;
			int tmpints = array[startIndex+0];
			ints[i] = tmpints;
		}
		return new MentalStateInfo(robotID, points, testing, testing2, anotherInt, test, testing3, testing4, boolArray, ints);
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
	public int getAnotherInt() {
		return anotherInt;
	}
	public int[] getTest() {
		return test;
	}
	public MapLocation[] getTesting3() {
		return testing3;
	}
	public Point[] getTesting4() {
		return testing4;
	}
	public boolean[] getBoolArray() {
		return boolArray;
	}
	public int[] getInts() {
		return ints;
	}
}