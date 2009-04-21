package teamJA_ND.comm;


import battlecode.common.MapLocation;
import battlecode.common.Clock;

public class AttackCommand extends SubMessageBody{
	private MapLocation location;
	public static final int ID = SubMessageBody.ATTACK_ID;
	public static final AttackCommand PARSER = new AttackCommand(null);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public AttackCommand (MapLocation location) {
		this.location = location;
	}

	public int getLength() {
		return 4;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		array[offset +2] = location.getX();
		array[offset +3] = location.getY();
	}
	public AttackCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		MapLocation location = new MapLocation(array[counter+0], array[counter+1]);
		return new AttackCommand(location);
	}
	public MapLocation getLocation() {
		return location;
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