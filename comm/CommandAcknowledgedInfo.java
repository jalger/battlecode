package teamJA_ND.comm;




public class CommandAcknowledgedInfo extends SubMessageBody{
	private int commandingRobotID;
	private int commandID;
	public static final int ID = SubMessageBody.COMMAND_ACKNOWLEDGED_ID;
	public static final CommandAcknowledgedInfo PARSER = new CommandAcknowledgedInfo(0, 0);


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
}