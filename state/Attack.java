/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package teamJA_ND.state;

import java.util.Comparator;

import teamJA_ND.DefaultRobot;
import teamJA_ND.KnowledgeBase;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Direction;

/**
 *
 * @author J.J. Alger
 */
public class Attack extends State {

    private KnowledgeBase kb;
    private RobotController robotController;
    private DefaultRobot robot;

    public Attack(KnowledgeBase kb, RobotController rc, DefaultRobot dr) {
        this.kb = kb;
        this.robotController = rc;
        this.robot = dr;
    }

    public void onEnter() {
        // If you can attack
        if (robotController.getRoundsUntilAttackIdle() == 0) {
            // Check to see if any enemy robots are in attack range
            
                // If so, pick which one to attack
        }
        
        // You can move and there are enemy robots within sensor range
        else if (robotController.getRoundsUntilMovementIdle() == 0) {
            
        }
        
        
        else {
            
        }
        
    }
    
    /**
    * Use a simplified method of potential fields to determine which of the
    * 8 surrounding squares is best to move to
    */
    public Direction getBestMoveDirection(KnowledgeBase kb) {
        
        
        
        //Direction d = heuristicBestDirection(kb);

        // For the 8 surrounding squares
        for (int i = 0; i < 8; i++) {    
           // For each robot in sensor range
           
           
                // If on our team:
                    // Add the bonus for our team
                    

        }
        return null;
                    
                    

    }
    
    
    

    public void update() {

    }

    public void onExit() {

    }
    
    
    /**
    * Given a list of potential enemy targets, determines the best one to 
    * attack. 
    **/
    public Robot pickOpponent(Robot[] groundTargets, Robot[] airTargets) {
        
        // Ground -> all other ground
        // Bombers -> only ground


        // Scouts -> only air
        // soldier-> can attack air
        
        // enemy towers only
        
        
        double damage = robotController.getRobotType().attackPower();
        
        // Make a list of all those you can kill in one shot
        Robot toKill = groundTargets[0];
        for (Robot target : groundTargets) {
            try{ 
                RobotInfo info = robotController.senseRobotInfo(target);
                // target will be killed if we attack it
                if (info.energonLevel < damage) {
                    // Prioritize by archons, followed
                }
            }
            catch (battlecode.common.GameActionException e) {}
        }
        // for any robot can kill in one shot
            // archons
            // pick the most damaged of those
            
        
        // Ground units at elevation - bonus to damage
        
        // most damaged
        
        return null;
        
    }
    /*
    public class RobotTypeComparator implements Comparable<RobotType> {
        
    
        
        public int compare(RobotInfo r1, RobotInfo r2) {
            
        }

        // TODO: implement this method correctly
        public boolean equals(Object obj) {
            return false;
        }
        
        
        ARCHON

        BOMBER

        MORTAR

        SCOUT

        SNIPER

        SOLDIER

        TOWER
    }*/
    
    public class EnemyComparator implements Comparator<RobotInfo> {
/*        private RobotController rc;
        public EnemyComparator(RobotController rc) {
            this.rc = rc;
        }*/
        
        
        public int compare(RobotInfo r1, RobotInfo r2) {
            // Archon is most important, deal with that
            if (r1.type == RobotType.ARCHON && r2.type != RobotType.ARCHON) {
                return -1;
            }
            else if (r1.type != RobotType.ARCHON && r2.type == RobotType.ARCHON) {
                return 1;
            }
            
            return 0;
        }

        // TODO: implement this method correctly
        public boolean equals(Object obj) {
            return false;
        }
    }
    
    
    
    
}
