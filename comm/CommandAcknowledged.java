package teamJA_ND.comm;




public class CommandAcknowledged extends SubMessageBody{
	private int commandingRobotID;
	private int commandID;
	public static final int ID = SubMessageBody.SubMessageBodyType.COMMAND_ACKNOWLEDGED.getID();
	public static final CommandAcknowledged PARSER = new CommandAcknowledged(0, 0);


	public CommandAcknowledged (int commandingRobotID, int commandID) {
		this.commandingRobotID = commandingRobotID;
		this.commandID = commandID;
	}

	public int getLength() {
		return 4;
	}
	public int getID() { return ID; }

	public int[] toIntArray() {
		final int LENGTH = getLength();
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		array[2] = commandingRobotID;
		array[3] = commandID;
		return array;
	}
	public CommandAcknowledged fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int commandingRobotID = array[counter+0];
		int commandID = array[counter +1];
		return new CommandAcknowledged(commandingRobotID, commandID);
	}
	public int getCommandingRobotID() {
		return commandingRobotID;
	}
	public int getCommandID() {
		return commandID;
	}
}