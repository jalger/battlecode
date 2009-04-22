package teamJA_ND.comm;


import battlecode.common.Clock;

public class ShoveCommand extends SubMessageBody{
	private int robotID;
	public static final int ID = SubMessageBody.SHOVE_ID;
	public static final ShoveCommand PARSER = new ShoveCommand(0);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public ShoveCommand (int robotID) {
		this.robotID = robotID;
	}

	public int getLength() {
		return 3;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		array[offset +2] = robotID;
	}
	public ShoveCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int robotID = array[counter+0];
		return new ShoveCommand(robotID);
	}
	public int getRobotID() {
		return robotID;
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
		 return "ShoveCommand\n"+		"robotID	:" + robotID;
	}
}