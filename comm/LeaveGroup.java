package teamJA_ND.comm;




public class LeaveGroup extends SubMessageBody{
	private int id;
	public static final int ID = SubMessageBody.SubMessageBodyType.LEAVE_GROUP.getID();
	public static final LeaveGroup PARSER = new LeaveGroup(0);


	public LeaveGroup (int id) {
		this.id = id;
	}

	public int getLength() {
		return 3;
	}
	public int getID() { return ID; }

	public int[] toIntArray() {
		final int LENGTH = getLength();
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		array[2] = id;
		return array;
	}
	public LeaveGroup fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int id = array[counter+0];
		return new LeaveGroup(id);
	}
	public int getId() {
		return id;
	}
}