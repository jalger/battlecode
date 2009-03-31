package jj_nick;

import battlecode.common.MapLocation;


/**
* @author Nicholas Dunn
*/
public class DefaultExtendedMessage implements ExtendedMessage {
    
    protected Range range;
    protected MapLocation origin;
    protected Recipient recipientType;
    protected boolean timeLimited;
    protected int latestRoundRelevant;
    protected int recipients;
    
    
    public boolean isTimeLimited() {
        return timeLimited;
    }
    
    /**
    * If a message is time limited, returns the
    * latest  
    */
    public int getLatestRoundRelevant() {
        return latestRoundRelevant;
    }
    
        
    /**
    * Every ExtendedMessage must be able to be packed into
    * a String in order to fit into a standard Message object.
    */
    public String toMessageString() {
        // TODO: Fix this shit
        return this.toString();
        
    }
    
    /**
    * @return the intended Range for this object
    */
    public Range getRange() {
        return range;
    }
    
    /**
    * @return the location from which this message was originally
    * sent.
    */
    public MapLocation getOrigin() {
        return origin;
    }
        
        
    public Recipient getRecipientType() {
        return recipientType;
    }
    
    /**
    * @return an int representing the intended recipients.
    * we use bit packing in the case of TASK_FORCE (can go
    * out to more than one group of robots)
    */
    public int getRecipients() {
        return recipients;
    }
    
    
    
    
    
    
    
    
}