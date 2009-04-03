package jj_nick;

/**
 * @author Nicholas Dunn
 * @date   February 28, 2009
 */
public interface Action {

    public enum ActionPossibilities {
            ATTACK,
            MOVE,
            DEFEND,
            CALCULATE_PATH,
            GIVE_ENERGON,
            JOIN_GROUP,
            LEAVE_GROUP,
            BELAY_ORDER
    }


    public boolean execute();

    public void pause();

    public void resume();

    public boolean isCompleted();

    public void abort();


}
