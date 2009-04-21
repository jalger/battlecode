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

	public int[] toIntArray() {
		final int LENGTH = getLength();
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		return array;
	}
	public DefendCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new DefendCommand();
	}
}