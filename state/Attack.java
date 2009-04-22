/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package teamJA_ND.state;

import teamJA_ND.KnowledgeBase;
import teamJA_ND.DefaultRobot;
import battlecode.common.RobotController;
import battlecode.common.Robot;
import battlecode.common.RobotType;
import battlecode.common.RobotInfo;
import java.util.Comparator;

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
