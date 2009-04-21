package teamJA_ND.comm;


import battlecode.common.MapLocation;
import teamJA_ND.Point;


public class Test extends SubMessageBody{
	 Point[][] points;
	 int[][] ints;
	 MapLocation[][] mapLocs;
	 boolean[][] booleans;
	public static final int ID = SubMessageBody.FRINGE_ID;
	public static final Test PARSER = new Test(null, null, null, null);


	public Test (Point[][] points, int[][] ints, MapLocation[][] mapLocs, boolean[][] booleans) {
		this.points = points;
		this.ints = ints;
		this.mapLocs = mapLocs;
		this.booleans = booleans;
	}

	public int getLength() {
		return  (2 * (points.length* points[0].length))+ (1 * (ints.length* ints[0].length))+ (2 * (mapLocs.length* mapLocs[0].length))+ (1 * (booleans.length* booleans[0].length))+10;
	}
	public int getID() { return ID; }

	public int[] toIntArray() {
		final int LENGTH = getLength();
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		int pointsnumRows = points.length;
		int pointsnumCols = points[0].length;
		array[2] = pointsnumRows;
		array[3] = pointsnumCols;
		for (int i = 0; i < points.length; i++){
			for (int j = 0; j < points[0].length; j++){
				int startIndex = (i * pointsnumCols* 2) + (2 * j) +4;
				array[startIndex+0] = points[i][j].x;
				array[startIndex+1] = points[i][j].y;
			}
		}
		int intsnumRows = ints.length;
		int intsnumCols = ints[0].length;
		array[ (2 * (points.length* points[0].length))+4] = intsnumRows;
		array[ (2 * (points.length* points[0].length))+5] = intsnumCols;
		for (int i = 0; i < ints.length; i++){
			for (int j = 0; j < ints[0].length; j++){
				int startIndex = (i * intsnumCols* 1) + (1 * j) + (2 * (points.length* points[0].length)) +6;
				array[startIndex+0] = ints[i][j];
			}
		}
		int mapLocsnumRows = mapLocs.length;
		int mapLocsnumCols = mapLocs[0].length;
		array[ (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length))+6] = mapLocsnumRows;
		array[ (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length))+7] = mapLocsnumCols;
		for (int i = 0; i < mapLocs.length; i++){
			for (int j = 0; j < mapLocs[0].length; j++){
				int startIndex = (i * mapLocsnumCols* 2) + (2 * j) + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) +8;
				array[startIndex+0] = mapLocs[i][j].getX();
				array[startIndex+1] = mapLocs[i][j].getY();
			}
		}
		int booleansnumRows = booleans.length;
		int booleansnumCols = booleans[0].length;
		array[ (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) + (2 * (mapLocs.length* mapLocs[0].length))+8] = booleansnumRows;
		array[ (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) + (2 * (mapLocs.length* mapLocs[0].length))+9] = booleansnumCols;
		for (int i = 0; i < booleans.length; i++){
			for (int j = 0; j < booleans[0].length; j++){
				int startIndex = (i * booleansnumCols* 1) + (1 * j) + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) + (2 * (mapLocs.length* mapLocs[0].length)) +10;
				array[startIndex+0] = booleans[i][j] ? 1 : 0;
			}
		}
		return array;
	}
	public Test fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int pointsnumRows = array[counter+0];
		int pointsnumCols = array[counter+1];
		Point[][] points = new Point[pointsnumRows][pointsnumCols];
		for (int i = 0; i < points.length; i++){
			for (int j = 0; j < points[0].length; j++){
				int startIndex = (i * pointsnumCols* 2) + (2 * j) + counter +2;
				Point tmppoints = new Point(array[startIndex], array[startIndex+1]);
				points[i][j] = tmppoints;
			}
		}
		int intsnumRows = array[counter + (2 * (points.length* points[0].length))+2];
		int intsnumCols = array[counter + (2 * (points.length* points[0].length))+3];
		int[][] ints = new int[intsnumRows][intsnumCols];
		for (int i = 0; i < ints.length; i++){
			for (int j = 0; j < ints[0].length; j++){
				int startIndex = (i * intsnumCols* 1) + (1 * j) + counter + (2 * (points.length* points[0].length)) +4;
				int tmpints = array[startIndex+0];
				ints[i][j] = tmpints;
			}
		}
		int mapLocsnumRows = array[counter + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length))+4];
		int mapLocsnumCols = array[counter + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length))+5];
		MapLocation[][] mapLocs = new MapLocation[mapLocsnumRows][mapLocsnumCols];
		for (int i = 0; i < mapLocs.length; i++){
			for (int j = 0; j < mapLocs[0].length; j++){
				int startIndex = (i * mapLocsnumCols* 2) + (2 * j) + counter + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) +6;
				MapLocation tmpmapLocs = new MapLocation(array[startIndex+0], array[startIndex+1]);
				mapLocs[i][j] = tmpmapLocs;
			}
		}
		int booleansnumRows = array[counter + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) + (2 * (mapLocs.length* mapLocs[0].length))+6];
		int booleansnumCols = array[counter + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) + (2 * (mapLocs.length* mapLocs[0].length))+7];
		boolean[][] booleans = new boolean[booleansnumRows][booleansnumCols];
		for (int i = 0; i < booleans.length; i++){
			for (int j = 0; j < booleans[0].length; j++){
				int startIndex = (i * booleansnumCols* 1) + (1 * j) + counter + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) + (2 * (mapLocs.length* mapLocs[0].length)) +8;
				boolean tmpbooleans = (array[startIndex+0] == 1);
				booleans[i][j] = tmpbooleans;
			}
		}
		return new Test(points, ints, mapLocs, booleans);
	}
	public Point[][] getPoints() {
		return points;
	}
	public int[][] getInts() {
		return ints;
	}
	public MapLocation[][] getMapLocs() {
		return mapLocs;
	}
	public boolean[][] getBooleans() {
		return booleans;
	}
}