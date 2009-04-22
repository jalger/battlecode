/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teamJA_ND;

/**
 *
 * @author J.J. Alger, Nick Dunn
 */
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Archon extends DefaultRobot {

    //Point arrays for updating map after moving
    public static final Point[] NEW_VIS_S = {new Point(-6, 0), new Point(-5, 3),
        new Point(-4, 4), new Point(-3, 5),
        new Point(-2, 5), new Point(-1, 5),
        new Point(0, 6), new Point(1, 5),
        new Point(2, 5), new Point(3, 5),
        new Point(4, 4), new Point(5, 3),
        new Point(6, 0)};
    public static final Point[] NEW_VIS_N = {new Point(-6, 0), new Point(-5, -3),
        new Point(-4, -4), new Point(-3, -5),
        new Point(-2, -5), new Point(-1, -5),
        new Point(0, -6), new Point(1, -5),
        new Point(2, -5), new Point(3, -5),
        new Point(4, -4), new Point(5, -3),
        new Point(6, 0)};
    public static final Point[] NEW_VIS_W = {new Point(0, -6), new Point(-3, -5),
        new Point(-4, -4), new Point(-5, -3),
        new Point(-5, -2), new Point(-5, -1),
        new Point(-6, 0), new Point(-5, 1),
        new Point(-5, 2), new Point(-5, 3),
        new Point(-4, 4), new Point(-3, 5),
        new Point(0, 6)};
    public static final Point[] NEW_VIS_E = {new Point(0, -6), new Point(3, -5),
        new Point(4, -4), new Point(5, -3),
        new Point(5, -2), new Point(5, -1),
        new Point(6, 0), new Point(5, 1),
        new Point(5, 2), new Point(5, 3),
        new Point(4, 4), new Point(3, 5),
        new Point(0, 6)};
    public static final Point[] NEW_VIS_NW = {new Point(3, -5), new Point(2, -5),
        new Point(0, -6), new Point(0, -5),
        new Point(-1, -5), new Point(-2, -5),
        new Point(-3, -5), new Point(-3, -4),
        new Point(-4, -4), new Point(-4, -3),
        new Point(-5, -3), new Point(-5, -2),
        new Point(-5, -1), new Point(-5, 0),
        new Point(-6, 0), new Point(-5, 2),
        new Point(-5, 3)};
    public static final Point[] NEW_VIS_SW = {new Point(3, 5), new Point(2, 5),
        new Point(0, 6), new Point(0, 5),
        new Point(-1, 5), new Point(-2, 5),
        new Point(-3, 5), new Point(-3, 4),
        new Point(-4, 4), new Point(-4, 3),
        new Point(-5, 3), new Point(-5, 2),
        new Point(-5, 1), new Point(-5, 0),
        new Point(-6, 0), new Point(-5, -2),
        new Point(-5, -3)};
    public static final Point[] NEW_VIS_NE = {new Point(-3, -5), new Point(-2, -5),
        new Point(0, -6), new Point(0, -5),
        new Point(1, -5), new Point(2, -5),
        new Point(3, -5), new Point(3, -4),
        new Point(4, -4), new Point(4, -3),
        new Point(5, -3), new Point(5, -2),
        new Point(5, -1), new Point(5, 0),
        new Point(6, 0), new Point(5, 2),
        new Point(5, 3)};
    public static final Point[] NEW_VIS_SE = {new Point(-3, 5), new Point(-2, 5),
        new Point(0, 6), new Point(0, 5),
        new Point(1, 5), new Point(2, 5),
        new Point(3, 5), new Point(3, 4),
        new Point(4, 4), new Point(4, 3),
        new Point(5, 3), new Point(5, 2),
        new Point(5, 1), new Point(5, 0),
        new Point(6, 0), new Point(5, -2),
        new Point(5, -3)};


    protected int mapIndex;
    protected int archonNumber;

    public Archon(RobotController rcIn) {
        super(rcIn);
    }

    public void run() {
        System.out.println("I'm an Archon!");
        MapLocation[] towers = rc.senseAlliedTowers();
        archonNumber = 1;
        System.out.println(towers[0].getX() + ", " + towers[0].getY());
        super.run();
    }

    protected void endTurn() {
        int oldX = super.x;
        int oldY = super.y;
        super.endTurn();
        if (super.x != oldX || super.y != oldY) {
            updateMap(oldX, oldY, super.x, super.y);
        }
    }

    protected void moveForward() {
        int x0 = rc.getLocation().getX();
        int y0 = rc.getLocation().getY();
        try {
            rc.moveForward();
        } catch (Exception e) {}
        rc.yield();
        updateMap(x0, y0, rc.getLocation().getX(), rc.getLocation().getY());
    }

    /*
     * Updates all squares on the map visible at x1,y1 but not visible at x0,y0.
     */
    protected void updateMap(int x0, int y0, int x1, int y1) {
        Point[] newVis;
        if (x1 == x0) {
            if (y1 > y0) {
                newVis = NEW_VIS_S;
            } else if (y1 < y0) {
                newVis = NEW_VIS_N;
            } else {
                newVis = null;
            }
        } else if (x1 > x0) {
            if (y1 > y0) {
                newVis = NEW_VIS_SE;
            } else if (y1 < y0) {
                newVis = NEW_VIS_NE;
            } else {
                newVis = NEW_VIS_E;
            }
        } else {
            if (y1 > y0) {
                newVis = NEW_VIS_SW;
            } else if (y1 < y0) {
                newVis = NEW_VIS_NW;
            } else {
                newVis = NEW_VIS_W;
            }
        }
        int newX, newY;
        for (int i = 0; i < newVis.length; i++) {
            newX = x1 + newVis[i].x;
            newY = y1 + newVis[i].y;
            updateMapSquare(new MapLocation(newX, newY));
        }
    }
}
