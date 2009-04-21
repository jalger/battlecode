package teamJA_ND.comm;




public class SelfTroopStrengthInfo extends SubMessageBody{
	public static final int ID = SubMessageBody.SELF_TROOP_STRENGTH_ID;
	public static final SelfTroopStrengthInfo PARSER = new SelfTroopStrengthInfo();


	public SelfTroopStrengthInfo () {
	}

	public int getLength() {
		return +2;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
	}
	public SelfTroopStrengthInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new SelfTroopStrengthInfo();
	}
}