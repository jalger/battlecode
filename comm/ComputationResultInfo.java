package teamJA_ND.comm;




public class ComputationResultInfo extends SubMessageBody{
	public static final int ID = SubMessageBody.COMPUTATION_RESULT_ID;
	public static final ComputationResultInfo PARSER = new ComputationResultInfo();


	public ComputationResultInfo () {
	}

	public int getLength() {
		return +2;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
	}
	public ComputationResultInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new ComputationResultInfo();
	}
}