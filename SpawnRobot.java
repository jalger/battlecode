package teamJA_ND;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.GameActionException;
import battlecode.common.*;
import teamJA_ND.util.UtilityFunctions;

import java.util.List;
import java.util.LinkedList;
import java.util.Comparator;

public class SpawnRobot extends DefaultRobot {
    
    private int numRobotsSpawned;
    private boolean justSpawned;
    
    private static final double MAX_ENERGON = RobotType.ARCHON.maxEnergon();
    
    private static final int MAX_NUM_SOLDIERS_TO_SPAWN = 2;
    
    private static final Direction[] DIRECTIONS = new Direction[] {
        Direction.NORTH,
        Direction.NORTH_EAST,
        Direction.EAST,
        Direction.SOUTH_EAST,
        Direction.SOUTH,
        Direction.SOUTH_WEST,
        Direction.WEST,
        Direction.NORTH_WEST,
        Direction.NONE
    };
    
    public static final boolean DEBUG = true;
    
    private List<Robot> children;
    
    private DistanceComparator distanceComp;
    private ClosestDamagedComparator closestDamagedComp;
    
    
    
     
    
    
    public SpawnRobot(RobotController rcIn) {
        super(rcIn);
        numRobotsSpawned = 0;
        justSpawned = false;
        children = new LinkedList<Robot>();
        
        distanceComp = new DistanceComparator(rc);
        
        closestDamagedComp = new ClosestDamagedComparator(rc);
    }

    public void run() {
        
        
        // If there are any units in sensor range that need energon
            // Pick one (closest, most damaged), move towards it, and 
            // transfer the max energon you can
        
        // Else if you are not up to your max number of units
            // If you can spawn the unit you're short on, do so
            
            // else
            
        // Else 
            // Exit state
        
        
        
        System.out.println("I'm an Archon!");
        try {
            while (true) {
                // Get information about the units around us
                startTurn();
                if (children.size() < MAX_NUM_SOLDIERS_TO_SPAWN &&
                    canSpawn(rc, RobotType.SOLDIER)) {
  
                    rc.spawn(RobotType.SOLDIER);
                    justSpawned = true;
                    rc.yield();
                }
                // Unit should appear in front of me
                if (justSpawned) {
                    Robot child = rc.senseGroundRobotAtLocation(rc.getLocation().add(rc.getDirection()));
                    
                    if (child != null) {
                        System.out.println("I just spawned, an a robot: " + 
                                            child + " just popped out.  " +
                                            "Adding to my children list.");
                        children.add(child);
                        justSpawned = false;      
                    }
                }
                
                // Don't heal anyone until you've spawned at least one unit
                if (children.size() > 0) {
                    heal();
                }
                
                if (!rc.isMovementActive()) {
                    System.out.println("Going to move now.");
                
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