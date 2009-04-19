package teamJA_ND.comm;




public class Shove extends SubMessageBody{
	private int robotID;
	public static final int ID = SubMessageBody.SubMessageBodyType.SHOVE.getID();
	public static final Shove PARSER = new Shove(0);


	public Shove (int robotID) {
		this.robotID = robotID;
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
		array[2] = robotID;
		return array;
	}
	public Shove fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int robotID = array[counter+0];
		return new Shove(robotID);
	}
	public int getRobotID() {
		return robotID;
	}
}