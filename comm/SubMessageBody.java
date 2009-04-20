package teamJA_ND.comm;
import teamJA_ND.util.Assert;
import java.util.Map;
import java.util.HashMap;


public abstract class SubMessageBody implements Transferable<SubMessageBody>
{
    // ID and Length
    public static final int MIN_REQUIRED_FIELDS = 2;
    public static final int LENGTH_FIELD_INDEX = 0;
    public static final int ID_FIELD_INDEX = 1;
    
    
    public enum SubMessageBodyType {
        // Actions/commands
        ATTACK(-1235098, AttackCommand.PARSER),
        MOVE_TO(1235125, MoveToCommand.PARSER),
        SHOVE(9206872, ShoveCommand.PARSER),
        DEFEND(-155, DefendCommand.PARSER),
        CALCULATE_PATH(155514, CalculatePathCommand.PARSER),
        GIVE_ENERGON(156166, GiveEnergonCommand.PARSER),
        JOIN_GROUP(55236, JoinGroupCommand.PARSER),
        LEAVE_GROUP(160968, LeaveGroupCommand.PARSER),
        BELAY_ORDER(-12358912, BelayOrderCommand.PARSER),

        // Information
        MAP_DELTA(66236, MapDeltaInfo.PARSER),
        SELF_TROOP_STRENGTH(161100128, SelfTroopStrengthInfo.PARSER),
        ENEMY_TROOP_STRENGTH(1238670, EnemyTroopStrengthInfo.PARSER),
        PATH(21534, PathInfo.PARSER),
        COMPUTATION_RESULT(629387, ComputationResultInfo.PARSER),
        MENTAL_STATE(862973, MentalStateInfo.PARSER),
        COMMAND_ACKNOWLEDGED(23525, CommandAcknowledgedInfo.PARSER),
        COMMAND_COMPLETED(2836708, CommandCompletedInfo.PARSER),
        COMMAND_REFUSED(2876098, CommandRefusedInfo.PARSER),
        FRINGE(20938698, FringeInfo.PARSER);
        
        private int id;
        private SubMessageBody parser;
        SubMessageBodyType(int id, SubMessageBody parser) {
            this.id = id;
            this.parser = parser;
        }
        public int getID() { return id; }
        public SubMessageBody getParser() {
            return parser;
        }
    };
    
    /**
     * Make a mapping from the IDs to their parsers
     * Based off of code from Joshua Bloch's Effective Java Second Edition,
     * p. 154
     */
    private static final Map<Integer, SubMessageBody> idToParser =
        new HashMap<Integer, SubMessageBody>();
    // Initialize the mapping
    static {
        for (SubMessageBodyType type : SubMessageBodyType.values()) {
            idToParser.put(type.getID(), type.getParser());
        }
    }
    public static SubMessageBody parserFromID(int id) {
        return idToParser.get(id);
    }
    
    public abstract int getID();
    
    public abstract int getLength();

    public abstract SubMessageBody fromIntArray(int[] array, int offset);

    public abstract int[] toIntArray();

    /**
    * Pass on the work of parsing the message onto the subclass.  The better
    * way of doing this would be via method reflection within the enumeration
    * but alas, we cannot do that with the restrictions BattleCode has
    * placed on us.
    **/
    public static SubMessageBody parse(int[] array, int offset) {
        int id = array[offset + ID_FIELD_INDEX];
        SubMessageBody PARSER = parserFromID(id);
        if (PARSER == null) {
            System.out.println("Error: unrecognized id : " + id + 
                " in SubMessageBody.parse()");
            return null;
        }
        else {
            return PARSER.fromIntArray(array, offset);
        }
        
    }


    public static void test() {
        
    }

}