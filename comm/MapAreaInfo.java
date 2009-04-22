package teamJA_ND.comm;


import battlecode.common.Clock;

public class MapAreaInfo extends SubMessageBody{
	private boolean[][] groundTraversable;
	private boolean[][] visited;
	private int left;
	private int top;
	private int right;
	private int bottom;
	public static final int ID = SubMessageBody.MAP_DELTA_ID;
	public static final MapAreaInfo PARSER = new MapAreaInfo(null, null, 0, 0, 0, 0);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public MapAreaInfo (boolean[][] groundTraversable, boolean[][] visited, int left, int top, int right, int bottom) {
		this.groundTraversable = groundTraversable;
		this.visited = visited;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public int getLength() {
		return  (1 * (groundTraversable.length* groundTraversable[0].length))+ (1 * (visited.length* visited[0].length))+10;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		int groundTraversablenumRows = groundTraversable.length;
		int groundTraversablenumCols = groundTraversable[0].length;
		array[offset +2] = groundTraversablenumRows;
		array[offset +3] = groundTraversablenumCols;
		for (int i = 0; i < groundTraversable.length; i++){
			for (int j = 0; j < groundTraversable[0].length; j++){
				int startIndex = (i * groundTraversablenumCols* 1) + (1 * j) + offset +4;
				array[startIndex+0] = groundTraversable[i][j] ? 1 : 0;
			}
		}
		int visitednumRows = visited.length;
		int visitednumCols = visited[0].length;
		array[offset + (1 * (groundTraversable.length* groundTraversable[0].length))+4] = visitednumRows;
		array[offset + (1 * (groundTraversable.length* groundTraversable[0].length))+5] = visitednumCols;
		for (int i = 0; i < visited.length; i++){
			for (int j = 0; j < visited[0].length; j++){
				int startIndex = (i * visitednumCols* 1) + (1 * j) + offset + (1 * (groundTraversable.length* groundTraversable[0].length)) +6;
				array[startIndex+0] = visited[i][j] ? 1 : 0;
			}
		}
		array[offset + (1 * (groundTraversable.length* groundTraversable[0].length)) + (1 * (visited.length* visited[0].length))+6] = left;
		array[offset + (1 * (groundTraversable.length* groundTraversable[0].length)) + (1 * (visited.length* visited[0].length)) +7] = top;
		array[offset + (1 * (groundTraversable.length* groundTraversable[0].length)) + (1 * (visited.length* visited[0].length)) +8] = right;
		array[offset + (1 * (groundTraversable.length* groundTraversable[0].length)) + (1 * (visited.length* visited[0].length)) +9] = bottom;
	}
	public MapAreaInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int groundTraversablenumRows = array[counter+0];
		int groundTraversablenumCols = array[counter+1];
		boolean[][] groundTraversable = new boolean[groundTraversablenumRows][groundTraversablenumCols];
		for (int i = 0; i < groundTraversable.length; i++){
			for (int j = 0; j < groundTraversable[0].length; j++){
				int startIndex = (i * groundTraversablenumCols* 1) + (1 * j) + counter +2;
				boolean tmpgroundTraversable = (array[startIndex+0] == 1);
				groundTraversable[i][j] = tmpgroundTraversable;
			}
		}
		int visitednumRows = array[counter + (1 * (groundTraversable.length* groundTraversable[0].length))+2];
		int visitednumCols = array[counter + (1 * (groundTraversable.length* groundTraversable[0].length))+3];
		boolean[][] visited = new boolean[visitednumRows][visitednumCols];
		for (int i = 0; i < visited.length; i++){
			for (int j = 0; j < visited[0].length; j++){
				int startIndex = (i * visitednumCols* 1) + (1 * j) + counter + (1 * (groundTraversable.length* groundTraversable[0].length)) +4;
				boolean tmpvisited = (array[startIndex+0] == 1);
				visited[i][j] = tmpvisited;
			}
		}
		int left = array[counter + (1 * (groundTraversable.length* groundTraversable[0].length)) + (1 * (visited.length* visited[0].length))+4];
		int top = array[counter + (1 * (groundTraversable.length* groundTraversable[0].length)) + (1 * (visited.length* visited[0].length)) +5];
		int right = array[counter + (1 * (groundTraversable.length* groundTraversable[0].length)) + (1 * (visited.length* visited[0].length)) +6];
		int bottom = array[counter + (1 * (groundTraversable.length* groundTraversable[0].length)) + (1 * (visited.length* visited[0].length)) +7];
		return new MapAreaInfo(groundTraversable, visited, left, top, right, bottom);
	}
	public boolean[][] getGroundTraversable() {
		return groundTraversable;
	}
	public boolean[][] getVisited() {
		return visited;
	}
	public int getLeft() {
		return left;
	}
	public int getTop() {
		return top;
	}
	public int getRight() {
		return right;
	}
	public int getBottom() {
		return bottom;
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
		 return "MapAreaInfo\n"+		"groundTraversable	:" + groundTraversable +
		"visited	:" + visited +
		"left	:" + left +
		"top	:" + top +
		"right	:" + right +
		"bottom	:" + bottom;
	}
}