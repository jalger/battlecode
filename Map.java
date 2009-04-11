/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teamJA_ND;

/**
 *
 * @author J.J. Alger
 */
import battlecode.common.*;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.Vector;

import teamJA_ND.comm.*;
import teamJA_ND.state.*;
import teamJA_ND.util.*;

public class Map {

    //Pathfinding TODO:
    //0. Debug tangentBug pathfinding
    //1. Communications w/ other robots to update maps
    //2. Communications w/ other robots about priority
    //3. String pulling post-processing
    //4. Updating path when new information is found

    //Map TODO:
    //1. Note which cells are explored, unexplored, for easy transmission
    //2. Compress into message
    //3. Decompress from message
    //4. Quadtree-esque cell setup?
    public static final int ARRAY_WIDTH = 2 * GameConstants.MAP_MAX_WIDTH;
    public static final int ARRAY_HEIGHT = 2 * GameConstants.MAP_MAX_HEIGHT;
    public static final int BYTES_PER_ROUND = DefaultRobot.BYTES_PER_ROUND;
    public static final double SQRT2 = 1.414;
    public static final double DIAGONAL_COST_SAVING = 0.5858; //2 - sqrt(2)
    protected boolean[][] airOnly;
    protected boolean[][] explored;
    protected boolean[][] visited; //Used for pathfinding
    //protected AStarNode[][] pathfindingNodes;
    //PriorityQueue<AStarNode> aStarQueue; //AStar currently not run.
    protected int xMin,  xMax,  yMin,  yMax;
    protected int clockTurnNum,  clockByteNum;
    protected Point[] directions;
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

    //protected int[][] terrainHeights;
    //Uses bits independently. 0th bit corresponds to "is explored".
    //Bits between 1 and AIR_ONLY_BIT - 1 are for height if such is stored.
    //AIR_ONLY_BIT indicates whether the square is known to be traversible at ground level.
    //OUT_OF_BOUNDS_BIT indicates whether the square is known to be out-of-bounds.
    protected int dx,  dy;

    public Map(int xCenterIn, int yCenterIn) {
        airOnly = new boolean[ARRAY_WIDTH][ARRAY_HEIGHT];
        explored = new boolean[ARRAY_WIDTH][ARRAY_HEIGHT];
        //terrainHeights = new int[ARRAY_WIDTH][ARRAY_HEIGHT];
        //Want dx such that xCenterIn + dx is the middle of the array
        dx = GameConstants.MAP_MAX_WIDTH - 1 - xCenterIn;
        dy = GameConstants.MAP_MAX_HEIGHT - 1 - yCenterIn;
        /*pathfindingNodes = new AStarNode[ARRAY_WIDTH][ARRAY_HEIGHT];
        for (int x = 0; x < ARRAY_WIDTH; x++) {
        for (int y = 0; y < ARRAY_HEIGHT; y++) {
        pathfindingNodes[x][y] = new AStarNode(x,y);
        }
        }*/
        xMin = 0;
        for (int i = 0; i < ARRAY_WIDTH; i++) {
            airOnly[xMin][i] = true;
            explored[xMin][i] = true;
        }
        xMax = ARRAY_WIDTH - 1;
        for (int i = 0; i < ARRAY_WIDTH; i++) {
            airOnly[xMax][i] = true;
            explored[xMax][i] = true;
        }
        yMin = 0;
        for (int i = 0; i < ARRAY_WIDTH; i++) {
            airOnly[i][yMin] = true;
            explored[i][yMin] = true;
        }
        yMax = ARRAY_HEIGHT - 1;
        for (int i = 0; i < ARRAY_WIDTH; i++) {
            airOnly[i][yMax] = true;
            explored[i][yMax] = true;
        }

        //To facilitate easy addition of rotated directions in pathfinding
        directions = new Point[NUM_DIRECS];
        directions[N_N] = new Point(0, -1);
        directions[N_NW] = new Point(-1, -1);
        directions[N_W] = new Point(-1, 0);
        directions[N_SW] = new Point(-1, 1);
        directions[N_S] = new Point(0, 1);
        directions[N_SE] = new Point(1, 1);
        directions[N_E] = new Point(1, 0);
        directions[N_NE] = new Point(1, -1);
    }

    public boolean groundPassable(MapLocation query) {
        return (!airOnly[query.getX() + dx][query.getY() + dy]);
    }

    public boolean groundPassable(int x, int y) {
        x += dx;
        y += dy;
        return groundPassableArrayCoords(x, y);
    }

    public boolean groundPassableArrayCoords(int x, int y) {
        return !airOnly[x][y];
    }

    public boolean isExplored(MapLocation query) {
        return (explored[query.getX() + dx][query.getY() + dy]);
    }

    public boolean isExploredArrayCoords(int x, int y) {
        return explored[x][y];
    }

    public void setTerrain(boolean airOnlyIn, MapLocation loc) {
        int x = loc.getX() + dx;
        int y = loc.getY() + dy;
        airOnly[x][y] = airOnlyIn;
        explored[x][y] = true;
    }

    /*
     * Designed for use when filling in with friendly message;
     * assumes that x and y are in array coordinates.
     */
    public void setTerrain(int x, int y, boolean airOnlyIn) {
        airOnly[x][y] = airOnlyIn;
        explored[x][y] = true;
    }

    public void setXMin(int xMinNew) {
        xMinNew += dx;
        if (xMinNew > xMin) {
            xMin = xMinNew;
        }
        for (int i = 0; i < ARRAY_WIDTH; i++) {
            airOnly[xMin][i] = true;
            explored[xMin][i] = true;
        }
    }

    public void setXMax(int xMaxNew) {
        xMaxNew += dx;
        if (xMaxNew < xMax) {
            xMax = xMaxNew;
        }
        for (int i = 0; i < ARRAY_WIDTH; i++) {
            airOnly[xMax][i] = true;
            explored[xMax][i] = true;
        }
    }

    public void setYMin(int yMinNew) {
        yMinNew += dy;
        if (yMinNew > yMin) {
            yMin = yMinNew;
        }
        for (int i = 0; i < ARRAY_HEIGHT; i++) {
            airOnly[i][yMin] = true;
            explored[i][yMin] = true;
        }
    }

    public void setYMax(int yMaxNew) {
        yMaxNew += dy;
        if (yMaxNew < yMax) {
            yMax = yMaxNew;
        }
        for (int i = 0; i < ARRAY_HEIGHT; i++) {
            airOnly[i][yMax] = true;
            explored[i][yMax] = true;
        }
    }

    /*
    public AStarNode getNode(int x, int y) {
    return pathfindingNodes[x + dx][y + dy];
    }*/

    /* Currently A* is far too slow; not being used.
    public void aStarPathfind(MapLocation end, Path result) {
    int count = 0;
    debug_tick();
    AStarNode start = result.getLast();
    int x0 = start.x;
    int y0 = start.y;
    int x = x0;
    int y = y0;
    int x1 = end.getX() + dx;
    int y1 = end.getY() + dy;

    visited = new boolean[ARRAY_WIDTH][ARRAY_HEIGHT];

    double d = 0;
    double h = estimateDistance(x,y,x1,y1);
    AStarNode root = pathfindingNodes[x0][y0];
    root.d = d;
    root.score = d + h;
    root.parent = null;
    AStarNode current = root;
    AStarNode temp;
    boolean working = true;
    aStarQueue = new PriorityQueue<AStarNode>();
    aStarQueue.add(current);
    while (!aStarQueue.isEmpty() && working) {
    System.out.println("New node. " + (count++));
    current = aStarQueue.poll();
    x0 = current.x;
    y0 = current.y;
    visited[x0][y0] = true;
    if (x0 == x1 && y0 == y1) {
    working = false;
    } else {
    x = x0 - 1;
    y = y0 - 1;
    if (!visited[x][y]) {
    if (groundPassableArrayCoords(x, y)) {
    d = current.d + SQRT2;
    h = estimateDistance(x, y, x1, y1);
    temp = pathfindingNodes[x][y];
    temp.d = d;
    temp.score = d+h;
    temp.parent = current;
    aStarQueue.add(temp);
    }
    }
    x = x0 - 1;
    y = y0;
    if (!visited[x][y]) {
    if (groundPassableArrayCoords(x, y)) {
    d = current.d + 1;
    h = estimateDistance(x, y, x1, y1);
    temp = pathfindingNodes[x][y];
    temp.d = d;
    temp.score = d+h;
    temp.parent = current;
    aStarQueue.add(temp);
    }
    }
    x = x0 - 1;
    y = y0 + 1;
    if (!visited[x][y]) {
    if (groundPassableArrayCoords(x, y)) {
    d = current.d + SQRT2;
    h = estimateDistance(x, y, x1, y1);
    temp = pathfindingNodes[x][y];
    temp.d = d;
    temp.score = d+h;
    temp.parent = current;
    aStarQueue.add(temp);
    }
    }
    x = x0;
    y = y0 - 1;
    if (!visited[x][y]) {
    if (groundPassableArrayCoords(x, y)) {
    d = current.d + 1;
    h = estimateDistance(x, y, x1, y1);
    temp = pathfindingNodes[x][y];
    temp.d = d;
    temp.score = d+h;
    temp.parent = current;
    aStarQueue.add(temp);
    }
    }
    x = x0;
    y = y0 + 1;
    if (!visited[x][y]) {
    if (groundPassableArrayCoords(x, y)) {
    d = current.d + 1;
    h = estimateDistance(x, y, x1, y1);
    temp = pathfindingNodes[x][y];
    temp.d = d;
    temp.score = d+h;
    temp.parent = current;
    aStarQueue.add(temp);
    }
    }
    x = x0 + 1;
    y = y0 - 1;
    if (!visited[x][y]) {
    if (groundPassableArrayCoords(x, y)) {
    d = current.d + SQRT2;
    h = estimateDistance(x, y, x1, y1);
    temp = pathfindingNodes[x][y];
    temp.d = d;
    temp.score = d+h;
    temp.parent = current;
    aStarQueue.add(temp);
    }
    }
    x = x0 + 1;
    y = y0;
    if (!visited[x][y]) {
    if (groundPassableArrayCoords(x, y)) {
    d = current.d + 1;
    h = estimateDistance(x, y, x1, y1);
    temp = pathfindingNodes[x][y];
    temp.d = d;
    temp.score = d+h;
    temp.parent = current;
    aStarQueue.add(temp);
    }
    }
    x = x0 + 1;
    y = y0 + 1;
    if (!visited[x][y]) {
    if (groundPassableArrayCoords(x, y)) {
    d = current.d + SQRT2;
    h = estimateDistance(x, y, x1, y1);
    temp = pathfindingNodes[x][y];
    temp.d = d;
    temp.score = d+h;
    temp.parent = current;
    aStarQueue.add(temp);
    }
    }
    }
    }


    result = new Path(current);
    while (current.parent != null) {
    result.insertAtStart(current.parent);
    current = current.parent;
    }
    System.out.println("Path built!");
    debug_tock();
    }
     */
    public Stack<VirtualBugLocation> tangentBug(MapLocation start, MapLocation end, double maxCost, Stack<VirtualBugLocation> result) {
        result = new Stack<VirtualBugLocation>();

        //Check if the bug starts at the goal
        if (start.equals(end)) {
            result.add(new VirtualBugLocation(end.getX() + dx, end.getY() + dy, 0));
            return result;
        }

        //Get goal array coordinates
        int x1 = end.getX() + dx;
        int y1 = end.getY() + dy;

        //Advance from starting position either to wall, or to goal
        VirtualBugLocation root = new VirtualBugLocation(start.getX() + dx, start.getY() + dy, 0);
        VirtualBugLocation current = new VirtualBugLocation(root);
        boolean hitWall = advanceToWall(current, x1, y1);
        if (!hitWall) {
            result.add(current);
            return result;
        }

        //On hitting a wall, add right-following and left-following virtual bugs to PQ
        PriorityQueue<VirtualBugLocation> virtualBugs = new PriorityQueue<VirtualBugLocation>();
        double heuristic = estimateDistance(current.x, current.y, x1, y1);
        virtualBugs.add(new VirtualBugLocation(current).setRightWallFollow(true).setDistanceRemainingEstimate(heuristic));
        virtualBugs.add(new VirtualBugLocation(current).setRightWallFollow(false).setDistanceRemainingEstimate(heuristic));

        VirtualBugLocation temp;
        VirtualBugLocation best = null;
        int counter = 0;
        while (!virtualBugs.isEmpty() && virtualBugs.peek().d < maxCost) {
            temp = virtualBugs.poll();
            boolean clear = traceAroundObstacle(temp, x1, y1, maxCost);
            if (clear) {
                hitWall = advanceToWall(temp, x1, y1);
                if (temp.d < maxCost) {
                    if (!hitWall) {
                        maxCost = temp.d;
                        best = temp;
                    } else {
                        virtualBugs.add(new VirtualBugLocation(temp).setRightWallFollow(true));
                        virtualBugs.add(new VirtualBugLocation(temp).setRightWallFollow(false));
                    }
                }
            }
        }
        if (best != null) {
            temp = best;
            result.add(temp);
            while (temp.parent != null) {
                temp = temp.parent;
                result.add(temp);
            }
        }
        return result;
    }

    public void debug_printLocations(Stack<VirtualBugLocation> toPrint) {
        VirtualBugLocation current;
        Stack<VirtualBugLocation> backup = new Stack<VirtualBugLocation>();
        while (!toPrint.empty()) {
            current = toPrint.pop();
            backup.push(current);
            System.out.println(current.x + ", " + current.y + ", " + current.d + ", " + current.rightWallFollow);
        }
        System.out.println();
        while (!backup.empty()) {
            toPrint.push(backup.pop());
        }
        debug_printMap();
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

    public boolean traceAroundObstacle(VirtualBugLocation current, int x1, int y1, double maxCost) {
        Assert.Assert(groundPassableArrayCoords(current.x, current.y));

        if (current.x == x1 && current.y == y1) {
            return true;
        }

        int n_preferred = deadReckonIndex(current.x, current.y, x1, y1);
        int xNext = current.x + directions[n_preferred].x;
        int yNext = current.y + directions[n_preferred].y;
        Assert.Assert(!groundPassableArrayCoords(xNext, yNext));

        double minDistance = estimateDistance(current.x, current.y, x1, y1);
        int direc = n_preferred;
        if (current.rightWallFollow) {
            while (!groundPassableArrayCoords(xNext, yNext)) {
                direc = rotateLeftIndex(direc);
                xNext = current.x + directions[direc].x;
                yNext = current.y + directions[direc].y;
            }
        } else {
            while (!groundPassableArrayCoords(xNext, yNext)) {
                direc = rotateRightIndex(direc);
                xNext = current.x + directions[direc].x;
                yNext = current.y + directions[direc].y;
            }
        }

        boolean clear = false;
        while (current.d < maxCost && (current.x != x1 || current.y != y1) && !clear) {
            n_preferred = deadReckonIndex(current.x, current.y, x1, y1);
            xNext = current.x + directions[n_preferred].x;
            yNext = current.y + directions[n_preferred].y;
            if (groundPassableArrayCoords(xNext, yNext) && estimateDistance(current.x, current.y, x1, y1) <= minDistance) {
                clear = true;
            } else {
                direc = oppositeIndex(direc);
                if (current.rightWallFollow) {
                    direc = rotateLeftIndex(direc);
                    xNext = current.x + directions[direc].x;
                    yNext = current.y + directions[direc].y;
                    while (!groundPassableArrayCoords(xNext, yNext)) {
                        direc = rotateLeftIndex(direc);
                        xNext = current.x + directions[direc].x;
                        yNext = current.y + directions[direc].y;
                    }
                } else {
                    direc = rotateRightIndex(direc);
                    xNext = current.x + directions[direc].x;
                    yNext = current.y + directions[direc].y;
                    while (!groundPassableArrayCoords(xNext, yNext)) {
                        direc = rotateRightIndex(direc);
                        xNext = current.x + directions[direc].x;
                        yNext = current.y + directions[direc].y;
                    }
                }
                current.move(directions[direc]);
            }
        }
        if (current.d >= maxCost) {
            return false;
        }
        return true;
    }

    public boolean advanceToWall(VirtualBugLocation current, int x1, int y1) {
        Assert.Assert(groundPassableArrayCoords(current.x, current.y));

        int n_preferred = -1;
        while (groundPassableArrayCoords(current.x, current.y) && (current.x != x1 || current.y != y1)) {
            n_preferred = deadReckonIndex(current.x, current.y, x1, y1);
            current.move(directions[n_preferred]);
        }
        if (current.x == x1 && current.y == y1) {
            return false;
        }
        current.unMove(directions[n_preferred]);
        return true;
    }

    public Vector<Point> buildPath(Stack<VirtualBugLocation> waypoints) {
        Assert.Assert(waypoints != null);
        Vector<Point> result = new Vector<Point>();
        if (waypoints.size() < 2) {
            if (waypoints.size() == 0) {
                return result;
            }
            //Else, size is 1 - just the start!
            VirtualBugLocation temp = waypoints.pop();
            result.add(new Point(temp.x, temp.y));
            return result;
        }

        //TODO: Refactor so this actually treats the Stack like a stack,
        //or it uses an actual Vector. This is a temporary fix.
        VirtualBugLocation end = waypoints.firstElement();
        int x1 = end.x;
        int y1 = end.y;
        VirtualBugLocation current = waypoints.pop();
        int x = current.x;
        int y = current.y;
        result.add(new Point(x, y)); //Add starting point
        current = waypoints.pop();
        x = current.x;
        y = current.y;
        result.add(new Point(x, y));
        VirtualBugLocation next;
        int n_preferred;
        int direc;
        int xNext, yNext;
        boolean clear;
        while (!waypoints.empty()) {
            //Set next
            next = waypoints.pop();
            //Fill in points between current and next
            clear = false;
            current.rightWallFollow = next.rightWallFollow;
            n_preferred = deadReckonIndex(current.x, current.y, x1, y1);
            direc = n_preferred;
            xNext = current.x + directions[direc].x;
            yNext = current.y + directions[direc].y;
            if (current.rightWallFollow) {
                while (!groundPassableArrayCoords(xNext, yNext)) {
                    direc = rotateLeftIndex(direc);
                    xNext = current.x + directions[direc].x;
                    yNext = current.y + directions[direc].y;
                }
            } else {
                while (!groundPassableArrayCoords(xNext, yNext)) {
                    direc = rotateRightIndex(direc);
                    xNext = current.x + directions[direc].x;
                    yNext = current.y + directions[direc].y;
                }
            }

            double minDistance = estimateDistance(current.x, current.y, x1, y1);

            while ((current.x != next.x || current.y != next.y) && !clear) {
                n_preferred = deadReckonIndex(current.x, current.y, x1, y1);
                xNext = current.x + directions[n_preferred].x;
                yNext = current.y + directions[n_preferred].y;
                if (groundPassableArrayCoords(xNext, yNext) && estimateDistance(current.x, current.y, x1, y1) <= minDistance) {
                    clear = true;
                    result.add(new Point(current.x, current.y));
                } else {
                    int rotationsCounter = OPPOSITE_OFFSET;
                    direc = oppositeIndex(direc);
                    if (current.rightWallFollow) {
                        direc = rotateLeftIndex(direc);
                        rotationsCounter--;
                        xNext = current.x + directions[direc].x;
                        yNext = current.y + directions[direc].y;
                        while (!groundPassableArrayCoords(xNext, yNext)) {
                            rotationsCounter--;
                            direc = rotateLeftIndex(direc);
                            xNext = current.x + directions[direc].x;
                            yNext = current.y + directions[direc].y;
                        }
                    } else {
                        direc = rotateRightIndex(direc);
                        rotationsCounter--;
                        xNext = current.x + directions[direc].x;
                        yNext = current.y + directions[direc].y;
                        while (!groundPassableArrayCoords(xNext, yNext)) {
                            rotationsCounter--;
                            direc = rotateRightIndex(direc);
                            xNext = current.x + directions[direc].x;
                            yNext = current.y + directions[direc].y;
                        }
                    }
                    if (rotationsCounter != 0) {
                        result.add(new Point(current.x, current.y));
                    }
                    current.move(directions[direc]);
                }
            }

            //Set current
            current = new VirtualBugLocation(next);
            result.add(new Point(current.x, current.y));
        }
        return result;
    }

    public Vector<Point> trimPath(Vector<Point> path) {
        Assert.Assert(path != null);
        Point start, end;
        for (int i = 0; i < path.size() - 2; i++) {
            for (int j = path.size() - 1; j > i + 1; j--) {
                start = path.elementAt(i);
                end = path.elementAt(j);
                if (clearPath(start, end)) {
                    while (j > i + 1) {
                        j--;
                        path.remove(j);
                    }
                }
            }
        }
        return path;
    }

    public boolean clearPath(Point start, Point end) {
        int x = start.x;
        int y = start.y;
        int x1 = end.x;
        int y1 = end.y;
        int n_preferred;
        while (groundPassableArrayCoords(x, y) && (x != x1 || y != y1)) {
            n_preferred = deadReckonIndex(x, y, x1, y1);
            x += directions[n_preferred].x;
            y += directions[n_preferred].y;
        }
        if (groundPassableArrayCoords(x, y)) {
            return true;
        }
        return false;
    }

    public int deadReckonIndex(int x0, int y0, int x1, int y1) {
        int deltaX = x1 - x0;
        int deltaY = y1 - y0;

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

    public double estimateDistance(int x0, int y0, int x1, int y1) {
        int deltaX = Math.abs(x1 - x0);
        int deltaY = Math.abs(y1 - y0);
        return deltaX + deltaY - DIAGONAL_COST_SAVING * Math.min(deltaX, deltaY);
    }

    public int getdx() {
        return dx;
    }

    public int getdy() {
        return dy;
    }

    public void debug_tick() {
        clockTurnNum = Clock.getRoundNum();
        clockByteNum = Clock.getBytecodeNum();
    }

    public void debug_tock() {
        int turnFinal = Clock.getRoundNum();
        int bytesFinal = Clock.getBytecodeNum() - 1; //The -1 accounts for the cost of calling debug_tock().
        int delta = bytesFinal - clockByteNum + BYTES_PER_ROUND * (turnFinal - clockTurnNum);
        System.out.println(delta + " bytecodes used since calling debug_tick().");
    }

    public void debug_printMap() {
        String result = "";
        int squareVal;
        for (int y = 0; y < ARRAY_HEIGHT; y++) {
            for (int x = 45; x < ARRAY_WIDTH - 5; x++) {
                squareVal = explored[x][y] ? 1 : 0;
                squareVal += airOnly[x][y] ? 2 : 0;
                result += squareVal;
            }
            System.out.println(result);
            result = "";
        }
        System.out.println("\n\n");
    }
}
