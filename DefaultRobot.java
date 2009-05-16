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
import java.util.Comparator;

import teamJA_ND.util.UtilityFunctions;

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
    
    private static final boolean DEBUG = true;
    private double MAX_ENERGON;
    private static final double MIN_ENERGON_RESERVE = 10.0;
    // Don't transfer energon if you have less than 40% life
    private static final double MIN_PROPORTION_ENERGON_TO_HEAL = 0.4;
    
    protected Comparator<RobotInfo> closestDamagedRobotComp;
    
    protected MapLocation towers[];
    
    public DefaultRobot(RobotController rcIn) {
        rc = rcIn;
        sensorRadius = rcIn.getRobotType().sensorRadius();

        kb = new KnowledgeBase();

        
        tracing = false;
        rightWallFollow = false;
        tracingDisengageDistance = 0;
        moveCooldown = 0;
        attackCooldown--;
        myHeight = rc.getRobot().getRobotLevel();
        myTeam = rc.getTeam();
        
        MAX_ENERGON = rc.getMaxEnergonLevel();
        
        closestDamagedRobotComp = new ClosestDamagedComparator(rc);
        
        towers = rc.senseAlliedTowers();
        // Create our map centered at our home base tower
        myMap = new Map(towers[0].getX(), towers[0].getY());
        
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
                kb = new KnowledgeBase(myMap);
                if (!(rc.getLocation().add(Direction.SOUTH_EAST).equals(towers[0]) || rc.getLocation().add(Direction.EAST).equals(towers[0])))
                    //if (!rc.getLocation().add(Direction.NORTH).equals(towers[0]))
                        rc.suicide();

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
                if (rc.getLocation().getY() != towers[0].getY()) {
                    myState = (State) (new Move(myMap, rc, this).setGoal(new Point(x + myMap.dx + 33,y + myMap.dy + 0)).addFollower(ally));
                } else {
                    myState = (State) (new Move(myMap, rc, this).setFollowing(ally, new Point(0,1)));
                }
                myState.onEnter();
                while (true) {
                    startTurn();
                    myState.update();
                    endTurn();
                }

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

        System.out.println("Checking robots.");
        debug_tick();

        Robot[] nearby = rc.senseNearbyGroundRobots();
        debug_tock();
        for (int i = 0; i < nearby.length; i++) {
            Robot current = nearby[i];
            try {
                RobotInfo currentI = rc.senseRobotInfo(current);
                if (currentI.team == myTeam)
                    kb.addFriendlyGroundRobot(currentI);
                else
                    kb.addEnemyGroundRobot(currentI);
            } catch (Exception e) {e.printStackTrace();}
        }
        debug_tock();
        nearby = rc.senseNearbyAirRobots();
        debug_tock();
        for (int i = 0; i < nearby.length; i++) {
            Robot current = nearby[i];
            try {
                RobotInfo currentI = rc.senseRobotInfo(current);
                if (currentI.team == myTeam)
                    kb.addFriendlyAirRobot(currentI);
                else
                    kb.addEnemyAirRobot(currentI);
            } catch (Exception e) {e.printStackTrace();}
        }

        debug_tock();

    }


    /**
    * @return the proportion of life the robot has left
    **/
    public static double getProportionHealth(RobotInfo r) {
        return r.eventualEnergon / r.maxEnergon;
    }

    /**
    * @return the proportion of life the robot has left
    **/
    public static double getProportionHealth(RobotController rc) {
        return rc.getEventualEnergonLevel() / rc.getMaxEnergonLevel();
    }

    /**
    * @return how much damage this robot has taken
    **/
    public static double getDamage(RobotInfo r) {
          return r.maxEnergon - r.eventualEnergon;
    }



    /**
    * @return true if we have enough energon to attempt to heal other units
    **/
    protected boolean shouldHeal() {
        double proportionHealth = rc.getEventualEnergonLevel() / MAX_ENERGON;
        return proportionHealth >= MIN_PROPORTION_ENERGON_TO_HEAL;
    }

    /**
    *
    * Transfer some of your energon to a nearby robot that has lower health, 
    * as a percentage.
    * Note that this method assumes that the knowledge base is up to date;
    * if the robots around it have moved and we attempt to transfer energon
    * to that square, we throw an exception; hence we explicitly try for that
    * condition.
    **/
    protected void heal() {
        
        // To whom are we going to attempt to transfer energon?
        RobotInfo transferTarget = null;
       
        RobotInfo closestGroundRobot = 
            UtilityFunctions.min(kb.friendlyGroundRobots, 0, kb.friendlyGroundRobotsSize, closestDamagedRobotComp);
        RobotInfo closestAirRobot = 
            UtilityFunctions.min(kb.friendlyAirRobots, 0, kb.friendlyAirRobotsSize, closestDamagedRobotComp);
        
        transferTarget = closestGroundRobot;
        // TODO: take into account air robot
   
        // No suitable targets to heal
        if (transferTarget == null) {
            if (DEBUG) {
                System.out.println("No targets are suitable for healing");
            }
            return;
        }
        
        // If we're not an archon, we only want to heal those around us
        // who are in worse shape than we are
        if (rc.getRobotType() != RobotType.ARCHON) {
            if (getProportionHealth(rc) <= getProportionHealth(transferTarget)) {
                if (DEBUG) {
                    System.out.println("Not going to heal this target because " +
                                        "I am more damaged than he is.");
                }
                return;
            }
        }
        
        // OK, we're going to heal this sucker.  Let's do it!
        
        MapLocation curLoc = rc.getLocation();
        MapLocation healingTargetLoc = transferTarget.location;
    
        // We can transfer the energon
        if (curLoc.equals(healingTargetLoc) || 
            curLoc.isAdjacentTo(healingTargetLoc)) {
            try {
                double amountEnergonToTransfer = 
                    calculateEnergonToTransfer(rc, transferTarget);
                // At what height is the robot we're trying to heal?
                RobotLevel levelOfTransferTarget = transferTarget.type.isAirborne() ? 
                                                    RobotLevel.IN_AIR :
                                                    RobotLevel.ON_GROUND;
                // Finally, we know how much, where, and at what height to
                // transfer energon.  Go ahead and try it
                rc.transferEnergon(amountEnergonToTransfer, 
                                    healingTargetLoc, 
                                    levelOfTransferTarget);
                rc.yield();
            }
            // The transfer failed for some reason
            catch (GameActionException e) {
                if (DEBUG) {
                    System.out.println("Attempted to heal a robot " + 
                                        transferTarget + " with out " +
                                        "of date information; robot must " +  
                                        "have moved");
                    e.printStackTrace();
                }
            }
        }
        
        // We are not adjacent; we need to move towards the robot
        else {
            
            if (DEBUG) {
                System.out.println("Trying to heal " + transferTarget + 
                                    " but it's too far away.");
            }
            
            // If we can move this turn, do so
            
            // Else ... 
        }
    }
    
    private double calculateEnergonToTransfer(RobotController rc, RobotInfo transferTarget) {
        // Give them either how much energon they need to get back to full 
        // health, or however much we can afford to give them without depleting
        // our reserves too much.  Make sure we don't transfer a negative amount
        return Math.max(0, Math.min(getDamage(transferTarget), 
                        rc.getEventualEnergonLevel() - MIN_ENERGON_RESERVE));
                                    
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
        Assert.Assert (loc.getX() + myMap.dx >= 0);
        Assert.Assert (loc.getY() + myMap.dy >= 0);
        Assert.Assert (loc.getX() + myMap.dx < myMap.ARRAY_WIDTH);
        Assert.Assert (loc.getY() + myMap.dy < myMap.ARRAY_HEIGHT);
        Assert.Assert (rc.canSenseSquare(loc));

        //Check for squares with invalid terrain
        if (!rc.senseTerrainTile(loc).isTraversableAtHeight(RobotLevel.ON_GROUND)) {
            return false;
        }

        //Check for tower squares
        Robot occupier = null;
        try {
            occupier = rc.senseGroundRobotAtLocation(loc);
        } catch (Exception e) {
        }
        if (occupier == null) {
            return true; //Nothing but terrain on the square
        }
        try {
            if (rc.senseRobotInfo(occupier).type == RobotType.TOWER) {
                return false; //Towers can't move or be moved. It's a permanent
                              //barrier.
            }
        } catch (Exception e) {
        }
        return true; //Whatever's on the square, it isn't a tower.
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
    
    
    /**
    * @return a Move object that will move the robot towards the
    * nearest enemy archon, but some angle to it.
    **/
    public Move startBlitz(MapLocation origin, Direction dir) {
        
        Point destination = getDistantPoint(origin, dir);
        Move blitz = new Move(myMap, rc, this).setGoal(destination);
        return blitz;
    }
    
    /**
    * @return a distant point that lies in the given direction from the
    * origin.  This lets us use tangent bug to go in this general direction
    * while avoiding obstacles along the way
    **/
    public Point getDistantPoint(MapLocation origin, Direction dir) {
        
        
            Assert.Assert(myMap != null);

        
        int pointIndex = Move.direcToPointIndex(dir);
        Point offset = Move.directions[pointIndex];
        
        System.out.println("Point index: " + pointIndex);
        System.out.println("Offset: " + offset);
        
        final int SCALE = GameConstants.MAP_MAX_WIDTH;
        
        Point scaledOffset = offset.scale(SCALE);
        
        Point originPrime = myMap.toPoint(origin);

        System.out.println("Scaled offset: " + scaledOffset);
        System.out.println("Origin prime: " + originPrime);
       
        Point destination = new Point(originPrime.x + scaledOffset.x,
                                    originPrime.y + scaledOffset.y);
        System.out.println("Destination: " + destination);
        return destination;
    }
    
    
    /**
    * Using this as a comparator, the minimum of an array of RobotInfo objects
    * will be the closest, most damaged, non-Archon unit.  In the case that only
    * archons are in the array being sorted, it will be the closest, most damaged
    * archon.  In the case that there are equally close units, the most
    * damaged unit is chosen. 
    **/
    protected class ClosestDamagedComparator implements Comparator<RobotInfo> {
        private DistanceComparator distanceComp;
        private HealthComparator healthComp;

        public ClosestDamagedComparator(RobotController rc) {
            distanceComp = new DistanceComparator(rc);
            healthComp = new HealthComparator();
        }
        
        public int compare(RobotInfo r1, RobotInfo r2) {
            // We want to make sure we do not ever return an archon for healing
            // purposes.  As such, we want anything that's NOT an archon to
            // be considered "less" than an archon
            // This is sort of a hack.
            if (r1.type == RobotType.ARCHON && r2.type != RobotType.ARCHON) {
                return 1;
            }
            else if (r1.type != RobotType.ARCHON && r2.type == RobotType.ARCHON) {
                return -1;
            }
            
            // Either both archons or both non-archons.  Compare normally.
            
            int distanceComparison = distanceComp.compare(r1, r2);
            
            if (distanceComparison != 0) {
                return distanceComparison;
            }
            
            // Tie break on damage.  Note that since we compare health,
            // take the negative of the result to get a comparison based on
            // damage
            return - healthComp.compare(r1, r2);
        }
    }
}
