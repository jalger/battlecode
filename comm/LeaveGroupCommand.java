package teamJA_ND.comm;


import battlecode.common.Clock;

public class LeaveGroupCommand extends SubMessageBody{
	private int id;
	public static final int ID = SubMessageBody.LEAVE_GROUP_ID;
	public static final LeaveGroupCommand PARSER = new LeaveGroupCommand(0);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public LeaveGroupCommand (int id) {
		this.id = id;
	}

	public int getLength() {
		return 3;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		array[offset +2] = id;
	}
	public LeaveGroupCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int id = array[counter+0];
		return new LeaveGroupCommand(id);
	}
	public int getId() {
		return id;
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
		 return "LeaveGroupCommand\n"+		"id	:" + id;
	}
}