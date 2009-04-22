package teamJA_ND.comm;


import battlecode.common.Clock;

public class BelayOrderCommand extends SubMessageBody{
	private int orderID;
	public static final int ID = SubMessageBody.BELAY_ORDER_ID;
	public static final BelayOrderCommand PARSER = new BelayOrderCommand(0);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

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

    public void debug_tick() {
        clockTurnNum = Clock.getRoundNum();
        clockByteNum = Clock.getBytecodeNum();
    }

    public void debug_tock() {
        int turnFinal = Clock.getRoundNum();
        int bytesFinal = Clock.getBytecodeNum() - 1; //The -1 accounts for the cost of calling debug_tock().
        int delta = bytesFinal - clockByteNum + BYTES_PER_ROUND*(turnFinal - clockTurnNum);
        System.out.println(delta + " bytecodes used since calling debug_tick().");
    }
	public String toString() {
		 return "BelayOrderCommand\n"+		"orderID	:" + orderID;
	}
}