package teamJA_ND;

import battlecode.common.Message;
import battlecode.common.*;
/**
* Test class to see if we can send and receive messages correctly
* @author Nicholas Dunn
**/
public class CommunicationRobot extends DefaultRobot 
{
    
    public CommunicationRobot(RobotController r)
    {   
        super(r);
    }
    
    // Each turn we will simply send a message with our robot id, location, 
    // current round number
    public void run() {
        
        while (true) {
            int roundNum = Clock.getRoundNum();
            MapLocation loc = rc.getLocation();
            int robotID = rc.getRobot().getID();
        
            int[] msg = new int[] {roundNum, robotID};
            MapLocation[] locs = new MapLocation[] {loc };
        
            Message m = new Message();
            m.ints = msg;
            m.locations = locs;
        
            try {
                rc.broadcast(m);
            }
            catch (GameActionException e) {
                e.printStackTrace();
            
            }
            
            
            // Check for messages
            Message [] messages = rc.getAllMessages();
            if (messages.length >= GameConstants.NUMBER_OF_INDICATOR_STRINGS) {
                for (int i = 0; i < GameConstants.NUMBER_OF_INDICATOR_STRINGS; i++) {
                    rc.setIndicatorString(i, fromMessage(messages[i]));
                }
            }
            rc.yield();
        }   

    }
 
    public static String fromMessage(Message m) {
        return java.util.Arrays.toString(m.ints) + 
                java.util.Arrays.toString(m.strings) +
                java.util.Arrays.toString(m.locations);
    }
    
}