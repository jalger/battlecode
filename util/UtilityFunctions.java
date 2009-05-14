package teamJA_ND;

import battlecode.common.*;


/**
* @author Nicholas Dunn
* @date   May 13, 2009
**/
class UtilityFunctions {
    
    public static int getNumNonArchons(RobotController rc) {
        return rc.getTeamUnitCount() - rc.getUnitCount(RobotType.ARCHON);
    }

    
}