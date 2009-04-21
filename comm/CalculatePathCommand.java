package teamJA_ND.comm;


import battlecode.common.Clock;

public class CalculatePathCommand extends SubMessageBody{
	public static final int ID = SubMessageBody.CALCULATE_PATH_ID;
	public static final CalculatePathCommand PARSER = new CalculatePathCommand();

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public CalculatePathCommand () {
	}

	public int getLength() {
		return +2;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
	}
	public CalculatePathCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new CalculatePathCommand();
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