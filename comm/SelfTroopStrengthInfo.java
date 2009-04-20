package teamJA_ND.comm;




public class SelfTroopStrengthInfo extends SubMessageBody{
	public static final int ID = SubMessageBody.SubMessageBodyType.SELF_TROOP_STRENGTH.getID();
	public static final SelfTroopStrengthInfo PARSER = new SelfTroopStrengthInfo();


	public SelfTroopStrengthInfo () {
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
	public SelfTroopStrengthInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new SelfTroopStrengthInfo();
	}
}