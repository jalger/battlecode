package teamJA_ND.comm;




public class CommandCompletedInfo extends SubMessageBody{
	private int commandingRobotID;
	private int commandID;
	public static final int ID = SubMessageBody.COMMAND_COMPLETED_ID;
	public static final CommandCompletedInfo PARSER = new CommandCompletedInfo(0, 0);


	public CommandCompletedInfo (int commandingRobotID, int commandID) {
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
	public CommandCompletedInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int commandingRobotID = array[counter+0];
		int commandID = array[counter +1];
		return new CommandCompletedInfo(commandingRobotID, commandID);
	}
	public int getCommandingRobotID() {
		return commandingRobotID;
	}
	public int getCommandID() {
		return commandID;
	}
}