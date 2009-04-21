package teamJA_ND.comm;




public class BelayOrder extends SubMessageBody{
	private int orderID;
	public static final int ID = SubMessageBody.BELAY_ORDER_ID;
	public static final BelayOrder PARSER = new BelayOrder(0);


	public BelayOrder (int orderID) {
		this.orderID = orderID;
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
		array[2] = orderID;
		return array;
	}
	public BelayOrder fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int orderID = array[counter+0];
		return new BelayOrder(orderID);
	}
	public int getOrderID() {
		return orderID;
	}
}