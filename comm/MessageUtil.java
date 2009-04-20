package teamJA_ND.comm;

import battlecode.common.Clock;
import battlecode.common.MapLocation;
import battlecode.common.Message;
import java.util.LinkedList;
import java.util.List;
import teamJA_ND.util.Assert;
import teamJA_ND.KnowledgeBase;

/**
* A Message contains a String array, an int array, and a
* MapLocation array.  We primarily use the int array in
* order to encode all the different information we may
* wish to send, including but not limited to Attack
* Square X, Retreat to square Y, and so on and so forth.
*
* Each message encoded within the int array has both a MiscInfo object
* and either a Command or Information associated with it.  The MiscInfo
* object is used to determine whether the message as a whole pertains to
* the current robot, and whether the robot should rebroadcast that
* portion of the message.
*
* Since both communication from us and from our enemy flows
* along the same channel, we need to worry about two main things:
* 1) Not acting on messages intended for the other team as if
* they were our own.
* 2) Not acting on messages we sent that the enemy received and
* somehow garbled and rebroadcast
*
* Constantly hashing our messages in order to determine if
* they are legitimate is a significant computational expense,
* and one we cannot afford, especially when sending large
* pieces of the map.  As such we include a piece of data relevant to the
* current state of the game that it would be impossible or very difficult
* for enemy robots to come up with themselves.  Furthermore a hash of this
* information is included as well, so if they just use a preexisting one
* and modify it, the hash will not match.
*
* This piece of information helps us determine the legitimacy in different
* ways depending on where the robot receiving the message is located.
* If the robot is farther than the broadcast range of the message, and it
* notices that in the information, then it knows an enemy robot
* rebroadcasted our message, modified or not.
* If a robot close to the original origin of the message receives two messages
* with this same header information, one of them must be the enemy trying
* to mess with us, since the information encoded within changes both for
* each turn, as well as for each robot who transmits.
*/
public class MessageUtil {

    // 3 for information, one for hash
    private static final int NUM_HEADER_FIELDS = 4;
    private static final int ROBOT_ID_INDEX = 0;
    private static final int ROBOT_MESSAGE_ID_INDEX = 1;
    private static final int CLOCK_INDEX = 2;
    private static final int HASH_INDEX = 3;

    // We include just 1 MapLocation in the message
    private static final int NUM_MAP_LOCATIONS = 1;

    public static void main (String [] args)
    {

    }


    /**
    * @return a list of SubMessage objects, each of which contains 
    * information about what action to take, or general information.
    **/
    public List<SubMessage> getRelevantSubMessages(Message m, KnowledgeBase kb) {


        // if Message is from our team
        if (fromOurTeam(m)) {
            System.out.println("The message " + m + "is from our team.");
            
            // If it's been tampered with, ignore it
            if (!isLegitimate(m)) {
                System.out.println("Message " + m + " has been tampered with");
                return null;
            }
            
            // Else it's legitimate.
            else {
                // We will go through each header and determine if the 
                // corresponding message pertains to us.  Only then do we need
                // to bother with parsing the message.
                
                // Parse all of the message headers.
                List <SubMessageHeader> headers = unpackHeaders(m);
                
                int start = NUM_HEADER_FIELDS;
                List <SubMessageBody> bodies = new LinkedList<SubMessageBody>();
                List <SubMessage> submessages = new LinkedList<SubMessage>();
                
                for (SubMessageHeader h : headers) {
                    if (h.pertainsToRobot(kb)) {
                        // Parse the body that corresponds with the header
                        SubMessageBody b = SubMessageBody.parse(m.ints, start);
                        bodies.add(b);
                        
                        submessages.add(new SubMessage(h, b));
                    }
                    /* Right now we are not rebroadcasting for simplicity.
                    if (h.shouldRebroadcast()) {
                        // Make sure that the resulting int stuff ends up there
                    }*/
                    
                    // We're done looking at this SubMessage; skip enough space
                    // to reach the next one.
                    start += h.getLength() + h.getBodySize();
                }
                
                return submessages;
                
            } // legitimate
        } // from our team

        // else it's from the other team
        else {
            // deal with knowledge we can gain from message (early detection of robots)

            // decide whether to try to screw up enemy communication by editing their message
            // and rebroadcasting
            System.out.println("Intercepted enemy message" + m);
        }
        return null;
    }


    /**
     *
     * @param m the message to unpack
     * @prerequisite m is not null, m is a message in the format of our team
     * @return
     */
    public static List<SubMessage> unpack(Message m) {
        int[] encodedMessages = m.ints;
        List<SubMessage> subMessages = new LinkedList<SubMessage>();

        // The first NUM_HEADER_FIELDS of int field have nothing to do
        // with the submessages contained inside; ignore them

        int start = NUM_HEADER_FIELDS;
        // We keep track of where the next submessage starts; if it is equal
        // to or greater than the length of the int array then we are done
        while (start < m.ints.length) {
            SubMessage message = SubMessage.parse(encodedMessages, start);
            start += message.getLength();
            subMessages.add(message);
        }

        return subMessages;
    }
    
    public static List <SubMessageHeader> unpackHeaders(Message m) {
        int[] encodedMessages = m.ints;
        List<SubMessageHeader> subMessageHeaders = 
            new LinkedList<SubMessageHeader>();
        
        // The first NUM_HEADER_FIELDS of int field have nothing to do
        // with the submessages contained inside; ignore them

        int start = NUM_HEADER_FIELDS;
        // We keep track of where the next submessage starts; if it is equal
        // to or greater than the length of the int array then we are done
        while (start < m.ints.length) {
            SubMessageHeader header = SubMessageHeader.PARSER.fromIntArray(encodedMessages, start);
            start += header.getLength() + header.getBodySize();
            subMessageHeaders.add(header);
        }

        return subMessageHeaders;
    }


    public static Message pack(List<SubMessage> messages, 
                               MapLocation currentSquare,
                               int robotID,
                               int robotMessageID) {
                               
        // Create a header and encode the location of sending robot
        int[] header = createHeader(robotID, robotMessageID);
        MapLocation[] locations = new MapLocation[] { currentSquare };

        // Pack all of the messages into their int arrays; need twice as much
        // space because 
        int[][] packedMessages = new int[2 * messages.size()][];
        int totalSize = header.length;
        int counter = 0;
        for (SubMessage m : messages) {
            int[] packedMessage = m.toIntArray();
            totalSize += packedMessage.length;
            packedMessages[counter++] = packedMessage;
        }

        // We now have all of our messages packed in a two dimensional
        // array; we need to flatten the data structure into a single int
        // array
        int[] wholeMessage = new int[totalSize];
        // Copy header into place
        System.arraycopy(header, 0, wholeMessage, 0, header.length);

        // Copy the rest of the messages into place
        for (int i = 0, curPos = header.length; i < packedMessages.length; i++) {
            int[] packed = packedMessages[i];
            System.arraycopy(packed, 0, wholeMessage, curPos, packed.length);
            curPos += packed.length;
        }


        Message m = new Message();
        m.strings = null;
        m.ints = wholeMessage;
        m.locations = locations;

        return m;
    }



    private static int[] createHeader(int robotID, int robotMessageID) {
        // Fill an array with all the information that we use to distinguish
        // legitimate information
        int[] header = new int[NUM_HEADER_FIELDS];
        header[ROBOT_MESSAGE_ID_INDEX] = robotMessageID;
        header[ROBOT_ID_INDEX] = robotID;
        header[CLOCK_INDEX] = Clock.getRoundNum();
        
        // Make a hash out of previous 3 values
        header[HASH_INDEX] = simpleHash(header, 0, 3);
        return header;
    }

    /**
    * Determines whether this message appears to
    * have originated from our team.
    */
    public static boolean fromOurTeam(Message m) {
        int[] ints = m.ints;
        String[] strings = m.strings;

        // We always include ints and MapLocations
        if (ints == null || m.locations == null) {
            return false;
        }
        // Only ever encode one map location
        if (m.locations.length != NUM_MAP_LOCATIONS) {
            return false;
        }
        // We do not pass messages as strings
        if (strings != null) {
            return false;
        }

        // Check that the header information stored in
        // ints array is legitimate
        return isWellFormedHeader(ints);

        // TODO: check to see if the distance from origin etc is legitimate


    }

    /**
    * Given an int array, examine the header and determine if it
    * matches our format and that the hash stored within the header
    * matches the hash of the message
    */
    public static boolean isWellFormedHeader(int[] header) {
        // Check for the hash to match
        int storedHash = header[header.length - 1];
        int resultHash = simpleHash(header, 0, header.length - 1);

        return storedHash == resultHash;
    }


    /**
    * Given a Message that is ostensibly from our team, determine
    * whether it has been tampered with.
    * Assumes that the strings and ints fields are not null.
    * @param m the Message to check for legitimacy
    */
    public static boolean isLegitimate(Message m) {
        String[] strings = m.strings;
        int[] ints = m.ints;

        // Check for violations of precondition; fromOurTeam should have handled
        // this earlier.
        Assert.Assert (strings != null && ints != null);

        // Check to see if there is exactly one integer (hash) per string
        if (strings.length != ints.length) {
            return false;
        }


        // Ensure that the hash of the header matches
        if (simpleHash(strings[0]) != ints[0]) {
            return false;
        }
        return false;
    }

    
    
    /**
    * Creates a hash of the given String in a simpler and less
    * computationally expensive way than the standard hashcode() function
    * Implements the djb2 hashing algorithm, reported by Dan Bernstein.
    * Modified from C source
    * {@link http://www.cse.yorku.ca/~oz/hash.html}
    * @param s the String to hash
    */
    public static int simpleHash(String s) {
        int i = 0;
        char[] letters = s.toCharArray();

        int hash = 5381;
        for (char c : letters) {
            hash = ((hash << 5) + hash) + c; // hash * 33 + c
        }

        return hash;
    }

    /**
    * Creates a hash of the given int array in a simpler and less
    * computationally expensive way than the standard hashcode() function
    * Implements the djb2 hashing algorithm, reported by Dan Bernstein.
    * Modified from C source
    * {@link http://www.cse.yorku.ca/~oz/hash.html}
    * @param numbers the array of ints to hash
    **/
    public static int simpleHash(int[] numbers) 
    {
        return simpleHash(numbers, 0, numbers.length);
    }

    public static int simpleHash(int[] numbers, int startIndex, 
                                int numberOfElements) 
    {

        int hash = 5381;
        for (int i = startIndex; i < startIndex + numberOfElements; i++) {
            hash = ((hash << 5) + hash) + numbers[i]; // hash * 33 + c
        }

        return hash;
    }



    
    

    

}

