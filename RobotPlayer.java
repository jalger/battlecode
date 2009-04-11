package teamJA_ND;

import battlecode.common.*;
import static battlecode.common.GameConstants.*;

public class RobotPlayer implements Runnable {

    protected final DefaultRobot me;

    public RobotPlayer(RobotController rcIn) {
        switch(rcIn.getRobotType()) {
            default: me = new CommunicationRobot(rcIn); break;
/*            case ARCHON: me = new Archon(rcIn); break;
            case SOLDIER: me = new Soldier(rcIn); break;
            default: me = new DefaultRobot(rcIn); break;*/
        }
    }

    public void run() {
        me.run();
    }
}
