package teamJA_ND.comm;


import battlecode.common.Clock;

public class EnemyTroopStrengthInfo extends SubMessageBody{
	public static final int ID = SubMessageBody.ENEMY_TROOP_STRENGTH_ID;
	public static final EnemyTroopStrengthInfo PARSER = new EnemyTroopStrengthInfo();

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public EnemyTroopStrengthInfo () {
	}

	public int getLength() {
		return +2;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
	}
	public EnemyTroopStrengthInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new EnemyTroopStrengthInfo();
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
    }