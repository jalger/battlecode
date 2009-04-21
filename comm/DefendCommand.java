package teamJA_ND.comm;




public class DefendCommand extends SubMessageBody{
	public static final int ID = SubMessageBody.DEFEND_ID;
	public static final DefendCommand PARSER = new DefendCommand();


	public DefendCommand () {
	}

	public int getLength() {
		return +2;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
	}
	public DefendCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new DefendCommand();
	}
}