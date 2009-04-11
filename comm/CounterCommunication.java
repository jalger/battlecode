package teamJA_ND.comm;

import battlecode.common.Message;

import teamJA_ND.util.Assert;

public class CounterCommunication 
{

    private static final String HUNDRED_BYTE_STRING = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent ultricies. Phasellus in urna amet. "; // 100 bytes
    private static final String TWO_HUNDRED_BYTE_STRING = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce facilisis varius risus. Sed lorem nibh, porta a, faucibus vitae, malesuada sit amet, tortor. Nam ornare nibh. Suspendisse ornare posuere. "; // 200 bytes
    private static final String FIVE_HUNDRED_BYTE_STRING = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In vehicula. Fusce luctus massa et nulla. Praesent eget eros nec tellus vestibulum tincidunt. Curabitur rutrum suscipit felis. Quisque euismod. Phasellus nec tortor. Ut nisl metus, faucibus sed, adipiscing in, vehicula ut, ligula. Aenean iaculis convallis tellus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet sapien. Donec id sapien. Praesent nec sapien nec risus consequat volutpat. Integer orci. Aenean augue amet. "; // 500 bytes
    private static final String THOUSAND_BYTE_STRING = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ligula magna, convallis sit amet, ornare nec, posuere sit amet, turpis. Etiam tempus felis a dolor. Aenean adipiscing pellentesque leo. Vivamus ligula metus, semper et, elementum congue, posuere sit amet, sapien. Quisque justo ligula, dignissim eget, sagittis vel, ornare sit amet, quam. Mauris condimentum odio non arcu. Nullam nec mi sed libero viverra pretium. Integer dictum erat sit amet mauris rutrum hendrerit. Fusce ac eros quis augue dapibus fringilla. Cras tempor adipiscing lacus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sit amet sapien. Sed nisi metus, fermentum gravida, mattis a, sodales vitae, nibh. Vivamus pulvinar ultricies orci. Nulla facilisi. Pellentesque ut nunc eget justo blandit tincidunt. Morbi tempus justo vestibulum augue. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Proin ac nunc at ante tristique posuere. Mauris mi. In semper felis amet. ";//1000 bytes


    private static final int[] HUNDRED_RANDOM_INTS = new int[] 
    {
        710945,  661997,  1226290,  534868,  112809,  581547,  212910,  669282, 
        394975,  551149,  500869,  1014562,  567211,  1207343,  8344,  877443,  
        121776,  1177924,  208367,  181931,  1052690,  1206548,  173276,  58199,  
        1090665,  305432,  982921,  1038699,  684030,  424290,  345984,  866808,  
        1201248,  415143,  790775,  1187362,  766377,  1012461,  896854,  1114416,  
        168447,  527588,  591625,  989979,  1140219,  679634,  318087,  627270,  
        393585,  403484,  791022,  800086,  970046,  178829,  928234,  1057771,  
        1138508,  23014,  771108,  294660,  822401,  79103,  784946,  282340,  
        35702,  650878,  402101,  864857,  1116699,  705054,  468481,  370455,  
        94697,  988514,  859348,  203253,  297911,  176833,  1040953,  807012,  
        680645,  114126,  191471,  35227,  773016,  993503,  315812,  -4646,  
        217472,  323321,  818663,  276794,  180621,  277466,  1133064,  916528,
        232712,  492410,  202799,  729655
    };


    private static final int[] FIFTY_RANDOM_INTS = new int[] 
    {
        739467,  525356,  805610,  785562,  855612,  
        907236,  481431,  1058888,  293190,  135444,  
        185364,  640235,  927087,  428718,  720905,  
        1015478,  907898,  1247513,  857419,  245587,  
        200392,  676535,  1095847,  583938,  337886,  
        353100,  1208218,  193138,  543694,  688,  
        492405,  697221,  225100,  314424,  302913,  
        987428,  309005,  1140122,  565548,  119191,  
        1120882,  510891,  1200994,  535300,  144503,  
        -7261,  4862,  638532,  497628, 488626
    };




    /*--------------------------------------------------------------------
    * These methods have to do with disrupting/jamming enemy communication
    *--------------------------------------------------------------------*/
    /*
    -Malicious communication
        +Null fields
        +Corrupted copies of messages
        +Extremely long messages - long array
        +Extremely long messages - long String in element 1 of String array
        +Observe for success
        +Guess at enemy message meaning?
    */

    /**
    *
    **/
    public static void corruptFields(Message m)
    {
    
    }

    /**
    * 
    **/
    public static void nullOutFields(Message m) 
    {
        m.ints = null;
        m.strings = null;
    
    }

    /**
    * Creates a long int array filled with nonsense
    * @param m the message to corrupt
    **/
    public static void sendRandomInts(Message m)
    {
       m.ints = HUNDRED_RANDOM_INTS; 
    }

    /**
    * Creates a single huge String and places it in the
    * strings portion of their message
    **/
    // TODO: make sure their String array isn't null
    public static void sendHugeString(Message m)
    {
        Assert.Assert(m.strings != null);
        m.strings[0] = FIVE_HUNDRED_BYTE_STRING;
    }
}