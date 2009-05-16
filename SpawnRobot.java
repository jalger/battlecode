package teamJA_ND;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.GameActionException;
import battlecode.common.*;

import java.util.List;
import java.util.LinkedList;
import java.util.Vector;

import teamJA_ND.comm.*;

public class SpawnRobot extends DefaultRobot {
    
    private Robot healingTarget;
    
    private int numRobotsSpawned;
    private boolean justSpawned;
    private static final double MIN_ENERGON_RESERVE = 10D;
    
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
    
    
    private List<Robot> children;
    
    
    public SpawnRobot(RobotController rcIn) {
        super(rcIn);
        healingTarget = null;
        numRobotsSpawned = 0;
        justSpawned = false;
        
        children = new LinkedList<Robot>();
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
                if (Clock.getRoundNum() == 500) {
                    myMap.debug_printMap();
                    senseRobots();
                    rc.yield();
                    debug_tick();
                    Message[] messages = rc.getAllMessages();
                    if (messages.length != 0) {
                        if (messages.length > 0) {
                            Message newMail = messages[messages.length - 1];
                            queuedMessages = new Vector<SubMessage>();
                            List<SubMessage> smL = MessageUtil.getRelevantSubMessages(newMail, kb);
                            if (smL != null) {
                                while (smL.size() != 0) {
                                    queuedMessages.add(smL.remove(0));
                                }
                            }
                        }
                        RobotInfoMessage relevant = (RobotInfoMessage) queuedMessages.get(0).getBody();
                        debug_tock();
                        System.out.println("Got message body from incoming message");
                        debug_tick();
                        kb.updateKBRobotInfo(relevant);
                        debug_tock();
                        System.out.println("Updated KB.");
                    }


                    while (true) {}
                }
                
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
                        healingTarget = child;              
                    }
                }
                
                // Don't heal anyone until you've spawned at least one unit
                if (children.size() > 0) {
                    
                    // Try to find robots to heal
                    if (healingTarget == null) {
                        //debug_tick();
                        // Check to see if any robots around you need energon
                        Robot[] groundUnits = rc.senseNearbyGroundRobots();
                    
                        healingTarget = getAdjacentFriendlyRobot(rc, groundUnits);
                        //healingTarget = getClosestDamagedFriendlyRobot(rc, groundUnits);
                        //healingTarget = getMostDamagedRobot(rc, groundUnits);
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
                }
                
                
                if (!rc.isMovementActive()) {
                    //System.out.println("Going to move now.");
                
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
    
    
    public static Robot getAdjacentFriendlyRobot(RobotController rc,
                                                Robot[] robots) 
                                                throws GameActionException 
    {
        MapLocation ml = rc.getLocation();
        for (Robot r : robots) {
           RobotInfo ri = rc.senseRobotInfo(r);
           
           // Adjacent
           if ( ml.isAdjacentTo(ri.location) &&
                // Friendly
                rc.getTeam() == ri.team &&
                // HACK: not archon
                ri.type != RobotType.ARCHON &&
                
                // Damaged
                getDamage(ri) > 0) {
               return r;
           }
        }
        return null;
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