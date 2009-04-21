package teamJA_ND.comm;




public class JoinGroupCommand extends SubMessageBody{
	private int robotID;
	private int groupID;
	public static final int ID = SubMessageBody.JOIN_GROUP_ID;
	public static final JoinGroupCommand PARSER = new JoinGroupCommand(0, 0);


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
}