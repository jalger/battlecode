package teamJA_ND.comm;
import teamJA_ND.util.Assert;

public abstract class SubMessageBody implements Transferable<SubMessageBody>
{
    // ID and Length
    public static final int MIN_REQUIRED_FIELDS = 2;
    
    
    public enum SubMessageBodyType {
        // Actions/commands
        ATTACK(-1235098),
        MOVE_TO(1235125),
        GET_OUT_OF_WAY(12512444),
        DEFEND(-155),
        CALCULATE_PATH(155514),
        GIVE_ENERGON(156166),
        JOIN_GROUP(55236),
        LEAVE_GROUP(160968),
        BELAY_ORDER(-12358912),

        // Information
        MAP_DELTA(66236),
        SELF_TROOP_STRENGTH(161100128),
        ENEMY_TROOP_STRENGTH(1238670),
        PATH(21534),
        COMPUTATION_RESULT(629387),
        MENTAL_STATE(862973),
        COMMAND_ACKNOWLEDGED(23525),
        COMMAND_COMPLETED(2836708),
        COMMAND_REFUSED(2876098),
        FRINGE(20938698);
        
        private int id;
        SubMessageBodyType(int id) {
            this.id = id;
        }
        public int getID() { return id; }
    };
    
    public abstract int getID();
    
    public abstract int getLength();

    public abstract SubMessageBody fromIntArray(int[] array, int offset);

    public abstract int[] toIntArray();


    public static void test() {
        
    }

}