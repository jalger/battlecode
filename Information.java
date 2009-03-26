package jj_nick;

/**
 * This class represents any sort of information that a robot might
 * want to either transfer or request via the Message protocol.
 * @author Nicholas Dunn
 * @date   February 28, 2009
 */
public class Information {
    public enum InformationType {
        MAP_DELTA,
            SELF_TROOP_STRENGTH,
            ENEMY_TROOP_STRENGTH,
            WAYPOINTS,
            // Is waypoints just a more specific form of this?
            COMPUTATION_RESULT,
            // J. J. Alger:
            // Transfer of priorities / mental state. If archons are in groups,
            // I can conceive of one archon wanting to update another about
            // its current thoughts so that if it dies the other one knows
            // what's going on for example. Not sure how to code this one,
            // as we don't really have any sort of architecture set up for
            // thinking yet.
            MENTAL_STATE,

            COMMAND_ACKNOWLEDGED,
            // 20: Message marking some prior message's request as already
            // satisfied (e.g., energon already provided)
            COMMAND_COMPLETED,
            COMMAND_REFUSED,
    }

}