package jj_nick;

import battlecode.common.*;
import static battlecode.common.GameConstants.*;


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
        
        start = Clock.getBytecodeNum();
        MiscInfo info = new MiscInfo.Builder(new MapLocation(100, 200)).range(MiscInfo.Range.SHORT).build();
        //System.out.println(info);
        info.toIntArray();                
        finalNum = Clock.getBytecodeNum();
        System.out.println("MiscInfo took " + (finalNum - start - delta) + " bytecodes.");
        
        
        
        /*
        try {
            Point.main(null);
        }
        catch (Exception e) { System.out.println("didn't work"); }


        start = Clock.getBytecodeNum();
         Path.main(null);
        finalNum = Clock.getBytecodeNum();
        System.out.println("Path took " + (finalNum - start - delta) + " bytecodes.");


        start = Clock.getBytecodeNum();
        Point.testFromString();
        finalNum = Clock.getBytecodeNum();
        System.out.println("Point.testFromString() took " + (finalNum - start - delta) + " bytecodes.");
        
        start = Clock.getBytecodeNum();
        Point.testToString();
        finalNum = Clock.getBytecodeNum();
        System.out.println("Point.testToString() took " + (finalNum - start - delta) + " bytecodes.");
            
        
        start = Clock.getBytecodeNum();
        MessageUtil.main(null);
        finalNum = Clock.getBytecodeNum();
        System.out.println("MessageUtil took " + (finalNum - start - delta) + " bytecodes.");
        */
        
    }

    public void run() {
        
        while(true) {}

    }
    
    
    
}
