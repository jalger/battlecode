package teamJA_ND.comm;




public class BelayOrderCommand extends SubMessageBody{
	private int orderID;
	public static final int ID = SubMessageBody.BELAY_ORDER_ID;
	public static final BelayOrderCommand PARSER = new BelayOrderCommand(0);


	public BelayOrderCommand (int orderID) {
		this.orderID = orderID;
	}

	public int getLength() {
		return 3;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		array[offset +2] = orderID;
	}
	public BelayOrderCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int orderID = array[counter+0];
		return new BelayOrderCommand(orderID);
	}
	public int getOrderID() {
		return orderID;
	}
}