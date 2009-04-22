package teamJA_ND;

import teamJA_ND.comm.SubMessageHeader;
import battlecode.common.RobotController;

public class RobotPlayer implements Runnable {

    protected final DefaultRobot me;

    public RobotPlayer(RobotController rcIn) {
        switch(rcIn.getRobotType()) {
            case ARCHON: me = new Archon(rcIn); break;
            case SOLDIER: me = new Soldier(rcIn); break;
//            default: me = new DefaultRobot(rcIn); break;
            default: me = new CommunicationRobot(rcIn); break;
        }
        
        SubMessageHeader.test();
        
    }

    public void run() {
        me.run();
    }
}
 