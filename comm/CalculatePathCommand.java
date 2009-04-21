package teamJA_ND.comm;




public class CalculatePathCommand extends SubMessageBody{
	public static final int ID = SubMessageBody.CALCULATE_PATH_ID;
	public static final CalculatePathCommand PARSER = new CalculatePathCommand();


	public CalculatePathCommand () {
	}

	public int getLength() {
		return +2;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
	}
	public CalculatePathCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new CalculatePathCommand();
	}
}