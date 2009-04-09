package comm.teamJA_ND;

import java.util.HashMap;
import java.util.Map;

import teamJA_ND.Action;


/**
 * A Command object represents a command that a commander delegates to
 * its units.  Commands are immutable; if a commander wishes to revoke
 * a command he must issue a new command cancelling the previous order,
 * or create a new command with higher priority.
 * @author Nicholas Dunn
 * @date   February 28, 2009
 */
public class Command implements Comparable<Command> {

    public static final int IDENTIFIER = 2849690;
    
    static public final Command PARSER = new Command(null,null);

    // A priority is used for ordering commands
    public enum Priority
    {
            LOW(0),
            MEDIUM(1),
            HIGH(2),
            URGENT(3);

        private final int urgency;
        Priority(int urgency) { this.urgency = urgency; }
        public int getUrgency() { return urgency; }
    }

    /**
     * Based off of code from Joshua Bloch's Effective Java Second Edition,
     * p. 154
     */
    private static final Map<Integer, Priority> integerToEnum =
        new HashMap<Integer, Priority>();
    // Initialize the mapping
    static {
        for (Priority priority : Priority.values()) {
            integerToEnum.put(priority.getUrgency(), priority);
        }
    }
    public static Priority fromInteger(int value) {
        return integerToEnum.get(value);
    }




    private final Action action;
    private final Priority priority;

    private final int id;

    private static int count;
    /**
     *
     */
    public Command(Action action, Priority priority) {
        this.action = action;
        this.priority = priority;
        id = Command.count++;
    }

    /**
     * Return the action associated with this command.  Note that since
     * Action is itself immutable, this is safe to do; calling classes
     * cannot do anything nefarious, even with a reference to this object.
     */
    public Action getAction() {
        return action;
    }

    /**
     * Return the priority associated with this command.  Note that since
     * enumerations are immutable, it is safe to return this reference.
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Commands are executed in order of priority.  In the case that they are
     * of equal priority, the most recent command is executed.
     */
    public int compareTo(Command o) {
        int result = priority.compareTo(o.priority);
        // One priority is higher or lower than the other
        if (result != 0) {
            return 0;
        }
        // The more recent message will have a higher id
        else {
            return id - o.id;
        }
    }

}