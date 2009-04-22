/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package teamJA_ND;

/**
 *
 * @author J.J. Alger, Nick Dunn
 */

import battlecode.common.RobotController;

public class Soldier extends DefaultRobot {

    public Soldier(RobotController rcIn) {
        super(rcIn);
    }

    public void run() {
        System.out.println("I'm a soldier!");
        super.run();
    }

}
