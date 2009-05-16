package teamJA_ND;

/**
 *
 * @author J.J. Alger, Nick Dunn
 */
import battlecode.common.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Vector;
import teamJA_ND.comm.*;
import teamJA_ND.state.*;
import teamJA_ND.util.*;
import java.util.LinkedList;
import java.util.List;

public class DefaultRobot implements Runnable {

    public static final int BYTES_PER_ROUND = 6000;
    public static final double COST_ESTIMATE_DIAGONAL_SAVING = 0.5858; //2-sqrt(2)

    protected RobotController rc;
    protected int sensorRadius;

    protected boolean tracing;
    protected boolean rightWallFollow;
    protected double tracingDisengageDistance;

    protected int moveCooldown;
    protected int attackCooldown;

    protected RobotLevel myHeight;
    protected MapLocation virtualLocation;

    protected int x,y;

    protected Map myMap;

    protected int clockTurnNum;
    protected int clockByteNum;
    protected int bytecodesReserved;

    protected Vector<SubMessage> queuedMessages;
    protected List<SubMessage> myNextMessageList;
    protected KnowledgeBase kb;
    protected Team myTeam;


    public DefaultRobot(RobotController rcIn) {
        rc = rcIn;
        sensorRadius = rcIn.getRobotType().sensorRadius();
        tracing = false;
        rightWallFollow = false;
        tracingDisengageDistance = 0;
        moveCooldown = 0;
        attackCooldown--;
        myHeight = rc.getRobot().getRobotLevel();
        myTeam = rc.getTeam();
        MapLocation towers[] = rc.senseAlliedTowers();
        myMap = new Map(towers[0].getX(), towers[0].getY());
        kb = new KnowledgeBase(myMap);
    }

    public void run() {


	/*
     //Main run method here; uncomment when stuff is available to make it work.
     startTurn();
        State temp = getTransitionState();
        if temp != null  {
            myState.onExit();
            myState = temp;
            myState.onEnter();
        }
        myState.update();
        endTurn();*/
                 


                bytecodesReserved = 200;

                startTurn();
                x = rc.getLocation().getX();
                y = rc. getLocation().getY();
                
                MapLocation loc;
                for (int i = -6; i < 7; i++) {
                    for (int j = -6; j < 7; j++) {
                        loc = new MapLocation(x + i, y + j);
                        if (rc.canSenseSquare(loc))
                            updateMapSquare(loc);
                    }
                }
        //while (true) {
            try {
                /*** beginning of main loop ***/
                rc.yield();
                State myState;
                MapLocation[] allyLocs = rc.senseAlliedArchons();
                MapLocation allyLoc = allyLocs[0];
                int myNumber = 0;
                if (rc.getLocation().equals(allyLocs[0])) {
                    allyLoc = allyLocs[1];
                }
                Robot ally = rc.senseGroundRobotAtLocation(allyLoc);

            /*** end of main loop ***/
            } catch (Exception e) {
                System.out.println("caught exception:");
                e.printStackTrace();
            }
        //}
    }

    protected void startTurn() {
        //Initialize data structures to be renewed each turn
        myNextMessageList = new LinkedList<SubMessage>();
        kb.resetRobotKnowledge();

        Message[] temp = rc.getAllMessages();
        queuedMessages = new Vector<SubMessage>();
        if (temp.length != 0) {
            Message m;
            for (int i = 0; i < temp.length; i++) {
                m = temp[i];
                List<SubMessage> smL = MessageUtil.getRelevantSubMessages(m, kb);
                if (smL != null)
                    while (smL.size() != 0) {
                        queuedMessages.add(smL.remove(0));
                    }
            }
            //And now parse messages as necessary.
        }

    }

    protected void messageConvert(Vector<Point> myPath) {
        debug_tick();
        int[] result = new int[2 + myPath.size()*2];
        result[0] = 0; //Message header info
        result[1] = 0; //More message header info
        Point temp;
        for (int i = 0; i < result.length - 2; i+= 2) {
            temp = myPath.elementAt(i/2);
            result[i+2] = temp.x;
            result[i+3] = temp.y;
        }
        debug_tock();
        Vector<Point> otherPath = new Vector<Point>(result.length/2);
        for (int i = 0; i < result.length - 2; i+= 2) {
            otherPath.add(new Point(result[i+2], result[i+3]));
        }
        debug_tock();
    }

    protected boolean headTowards(MapLocation goal) {
        Stack<VirtualBugLocation> result = null;
        result = myMap.tangentBug(rc.getLocation(), goal, 200.0, result);
        Vector<Point> myPath = myMap.buildPath(result);
        myMap.trimPath(myPath);
        if (myPath.size() > 4) {
            System.out.println("Path length: " + myPath.size());
            messageConvert(myPath);
        }
        boolean stillMoving = true;
        int current = 0;
        while (stillMoving && current < myPath.size()) {
            int x = myPath.elementAt(current).x - myMap.dx;
            int y = myPath.elementAt(current).y - myMap.dy;
            MapLocation next = new MapLocation(x,y);
            stillMoving = moveTo(next);
            current++;
        }
        if (!stillMoving)
            return false;
        return true;

    }

    protected void moveForward() {
        try {
            rc.moveForward();
            rc.yield();
        } catch(Exception e) {}
    }

    /*
     * Takes in a start and end MapLocation, returns the direction which
     * points most directly from the start to the end.
     * <50 bytecodes required.
     *
     * 2-24-09
     */
    public Direction deadReckon(MapLocation start, MapLocation end) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();

        //Note: If terrain elevations are to be considered, then
        //the factors of 2 appearing in here will be shifted somewhat.
        //Ignored for now, as it probably isn't worth the computation time.
        if (dx > 0) {
            if (dy > 0) { //SE quadrant
                if (dy > 2 * dx) {
                    return Direction.SOUTH;
                }
                if (2 * dy > dx) {
                    return Direction.SOUTH_EAST;
                }
                return Direction.EAST;
            }
            //NE quadrant
            if (-dy > 2 * dx) {
                return Direction.NORTH;
            }
            if (-2 * dy > dx) {
                return Direction.NORTH_EAST;
            }
            return Direction.EAST;
        }

        if (dy > 0) { //SW quadrant
            if (dy > -2 * dx) {
                return Direction.SOUTH;
            }
            if (-2 * dy < dx) {
                return Direction.SOUTH_WEST;
            }
            return Direction.WEST;
        }
        //NW quadrant
        if (2 * dx > dy) {
            return Direction.NORTH;
        }
        if (dx > 2 * dy) {
            return Direction.NORTH_WEST;
        }
        return Direction.WEST;
    }

    protected void endTurn() {
        if (myNextMessageList.size() > 0) {
            Message m = MessageUtil.pack(myNextMessageList, rc.getLocation(), rc.getRobot().getID(), 0);
            try {rc.broadcast(m);} catch (Exception e) {System.out.println("There was an error.");}
        }
        rc.yield();
        x = rc.getLocation().getX();
        y = rc.getLocation().getY();
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

    public double estimateMoveCost(MapLocation start, MapLocation end) {
        int x1 = end.getX();
        int x0 = start.getX();
        int y1 = end.getY();
        int y0 = start.getY();
        return estimateMoveCost(x0,y0,x1,y1);
    }

    public double estimateMoveCost(int x0, int y0, int x1, int y1) {
        int dx = Math.abs(x1-x0);
        int dy = Math.abs(y1-y0);
        return dx + dy - COST_ESTIMATE_DIAGONAL_SAVING*Math.min(dx, dy);
    }

    public Direction bugFind(MapLocation end) {
        MapLocation current = rc.getLocation();
        Direction preferred = deadReckon(current, end);
        if (tracing) {
            boolean freelySpinning = myMap.groundPassable(current.add(preferred));
            if (freelySpinning && tracingDisengageDistance > estimateMoveCost(current, end)) {
                tracing = false;
                return bugFind(end);
            }
            Direction result = rc.getDirection();
            if (rightWallFollow) {
                while (myMap.groundPassable(current.add(result)))
                    result = result.rotateRight();
                while (!myMap.groundPassable(current.add(result)))
                    result = result.rotateLeft();
            } else {
                while (myMap.groundPassable(current.add(result)))
                    result = result.rotateLeft();
                while (!myMap.groundPassable(current.add(result)))
                    result = result.rotateRight();
            }
            return result;
        } else {
            if (myMap.groundPassable(current.add(preferred)))
                return preferred;
            else {
                tracing = true;
                rightWallFollow = true;
                tracingDisengageDistance = estimateMoveCost(current, end);
                while (!myMap.groundPassable(current.add(preferred))) {
                    if (rightWallFollow)
                        preferred = preferred.rotateLeft();
                    else
                        preferred = preferred.rotateRight();
                }
                return preferred;
            }
        }
    }



    protected  void updateMapSquare(MapLocation loc) {
        if ((loc.getX() + myMap.dx >= 0) && (loc.getX() + myMap.dx < myMap.ARRAY_WIDTH) &&
            (loc.getY() + myMap.dy >= 0) && (loc.getY() + myMap.dy < myMap.ARRAY_HEIGHT)) {
            if (!myMap.isExplored(loc)) {
                if (rc.senseTerrainTile(loc) == TerrainTile.OFF_MAP)
                    markBoundary(loc);
                else
                    myMap.setTerrain(!senseMovable(loc), loc);
            }
        }
    }

    protected void markBoundary(MapLocation loc) {
        MapLocation temp = loc.add(Direction.NORTH);
        if (rc.canSenseSquare(temp) && rc.senseTerrainTile(temp) != TerrainTile.OFF_MAP) {
            myMap.setYMax(temp.getY());
        } else {
            temp = loc.add(Direction.SOUTH);
            if (rc.canSenseSquare(temp) && rc.senseTerrainTile(temp) != TerrainTile.OFF_MAP) {
                myMap.setYMin(temp.getY());
            } else {
                temp = loc.add(Direction.EAST);
                if (rc.canSenseSquare(temp) && rc.senseTerrainTile(temp) != TerrainTile.OFF_MAP) {
                    myMap.setXMin(temp.getX());
                } else {
                    temp = loc.add(Direction.WEST);
                    if (rc.canSenseSquare(temp) && rc.senseTerrainTile(temp) != TerrainTile.OFF_MAP) {
                        myMap.setXMax(temp.getX());
                    }
                }
            }
        }
    }

    public boolean senseMovable(int x, int y) {
        MapLocation loc = new MapLocation(x, y);
        return senseMovable(loc);
    }

    public boolean senseMovable(MapLocation loc) {
        //Make sure that the location is in-bounds
        /*Assert.Assert (loc.getX() + myMap.dx >= 0);
        Assert.Assert (loc.getY() + myMap.dy >= 0);
        Assert.Assert (loc.getX() + myMap.dx < myMap.ARRAY_WIDTH);
        Assert.Assert (loc.getY() + myMap.dy < myMap.ARRAY_HEIGHT);
        Assert.Assert (rc.canSenseSquare(loc));*/
        
        //Check for squares with invalid terrain
        if (!rc.senseTerrainTile(loc).isTraversableAtHeight(RobotLevel.ON_GROUND)) {
            return false;
        }
        return true;
    }

    public boolean moveTo(MapLocation goal) {
        MapLocation current = rc.getLocation();
        while (!rc.getLocation().equals(goal)) {

                while (rc.isMovementActive()) {
                    rc.yield();
                }
            Direction preferred = deadReckon(current, goal);
            if (rc.getLocation().getX() + myMap.dx == 80 && rc.getLocation().getY() + myMap.dy == 74)
                System.out.println("Here.");
            try {
                if (rc.canMove(preferred)) {
                    if (rc.getDirection() == preferred) {
                        rc.moveForward();
                    } else if (rc.getDirection().opposite() == preferred) {
                        rc.moveBackward();
                    } else {
                        rc.setDirection(preferred);
                    }
                }
                else {
                    return false;
                }
            } catch (Exception e) {}
            endTurn();
            current = rc.getLocation();
        }
        if (rc.getLocation().equals(goal))
            return true;
        return false;
    }

    public int getBytecodesReserved() {
        return bytecodesReserved;
    }

    public void senseRobots() throws GameActionException{
        debug_tick();
        //First get the full list of ground robots around, and sort into teams
        Robot[] groundRobots = rc.senseNearbyGroundRobots();
        Robot[] airRobots = rc.senseNearbyAirRobots();
        RobotInfo[] sortedInfo = new RobotInfo[groundRobots.length + airRobots.length];
        int friendlyGroundIndex = 0;
        int enemyGroundIndex = groundRobots.length-1;
        int friendlyAirIndex = groundRobots.length;
        int enemyAirIndex = sortedInfo.length - 1;
        Robot current;
        RobotInfo currentInfo;

        int[] robotIDs = new int[sortedInfo.length];

        for (int i = 0; i < groundRobots.length; i++) {
            current = groundRobots[i];
            currentInfo = rc.senseRobotInfo(current);
            if (currentInfo.team.equals(myTeam)) {
                robotIDs[friendlyGroundIndex] = current.getID();
                sortedInfo[friendlyGroundIndex++] = currentInfo;
            } else {
                robotIDs[enemyGroundIndex] = current.getID();
                sortedInfo[enemyGroundIndex--] = currentInfo;
            }
        }
        for (int i = 0; i < airRobots.length; i++) {
            current = airRobots[i];
            currentInfo = rc.senseRobotInfo(current);
            if (currentInfo.team.equals(myTeam)) {
                robotIDs[friendlyAirIndex] = current.getID();
                sortedInfo[friendlyAirIndex++] = currentInfo;
            } else {
                robotIDs[enemyAirIndex] = current.getID();
                sortedInfo[enemyAirIndex--] = currentInfo;
            }
        }

        int numRobots = sortedInfo.length + 1;
        int[] currentEnergons = new int[numRobots];
        int[] eventualEnergons = new int[numRobots];
        int[] currentXPos = new int[numRobots];
        int[] currentYPos = new int[numRobots];
        int[] robotTypes = new int[numRobots];
        int[] sortedRobotIDs = new int[numRobots];
        
        int friendlyGroundRobots = friendlyGroundIndex;
        int enemyGroundRobots = groundRobots.length - friendlyGroundIndex;
        int friendlyAirRobots = friendlyAirIndex - groundRobots.length;
        int enemyAirRobots = sortedInfo.length - friendlyAirIndex;

        int index = 0;

        if (rc.getRobot().getRobotLevel().equals(RobotLevel.ON_GROUND)) {
            friendlyGroundRobots++;
            currentEnergons[index] = (int)(100*rc.getEnergonLevel());
            eventualEnergons[index] = (int)(100*rc.getEventualEnergonLevel());
            currentXPos[index] = rc.getLocation().getX();
            currentYPos[index] = rc.getLocation().getY();
            robotTypes[index] = rc.getRobotType().compareTo(RobotType.SOLDIER);
            sortedRobotIDs[index++] = rc.getRobot().getID();
        } else {
            friendlyAirRobots++;
        }
        for (int i = 0; i < groundRobots.length; i++) {
            currentInfo = sortedInfo[i];
            currentEnergons[index] = (int)(100*currentInfo.energonLevel);
            eventualEnergons[index] = (int)(100*currentInfo.eventualEnergon);
            currentXPos[index] = currentInfo.location.getX();
            currentYPos[index] = currentInfo.location.getY();
            robotTypes[index] = currentInfo.type.compareTo(RobotType.SOLDIER);
            sortedRobotIDs[index++] = robotIDs[i];
        }
        if (rc.getRobot().getRobotLevel().equals(RobotLevel.IN_AIR)) {
            currentEnergons[index] = (int)(100*rc.getEnergonLevel());
            eventualEnergons[index] = (int)(100*rc.getEventualEnergonLevel());
            currentXPos[index] = rc.getLocation().getX();
            currentYPos[index] = rc.getLocation().getY();
            robotTypes[index] = rc.getRobotType().compareTo(RobotType.SOLDIER);
            sortedRobotIDs[index++] = rc.getRobot().getID();
        }
        for (int i = groundRobots.length; i < sortedInfo.length; i++) {
            currentEnergons[index] = (int)(100*rc.getEnergonLevel());
            eventualEnergons[index] = (int)(100*rc.getEventualEnergonLevel());
            currentXPos[index] = rc.getLocation().getX();
            currentYPos[index] = rc.getLocation().getY();
            robotTypes[index] = rc.getRobotType().compareTo(RobotType.SOLDIER);
            sortedRobotIDs[index++] = rc.getRobot().getID();
        }

        debug_tock();
        System.out.println("Finished constructing robot info. arrays");

        debug_tick();
        List<SubMessage> shortMessageList = new LinkedList<SubMessage>();
        SubMessageBody b = new RobotInfoMessage(friendlyGroundRobots, enemyGroundRobots,
                                                friendlyAirRobots, enemyAirRobots,
                                                currentEnergons, eventualEnergons,
                                                currentXPos, currentYPos,
                                                robotTypes, sortedRobotIDs);
        kb.updateKBRobotInfo((RobotInfoMessage)b);
        SubMessageHeader h = new SubMessageHeader.Builder(rc.getLocation(), b.getLength()).build();
        SubMessage sm = new SubMessage(h,b);
        shortMessageList.add(sm);

        Message m = MessageUtil.pack(shortMessageList, rc.getLocation(), rc.getRobot().getID(), 0);
        try {rc.broadcast(m);} catch (Exception e) {System.out.println("There was an error.");}
        debug_tock();
        System.out.println("Updated personal map, and broadcast message");

    }
}
