package teamJA_ND;

/**
 * This class represents any sort of information that a robot might
 * want to either transfer or request via the Message protocol.
 * Unlike a Command, an Information object has no associated
 * priority.
 * @author Nicholas Dunn
 * @date   February 28, 2009
 */
public abstract class Information implements Transferable<Information> {
    
    static public final int IDENTIFIER = 399564;
    
    public enum InformationType {
        MAP_DELTA,
        SELF_TROOP_STRENGTH,
        ENEMY_TROOP_STRENGTH,
        PATH,
        COMPUTATION_RESULT,
        MENTAL_STATE,
        COMMAND_ACKNOWLEDGED,
        COMMAND_COMPLETED,
        COMMAND_REFUSED,
        FRINGE
    }
    
    
    
}