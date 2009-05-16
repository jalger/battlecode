package teamJA_ND;

import java.util.Comparator;
import battlecode.common.MapLocation;
import battlecode.common.RobotInfo;
import battlecode.common.RobotController;


public class DistanceComparator implements Comparator<RobotInfo> {
    
    private RobotController rc;
    
    public DistanceComparator(RobotController rc) {
        this.rc = rc;
    }
    

    public int compare(RobotInfo r1, RobotInfo r2) {
        MapLocation curLoc = rc.getLocation();
        return r1.location.distanceSquaredTo(curLoc) - r2.location.distanceSquaredTo(curLoc);
    }
}
