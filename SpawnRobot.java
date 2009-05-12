package teamJA_ND;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.*;


public class SpawnRobot extends DefaultRobot {
    
    private boolean justSpawned;
    
    
    public SpawnRobot(RobotController rcIn) {
        super(rcIn);
        justSpawned = false;
    }

    public void run() {
        System.out.println("I'm an Archon!");
        try {
            while (true) {
                
                if (justSpawned) {
                    MapLocation oneSquareForward = rc.getLocation().add(rc.getDirection());
                    Robot spawned = rc.senseGroundRobotAtLocation(oneSquareForward);
                    if (spawned != null) {
                        RobotInfo spawnedInfo = rc.senseRobotInfo(spawned);
                        
                        // It's not fully healed
                        if (spawnedInfo.eventualEnergon < spawnedInfo.maxEnergon) {
                            double amountNeeded = spawnedInfo.maxEnergon - spawnedInfo.energonLevel;
                            // Don't kill yourself to heal the guy
                            double maxToTransfer = Math.min(rc.getEnergonLevel(), amountNeeded);
                            
                            rc.transferEnergon(maxToTransfer,
                                                oneSquareForward,
                                                RobotLevel.ON_GROUND);
                        }
                    }
                }
                
                // Check to see if any robots around you need energon
                
                
                    // If they do, and they're adjacent, heal them
                
                    // Else, move in their direction
                
                // Else if you can spawn a unit, do so
                
                // Check to see if square in front of you is free
                else if (canSpawn(rc, RobotType.SOLDIER)) {
                    try {
                        rc.spawn(RobotType.SOLDIER);
                        justSpawned = true;
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
    * Checks whether creating the requested robot will succeed.
    *
    * Assumes that you're not being stupid, e.g. you are not calling
    * this with a non-archon, you are not trying to spawn an evolved
    * unit type, and you haven't already queued an action this round.
    * @return true if spawning a robot of <code>RobotType</code> will
    * succeed (subject to limitations outlined above)
    *
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