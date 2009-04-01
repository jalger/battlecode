package jj_nick;

import battlecode.common.MapLocation;


/**
* Represents
* @author Nicholas Dunn
* @date   03/27/09
*/
public class MiscInfo implements Transferable {
    
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
        SQUAD,
        ROBOT,
        ROBOT_TYPE,
        ALL
    }
    
    private final Range range;
    private final MapLocation origin;
    private final Recipient recipientType;
    private final boolean timeLimited;
    private final int latestRoundRelevant;
    private final int recipients;
    
    /**
    * Builder pattern is described in Item 2 of Effective Java; it allows
    * caller to emulate named optional parameters.
    * One sample usage is as follows:
    * MiscInfo f = new MiscInfo.Builder(new MapLocation(100, 200)).range(Range.SHORT).build();
    * This would create a MiscInfo with default values except for the range and the passed
    * in (required) origin
    */
    public static class Builder {
        
        // Required parameter
        private MapLocation origin;
        
        // Optional parameters - initialized to default values
        private Range range             = Range.INFINITE;
        private Recipient recipientType = Recipient.ALL;
        private boolean timeLimited     = false;
        private int latestRoundRelevant = 0;
        private int recipients          = 0;
        
        public Builder(MapLocation origin) {
            this.origin = origin;
        }
        
        public Builder range(Range range) {
            this.range = range;
            return this;
        }
        
        public Builder recipientType(Recipient type) {
            this.recipientType = type;
            return this;
        }
        
        public Builder timeLimited(boolean timeLimted) {
            this.timeLimited = timeLimited;
            return this;
        }
        
        public Builder latestRoundRelevant(int latestRoundRelevant) {
            this.latestRoundRelevant = latestRoundRelevant;
            return this;
        }
        
        public Builder recipients(int recipients) {
            this.recipients = recipients;
            return this;
        }
        
        public MiscInfo build() {
            return new MiscInfo(this);
        }
        
    }
    
    
    
    // 6 variables, one of which requires two ints to represent
    public static final int NUM_FIELDS = 7;
    
    
    public MiscInfo(Builder builder) {
        this.range                  = builder.range;
        this.origin                 = builder.origin;
        this.recipientType          = builder.recipientType;
        this.timeLimited            = builder.timeLimited;
        this.latestRoundRelevant    = builder.latestRoundRelevant;
        this.recipients             = builder.recipients;           
    }
    
    

    /**
    * @return whether the corresponding message is relevant for
    * a set time only.
    */
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
    * @return the intended Range for this object
    */
    public final Range getRange() {
        return range;
    }
    
    /**
    * @return the location from which this message was originally
    * sent.
    */
    public final MapLocation getOrigin() {
        return origin;
    }
        
        
    public final Recipient getRecipientType() {
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
    
    
   
    /**
    * toIntArray and fromIntArray must be modified in parallel if the 
    * encoding scheme ever changes.
    */    
    public int[] toIntArray() {
        int[] message = new int[] 
        {
            NUM_FIELDS,
            range.ordinal(),
            origin.getX(),
            origin.getY(),
            recipientType.ordinal(),
            timeLimited ? 1 : 0,
            latestRoundRelevant,
            recipients
        };
        return message;
    }
    
    /**
    * Attempts to populate the fields of this object from the
    * values in the int array
    */
    public boolean fromIntArray(int[] array, int offset) {
        return true;
        
        /*
        // We're missing fields somehow!
        if (array.length - offset < (NUM_FIELDS + 1)) {
            return false;
        }
        int rangeOrdinal = array[offset + 1];
        range = Range.values()[rangeOrdinal];
        
        int x = array[offset + 2];
        int y = array[offset + 3];
        
        origin = new MapLocation(x, y);
        
        int recipientTypeOrdinal = array[offset + 4];
        recipientType = Recipient.values()[recipientTypeOrdinal];
        
        timeLimited = array[offset + 5] == 1 ? true : false;
        latestRoundRelevant = array[offset + 6];
        recipients = array[offset + 7];
        
        return true;*/
    }
    
    public String toString() {
        return String.format("Origin:\t %s %n" +
                "Range:\t%s %n" +
                "Recipient:\t%s %n" +
                "Time limited:\t%b %n" +
                "Latest round relevant:\t%d %n" +
                "Recipients:\t%d %n",
                origin, range, recipientType, timeLimited, latestRoundRelevant, recipients);
        
    }
    
}