package teamJA_ND;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.GameActionException;
import battlecode.common.*;
import teamJA_ND.util.UtilityFunctions;

import java.util.List;
import java.util.LinkedList;

public class SpawnRobot extends DefaultRobot {
    
    private Robot healingTarget;
    
    private int numRobotsSpawned;
    private boolean justSpawned;
    private static final double MIN_ENERGON_RESERVE = 10.0;
    // Don't transfer energon if you have less than 40% life
    private static final double MIN_PROPORTION_ENERGON_TO_HEAL = 0.4;
    
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
    
    
    public SpawnRobot(RobotController rcIn) {
        super(rcIn);
        healingTarget = null;
        numRobotsSpawned = 0;
        justSpawned = false;
        children = new LinkedList<Robot>();
        
        distanceComp = new DistanceComparator(rc);
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
                        healingTarget = child;              
                    }
                }
                
                
                
                // Don't heal anyone until you've spawned at least one unit
                if (children.size() > 0) {
                
                    heal();
                }
                    // // Try to find robots to heal
                    //                     if (healingTarget == null) {
                    //                         //debug_tick();
                    //                         // Check to see if any robots around you need energon
                    //                         Robot[] groundUnits = rc.senseNearbyGroundRobots();
                    //                     
                    //                         healingTarget = getAdjacentFriendlyRobot(rc, groundUnits);
                    //                         //healingTarget = getClosestDamagedFriendlyRobot(rc, groundUnits);
                    //                         //healingTarget = getMostDamagedRobot(rc, groundUnits);
                    //                     }
                    //                 
                    //                     // We have a target we're trying to heal
                    //                     if (healingTarget != null) {
                    //                     
                    //                     
                    //                         System.out.println("Attempting to heal " + healingTarget);
                    //                     
                    //                         // TODO: We need to ensure that our target does not move
                    //                         // out of range from the time we choose it, and then
                    //                         // try to sense it.  Otherwise we'll get an exception
                    //                     
                    //                     
                    //                         // Find out information about the robot we're going to
                    //                         // heal
                    //                         RobotInfo healingTargetInfo = rc.senseRobotInfo(healingTarget);
                    //                     
                    //                         MapLocation curLoc = rc.getLocation();
                    //                         MapLocation healingTargetLoc = healingTargetInfo.location;
                    //                     
                    //                         // We can transfer the energon
                    //                         if (curLoc.equals(healingTargetLoc) || 
                    //                             curLoc.isAdjacentTo(healingTargetLoc)) {
                    //                         
                    //                             double energonAmt = Math.min(rc.getEnergonLevel() - 
                    //                                                         MIN_ENERGON_RESERVE, 
                    //                                                         getDamage(healingTargetInfo));
                    //                                                     
                    //                             if (energonAmt < 0) {
                    //                                 energonAmt = 0;
                    //                             }                            
                    //                             rc.transferEnergon( energonAmt, 
                    //                                                 healingTargetLoc,
                    //                                                 healingTarget.getRobotLevel()); 
                    //                         
                    //                             // Ensure we don't try to do two actions this turn
                    //                             healingTarget = null;
                    //                             rc.yield();
                    //                         }
                    //                     
                    //                         // We need to move towards the robot
                    //                         else {
                    //                             System.out.println("I am at " + curLoc + " and need to move to reach " + healingTargetLoc);
                    //                         
                    //                             //moveTo(healingTargetLoc);
                    //                             continue;
                    //                         }
                    //                     
                    //                     
                    //                     }
                    //                 }
                
                
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
    
    /**
    * @return the proportion of life the robot has left
    **/
    public static double getProportionHealth(RobotInfo r) {
        return r.eventualEnergon / r.maxEnergon;
    }

    /**
    * @return the proportion of life the robot has left
    **/
    public static double getProportionHealth(RobotController rc) {
        return rc.getEventualEnergonLevel() / rc.getMaxEnergonLevel();
    }


    /**
    * @return true if we have enough energon to attempt to heal other units
    **/
    private boolean shouldHeal() {
        double proportionHealth = rc.getEventualEnergonLevel() / MAX_ENERGON;
        return proportionHealth >= MIN_PROPORTION_ENERGON_TO_HEAL;
    }

    /**
    *
    * Transfer some of your energon to a nearby robot that has lower health, 
    * as a percentage.
    * Note that this method assumes that the knowledge base is up to date;
    * if the robots around it have moved and we attempt to transfer energon
    * to that square, we throw an exception; hence we explicitly try for that
    * condition.
    **/
    private void heal() {
        
        // To whom are we going to attempt to transfer energon?
        RobotInfo transferTarget = null;
       
        RobotInfo closestGroundRobot = 
            UtilityFunctions.min(kb.friendlyGroundRobots, 0, kb.friendlyGroundRobotsSize, distanceComp);
        RobotInfo closestAirRobot = 
            UtilityFunctions.min(kb.friendlyAirRobots, 0, kb.friendlyAirRobotsSize, distanceComp);
        
        transferTarget = closestGroundRobot;
        // TODO: take into account air robot
   
        // No suitable targets to heal
        if (transferTarget == null) {
            if (DEBUG) {
                System.out.println("No targets are suitable for healing");
            }
            return;
        }
        
        MapLocation curLoc = rc.getLocation();
        MapLocation healingTargetLoc = transferTarget.location;
    
        // We can transfer the energon
        if (curLoc.equals(healingTargetLoc) || 
            curLoc.isAdjacentTo(healingTargetLoc)) {
            try {
                double amountEnergonToTransfer = 
                    calculateEnergonToTransfer(rc, transferTarget);
                // At what height is the robot we're trying to heal?
                RobotLevel levelOfTransferTarget = transferTarget.type.isAirborne() ? 
                                                    RobotLevel.IN_AIR :
                                                    RobotLevel.ON_GROUND;
                // Finally, we know how much, where, and at what height to
                // transfer energon.  Go ahead and try it
                rc.transferEnergon(amountEnergonToTransfer, 
                                    healingTargetLoc, 
                                    levelOfTransferTarget);
                rc.yield();
            }
            // The transfer failed for some reason
            catch (GameActionException e) {
                if (DEBUG) {
                    System.out.println("Attempted to heal a robot " + 
                                        transferTarget + " with out " +
                                        "of date information; robot must " +  
                                        "have moved");
                    e.printStackTrace();
                }
            }
        }
        
        // We are not adjacent; we need to move towards the robot
        else {
            
            if (DEBUG) {
                System.out.println("Trying to heal " + transferTarget + 
                                    " but it's too far away.");
            }
            
            // If we can move this turn, do so
            
            // Else ... 
            
            
        }
    
        
    }
    
    private double calculateEnergonToTransfer(RobotController rc, RobotInfo transferTarget) {
        // Give them either how much energon they need to get back to full 
        // health, or however much we can afford to give them without depleting
        // our reserves too much
        return Math.min(getDamage(transferTarget), 
                        rc.getEventualEnergonLevel() - MIN_ENERGON_RESERVE);
                                    
    }
    
    
    
}