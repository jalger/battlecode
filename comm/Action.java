package teamJA_ND.comm;

/**
 * @author Nicholas Dunn
 * @date   February 28, 2009
 */
public interface Action {

    public enum ActionPossibilities {
            ATTACK,
            MOVE_TO,
            GET_OUT_OF_WAY,
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
