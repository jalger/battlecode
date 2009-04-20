package teamJA_ND.comm;




public class EnemyTroopStrengthInfo extends SubMessageBody{
	public static final int ID = SubMessageBody.SubMessageBodyType.ENEMY_TROOP_STRENGTH.getID();
	public static final EnemyTroopStrengthInfo PARSER = new EnemyTroopStrengthInfo();


	public EnemyTroopStrengthInfo () {
	}

	public int getLength() {
		return +2;
	}
	public int getID() { return ID; }

	public int[] toIntArray() {
		final int LENGTH = getLength();
		int[] array = new int[LENGTH];
		array[0] = LENGTH;
		array[1] = ID;
		return array;
	}
	public EnemyTroopStrengthInfo fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		return new EnemyTroopStrengthInfo();
	}
}