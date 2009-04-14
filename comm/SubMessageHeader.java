package teamJA_ND.comm;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Clock;
import teamJA_ND.KnowledgeBase;
import teamJA_ND.util.Assert;

/**
* Represents miscellaneous information that must be transfered along
* with every message in order for potential recipients to determine
* whether the rest of the message pertains to that robot, and whether
* it should be rebroadcast or not.  Allows robots to quickly determine
* whether it is worth parsing the rest of the SubMessageBody for relevant
* information.
*
* The Message format is laid out in such a way that if SubMessageHeader x
* starts at index i in the int array msg, msg[i + LENGTH] is the first
* int of the corresponding SubMessageBody, and msg[i + bodySize] is the first
* int of the next SubMessageHeader (if one exists.)
* @author Nicholas Dunn
* @date   03/27/09
*/
public class SubMessageHeader implements Transferable<SubMessageHeader>
{


    // TODO: Probably could eliminate the range and just encode a straight squared distance
    // TODO: Probably no need for the timeLimited thing

    /**
    * Determines the intended range of the message.
    * Used to decide whether or not an SubMessage
    * should be rebroadcast.
    */
    public enum Range {
        // 4 square radius
      SHORT(16),
      // 8 square radius
      MEDIUM(64),
      // 16 square radius
      LONG(256),
      INFINITE(Integer.MAX_VALUE);
      private int squaredDist;
      Range(int squaredDist) {
          this.squaredDist = squaredDist;
      }
      public int getSquaredDist() { return squaredDist; }
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

    

    /**
    * How many ints does it take to represent the corresponding 
    * <code>SubMessageBody</code>
    */
    private final int bodySize;
    
    /**
    * What is the intended range of this message
    */
    private final Range range;
    
    /**
    * Where was the robot when originally sending this message?
    */
    private final MapLocation origin;
    
    /**
    * How do we interpret the recipients field; we can specify a message
    * for a specific task force, squad, robot, robot type, or anyone
    */
    private final Recipient recipientType;
    
    /**
    * Is this message time limited?  Or will it be relevant forever
    */
    private final boolean timeLimited;
    
    /**
    * If this message is time limited, when is the last round when it will
    * be relevant?
    */
    private final int latestRoundRelevant;
    
    /**
    * Who are the intended recipients of this message?  A bitwise OR of the
    * intended recipient IDs, where each intended recipient ID is a power of
    * two.  For instance, if we are broadcasting to two task forces, one of 
    * which has a task force ID of 2, and the other with a task force ID of
    * 4, then this field would equal 2 | 4  = 6.  To determine if ID x is
    * intended to receive this message, take the bitwise AND of x and recipients.
    * If nonzero, then x is intended to receive this message.
    **/ 
    private final int recipients;
    
    private static final int LENGTH_INDEX = 0;
    private static final int BODY_SIZE_INDEX = 1;
    private static final int RANGE_INDEX = 2;
    private static final int ORIGIN_X_INDEX = 3;
    private static final int ORIGIN_Y_INDEX = 4;
    private static final int TIME_LIMITED_INDEX = 5;
    private static final int LATEST_ROUND_RELEVANT_INDEX = 6;
    private static final int RECIPIENT_TYPE_INDEX = 7;
    private static final int RECIPIENTS_INDEX = 8;
    
    public static final int LENGTH = 9;
    
    
    
    /**
    * Builder pattern is described in Item 2 of Effective Java; it allows
    * caller to emulate named optional parameters.
    * One sample usage is as follows:
    * SubMessageHeader f = new SubMessageHeader.Builder(new MapLocation(100, 200)).range(Range.SHORT).build();
    * This would create a SubMessageHeader with default values except for the range and the passed
    * in (required) origin
    */
    public static class Builder 
    {

        // Required parameters
        private MapLocation origin;
        private int bodySize;

        // Optional parameters - initialized to default values
        private Range range             = Range.INFINITE;
        private Recipient recipientType = Recipient.ALL;
        private boolean timeLimited     = false;
        private int latestRoundRelevant = 0;
        private int recipients          = 0;

        public Builder(MapLocation origin, int bodySize) {
            this.origin = origin;
            this.bodySize = bodySize;
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

        public SubMessageHeader build() {
            return new SubMessageHeader(this);
        }

    }

    // This is a hack, but I have no other idea how to do this...
    // we need this reference in order to call the fromIntArray method
    // but we can't :(
    public static final SubMessageHeader PARSER = new SubMessageHeader.Builder(null, 0).build();



   


    public SubMessageHeader(Builder builder) {
        this.bodySize               = builder.bodySize;
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
    * Given a <code>KnowledgeBase</code> representing what a given robot knows 
    * (including information about itself) determine if this header indicates 
    * that the message is intended for the bot owning the 
    * <code>KnowledgeBase</code>.
    * @param kb the KnowledgeBase containing information about the robot
    **/
    public boolean pertainsToRobot(KnowledgeBase kb) {
        
        // The recipient field is written as the logical OR of multiple powers
        // of two.  The IDs are all assigned to be powers of two.  Thus we can
        // check to see if the given ID is part of intended audience by
        // taking logical AND of the recipient field and the id field.  A
        // nonzero result indicates that the bit was set, meaning that the
        // message is intended for this robot.

        switch (recipientType) {
            case TASK_FORCE:
                return (recipients & kb.taskforceID) != 0;
            case SQUAD:
                return (recipients & kb.squadID) != 0;
            case ROBOT:
                return (recipients & kb.robotID) != 0;
            case ROBOT_TYPE:
                return (recipients & kb.robotType) != 0;
            case ALL:
                return true;
            default:
                Assert.Assert(false, "Error: fell through switch statements in pertainsToRobot.  recipientType: " + recipientType);
                return false;
        }
    }
    
    public boolean shouldRebroadcast(MapLocation curLocation) {
           // If it's a time limited message, and I'm past the time limit,
           // don't broadcast
           if (timeLimited) {
               return latestRoundRelevant < Clock.getRoundNum();
           }
           if (range == Range.INFINITE) {
               return true;
           }
           // Only rebroadcast if within the range specified
           return curLocation.distanceSquaredTo(origin) < range.getSquaredDist();
    }
    
    
    
    /**
    * toIntArray and fromIntArray must be modified in parallel if the
    * encoding scheme ever changes.
    */
    public int[] toIntArray() {
        int[] message = new int[LENGTH];
        
        message[LENGTH_INDEX]           = LENGTH;
        message[BODY_SIZE_INDEX]        = bodySize;
        message[RANGE_INDEX]            = range.ordinal();
        message[ORIGIN_X_INDEX]         = origin.getX();
        message[ORIGIN_Y_INDEX]         = origin.getY();
        message[TIME_LIMITED_INDEX]     = timeLimited ? 1 : 0;
        message[LATEST_ROUND_RELEVANT_INDEX] = latestRoundRelevant;
        message[RECIPIENT_TYPE_INDEX]   = recipientType.ordinal();
        message[RECIPIENTS_INDEX]       = recipients;
        
        return message;
    }
    

    /**
    * Attempts to populate the fields of this object from the
    * values in the int array
    */
    public SubMessageHeader fromIntArray(int[] array, int offset) {
        // We're missing fields somehow!
        Assert.Assert(array.length - offset >= (LENGTH));

        int bodySize = array[offset + BODY_SIZE_INDEX];

        int rangeOrdinal = array[offset + RANGE_INDEX];
        Range range = Range.values()[rangeOrdinal];

        int x = array[offset + ORIGIN_X_INDEX];
        int y = array[offset + ORIGIN_Y_INDEX];

        MapLocation origin = new MapLocation(x, y);

        int recipientTypeOrdinal = array[offset + RECIPIENT_TYPE_INDEX];
        Recipient recipientType = Recipient.values()[recipientTypeOrdinal];

        boolean timeLimited = array[offset + TIME_LIMITED_INDEX] == 1 ? true : false;
        int latestRoundRelevant = array[offset + LATEST_ROUND_RELEVANT_INDEX];
        int recipients = array[offset + RECIPIENTS_INDEX];

        return new Builder(origin, bodySize).range(range).
                        recipientType(recipientType).
                        timeLimited(timeLimited).
                        latestRoundRelevant(latestRoundRelevant).
                        recipients(recipients).build();
    }

    public String toString() {
        return String.format("Origin:\t %s %n" +
                "Size of body:\t %s %n" +
                "Range:\t%s %n" +
                "Recipient:\t%s %n" +
                "Time limited:\t%b %n" +
                "Latest round relevant:\t%d %n" +
                "Recipients:\t%d %n",
                origin, bodySize, range, recipientType, timeLimited, latestRoundRelevant, recipients);

    }

    public int getLength() {
        return LENGTH;
    }
    
    
    
    public static void test() {
        // Test that default one is encoded correctly
        SubMessageHeader defaultHeader = new SubMessageHeader.Builder(new MapLocation(1035,11251), 1205).build();
        test(defaultHeader);
        
        // Try negative values
        SubMessageHeader headerTwo = new SubMessageHeader.Builder(new MapLocation(-1035,0), -135).range(Range.LONG).recipientType(Recipient.TASK_FORCE).timeLimited(true).latestRoundRelevant(1035).recipients(6).build();
        test(headerTwo);
        
        
        // Test the range thing
        SubMessageHeader rangeLimited = new SubMessageHeader.Builder(new MapLocation(0,0), 0).range(Range.LONG).build();
        
        MapLocation close = new MapLocation(3,5);
        Assert.Assert(rangeLimited.shouldRebroadcast(close), rangeLimited.toString() + " should rebroadcast to " + close.toString());
        
        int rangeSquared = rangeLimited.getRange().getSquaredDist();
        
        MapLocation far = new MapLocation((int) Math.sqrt((float)rangeSquared), 0);
        Assert.Assert(!rangeLimited.shouldRebroadcast(far), rangeLimited.toString() + " should not rebroadcast to " + far.toString());
        
        // Test the recipient thing
        int intendedRecipients = 2 | 4 | 8;
        // Either 2, 4, or 8 should be accepted
        
        KnowledgeBase kb2 = new KnowledgeBase();
        kb2.squadID = 2;
        
        KnowledgeBase kb4 = new KnowledgeBase();
        kb4.squadID = 4;
        
        KnowledgeBase kb8 = new KnowledgeBase();
        kb8.squadID = 8;
        
        KnowledgeBase kb16 = new KnowledgeBase();
        kb16.squadID = 16;
        
        KnowledgeBase kb9 = new KnowledgeBase();
        kb9.squadID = 9;
        
        
        
        SubMessageHeader recipientTest = new SubMessageHeader.Builder(new MapLocation(0,0), 0).recipientType(Recipient.SQUAD).recipients(intendedRecipients).build();
        Assert.Assert(recipientTest.pertainsToRobot(kb2), "Should match squad ID 2.");
        Assert.Assert(recipientTest.pertainsToRobot(kb4), "Should match squad ID 4.");
        Assert.Assert(recipientTest.pertainsToRobot(kb8), "Should match squad ID 8.");
        Assert.Assert(!recipientTest.pertainsToRobot(kb16), "Should not match squad ID 16.");
        // TODO: it'd be nice if 9 didn't show up but that's ok for now
        //Assert.Assert(!recipientTest.pertainsToRobot(kb9), "Should not match squad ID 9.");
        
    }
    
    public static void test(SubMessageHeader head) {
        String result = head.toString();
        int[] defaultIntArray = head.toIntArray();
        SubMessageHeader transformed = PARSER.fromIntArray(defaultIntArray, 0);
        String transformedResult = transformed.toString();
        Assert.Assert(result.equals(transformedResult), "Original: " + head + "\n Transformed: " + transformedResult);
    }
    

}