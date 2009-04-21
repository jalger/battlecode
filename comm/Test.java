package teamJA_ND.comm;


import battlecode.common.MapLocation;
import teamJA_ND.Point;
import battlecode.common.Clock;

public class Test extends SubMessageBody{
	 Point[][] points;
	 int[][] ints;
	 MapLocation[][] mapLocs;
	 boolean[][] booleans;
	public static final int ID = SubMessageBody.FRINGE_ID;
	public static final Test PARSER = new Test(null, null, null, null);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

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

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		int pointsnumRows = points.length;
		int pointsnumCols = points[0].length;
		array[offset +2] = pointsnumRows;
		array[offset +3] = pointsnumCols;
		for (int i = 0; i < points.length; i++){
			for (int j = 0; j < points[0].length; j++){
				int startIndex = (i * pointsnumCols* 2) + (2 * j) + offset +4;
				array[startIndex+0] = points[i][j].x;
				array[startIndex+1] = points[i][j].y;
			}
		}
		int intsnumRows = ints.length;
		int intsnumCols = ints[0].length;
		array[offset + (2 * (points.length* points[0].length))+4] = intsnumRows;
		array[offset + (2 * (points.length* points[0].length))+5] = intsnumCols;
		for (int i = 0; i < ints.length; i++){
			for (int j = 0; j < ints[0].length; j++){
				int startIndex = (i * intsnumCols* 1) + (1 * j) + offset + (2 * (points.length* points[0].length)) +6;
				array[startIndex+0] = ints[i][j];
			}
		}
		int mapLocsnumRows = mapLocs.length;
		int mapLocsnumCols = mapLocs[0].length;
		array[offset + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length))+6] = mapLocsnumRows;
		array[offset + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length))+7] = mapLocsnumCols;
		for (int i = 0; i < mapLocs.length; i++){
			for (int j = 0; j < mapLocs[0].length; j++){
				int startIndex = (i * mapLocsnumCols* 2) + (2 * j) + offset + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) +8;
				array[startIndex+0] = mapLocs[i][j].getX();
				array[startIndex+1] = mapLocs[i][j].getY();
			}
		}
		int booleansnumRows = booleans.length;
		int booleansnumCols = booleans[0].length;
		array[offset + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) + (2 * (mapLocs.length* mapLocs[0].length))+8] = booleansnumRows;
		array[offset + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) + (2 * (mapLocs.length* mapLocs[0].length))+9] = booleansnumCols;
		for (int i = 0; i < booleans.length; i++){
			for (int j = 0; j < booleans[0].length; j++){
				int startIndex = (i * booleansnumCols* 1) + (1 * j) + offset + (2 * (points.length* points[0].length)) + (1 * (ints.length* ints[0].length)) + (2 * (mapLocs.length* mapLocs[0].length)) +10;
				array[startIndex+0] = booleans[i][j] ? 1 : 0;
			}
		}
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
    }