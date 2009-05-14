package teamJA_ND;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.*;

public class WanderRobot extends DefaultRobot {
    
    private RobotController rc;
    
    public WanderRobot(RobotController rc) 
    {
        super(rc);
        System.out.println("I'm a wandering fool!");
        this.rc = rc;
        
    }
    
    public void run()
    {
        
        try {
            while (true) {
                
                
                
                // HACK
                rc.yield();
                // rc.yield();
                //                 System.out.println(this + " going to explode now.");
                //                 rc.suicide();
                //                 
                
                if (!rc.isMovementActive()) {
                
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
    }
    
    
    
}