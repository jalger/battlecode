import battlecode.common.MapLocation;


/**
* Represents a message that a robot sends.
* @author Nicholas Dunn
* @date   03/27/09
*/
public interface ExtendedMessage {
    
    /**
    * Determines the intended range of the message.  
    * Used to decide whether or not an ExtendedMessage
    * should be rebroadcast.
    */
    public enum Range {
      SHORT,
      MEDIUM,
      LONG,
      INFINITE  
    };
    
    
    /**
    * Who should act on this message?
    */
    public enum Recipient {
        TASK_FORCE,
        ROBOT,
        ROBOT_TYPE,
        ALL
    }
    
    
    public boolean isTimeLimited();
    
    /**
    * If a message is time limited, returns the
    * latest  
    */
    public int getLatestRoundRelevant();
    
        
    /**
    * Every ExtendedMessage must be able to be packed into
    * a String in order to fit into a standard Message object.
    */
    public String toMessageString();
    
    /**
    * @return the intended Range for this object
    */
    public Range getRange();
    
    /**
    * @return the location from which this message was originally
    * sent.
    */
    public MapLocation getOrigin();
        
        
    public Recipient getRecipientType();
    
    /**
    * @return an int representing the intended recipients.
    * we use bit packing in the case of TASK_FORCE (can go
    * out to more than one group of robots)
    */
    public int getRecipients();
    
}