/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teamJA_ND;

/**
 *
 * @author J.J. Alger
 */
import teamJA_ND.State;
import java.util.Vector;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.List;

import teamJA_ND.*;
import teamJA_ND.util.*;
import teamJA_ND.comm.*;
import battlecode.common.*;


public class Move extends State {

    public static final int NUM_DIRECS = 8;
    public static final int RIGHT_ROTATE_OFFSET = 1;
    public static final int LEFT_ROTATE_OFFSET = NUM_DIRECS - 1;
    public static final int OPPOSITE_OFFSET = 4;
    public static final int N_N = 0;
    public static final int N_NW = 1;
    public static final int N_W = 2;
    public static final int N_SW = 3;
    public static final int N_S = 4;
    public static final int N_SE = 5;
    public static final int N_E = 6;
    public static final int N_NE = 7;
    public static final int LOOK_AHEAD_DISTANCE = 3;
    public static final double DIAGONAL_COST_SAVING = 0.5858; //2 - sqrt(2)
    public static final double MAX_COST = 200;
    public static final int ADVANCE_TO_FIRST_WALL_STAGE = 0;
    public static final int TANGENT_BUG_STAGE = 1;
    public static final int PATH_CONSTRUCTION_STAGE = 2;
    public static final int PATH_REFINEMENT_STAGE = 3;
    public static final int WAYPOINT_SKIPPING_STAGE = 4;
    public static final int FINISHED_STAGE = 5;
    //Private variables for conducting tangentBug to find a good path to the goal
    protected PriorityQueue<VirtualBugLocation> virtualBugs;
    protected VirtualBugLocation currentBug;
    protected VirtualBugLocation bestBug;
    protected VirtualBugLocation closestBug;
    protected double closestScore;
    protected boolean tracingDone, advancingDone;
    protected double maxCost;
    protected int previousTracingDirec;
    protected boolean tracingStarted;
    protected double tracingDisengageDistance;
    protected int aCounter = 0;

    //Private variables for constructing a path from the final tangentBug node
    Stack<VirtualBugLocation> sparsePath;

    //Private variables for improving the path
    protected int lastCertainWaypoint, waypointToSkipTo;

    //Private variables for moving along the given path
    protected boolean flying;
    protected boolean pathfinding;
    protected boolean following;
    protected boolean idle;
    protected int priorityMove;

    //Private variables for following mode
    Robot leader;
    Vector<Robot> followers;
    Point preferredDisplacement;

    //Private variables to speed up access to some frequently-callled methods and whatnot.
    protected KnowledgeBase myKnowledge;
    protected Map myMap;
    protected RobotController rc;
    protected Vector<Point> myPath;
    protected int currentRound;
    protected int x0,  y0;
    protected int currDirIndex;
    protected Point goal;
    protected int airMoveRate,  groundMoveRate;
    protected int pathfindingStage;
    protected DefaultRobot brains;
    protected boolean currentStageInProgress;
    protected boolean finishedMoving;
    protected int counter;
    
    protected static Point[] directions = new Point[NUM_DIRECS];
    
    // Initialize all the directions
    static {
        directions[N_N] = new Point(0, -1);
        directions[N_NW] = new Point(-1, -1);
        directions[N_W] = new Point(-1, 0);
        directions[N_SW] = new Point(-1, 1);
        directions[N_S] = new Point(0, 1);
        directions[N_SE] = new Point(1, 1);
        directions[N_E] = new Point(1, 0);
        directions[N_NE] = new Point(1, -1);
    }


    public Move(KnowledgeBase kbIn, RobotController rcIn, DefaultRobot controller) {
        myKnowledge = kbIn;
        myMap = myKnowledge.myMap;
        rc = rcIn;
        brains = controller;
        followers = new Vector<Robot>();
    }

    public Move(Map mapIn, RobotController rcIn, DefaultRobot controller) {
        myMap = mapIn;
        rc = rcIn;
        brains = controller;
        followers = new Vector<Robot>();
    }

    public Move setGoal(Point goalIn) {
        pathfinding = true;
        following = false;
        resetPath();
        goal = goalIn;
        return this;
    }

    public Move setFollowing(Robot friendToFollow, Point displacement) {
        pathfinding = false;
        following = true;
        preferredDisplacement = displacement;
        leader = friendToFollow;
        return this;
    }

    public Move addFollower(Robot friend) {
        followers.add(friend);
        return this;
    }

    public void onEnter() {
        counter = 0;
        priorityMove = -1;
        currDirIndex = direcToPointIndex(rc.getDirection());
        airMoveRate = 0;
        groundMoveRate = rc.getRobotType().moveDelayDiagonal();
        virtualBugs = new PriorityQueue<VirtualBugLocation>();
        bestBug = null;
        MapLocation temp = rc.getLocation();
        x0 = temp.getX() + myMap.getdx();
        y0 = temp.getY() + myMap.getdy();
        tracingStarted = false;
        resetPath();
        flying = rc.getRobot().getRobotLevel() == RobotLevel.IN_AIR;
        if (!following)
            debug_warn("Moving to: " + goal.x + ", " + goal.y);
        else
            debug_warn("Following robot: " + leader.getID() + "; displacement: " + preferredDisplacement.x + ", " + preferredDisplacement.y);
    }

    public void update() {
        //If we already know where we're going next
        if (Clock.getRoundNum() > currentRound) {
            currentRound = Clock.getRoundNum();
        } else {
            return; //This update method does not support parallel work with
        }           //other states, as it's too dangerous. Update can be called
                    //only once per turn.

        if (finishedMoving)
            return;
        
        advance(); //Handles moving the robot towards
                   //the next point on its myPath vector.
        if (pathfinding)
            compute(); //Works on constructing/improving myPath vector
    }

    public void onExit() {

    }

    //Note bytecode-safe. Should be called near start of turn.
    private void advance() {
        if (brains.queuedMessages.size() != 0) {
            SubMessage sm = brains.queuedMessages.get(0);
            priorityMove = deadReckonIndex(x0, y0, x0 - 1, y0 + 1);
            if (pathfinding) {
                myPath.insertElementAt(new Point(x0, y0), 0);
            }
        }

        if (priorityMove != -1) {
            int xOld = x0;
            int yOld = y0;
            step(priorityMove);
            if (xOld != x0 || yOld != y0)
                priorityMove = -1;
            return;
        }

        if (pathfinding) {
            if (myPath.size() == 0) {
                if (pathfindingStage == FINISHED_STAGE) {
                    debug_warn("Done moving. Current location: " + x0 + ", " + y0);
                    finishedMoving = true;
                    return;
                }
                if (goal.x != x0 || goal.y != y0) {
                    int n_preferred = deadReckonIndex(x0, y0, goal.x, goal.y);
                    Direction next = pointToDirec(directions[n_preferred]);
                    if (rc.canMove(next)) {
                        step(n_preferred);
                    }
                }
                return;
            }

            Point tempGoal = myPath.get(0);
            if (x0 == tempGoal.x && y0 == tempGoal.y) {
                myPath.remove(0);
                advance();
                return;
            }

            int preferred_direc_index = deadReckonIndex(x0, y0, tempGoal.x, tempGoal.y);

            step(preferred_direc_index);
            
            if (tempGoal.x == x0 &&
                    tempGoal.y == y0) {
                myPath.remove(0);
            }

            //Make sure the robot's path is still clear
            //This should allow the robot to have at least 5 turns of advance
            //warning to recompute the path if
            //there is a barrier in the way.
            if (myPath.size() > 0) {
                tempGoal = myPath.get(0);
                preferred_direc_index = deadReckonIndex(x0, y0, tempGoal.x, tempGoal.y);
                int xNext = x0 + directions[preferred_direc_index].x;
                int yNext = y0 + directions[preferred_direc_index].y;
                if (!myMap.groundPassableArrayCoords(xNext, yNext)) {
                    resetPath();
                }
            }
        }
        else if (following) {
            if (rc.getRoundsUntilMovementIdle() != 0)
                    return;
            MapLocation leaderLoc = rc.getLocation();
            try {
                RobotInfo leaderInfo = rc.senseRobotInfo(leader);
                leaderLoc = leaderInfo.location;
            } catch (Exception e) {}
            int dx = -(x0 - myMap.dx - leaderLoc.getX() - preferredDisplacement.x);
            int dy = -(y0 - myMap.dy - leaderLoc.getY() - preferredDisplacement.y);
            if (dx == 0 && dy == 0)
                return;

            int n_preferred = getFollowingDirecIndex(dx, dy);
            if (n_preferred != -1)
                step(n_preferred);
        }
    }

    private int getFollowingDirecIndex(int dx, int dy) {
        int n_preferred = deadReckonIndex(x0, y0, x0 + dx, y0 + dy);
        Direction next = pointToDirec(directions[n_preferred]);
        if (rc.canMove(next))
            return n_preferred;
        int xNext = x0 + directions[n_preferred].x;
        int yNext = y0 + directions[n_preferred].y;

        //Check for a map obstruction causing blockage
        if (!flying && !myMap.groundPassableArrayCoords(xNext, yNext)) {
            n_preferred = rotateForBestClear(n_preferred, dx, dy);
            next = pointToDirec(directions[n_preferred]);
            if (rc.canMove(next)) {
                return n_preferred;
            }
        }

        //Check in-bounds
        if (myMap.xMax <= x0 + dx || myMap.xMin >= x0 + dx||
            myMap.yMax <= y0 + dy || myMap.yMin >= y0 + dy) {
            debug_warn("Group trying to leave map! This behavior should not occur.");
            n_preferred = rotateForBestClear(n_preferred, dx, dy);
            next = pointToDirec(directions[n_preferred]);
            if (rc.canMove(next))
                return n_preferred;
        }

        //Should be a robot in the way then. What behavior to undertake then?
        n_preferred = rotateForBestMovableClear(n_preferred, dx, dy);
        return n_preferred;

    }

    public int rotateForBestClear(int n_preferred, int dx, int dy) {

            int rightTurningIndex = n_preferred;
            int xNext = x0 + directions[rightTurningIndex].x;
            int yNext = y0 + directions[rightTurningIndex].y;

            while (!myMap.groundPassableArrayCoords(xNext, yNext)) {
                rightTurningIndex = rotateRightIndex(rightTurningIndex);
                xNext = x0 + directions[rightTurningIndex].x;
                yNext = y0 + directions[rightTurningIndex].y;
            }
            int leftTurningIndex = n_preferred;
            xNext = x0 + directions[leftTurningIndex].x;
            yNext = y0 + directions[leftTurningIndex].y;
            while (!myMap.groundPassableArrayCoords(xNext, yNext)) {
                leftTurningIndex = rotateLeftIndex(leftTurningIndex);
                xNext = x0 + directions[leftTurningIndex].x;
                yNext = y0 + directions[leftTurningIndex].y;
            }
            double leftD = estimateDistance(dx, dy, directions[leftTurningIndex].x, directions[leftTurningIndex].y);
            double rightD = estimateDistance(dx, dy, directions[rightTurningIndex].y, directions[rightTurningIndex].y);
            if (leftD < rightD) {
                n_preferred = leftTurningIndex;
            } else {
                n_preferred = rightTurningIndex;
            }

            return n_preferred;
    }

    public int rotateForBestMovableClear(int n_preferred, int dx, int dy) {

            int rightTurningIndex = n_preferred;

            Direction next = pointToDirec(directions[rightTurningIndex]);
            int counter = 8;
            while (!rc.canMove(next) && counter != 0) {
                rightTurningIndex = rotateRightIndex(rightTurningIndex);
                next = pointToDirec(directions[rightTurningIndex]);
                counter--;
            }
            int leftTurningIndex = n_preferred;
            next = pointToDirec(directions[leftTurningIndex]);
            counter = 8;
            while (!rc.canMove(next) && counter != 0) {
                rightTurningIndex = rotateRightIndex(rightTurningIndex);
                next = pointToDirec(directions[rightTurningIndex]);
                counter--;
            }
            double currentD = estimateDistance(dx, dy, 0, 0);
            double leftD = estimateDistance(dx, dy, directions[leftTurningIndex].x, directions[leftTurningIndex].y);
            double rightD = estimateDistance(dx, dy, directions[rightTurningIndex].y, directions[rightTurningIndex].y);
            if (currentD < leftD && currentD < rightD) {
                n_preferred = -1;
            } else if (leftD < rightD) {
                n_preferred = leftTurningIndex;
            } else {
                n_preferred = rightTurningIndex;
            }
            return n_preferred;
    }

    private void step(int direcIndex) {
        //Wait if necessary before rotating
        if (rc.getRoundsUntilMovementIdle() != 0)
            return;

        //Rotate if necessary
        if (!(direcIndex == currDirIndex || oppositeIndex(direcIndex) == currDirIndex)) {
            try {
                Direction next = pointToDirec(directions[direcIndex]);
                rc.setDirection(next);
                currDirIndex = direcIndex;
            } catch (Exception e) {}
            return;
        }

        //Wait if necessary before moving
        if (!readyToMove())
            return;

        Direction next = rc.getDirection();

        //Move forward if able
        if (currDirIndex == direcIndex) {
            next = pointToDirec(directions[direcIndex]);
            if (rc.canMove(next))
                try {
                    rc.moveForward();
                    x0 += directions[direcIndex].x;
                    y0 += directions[direcIndex].y;
                    return;
                } catch (Exception e) { System.out.println("Error in movement.");}
        }

        //Move backwards if able
        if (oppositeIndex(direcIndex) == currDirIndex) {
            next = pointToDirec(directions[direcIndex]).opposite();
            if (rc.canMove(next))
                try {
                    rc.moveBackward();
                    x0 += directions[direcIndex].x;
                    y0 += directions[direcIndex].y;
                    return;
                } catch (Exception e) { System.out.println("Error in movement.");}
        }

        //Something is in the way.
        Robot interferer = null;
        try {
            if (flying)
                interferer = rc.senseAirRobotAtLocation(rc.getLocation().add(next));
            else
                interferer = rc.senseGroundRobotAtLocation(rc.getLocation().add(next));
        } catch (Exception e) {}

        if (interferer != null) {
            //Broadcast a message instructing them to get out of my way.

            SubMessageBody b = new ShoveCommand(interferer.getID());
            SubMessageHeader h = new SubMessageHeader.Builder(rc.getLocation(), b.getLength()).build();
            SubMessage sm = new SubMessage(h,b);
            brains.myNextMessageList.add(sm);
        }
        else {
            debug_warn("Warning: Blocked passage, but no robot in the way. Probable error in map or expected robot location.");
            debug_warn("Direction index: " + direcIndex);
            debug_warn("Location: " + x0 + ", " + y0);
            debug_warn("Actual location: " + (rc.getLocation().getX() + myMap.getdx() + ", " + (rc.getLocation().getY() + myMap.getdy())));
            debug_warn("Current robot direction: " + direcToPointIndex(rc.getDirection()));
            debug_warn("Updating position by: " + directions[direcIndex].x + ", " + directions[direcIndex].y);
            counter++;
            if (counter > 3)
                while(true){}
        }
    }

    private void compute() {
        if (pathfindingStage != FINISHED_STAGE) {
            while (GameConstants.BYTECODES_PER_ROUND - Clock.getBytecodeNum() - brains.getBytecodesReserved() >
                    estimateMinBytecodes(pathfindingStage)) {
                switch (pathfindingStage) {
                    case ADVANCE_TO_FIRST_WALL_STAGE:
                        updateAdvanceToWall();
                        break;
                    case TANGENT_BUG_STAGE:
                        updateTangentBug();
                        break;
                    case PATH_CONSTRUCTION_STAGE:
                        updatePathConstruction();
                        break;
                    case PATH_REFINEMENT_STAGE:
                        updatePathRefinement();
                        break;
                    case WAYPOINT_SKIPPING_STAGE:
                        updateWaypointSkipping();
                        break;
                }
            }
        }
    }

    public int estimateMinBytecodes(int currentStage) {
        switch (pathfindingStage) {
            case FINISHED_STAGE:
                return Integer.MAX_VALUE;
            case ADVANCE_TO_FIRST_WALL_STAGE:
                return 100;
            case TANGENT_BUG_STAGE:
                return 1000;
            case PATH_CONSTRUCTION_STAGE:
                return 500;
            case PATH_REFINEMENT_STAGE:
                return 1000;
            case WAYPOINT_SKIPPING_STAGE:
                return 1000;
            default:
                return Integer.MAX_VALUE;
        }
    }

    public int deadReckonIndex(int xI, int yI, int x1, int y1) {
        int deltaX = x1 - xI;
        int deltaY = y1 - yI;

        if (deltaX > 0) {
            if (deltaY > 0) { //SE quadrant
                if (deltaY > 2 * deltaX) {
                    return N_S;
                }
                if (2 * deltaY > deltaX) {
                    return N_SE;
                }
                return N_E;
            }
            //NE quadrant
            if (-deltaY > 2 * deltaX) {
                return N_N;
            }
            if (-2 * deltaY > deltaX) {
                return N_NE;
            }
            return N_E;
        }

        if (deltaY > 0) { //SW quadrant
            if (deltaY > -2 * deltaX) {
                return N_S;
            }
            if (-2 * deltaY < deltaX) {
                return N_SW;
            }
            return N_W;
        }
        //NW quadrant
        if (2 * deltaX > deltaY) {
            return N_N;
        }
        if (deltaX > 2 * deltaY) {
            return N_NW;
        }
        return N_W;
    }

    public static int direcToPointIndex(Direction direc) {
        switch (direc) {
            case NORTH:
                return N_N;
            case NORTH_WEST:
                return N_NW;
            case WEST:
                return N_W;
            case SOUTH_WEST:
                return N_SW;
            case SOUTH:
                return N_S;
            case SOUTH_EAST:
                return N_SE;
            case EAST:
                return N_E;
            case NORTH_EAST:
                return N_NE;
            default:
                return -1;
        }
    }

    public Direction pointToDirec(Point p) {
        if (p.x > 0) {
            if (p.y > 0) {
                return Direction.SOUTH_EAST;
            }
            if (p.y == 0) {
                return Direction.EAST;
            }
            return Direction.NORTH_EAST;
        }
        if (p.x == 0) {
            if (p.y > 0) {
                return Direction.SOUTH;
            } else if (p.y == 0) {
                return Direction.NONE;
            }
            return Direction.NORTH;
        }
        if (p.y > 0) {
            return Direction.SOUTH_WEST;
        }
        if (p.y == 0) {
            return Direction.WEST;
        }
        return Direction.NORTH_WEST;
    }

    private boolean readyToMove() {
        if (rc.getRoundsUntilMovementIdle() != 0)
            return false;
        return teammatesInPlace();
    }

    private boolean teammatesInPlace() {
        for (int i = 0; i < followers.size(); i++) {
            Robot current = followers.get(i);
            int dSquared = Integer.MAX_VALUE;
            try {
                if (rc.canSenseObject(current)) {
                    RobotInfo currentI = rc.senseRobotInfo(current);
                    dSquared = rc.getLocation().distanceSquaredTo(currentI.location);
                }
            } catch (Exception e) {}
            if (dSquared > 9)
                return false;
        }
        return true;
    }

    private void resetPath() {
        pathfindingStage = ADVANCE_TO_FIRST_WALL_STAGE;
        myPath = new Vector<Point>();
        myPath.add(new Point(x0, y0));
        finishedMoving = false;
    }

    private void updateAdvanceToWall() {
        int xCurr = x0;
        int yCurr = y0;
        //By experimentation, 400 bytecodes is a generous assumption about the
        //maximum bytecodes it will take to go from through the while loop
        //once (no looping) and on to the end of the method.
        //Ensures that this method will return with at least
        //brains.getBytecodesReserved() bytecodes left on the Clock for this
        //robot.
        int bytecodeCutoff = GameConstants.BYTECODES_PER_ROUND - brains.getBytecodesReserved() - 400;
        if (myPath.size() > 0) {
            Point start = myPath.remove(myPath.size() - 1);
            xCurr = start.x;
            yCurr = start.y;
        }
        int n_preferred = -1;
        while (Clock.getBytecodeNum() < bytecodeCutoff &&
                (xCurr != goal.x || yCurr != goal.y) &&
                myMap.groundPassableArrayCoords(xCurr, yCurr)) {
            n_preferred = deadReckonIndex(xCurr, yCurr, goal.x, goal.y);
            xCurr += directions[n_preferred].x;
            yCurr += directions[n_preferred].y;
        }
        //Goal reached by dead reckoning alone! Simply add goal, and stop
        //pathfinding.
        if (xCurr == goal.x && yCurr == goal.y) {
            if (!myMap.groundPassableArrayCoords(xCurr, yCurr)) {
                xCurr -= directions[n_preferred].x;
                yCurr -= directions[n_preferred].y;
                bestBug = new VirtualBugLocation(xCurr, yCurr, 0);
            }
            myPath.add(new Point(xCurr, yCurr));
            pathfindingStage = FINISHED_STAGE;
            idle = true;
            return;
        }
        //Reached first barrier. Switch to tangentBug portion of
        //search.
        if (!myMap.groundPassableArrayCoords(xCurr, yCurr)) {
            if (n_preferred >= 0) {
                xCurr -= directions[n_preferred].x;
                yCurr -= directions[n_preferred].y;
                myPath.add(new Point(xCurr, yCurr));
                pathfindingStage = TANGENT_BUG_STAGE;
                currentStageInProgress = false;
            } else {
                //This should never happen!
                debug_warn("Unreachable point in path! Resetting path...");
                resetPath();
            }
        }
        //Clock running out case - just add
        //the current point, and carry on from here next time.
        myPath.add(new Point(xCurr, yCurr));
    }

    private void updateTangentBug() {
        if (!currentStageInProgress) {
            Point temp = myPath.lastElement();

            //Make sure conditions are as suspected
            if (!myMap.groundPassableArrayCoords(temp.x, temp.y)) {
                debug_warn("Unreachable point in path! Resetting path...");
                resetPath();
            }
            int n_preferred = deadReckonIndex(temp.x, temp.y, goal.x, goal.y);
            int xTemp = temp.x + directions[n_preferred].x;
            int yTemp = temp.y + directions[n_preferred].y;
            if (myMap.groundPassableArrayCoords(xTemp, yTemp)) {
                debug_warn("Tangent called, but not on a wall. Returning to free-moving...");
                pathfindingStage = ADVANCE_TO_FIRST_WALL_STAGE;
                return;
            }

            //Initialize tangentBug structures, and set up first two bugs.
            double d = estimateDistance(x0, y0, temp.x, temp.y);
            currentBug = new VirtualBugLocation(temp.x, temp.y, d);
            virtualBugs = new PriorityQueue<VirtualBugLocation>();
            double heuristic = estimateDistance(currentBug.x, currentBug.y, goal.x, goal.y);
            virtualBugs.add(new VirtualBugLocation(currentBug).setRightWallFollow(true).setDistanceRemainingEstimate(heuristic));
            virtualBugs.add(new VirtualBugLocation(currentBug).setRightWallFollow(false).setDistanceRemainingEstimate(heuristic));
            currentStageInProgress = true;
            currentBug = virtualBugs.poll();
            tracingDone = false;
            advancingDone = false;
            bestBug = null;
            maxCost = Math.min(MAX_COST, 4*estimateDistance(currentBug.x, currentBug.y, goal.x, goal.y));
        }

        int bytecodeCutoff = GameConstants.BYTECODES_PER_ROUND - brains.getBytecodesReserved() - 300;
        if (Clock.getBytecodeNum() < bytecodeCutoff && currentBug.d < maxCost) {
            if (!tracingDone)
                tracingDone = traceAroundObstacle(currentBug, goal.x, goal.y, maxCost);
            else if (!advancingDone)
                advancingDone = advanceToWall(currentBug, goal.x, goal.y);
            else {
                if (currentBug.d < maxCost) {
                    if (currentBug.x == goal.x &&
                        currentBug.y == goal.y) {
                        bestBug = currentBug;
                        maxCost = currentBug.d;
                    }
                    else {
                        double heuristic = estimateDistance(currentBug.x, currentBug.y, goal.x, goal.y);
                        if (heuristic < closestScore) {
                            closestBug = currentBug;
                            closestScore = heuristic;
                        }
                        virtualBugs.add(new VirtualBugLocation(currentBug).setRightWallFollow(true).setDistanceRemainingEstimate(heuristic));
                        virtualBugs.add(new VirtualBugLocation(currentBug).setRightWallFollow(false).setDistanceRemainingEstimate(heuristic));
                    }
                }
                //And set up for the next bug...
                //There are no bugs left; go with the best so far
                if (virtualBugs.isEmpty()) {
                    pathfindingStage = PATH_CONSTRUCTION_STAGE;
                    currentStageInProgress = false;
                    //Goal not reached within distance limit; this is the closest.
                    if (bestBug == null) {
                        bestBug = closestBug;
                    }
                } else {
                //Set up next virtual bug for checking...
                    currentBug = virtualBugs.poll();
                    tracingDone = false;
                    advancingDone = false;
                }
            }
        }
        if (currentBug.d >= maxCost) {
            if (!virtualBugs.isEmpty()) {
                currentBug = virtualBugs.poll();
                tracingDone = false;
                advancingDone = false;
            } else {
                pathfindingStage = PATH_CONSTRUCTION_STAGE;
                currentStageInProgress = false;
                if (bestBug == null) {
                    bestBug = closestBug;
                    debug_warn("Unreachable goal");
                    debug_warn("Best location: " + closestBug.x + ", " + closestBug.y);
                }
            }
        }
    }

    private boolean traceAroundObstacle(VirtualBugLocation current, int x1, int y1, double maxCost) {
        if (!tracingStarted) {
            Assert.Assert(myMap.groundPassableArrayCoords(current.x, current.y));

            int n_preferred = deadReckonIndex(current.x, current.y, x1, y1);
            int xNext = current.x + directions[n_preferred].x;
            int yNext = current.y + directions[n_preferred].y;
            Assert.Assert(!myMap.groundPassableArrayCoords(xNext, yNext));

            tracingDisengageDistance = estimateDistance(current.x, current.y, x1, y1);
            previousTracingDirec = n_preferred;

            if (current.rightWallFollow) {
                while (!myMap.groundPassableArrayCoords(xNext, yNext)) {
                    previousTracingDirec = rotateLeftIndex(previousTracingDirec);
                    xNext = current.x + directions[previousTracingDirec].x;
                    yNext = current.y + directions[previousTracingDirec].y;
                }
            } else {
                while (!myMap.groundPassableArrayCoords(xNext, yNext)) {
                    previousTracingDirec = rotateRightIndex(previousTracingDirec);
                    xNext = current.x + directions[previousTracingDirec].x;
                    yNext = current.y + directions[previousTracingDirec].y;
                }
            }
            tracingStarted = true;
        }


        boolean clear = false;
        int n_preferred, xNext, yNext;
        int bytecodeCutoff = GameConstants.BYTECODES_PER_ROUND - brains.getBytecodesReserved() - 250;
        if (current.d < maxCost && (current.x != x1 || current.y != y1)
                && Clock.getBytecodeNum() < bytecodeCutoff) {
            n_preferred = deadReckonIndex(current.x, current.y, x1, y1);
            xNext = current.x + directions[n_preferred].x;
            yNext = current.y + directions[n_preferred].y;
            if (myMap.groundPassableArrayCoords(xNext, yNext) && estimateDistance(current.x, current.y, x1, y1) <= tracingDisengageDistance) {
                clear = true;
            } else {
                previousTracingDirec = oppositeIndex(previousTracingDirec);
                if (current.rightWallFollow) {
                    previousTracingDirec = rotateLeftIndex(previousTracingDirec);
                    xNext = current.x + directions[previousTracingDirec].x;
                    yNext = current.y + directions[previousTracingDirec].y;
                    while (!myMap.groundPassableArrayCoords(xNext, yNext)) {
                        previousTracingDirec = rotateLeftIndex(previousTracingDirec);
                        xNext = current.x + directions[previousTracingDirec].x;
                        yNext = current.y + directions[previousTracingDirec].y;
                    }
                } else {
                    previousTracingDirec = rotateRightIndex(previousTracingDirec);
                    xNext = current.x + directions[previousTracingDirec].x;
                    yNext = current.y + directions[previousTracingDirec].y;
                    while (!myMap.groundPassableArrayCoords(xNext, yNext)) {
                        previousTracingDirec = rotateRightIndex(previousTracingDirec);
                        xNext = current.x + directions[previousTracingDirec].x;
                        yNext = current.y + directions[previousTracingDirec].y;
                    }
                }
                current.move(directions[previousTracingDirec]);
            }
        }
        if (current.d > maxCost || clear || (current.x == goal.x && current.y == goal.y)) {
            tracingStarted = false;
            return true; //Reached a terminal tracing state
        }
        return false; //Clock ran out
    }


    private boolean traceAndAppend(VirtualBugLocation current, int x1, int y1, double maxCost, Vector<Point> appendee) {
        if (!tracingStarted) {
            Assert.Assert(myMap.groundPassableArrayCoords(current.x, current.y));

            int n_preferred = deadReckonIndex(current.x, current.y, x1, y1);
            int xNext = current.x + directions[n_preferred].x;
            int yNext = current.y + directions[n_preferred].y;
            Assert.Assert(!myMap.groundPassableArrayCoords(xNext, yNext));

            tracingDisengageDistance = estimateDistance(current.x, current.y, x1, y1);
            previousTracingDirec = n_preferred;

            if (current.rightWallFollow) {
                while (!myMap.groundPassableArrayCoords(xNext, yNext)) {
                    previousTracingDirec = rotateLeftIndex(previousTracingDirec);
                    xNext = current.x + directions[previousTracingDirec].x;
                    yNext = current.y + directions[previousTracingDirec].y;
                }
            } else {
                while (!myMap.groundPassableArrayCoords(xNext, yNext)) {
                    previousTracingDirec = rotateRightIndex(previousTracingDirec);
                    xNext = current.x + directions[previousTracingDirec].x;
                    yNext = current.y + directions[previousTracingDirec].y;
                }
            }
            tracingStarted = true;
        }


        boolean clear = false;
        int n_preferred, xNext, yNext;
        int bytecodeCutoff = GameConstants.BYTECODES_PER_ROUND - brains.getBytecodesReserved() - 250;
        if (current.d < maxCost && (current.x != x1 || current.y != y1)
                && Clock.getBytecodeNum() < bytecodeCutoff) {
            n_preferred = deadReckonIndex(current.x, current.y, x1, y1);
            xNext = current.x + directions[n_preferred].x;
            yNext = current.y + directions[n_preferred].y;
            if (myMap.groundPassableArrayCoords(xNext, yNext) && estimateDistance(current.x, current.y, x1, y1) <= tracingDisengageDistance) {
                clear = true;
            } else {
                previousTracingDirec = oppositeIndex(previousTracingDirec);
                int rotationCounter = 3;
                if (current.rightWallFollow) {
                    previousTracingDirec = rotateLeftIndex(previousTracingDirec);
                    xNext = current.x + directions[previousTracingDirec].x;
                    yNext = current.y + directions[previousTracingDirec].y;
                    while (!myMap.groundPassableArrayCoords(xNext, yNext)) {
                        previousTracingDirec = rotateLeftIndex(previousTracingDirec);
                        xNext = current.x + directions[previousTracingDirec].x;
                        yNext = current.y + directions[previousTracingDirec].y;
                        rotationCounter--;
                    }
                } else {
                    previousTracingDirec = rotateRightIndex(previousTracingDirec);
                    xNext = current.x + directions[previousTracingDirec].x;
                    yNext = current.y + directions[previousTracingDirec].y;
                    while (!myMap.groundPassableArrayCoords(xNext, yNext)) {
                        previousTracingDirec = rotateRightIndex(previousTracingDirec);
                        xNext = current.x + directions[previousTracingDirec].x;
                        yNext = current.y + directions[previousTracingDirec].y;
                        rotationCounter--;
                    }
                }
                if (rotationCounter != 0) {
                    Point temp = new Point(current.x, current.y);
                    appendee.add(new Point(current.x, current.y));
                }
                current.move(directions[previousTracingDirec]);
            }
        }
        if (current.d > maxCost || clear || (current.x == goal.x && current.y == goal.y)) {
            tracingStarted = false;
            return true; //Reached a terminal tracing state
        }
        return false; //Clock ran out
    }

    public int rotateRightIndex(int currentDirecIndex) {
        return (currentDirecIndex + RIGHT_ROTATE_OFFSET) % NUM_DIRECS;
    }

    public int rotateLeftIndex(int currentDirecIndex) {
        return (currentDirecIndex + LEFT_ROTATE_OFFSET) % NUM_DIRECS;
    }

    public int oppositeIndex(int currentDirecIndex) {
        return (currentDirecIndex + OPPOSITE_OFFSET) % NUM_DIRECS;
    }

    private boolean advanceToWall(VirtualBugLocation current, int x1, int y1) {
        Assert.Assert(myMap.groundPassableArrayCoords(current.x, current.y));
        int n_preferred = -1;
        if (myMap.groundPassableArrayCoords(current.x, current.y) && (current.x != x1 || current.y != y1)) {
            n_preferred = deadReckonIndex(current.x, current.y, x1, y1);
            current.move(directions[n_preferred]);
        }
        if (current.x == x1 && current.y == y1) {
            return true; //Reached goal; done advancing
        }
        if (!myMap.groundPassableArrayCoords(current.x, current.y)) {
            current.unMove(directions[n_preferred]);
            return true; //Reached wall; done advancing
        }
        return false; //Clock ran out
    }

    private void updatePathConstruction() {
        sparsePath = new Stack<VirtualBugLocation>();
        if (bestBug == null) {
            //This would mean that no bugs were used. This should never happen.
            debug_warn("Failed to create bugs before going to updatePathConstruction.");
            resetPath();
            return;
        }
        VirtualBugLocation temp = bestBug;
        sparsePath.push(temp);
        while (temp.parent != null) {
            temp = temp.parent;
            sparsePath.push(temp);
        }
        pathfindingStage = PATH_REFINEMENT_STAGE;
        currentStageInProgress = false;
    }

    private void updatePathRefinement() {
        if (!currentStageInProgress) {
            Assert.Assert(sparsePath != null);
            if (sparsePath.size() < 2) {
                if (sparsePath.size() == 0) {
                    debug_warn("Warning: empty path.");
                    pathfindingStage = WAYPOINT_SKIPPING_STAGE;
                    currentStageInProgress = false;
                }
                //Else, size is 1 - just the start!
                //This should really not happen...
                debug_warn("Warning: tangentBug probably failed.");
                pathfindingStage = WAYPOINT_SKIPPING_STAGE;
                currentStageInProgress = false;
                return;
            }
            currentBug = sparsePath.pop();
            //This method can add one extra copy of the first point
            if (myPath.size() > 0)
                myPath.remove(myPath.size() - 1);
            currentBug.rightWallFollow = sparsePath.peek().rightWallFollow;
            maxCost = Math.min(MAX_COST, 4*estimateDistance(currentBug.x, currentBug.y, goal.x, goal.y));
            currentStageInProgress = true;
        }
        boolean tracingDone = traceAndAppend(currentBug, goal.x, goal.y, maxCost, myPath);
        if (tracingDone) {
            currentBug = sparsePath.pop();
            if (!sparsePath.isEmpty())
                currentBug.rightWallFollow = sparsePath.peek().rightWallFollow;
            else {
                myPath.add(new Point(currentBug.x, currentBug.y));
                pathfindingStage = WAYPOINT_SKIPPING_STAGE;
                currentStageInProgress = false;
            }
        }
    }

    private void updateWaypointSkipping() {
        if (!currentStageInProgress) {
            if (rc.getRoundsUntilMovementIdle() <= 4)
                return;
            Point current = new Point(x0, y0);
            myPath.insertElementAt(current, 0);
            lastCertainWaypoint = 0;
            waypointToSkipTo = myPath.size() - 1;
            currentStageInProgress = true;
        }
        //Check one pair of indices...
        Point start = myPath.elementAt(lastCertainWaypoint);
        if (waypointToSkipTo >= myPath.size()) {
            waypointToSkipTo = myPath.size() - 1;
        }
        Point end = myPath.elementAt(waypointToSkipTo);
        boolean skippable = myMap.clearPath(start, end);
        --waypointToSkipTo;
        if (skippable) {
            while (waypointToSkipTo > lastCertainWaypoint) {
                myPath.remove(waypointToSkipTo);
                waypointToSkipTo--;
            }
        }
        if (lastCertainWaypoint + 2 > waypointToSkipTo) {
            lastCertainWaypoint++;
            if (lastCertainWaypoint > myPath.size() - 3) {
                pathfindingStage = FINISHED_STAGE;
                currentStageInProgress = false;
                return;
            }
            waypointToSkipTo = myPath.size() - 1;
        }
    }

    public double estimateDistance(int xI, int yI, int x1, int y1) {
        int deltaX = Math.abs(x1 - xI);
        int deltaY = Math.abs(y1 - yI);
        return deltaX + deltaY - DIAGONAL_COST_SAVING * Math.min(deltaX, deltaY);
    }

    public void debug_warn(String warningMessage) {
        System.out.println(warningMessage);
    }

    public void debug_printMyPath() {
        System.out.println();
        for (int i = 0; i < myPath.size(); i++) {
            Point temp = myPath.get(i);
            System.out.println(temp.x + ", " + temp.y);
        }
        System.out.println();
    }
}
