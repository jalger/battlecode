package teamJA_ND.comm;




public class LeaveGroupCommand extends SubMessageBody{
	private int id;
	public static final int ID = SubMessageBody.LEAVE_GROUP_ID;
	public static final LeaveGroupCommand PARSER = new LeaveGroupCommand(0);


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
}