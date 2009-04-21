package teamJA_ND.comm;


import battlecode.common.Clock;

public class CommandAcknowledgedInfo extends SubMessageBody{
	private int commandingRobotID;
	private int commandID;
	public static final int ID = SubMessageBody.COMMAND_ACKNOWLEDGED_ID;
	public static final CommandAcknowledgedInfo PARSER = new CommandAcknowledgedInfo(0, 0);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public CommandAcknowledgedInfo (int commandingRobotID, int commandID) {
		this.commandingRobotID = commandingRobotID;
		this.commandID = commandID;
	}

	public int getLength() {
		return 4;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		array[offset +2] = commandingRobotID;
		array[offset +3] = commandID;
	}
	public CommandAcknowledgedInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int commandingRobotID = array[counter+0];
		int commandID = array[counter +1];
		return new CommandAcknowledgedInfo(commandingRobotID, commandID);
	}
	public int getCommandingRobotID() {
		return commandingRobotID;
	}
	public int getCommandID() {
		return commandID;
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