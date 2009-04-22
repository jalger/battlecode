package teamJA_ND.comm;


import battlecode.common.Clock;

public class SelfTroopStrengthInfo extends SubMessageBody{
	public static final int ID = SubMessageBody.SELF_TROOP_STRENGTH_ID;
	public static final SelfTroopStrengthInfo PARSER = new SelfTroopStrengthInfo();

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

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
		 return "SelfTroopStrengthInfo\n";
	}
}