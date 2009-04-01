package jj_nick;

import battlecode.common.Clock;
import battlecode.common.Message;
import battlecode.common.MapLocation;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;



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

    // We include just 1 MapLocation in 
    private static final int NUM_MAP_LOCATIONS = 1;

   


    public static void main (String [] args)
    {
        /*
        MapLocation curSquare = new MapLocation(100, 200);
        int robotID = 1235124;
        int robotMessageID = 0;
        
        ExtendedMessage theMessage = new DefaultExtendedMessage();
        List<ExtendedMessage> messageList = new LinkedList<ExtendedMessage>();
        messageList.add(theMessage);
        
        Message m = pack(messageList, curSquare, robotID, robotMessageID);
        
        System.out.println(Arrays.toString(m.ints));
        System.out.println(Arrays.toString(m.strings));
        System.out.println(Arrays.toString(m.locations));*/
        
    }


    public void handleMessage(Message m) {

    // if Message is from our team
        // If it's been tampered with, ignore it
    
        // Else it's legitimate.  
            // Parse it, take action if necessary


            // If should rebroadcast
                // rebroadcast, changing the header information to match
                // current state of game and robot information
                
            
    // else it's from the other team
        // deal with knowledge we can gain from message (early detection of robots)
    
        // decide whether to try to screw up enemy communication by editing their message
        // and rebroadcasting
    
    }
    
    
    public static Message pack(List<ExtendedMessage> messages, MapLocation currentSquare, 
                                int robotID, 
                                int robotMessageID) {
                                    /*
        String[] messageStrings = new String[messages.size()];
        int counter = 0;
        for (ExtendedMessage m : messages) {
            messageStrings[counter++] = m.toString();//toMessageString();
        }
        int[] header = createHeader(robotID, robotMessageID);
        MapLocation[] locations = new MapLocation[] { currentSquare };
        
        Message m = new Message();
        m.strings = messageStrings;
        m.ints = header;
        m.locations = locations;
        
        return m;*/
        return null;
    }


    
    private static int[] createHeader(int robotID, int robotMessageID) {
        
       
        // Fill an array with all the information that we use to distinguish
        // legitimate information
        int[] headerInfo = new int[] {
            robotID,
            robotMessageID,
            Clock.getRoundNum()
        };
        // Make a cryptographic hash out of this information
        int hash = simpleHash(headerInfo);
        // Make an array big enough to hold all the header info and hash
        // information
        int header[] = new int[headerInfo.length + 1];
        System.arraycopy(headerInfo, 0, header, 0, headerInfo.length);
    
        header[headerInfo.length] = hash;

        return header;
    }




    public static boolean pertainsToMe() {
        // Check if there is an intended robot ID and it matches you
    
    
        // Check if there is a group of intended recipients and it matches one of your groups
    
        // Check if there is a specified robot type and it matches your robot type
    
        return false;
    }

    /**
    * Determines whether this message appears to
    * have originated from our team.
    */        
    public static boolean fromOurTeam(Message m) {
        int[] ints = m.ints;
        String[] strings = m.strings;

        // We always include strings and ints and MapLocations
        if (strings == null || ints == null || m.locations == null) {
            return false; 
        }
        
        if (m.locations.length != NUM_MAP_LOCATIONS) {
            
        }
    
        // Check that the header information stored in
        // ints array is legitimate
        return (isWellFormedHeader(ints));
    
    }   
    
    /**
    * Given a header, determine if it matches our format and that
    * the hash stored within the header matches the hash of the
    
    */
    public static boolean isWellFormedHeader(int[] header) {
        if (header.length != NUM_HEADER_FIELDS) {
            return false;
        }

        // Check for the hash to match
        int storedHash = header[header.length - 1];
        int resultHash = simpleHash(header, 0, NUM_HEADER_FIELDS - 1);

        return (storedHash == resultHash);
        
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
        assert (strings != null && ints != null);
    
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
    */
    public static int simpleHash(int[] numbers) {
        return simpleHash(numbers, 0, numbers.length);
    }

    public static int simpleHash(int[] numbers, int startIndex, int numberOfElements) {
      
        int hash = 5381;
        for (int i = startIndex; i < startIndex + numberOfElements; i++) {
            hash = ((hash << 5) + hash) + numbers[i]; // hash * 33 + c
        }
    
        return hash;
    }

        
        
    public static boolean shouldRebroadcast(ExtendedMessage m) {
        // If it's a time limited message, and I'm past the time limit,
        // don't broadcast
        /*
        if (m.isTimeLimited()) {
            int latestRound = m.getLatestRoundRelevant();
            if (latestRound <= Clock.getRoundNum()) {
                
            }
        }*/
    

        // If I'm at the edge of the range of the intended audience
        // of this message, don't rebroadcast
    
        return false;

    }        

}
        
        