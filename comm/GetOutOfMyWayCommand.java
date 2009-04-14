package teamJA_ND.comm;


public class GetOutOfMyWayCommand extends SubMessageBody {

    private final int robotID;
    private static final int LENGTH = SubMessageBody.MIN_REQUIRED_FIELDS + 1;
    private static final int ID = SubMessageBody.SubMessageBodyType.GET_OUT_OF_WAY.getID();
    
    public static final GetOutOfMyWayCommand PARSER = new GetOutOfMyWayCommand(0);
    
    public GetOutOfMyWayCommand(int robotID) {
        this.robotID = robotID;
    }
    
    public int getID() { return ID; } 

    public int getLength() { return LENGTH; }

    public SubMessageBody fromIntArray(int[] array, int offset) {
        int robotID = array[offset + LENGTH - 1];
        return new GetOutOfMyWayCommand(robotID);
    }

    public int[] toIntArray() {
        return new int[] { LENGTH, ID, robotID};
    }
}