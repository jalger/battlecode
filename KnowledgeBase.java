package teamJA_ND;

/**
 * A knowledge base holds all the information a robot accumulates during
 * the course of the game.  This information includes but is not limited to
 * <ul>
 * <li> Map information
 * <li> Troop strength
 *      <ul>
 *          <li> Own
 *          <li> Enemy
 *      </ul>
 * <li> Task force id
 * </ul>
 * 
 * @author J.J. Alger, Nicholas Dunn
 * @date 4/9/09
 */
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.MapLocation;
import java.util.Vector;

import teamJA_ND.comm.*;

public class KnowledgeBase {

    protected Map myMap;
    protected Vector<MapLocation> towers;
    //And some other stuff

    // Own troop strength

    //Pseudo-vector implementation - stores a pointer to the last "valid" information, so
    //It doesn't need to know how many robots each turn and doesn't have to waste time garbage-collecting.
    //At the same time, it gets the speed of using a flat-out array, and by initially sizing them at
    //100 elements (more than should ever be found), it is never necessary to resize these arrays.
    public RobotInfo[] friendlyGroundRobots, friendlyAirRobots, enemyGroundRobots, enemyAirRobots;
    public int friendlyGroundRobotsSize, friendlyAirRobotsSize, enemyGroundRobotsSize, enemyAirRobotsSize;
    // Enemy troop strength
    // Task force id
    
    // TODO: These really should not be public.
    public int robotID;
    public int taskforceID;
    public int squadID;
    /**
    * Represents the type of the robot; mimics the enum already written but
    * in powers of 2 so we can do bit packing.
    **/
    public int robotType;
    protected KBRobotInfo otherRobotKnowledge;

    public KnowledgeBase() {
        friendlyGroundRobots = new RobotInfo[100];
        enemyGroundRobots = new RobotInfo[100];
        friendlyAirRobots = new RobotInfo[100];
        enemyAirRobots = new RobotInfo[100];
        friendlyGroundRobotsSize = 0;
        friendlyAirRobotsSize = 0;
        enemyGroundRobotsSize = 0;
        enemyAirRobotsSize = 0;
        otherRobotKnowledge = new KBRobotInfo();
        towers = new Vector<MapLocation>();
    }

    public KnowledgeBase(Map mapIn) {
        myMap = mapIn;
        friendlyGroundRobots = new RobotInfo[100];
        enemyGroundRobots = new RobotInfo[100];
        friendlyAirRobots = new RobotInfo[100];
        enemyAirRobots = new RobotInfo[100];
        friendlyGroundRobotsSize = 0;
        friendlyAirRobotsSize = 0;
        enemyGroundRobotsSize = 0;
        enemyAirRobotsSize = 0;
        otherRobotKnowledge = new KBRobotInfo();
        towers = new Vector<MapLocation>();
    }

    public void resetRobotKnowledge() {
        friendlyGroundRobotsSize = 0;
        friendlyAirRobotsSize = 0;
        enemyGroundRobotsSize = 0;
        enemyAirRobotsSize = 0;
    }

    public void addFriendlyGroundRobot(RobotInfo newRobot) {
        friendlyGroundRobots[friendlyGroundRobotsSize] = newRobot;
        friendlyGroundRobotsSize++;
    }

    public void addEnemyGroundRobot(RobotInfo newRobot) {
        enemyGroundRobots[enemyGroundRobotsSize] = newRobot;
        enemyGroundRobotsSize++;
    }

    public void addFriendlyAirRobot(RobotInfo newRobot) {
        friendlyAirRobots[friendlyAirRobotsSize] = newRobot;
        friendlyAirRobotsSize++;
    }

    public void addEnemyAirRobot(RobotInfo newRobot) {
        enemyAirRobots[enemyAirRobotsSize] = newRobot;
        enemyAirRobotsSize++;
    }

    public void updateKBRobotInfo(RobotInfoMessage newInfo) {
        otherRobotKnowledge.numFGround = newInfo.friendlyGroundRobots;
        otherRobotKnowledge.numEGround = newInfo.enemyGroundRobots;
        otherRobotKnowledge.numFAir = newInfo.friendlyAirRobots;
        otherRobotKnowledge.numEAir = newInfo.enemyAirRobots;
        otherRobotKnowledge.currEnergons = newInfo.currentEnergons;
        otherRobotKnowledge.finalEnergons = newInfo.eventualEnergons;
        otherRobotKnowledge.xCoords = newInfo.currentXPos;
        otherRobotKnowledge.yCoords = newInfo.currentYPos;
        otherRobotKnowledge.robotIDs = newInfo.robotIDs;

        otherRobotKnowledge.robotTypes = new RobotType[newInfo.numRobots];
        for (int i = 0; i < newInfo.numRobots; i++) {
            otherRobotKnowledge.robotTypes[i] = RobotType.values()[newInfo.robotTypes[i]];
            if (otherRobotKnowledge.robotTypes[i].equals(RobotType.TOWER)) {
                MapLocation temp = new MapLocation(otherRobotKnowledge.xCoords[i], otherRobotKnowledge.yCoords[i]);
                myMap.setTerrain(false, temp);
                addTower(temp);
            }
        }
    }

    protected void addTower(MapLocation towerLoc) {
        if (!towers.contains(towerLoc))
            towers.add(towerLoc);
    }

    private class KBRobotInfo {
        protected int numFGround, numEGround, numFAir, numEAir;
        protected int[] currEnergons, finalEnergons, xCoords, yCoords, robotIDs;
        protected RobotType[] robotTypes;

        public KBRobotInfo() {
            numFGround = 0;
            numEGround = 0;
            numEAir = 0;
            numFAir = 0;
            currEnergons = null;
            finalEnergons = null;
            xCoords = null;
            yCoords = null;
            robotIDs = null;
            robotTypes = null;
        }

    }


    
}
