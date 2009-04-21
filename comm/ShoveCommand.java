package teamJA_ND.comm;




public class ShoveCommand extends SubMessageBody{
	private int robotID;
	public static final int ID = SubMessageBody.SHOVE_ID;
	public static final ShoveCommand PARSER = new ShoveCommand(0);


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
}