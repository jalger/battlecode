package teamJA_ND.comm;


import battlecode.common.MapLocation;


public class AttackCommand extends SubMessageBody{
	private MapLocation location;
	public static final int ID = SubMessageBody.ATTACK_ID;
	public static final AttackCommand PARSER = new AttackCommand(null);


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
}