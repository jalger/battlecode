package teamJA_ND;

import java.util.Comparator;
import battlecode.common.MapLocation;
import battlecode.common.RobotInfo;
import battlecode.common.RobotController;



public class HealthComparator implements Comparator<RobotInfo> {
    
    /**
    * Compare the amount of health each robot has
    **/
    public int compare(RobotInfo r1, RobotInfo r2) {
        double proportionOfHealth1 = SpawnRobot.getProportionHealth(r1);
        double proportionOfHealth2 = SpawnRobot.getProportionHealth(r2);
        if (proportionOfHealth1 == proportionOfHealth2) {
            return 0;
        }
        else if (proportionOfHealth1 < proportionOfHealth2) {
            return -1;
        }
        else {
            return 1;
        }
    }
    
    

}
