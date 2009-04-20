package teamJA_ND.comm;




public class CommandRefusedInfo extends SubMessageBody{
	private int commandingRobotID;
	private int commandID;
	public static final int ID = SubMessageBody.SubMessageBodyType.COMMAND_REFUSED.getID();
	public static final CommandRefusedInfo PARSER = new CommandRefusedInfo(0, 0);


	public CommandRefusedInfo (int commandingRobotID, int commandID) {
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
	public CommandRefusedInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int commandingRobotID = array[counter+0];
		int commandID = array[counter +1];
		return new CommandRefusedInfo(commandingRobotID, commandID);
	}
	public int getCommandingRobotID() {
		return commandingRobotID;
	}
	public int getCommandID() {
		return commandID;
	}
}