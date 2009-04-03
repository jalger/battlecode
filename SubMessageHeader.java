package jj_nick;

import battlecode.common.MapLocation;


/**
* Represents miscellaneous information that must be transfered along
* with every message in order for potential recipients to determine
* whether the rest of the message pertains to that robot, and whether
* it should be rebroadcast or not.
* @author Nicholas Dunn
* @date   03/27/09
*/
public class SubMessageHeader implements Transferable<SubMessageHeader>
{

    /**
    * Determines the intended range of the message.
    * Used to decide whether or not an SubMessage
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
    * SubMessageHeader f = new SubMessageHeader.Builder(new MapLocation(100, 200)).range(Range.SHORT).build();
    * This would create a SubMessageHeader with default values except for the range and the passed
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

        public SubMessageHeader build() {
            return new SubMessageHeader(this);
        }

    }

    // This is a hack, but I have no other idea how to do this...
    // we need this reference in order to call the fromIntArray method
    // but we can't :(
    public static final SubMessageHeader PARSER = new SubMessageHeader.Builder(null).build();



    // 6 variables, one of which requires two ints to represent
    public static final int NUM_FIELDS = 6;
    public static final int LENGTH = 7;


    public SubMessageHeader(Builder builder) {
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
            LENGTH,
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
    public SubMessageHeader fromIntArray(int[] array, int offset) {
        // We're missing fields somehow!
        if (array.length - offset < (NUM_FIELDS + 1)) {
            return null;
        }
        int rangeOrdinal = array[offset + 1];
        Range range = Range.values()[rangeOrdinal];

        int x = array[offset + 2];
        int y = array[offset + 3];

        MapLocation origin = new MapLocation(x, y);

        int recipientTypeOrdinal = array[offset + 4];
        Recipient recipientType = Recipient.values()[recipientTypeOrdinal];

        boolean timeLimited = array[offset + 5] == 1 ? true : false;
        int latestRoundRelevant = array[offset + 6];
        int recipients = array[offset + 7];

        return new Builder(origin).range(range).
                        recipientType(recipientType).
                        timeLimited(timeLimited).
                        latestRoundRelevant(latestRoundRelevant).
                        recipients(recipients).build();
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

    public int getLength() {
        return LENGTH;
    }

}