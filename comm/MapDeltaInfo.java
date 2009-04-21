package teamJA_ND.comm;




public class MapDeltaInfo extends SubMessageBody{
	public static final int ID = SubMessageBody.MAP_DELTA_ID;
	public static final MapDeltaInfo PARSER = new MapDeltaInfo();


	public MapDeltaInfo () {
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
	public MapDeltaInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new MapDeltaInfo();
	}
}