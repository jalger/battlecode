package teamJA_ND;

/**
 * A State is a high level representation of the way a robot behaves.  For
 * instances, a Move state would be used any time a robot needs to move,
 * while and Attack state would be used any time a robot needs to attack.
 *
 * Each turn, robots will check whether they need to transition from their
 * current state.  If they do, they call onExit() on their current state,
 * switch over to new state, and call onEnter() on new state.  If they are
 * to remain in their current state, they just continue doing what they're
 * doing via update().
 * @author J.J. Alger, Nick Dunn
 */
public abstract class State {

    public abstract void onEnter();
    public abstract void update();
    public abstract void onExit();

}
