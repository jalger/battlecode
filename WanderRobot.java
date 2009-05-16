package teamJA_ND;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.*;

public class WanderRobot extends DefaultRobot {
    
    private RobotController rc;
    
    private Move blitzArchon;
    
    public WanderRobot(RobotController rc) 
    {
        super(rc);
        System.out.println("I'm a wandering fool!");
        this.rc = rc;
        
        
        Direction dirToEnemyArchon = rc.senseEnemyArchon();
        
        blitzArchon = startBlitz(rc.getLocation(), dirToEnemyArchon);
        
        blitzArchon.onEnter();
        
    }
    
    public void run()
    {
        
        try {
            while (true) {
                
                
                if (shouldHeal()) {
                    heal();
                }
                
                blitzArchon.update();
                rc.yield();
            }
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
}