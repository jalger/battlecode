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
public class AStarMapLocation implements Comparable<AStarMapLocation>{

    protected MapLocation loc;
    protected double h;
    protected double d;
    protected AStarMapLocation parent;

    public AStarMapLocation(MapLocation location, double dIn, double hIn) {
        loc = location;
        h = hIn;
        d = dIn;
    }

    public AStarMapLocation(MapLocation location, double dIn, double hIn, AStarMapLocation parentIn) {
        loc = location;
        h = hIn;
        d = dIn;
        parent = parentIn;
    }

    public int compareTo(AStarMapLocation other) {
        return (int)(getEstimate() - other.getEstimate());
    }

    public double getEstimate() {
        return h + d;
    }

    public MapLocation getLocation() {
        return loc;
    }

    public double getDistance() {
        return d;
    }

    public AStarMapLocation getParent() {
        return parent;
    }
}
