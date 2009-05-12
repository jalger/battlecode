package teamJA_ND;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.GameActionException;
import battlecode.common.*;


public class SpawnRobot extends DefaultRobot {
    
    private Robot healingTarget;
    
    private static final double MIN_ENERGON_RESERVE = 10D;
    
    public SpawnRobot(RobotController rcIn) {
        super(rcIn);
        healingTarget = null;
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
                
                // Try to find robots to heal
                if (healingTarget == null) {
                    // Check to see if any robots around you need energon
                    Robot[] groundUnits = rc.senseNearbyGroundRobots();
                    healingTarget = getClosestDamagedFriendlyRobot(rc, groundUnits);
                
                }
                
                // We have a target we're trying to heal
                if (healingTarget != null) {
                    
                    
                    System.out.println("Attempting to heal " + healingTarget);
                    
                    // TODO: We need to ensure that our target does not move
                    // out of range from the time we choose it, and then
                    // try to sense it.  Otherwise we'll get an exception
                    
                    
                    // Find out information about the robot we're going to
                    // heal
                    RobotInfo healingTargetInfo = rc.senseRobotInfo(healingTarget);
                    
                    MapLocation curLoc = rc.getLocation();
                    MapLocation healingTargetLoc = healingTargetInfo.location;
                    
                    // We can transfer the energon
                    if (curLoc.equals(healingTargetLoc) || 
                        curLoc.isAdjacentTo(healingTargetLoc)) {
                        
                        double energonAmt = Math.min(rc.getEnergonLevel() - 
                                                    MIN_ENERGON_RESERVE, 
                                                    getDamage(healingTargetInfo));
                                                    
                        if (energonAmt < 0) {
                            energonAmt = 0;
                        }                            
                        rc.transferEnergon( energonAmt, 
                                            healingTargetLoc,
                                            healingTarget.getRobotLevel()); 
                        
                        // Ensure we don't try to do two actions this turn
                        healingTarget = null;
                        rc.yield();
                    }
                    
                    // We need to move towards the robot
                    else {
                        System.out.println("I am at " + curLoc + " and need to move to reach " + healingTargetLoc);
                        
                        //moveTo(healingTargetLoc);
                        continue;
                    }
                    
                    
                }
                
                
                
                    // Else, move in their direction
                
                // Else if you can spawn a unit, do so
                
                // Check to see if square in front of you is free
                else if (canSpawn(rc, RobotType.SOLDIER)) {
                    try {
                        rc.spawn(RobotType.SOLDIER);
//                        justSpawned = true;
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
    
    
    /**
    * @return the Robot which has taken the most damage in absolute
    * terms 
    **/
    public static Robot getMostDamagedRobot(RobotController rc, 
                                            Robot[] groundRobots)
                                            throws GameActionException 
    {
        // There are no robots around                                        
        if (groundRobots.length == 0) { return null; }


        // Pick initial robot
        Robot mostDamaged = groundRobots[0];
        double maxDamage = getDamage(rc.senseRobotInfo(mostDamaged));
        
        // Iterate and find those that are more damaged
        for (Robot r : groundRobots) {
            double newDamage = getDamage(rc.senseRobotInfo(r));
            if (newDamage > maxDamage) {
                mostDamaged = r;
                maxDamage = newDamage;
            }
        }
        return mostDamaged;
    }
    
    
    /**
    * @return the Robot which is damaged and closest to calling robot
    **/
    public static Robot getClosestDamagedFriendlyRobot(RobotController rc,
                                                Robot[] groundRobots) 
                                                throws GameActionException
    {
        // There are no robots around                                        
        if (groundRobots.length == 0) { return null; }
        
        MapLocation curLocation = rc.getLocation();
        
        // Pick initial robot
        Robot closestDamaged = null;
        int minDistance = Integer.MAX_VALUE;
                
        // Iterate and find those that are closer
        for (Robot r : groundRobots) {
            
            RobotInfo info = rc.senseRobotInfo(r);
            
            // Only want robots on our team
            if (info.team != rc.getTeam()) { continue; }
            
            
            // Only want damaged robots
            if (getDamage(info) == 0) { continue; }
            
            // HACK: don't heal archons
            if (info.type == RobotType.ARCHON) { continue; }
            
            
            
            int newDistance = info.location.distanceSquaredTo(curLocation);
            
            if (newDistance <= minDistance) {
                closestDamaged = r;
                minDistance = newDistance;
            }
        }
        
        
        return closestDamaged;
        
    }
    
    public static double getDamage(RobotInfo r) {
        return r.maxEnergon - r.eventualEnergon;
    }
    
}