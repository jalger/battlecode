package teamJA_ND.comm;




public class ComputationResultInfo extends SubMessageBody{
	public static final int ID = SubMessageBody.SubMessageBodyType.COMPUTATION_RESULT.getID();
	public static final ComputationResultInfo PARSER = new ComputationResultInfo();


	public ComputationResultInfo () {
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
	public ComputationResultInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new ComputationResultInfo();
	}
}