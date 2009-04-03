package jj_nick;

import battlecode.common.Clock;
import battlecode.common.RobotController;


public class RobotPlayer implements Runnable {

    private RobotController rc;


    public RobotPlayer(RobotController rcIn) {
        rc = rcIn;
        
        rc.yield();
        int start = 0;
        int finalNum = 0;
        start = Clock.getBytecodeNum();
        finalNum = Clock.getBytecodeNum();
        int delta = finalNum - start;
        int elapsed;
        
        
          Point [] points1 = new Point[] 
            {                               
                new Point(100, 200),
                new Point(0,0),
                new Point(-120581204,1),
                new Point(105,105),
                new Point (100, 20+31),
                new Point(1500, 200),
                new Point(0,20),
                new Point(-125204,152),
                new Point(105,1052),
                new Point (100, 206)  
            };
            boolean [] traversable = new boolean[]
            {
                true,
                true,
                false,
                false,
                true,
                true,
                true,
                false,
                false,
                true
            };

            int [] heights = new int[]
            {
                1,
                3,
                5,
                -1,
                0,
                26,
                6,
                1,
                2,
                3
            };
            
            
            Fringe f = new Fringe(points1, traversable, heights);
            
            
        
        start = Clock.getBytecodeNum();

        f.toIntArray();


        finalNum = Clock.getBytecodeNum();
        elapsed = (finalNum - start) - delta;
        System.out.println("Took " + elapsed + " bytecodes to go to int "
        +"arraye");


        
    }

    public void run() {
        
        while(true) {}

    }
    
    
    
}
