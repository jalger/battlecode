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
        /*
        start = Clock.getBytecodeNum();
        MiscInfo info = new MiscInfo.Builder(new MapLocation(100, 200)).range(MiscInfo.Range.SHORT).build();
        
        int [] result = info.toIntArray();
        MiscInfo info2 = MiscInfo.PARSER.fromIntArray(result, 0);
        
        //System.out.println(info);
        finalNum = Clock.getBytecodeNum();
        System.out.println("MiscInfo took " + (finalNum - start - delta) + " bytecodes.");
*/        
        /*
        
        start = Clock.getBytecodeNum();
        Path.testFromIntArray();
        finalNum = Clock.getBytecodeNum();
        System.out.println("Path.testFromIntArray took " + (finalNum - start - delta) + " bytecodes.");

        start = Clock.getBytecodeNum();
        Path.testToIntArray();
        finalNum = Clock.getBytecodeNum();
        System.out.println("Path.testToIntArray took " + (finalNum - start - delta) + " bytecodes.");
*/
        start = Clock.getBytecodeNum();
        Path.main(null);
        finalNum = Clock.getBytecodeNum();
        System.out.println("Path.main took " + (finalNum - start - delta) + " bytecodes.");
        
        start = Clock.getBytecodeNum();
        Path.main2(null);
        finalNum = Clock.getBytecodeNum();
        System.out.println("Path.main2 took " + (finalNum - start - delta) + " bytecodes.");
        
        
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
