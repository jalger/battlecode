package teamJA_ND;

import teamJA_ND.comm.MessageUtil;
import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.Message;
import battlecode.common.RobotController;

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
            debug_tick();
            MessageUtil.test();
            debug_tock();
            /*
            int ROBOT_ID = 5;
            int ROBOT_MESSAGE_ID = 236;
            MapLocation CUR_LOC = new MapLocation(5,10);
            final int NUM_POINTS = 17;
            boolean[] groundTraversable = new boolean[NUM_POINTS];

            for (int i = 0; i < NUM_POINTS; i++) {
                groundTraversable[i] = (i % 2 == 0);
            }

            SubMessageBody b4 = new FringeInfo(1, groundTraversable);
            SubMessageHeader h4 = new SubMessageHeader.Builder(CUR_LOC, b4.getLength()).build();
            SubMessage m4 = new SubMessage(h4, b4);

            java.util.List<SubMessage> sms = java.util.Arrays.asList(new SubMessage[] {m4});

            debug_tick();
            Message packed = MessageUtil.pack(sms, 
                                CUR_LOC,
                                ROBOT_ID,
                                ROBOT_MESSAGE_ID);
            debug_tock();
            */
            rc.yield();
        }   

    }
 
    public static String fromMessage(Message m) {
        return java.util.Arrays.toString(m.ints) + 
                java.util.Arrays.toString(m.strings) +
                java.util.Arrays.toString(m.locations);
    }
    
}