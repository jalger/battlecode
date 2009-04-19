package teamJA_ND.comm;




public class JoinGroup extends SubMessageBody{
	private int robotID;
	private int groupID;
	public static final int ID = SubMessageBody.SubMessageBodyType.JOIN_GROUP.getID();
	public static final JoinGroup PARSER = new JoinGroup(0, 0);


	public JoinGroup (int robotID, int groupID) {
		this.robotID = robotID;
		this.groupID = groupID;
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
		array[2] = robotID;
		array[3] = groupID;
		return array;
	}
	public JoinGroup fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int robotID = array[counter+0];
		int groupID = array[counter +1];
		return new JoinGroup(robotID, groupID);
	}
	public int getRobotID() {
		return robotID;
	}
	public int getGroupID() {
		return groupID;
	}
}