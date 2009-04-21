package teamJA_ND.comm;




public class CalculatePath extends SubMessageBody{
	private int x0;
	private int y0;
	private int x1;
	private int y1;
	public static final int ID = SubMessageBody.CALCULATE_PATH_ID;
	public static final CalculatePath PARSER = new CalculatePath(0, 0, 0, 0);


	public CalculatePath (int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	public int getLength() {
		return 6;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		array[offset +2] = x0;
		array[offset +3] = y0;
		array[offset +4] = x1;
		array[offset +5] = y1;
	}
	public CalculatePath fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int x0 = array[counter+0];
		int y0 = array[counter +1];
		int x1 = array[counter +2];
		int y1 = array[counter +3];
		return new CalculatePath(x0, y0, x1, y1);
	}
	public int getX0() {
		return x0;
	}
	public int getY0() {
		return y0;
	}
	public int getX1() {
		return x1;
	}
	public int getY1() {
		return y1;
	}
}