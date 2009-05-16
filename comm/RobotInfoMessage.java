package teamJA_ND.comm;


import battlecode.common.Clock;

public class RobotInfoMessage extends SubMessageBody{
    
    public int friendlyGroundRobots;
    public int enemyGroundRobots;
    public int friendlyAirRobots;
    public int enemyAirRobots;
    public int numRobots;
    public int[] currentEnergons;
    public int[] eventualEnergons;
    public int[] currentXPos;
    public int[] currentYPos;
    public int[] robotTypes;
    public int[] robotIDs;
    
	public static final int ID = SubMessageBody.ROBOT_INFO_MESSAGE_ID;
	public static final RobotInfoMessage PARSER = new RobotInfoMessage(0, 0, 0, 0, null, null, null, null, null, null);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public RobotInfoMessage (int friendlyGround, int enemyGround, int friendlyAir,
                             int enemyAir, int[] currEnergon, int[] eventualEner,
                             int[] currXPos, int[] currYPos, int[] robotTypesIn,
                             int[] robotIDsIn) {
		friendlyGroundRobots = friendlyGround;
        enemyGroundRobots = enemyGround;
        friendlyAirRobots = friendlyAir;
        enemyAirRobots = enemyAir;

        numRobots = friendlyGroundRobots + enemyGroundRobots + friendlyAirRobots + enemyAirRobots;

        currentEnergons = currEnergon;
        eventualEnergons = eventualEner;
        currentXPos = currXPos;
        currentYPos = currYPos;
        robotTypes = robotTypesIn;
        robotIDs = robotIDsIn;
	}

	public int getLength() {
		return  (6*numRobots)+ 7;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset++] = getLength();
		array[offset++] = ID;
        array[offset++] = friendlyGroundRobots;
        array[offset++] = enemyGroundRobots;
        array[offset++] = friendlyAirRobots;
        array[offset++] = enemyAirRobots;
		array[offset++] = numRobots;
        System.arraycopy(currentEnergons, 0, array, offset, numRobots);
        offset += numRobots;
        System.arraycopy(eventualEnergons, 0, array, offset, numRobots);
        offset += numRobots;
        System.arraycopy(currentXPos, 0, array, offset, numRobots);
        offset += numRobots;
        System.arraycopy(currentYPos, 0, array, offset, numRobots);
        offset += numRobots;
        System.arraycopy(robotTypes, 0, array, offset, numRobots);
        offset += numRobots;
        System.arraycopy(robotIDs, 0, array, offset, numRobots);
	}
    
	public RobotInfoMessage fromIntArray(int[] array, int offset) {
        offset += 2;
        int friendlyGroundRobots = array[offset++];
        int enemyGroundRobots = array[offset++];
        int friendlyAirRobots = array[offset++];
        int enemyAirRobots = array[offset++];
        int numRobots = array[offset++];

        int[] currentEnergons = new int[numRobots];
        int[] eventualEnergons = new int[numRobots];
        int[] currentXPos = new int[numRobots];
        int[] currentYPos = new int[numRobots];
        int[] robotTypes = new int[numRobots];
        int[] robotIDs = new int[numRobots];

        System.arraycopy(array, offset, currentEnergons, 0, numRobots);
        offset += numRobots;
        System.arraycopy(array, offset, eventualEnergons, 0, numRobots);
        offset += numRobots;
        System.arraycopy(array, offset, currentXPos, 0, numRobots);
        offset += numRobots;
        System.arraycopy(array, offset, currentYPos, 0, numRobots);
        offset += numRobots;
        System.arraycopy(array, offset, robotTypes, 0, numRobots);
        offset += numRobots;
        System.arraycopy(array, offset, robotIDs, 0, numRobots);
		return new RobotInfoMessage(friendlyGroundRobots, enemyGroundRobots,
                                    friendlyAirRobots, enemyAirRobots,
                                    currentEnergons, eventualEnergons,
                                    currentXPos, currentYPos,
                                    robotTypes, robotIDs);
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
		 return "Method not supported in this version of this class. -JJ";
	}
}