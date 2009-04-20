package teamJA_ND.comm;




public class CalculatePathCommand extends SubMessageBody{
	public static final int ID = SubMessageBody.SubMessageBodyType.CALCULATE_PATH.getID();
	public static final CalculatePathCommand PARSER = new CalculatePathCommand();


	public CalculatePathCommand () {
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
	public CalculatePathCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new CalculatePathCommand();
	}
}