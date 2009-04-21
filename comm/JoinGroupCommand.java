package teamJA_ND.comm;


import battlecode.common.Clock;

public class JoinGroupCommand extends SubMessageBody{
	private int robotID;
	private int groupID;
	public static final int ID = SubMessageBody.JOIN_GROUP_ID;
	public static final JoinGroupCommand PARSER = new JoinGroupCommand(0, 0);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public JoinGroupCommand (int robotID, int groupID) {
		this.robotID = robotID;
		this.groupID = groupID;
	}

	public int getLength() {
		return 4;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		array[offset +2] = robotID;
		array[offset +3] = groupID;
	}
	public JoinGroupCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int robotID = array[counter+0];
		int groupID = array[counter +1];
		return new JoinGroupCommand(robotID, groupID);
	}
	public int getRobotID() {
		return robotID;
	}
	public int getGroupID() {
		return groupID;
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