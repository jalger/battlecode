package teamJA_ND.comm;




public class BelayOrderCommand extends SubMessageBody{
	private int id;
	public static final int ID = SubMessageBody.SubMessageBodyType.BELAY_ORDER.getID();
	public static final BelayOrderCommand PARSER = new BelayOrderCommand(0);


	public BelayOrderCommand (int id) {
		this.id = id;
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
		array[2] = id;
		return array;
	}
	public BelayOrderCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int id = array[counter+0];
		return new BelayOrderCommand(id);
	}
	public int getId() {
		return id;
	}
}