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
    
    
    public static final int ATTACK_ID = -1235023;
    public static final int MOVE_TO_ID = 11512084;
    public static final int SHOVE_ID = 138951029;
    public static final int DEFEND_ID = 9863;
    public static final int CALCULATE_PATH_ID = 6982346;
    public static final int GIVE_ENERGON_ID = 36890273;
    public static final int JOIN_GROUP_ID = 652356;
    public static final int LEAVE_GROUP_ID = 698236;
    public static final int BELAY_ORDER_ID = 62983676;
    
    public static final int MAP_DELTA_ID = 69083260;
    public static final int SELF_TROOP_STRENGTH_ID = 42931;
    public static final int ENEMY_TROOP_STRENGTH_ID = 62529335;
    public static final int PATH_ID = 6923623;
    public static final int COMPUTATION_RESULT_ID = 309712612;
    public static final int MENTAL_STATE_ID = 629312316;
    public static final int COMMAND_ACKNOWLEDGED_ID =4235323;
    public static final int COMMAND_COMPLETED_ID = 36928367;
    public static final int COMMAND_REFUSED_ID = 26938523;
    public static final int FRINGE_ID = 134125125;
    
    
    
    /**
    * Every subclass must have a unique ID so that we know which parser to
    * use to turn the array of ints into an intelligible message
    **/    
    public abstract int getID();
    
    /**
    * Every subclass must declare how many ints it takes to represent itself
    **/
    public abstract int getLength();

    /**
    * Every subclass must be able to be converted from an array of ints into
    * an instance.
    */
    public abstract SubMessageBody fromIntArray(int[] array, int offset);

    /**
    * Every subclass must be able to be converted into an array of ints for
    * storage within a message
    */
    public abstract void toIntArray(int[] array, int offset);

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
    
    public static SubMessageBody parserFromID(int id) {
        
        switch (id) {
            case ATTACK_ID:
                return AttackCommand.PARSER;
            
            case MOVE_TO_ID:
                return MoveToCommand.PARSER;
            
            case SHOVE_ID:
                return ShoveCommand.PARSER;
                
            case DEFEND_ID:
                return DefendCommand.PARSER;
                
            case CALCULATE_PATH_ID:
                return CalculatePathCommand.PARSER;
                
            case GIVE_ENERGON_ID:
                return GiveEnergonCommand.PARSER;
                
            case JOIN_GROUP_ID:
                return JoinGroupCommand.PARSER;
                
            case LEAVE_GROUP_ID:
                return LeaveGroupCommand.PARSER;
                
            case BELAY_ORDER_ID:
                return BelayOrderCommand.PARSER;
            
            // Info types
            
            case MAP_DELTA_ID:
                return MapDeltaInfo.PARSER;
                
            case SELF_TROOP_STRENGTH_ID:
                return SelfTroopStrengthInfo.PARSER;
                
            case ENEMY_TROOP_STRENGTH_ID:
                return EnemyTroopStrengthInfo.PARSER;
                
            case PATH_ID:
                return PathInfo.PARSER;
                
            case COMPUTATION_RESULT_ID:
                return ComputationResultInfo.PARSER;
                
            case MENTAL_STATE_ID:
                return MentalStateInfo.PARSER;
                
            case COMMAND_ACKNOWLEDGED_ID:
                return CommandAcknowledgedInfo.PARSER;
                
            case COMMAND_COMPLETED_ID:
                return CommandCompletedInfo.PARSER;
                
            case COMMAND_REFUSED_ID:
                return CommandRefusedInfo.PARSER;
                
            case FRINGE_ID:
                return FringeInfo.PARSER;
            
            default:
                return null;
            
        }
            
    }


    public static void test() {
        
    }

}