package teamJA_ND.comm;


import battlecode.common.MapLocation;


public class AttackCommand extends SubMessageBody{
	private MapLocation location;
	public static final int ID = SubMessageBody.SubMessageBodyType.ATTACK.getID();
	public static final AttackCommand PARSER = new AttackCommand(null);


	public AttackCommand (MapLocation location) {
		this.location = location;
	}

	public int getLength() {
		return 4;
	}
	public int getID() { return ID; }

	public int[] toIntArray() {
		final int LENGTH = 4;
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		array[2] = location.getX();
		array[3] = location.getY();
		return array;
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