package teamJA_ND.comm;


import battlecode.common.MapLocation;


public class GiveEnergonCommand extends SubMessageBody{
	private int robotID;
	private int requestedAmount;
	private MapLocation location;
	public static final int ID = SubMessageBody.SubMessageBodyType.GIVE_ENERGON.getID();
	public static final GiveEnergonCommand PARSER = new GiveEnergonCommand(0, 0, null);


	public GiveEnergonCommand (int robotID, int requestedAmount, MapLocation location) {
		this.robotID = robotID;
		this.requestedAmount = requestedAmount;
		this.location = location;
	}

	public int getLength() {
		return 6;
	}
	public int getID() { return ID; }

	public int[] toIntArray() {
		final int LENGTH = getLength();
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		array[2] = robotID;
		array[3] = requestedAmount;
		array[4] = location.getX();
		array[5] = location.getY();
		return array;
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
}