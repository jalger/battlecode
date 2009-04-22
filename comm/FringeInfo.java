package teamJA_ND.comm;


import battlecode.common.Clock;

public class FringeInfo extends SubMessageBody{
	private int directionIndex;
	private boolean[] groundTraversable;
	public static final int ID = SubMessageBody.FRINGE_ID;
	public static final FringeInfo PARSER = new FringeInfo(0, null);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public FringeInfo (int directionIndex, boolean[] groundTraversable) {
		this.directionIndex = directionIndex;
		this.groundTraversable = groundTraversable;
	}

	public int getLength() {
		return  (1 * groundTraversable.length)+4;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
        array[++offset] = ID;
        array[++offset] = directionIndex;
        array[++offset] = groundTraversable.length;
        for (int i = 0; i < groundTraversable.length; i++) {
            array[++offset] = groundTraversable[i] ? 1 : 0;
        }
    }
	
	public FringeInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int directionIndex = array[counter+0];
		int groundTraversablesize = array[counter +1];
		boolean[] groundTraversable = new boolean[groundTraversablesize];
		for (int i = 0; i < groundTraversable.length; i++) {
			int startIndex = counter + (1 * i) +2;
			boolean tmpgroundTraversable = (array[startIndex+0] == 1);
			groundTraversable[i] = tmpgroundTraversable;
		}
		return new FringeInfo(directionIndex, groundTraversable);
	}
	public int getDirectionIndex() {
		return directionIndex;
	}
	public boolean[] getGroundTraversable() {
		return groundTraversable;
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
		 return "FringeInfo\n"+		"directionIndex	:" + directionIndex +
		"groundTraversable	:" + java.util.Arrays.toString(groundTraversable);
	}
}