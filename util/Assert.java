package util.teamJA_ND;

/**
* Stupid little class to let us to assertions since battlecode does not
* @author Nick Dunn
*/
public class Assert {
    
    
    public static void Assert(boolean b) {
        if (!b) {
            System.exit(1);
        }
    }
    
    public static void Assert(boolean b, String errorMessage) {
        if (!b) {
            System.out.println(errorMessage);
            System.exit(1);
        }
    }
}