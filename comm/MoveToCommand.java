package teamJA_ND.comm;




public class MoveToCommand extends SubMessageBody{
	private int x;
	private int y;
	private boolean absolute;
	public static final int ID = SubMessageBody.MOVE_TO_ID;
	public static final MoveToCommand PARSER = new MoveToCommand(0, 0, false);


	public MoveToCommand (int x, int y, boolean absolute) {
		this.x = x;
		this.y = y;
		this.absolute = absolute;
	}

	public int getLength() {
		return 5;
	}
	public int getID() { return ID; }

	public int[] toIntArray() {
		final int LENGTH = getLength();
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		array[2] = x;
		array[3] = y;
		array[4] = absolute ? 1 : 0;
		return array;
	}
	public MoveToCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int x = array[counter+0];
		int y = array[counter +1];
		boolean absolute = (array[counter +2] == 1);
		return new MoveToCommand(x, y, absolute);
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean getAbsolute() {
		return absolute;
	}
}