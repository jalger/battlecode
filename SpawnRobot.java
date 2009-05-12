package teamJA_ND;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.*;


public class SpawnRobot extends DefaultRobot {
    
    public SpawnRobot(RobotController rcIn) {
        super(rcIn);
    }

    public void run() {
        System.out.println("I'm an Archon!");
        try {
            while (true) {
                // Check to see if square in front of you is free
                if (canSpawn(rc, RobotType.SOLDIER)) {
                    try {
                        rc.spawn(RobotType.SOLDIER);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (!rc.isMovementActive()) {
                
                    if(rc.canMove(rc.getDirection())) {
                       rc.moveForward();
                    }
                    else {
                       rc.setDirection(rc.getDirection().rotateRight());
                    }
                }
                rc.yield();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
       // super.run();
    }

    /**
    * @return true if spawning a robot of <code>RobotType</code> will
    * succeed
    **/
    public static boolean canSpawn(RobotController rc, RobotType t) {
        // Not in range of a tower
        if (!rc.canSpawn()) { return false; }
        
        // Don't have enough energon
        if (rc.getEnergonLevel() <= t.spawnCost()) {
            return false;
        }
        // If we can move forward, then we can spawn forward
        return rc.canMove(rc.getDirection());
    }
    
    
}