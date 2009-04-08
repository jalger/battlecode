/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package teamJA_ND;

/**
 *
 * @author J.J. Alger
 */
public abstract class State {

    public abstract void onEnter();
    public abstract void update();
    public abstract void onExit();

}
