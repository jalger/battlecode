/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package teamJA_ND;

/**
 *
 * @author J.J. Alger
 */

public class VirtualBugLocation implements Comparable<VirtualBugLocation>{

    public int x, y; //Please don't abuse the public int. Needs to be public
                     //for very-fast access in some time-sensitive operations
                     //in other packages.
    public double d;
    public double scoreEstimate;
    public VirtualBugLocation parent;
    public boolean rightWallFollow;
    public static final double SQRT2 = 1.414;

    public VirtualBugLocation(int xIn, int yIn, double dIn) {
        x = xIn;
        y = yIn;
        d = dIn;
        scoreEstimate = Integer.MAX_VALUE;
        parent = null;
    }

    public VirtualBugLocation(VirtualBugLocation parentIn) {
        parent = parentIn;
        x = parent.x;
        y = parent.y;
        d = parent.d;
        scoreEstimate = Integer.MAX_VALUE;
    }

    public void move(Point direc) {
        x += direc.x;
        y += direc.y;
        if (x != 0)
            if (y != 0)
                d += SQRT2;
            else
                d++;
        else
            d++;
    }

    public void unMove(Point direc) {
        x -= direc.x;
        y -= direc.y;
        if (x != 0)
            if (y != 0)
                d -= SQRT2;
            else
                d--;
        else
            d--;
    }

    public VirtualBugLocation setDistanceRemainingEstimate(double estimate) {
        scoreEstimate = d + estimate;
        return this;
    }

    public VirtualBugLocation setRightWallFollow(boolean rWallSet) {
        rightWallFollow = rWallSet;
        return this;
    }

    public int compareTo(VirtualBugLocation other) {
        return (int)(scoreEstimate - other.scoreEstimate);
    }

}
