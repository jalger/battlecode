package teamJA_ND.comm;


import battlecode.common.MapLocation;
import battlecode.common.Clock;

public class GiveEnergonCommand extends SubMessageBody{
	private int robotID;
	private int requestedAmount;
	private MapLocation location;
	public static final int ID = SubMessageBody.GIVE_ENERGON_ID;
	public static final GiveEnergonCommand PARSER = new GiveEnergonCommand(0, 0, null);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public GiveEnergonCommand (int robotID, int requestedAmount, MapLocation location) {
		this.robotID = robotID;
		this.requestedAmount = requestedAmount;
		this.location = location;
	}

	public int getLength() {
		return 6;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		array[offset +2] = robotID;
		array[offset +3] = requestedAmount;
		array[offset +4] = location.getX();
		array[offset +5] = location.getY();
	}
	public GiveEnergonCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int robotID = array[counter+0];
		int requestedAmount = array[counter +1];
		MapLocation location = new MapLocation(array[counter +2], array[counter +3]);
		return new GiveEnergonCommand(robotID, requestedAmount, location);
	}
	public int getRobotID() {
		return robotID;
	}
	public int getRequestedAmount() {
		return requestedAmount;
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
	public String toString() {
		 return "GiveEnergonCommand\n"+		"robotID	:" + robotID +
		"requestedAmount	:" + requestedAmount +
		"location	:" + location;
	}
}