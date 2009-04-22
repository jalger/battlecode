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

public class KnowledgeBase {

    protected Map myMap;
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

    public KnowledgeBase() {
        friendlyGroundRobots = new RobotInfo[100];
        enemyGroundRobots = new RobotInfo[100];
        friendlyAirRobots = new RobotInfo[100];
        enemyAirRobots = new RobotInfo[100];
        friendlyGroundRobotsSize = 0;
        friendlyAirRobotsSize = 0;
        enemyGroundRobotsSize = 0;
        enemyAirRobotsSize = 0;
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


    
}
