package teamJA_ND.comm;

public class MoveToCommand extends SubMessageBody {
    
    private final int x;
    private final int y;
    private final boolean absolute;
    
    private static final int LENGTH = SubMessageBody.MIN_REQUIRED_FIELDS + 3;
    private static final int ID = SubMessageBody.SubMessageBodyType.MOVE_TO.getID();
    
    public static final MoveToCommand PARSER = new MoveToCommand(0,0,false);
    
    public MoveToCommand(int x, int y, boolean absolute) {
        this.x = x;
        this.y = y;
        this.absolute = absolute;
    }
    
    public int getID() { return ID; } 

    public int getLength() { return LENGTH; }

    public SubMessageBody fromIntArray(int[] array, int offset) {
        int x = array[offset + SubMessageBody.MIN_REQUIRED_FIELDS];
        int y = array[offset + SubMessageBody.MIN_REQUIRED_FIELDS + 1];
        boolean absolute =  array[offset + SubMessageBody.MIN_REQUIRED_FIELDS + 2] == 1;
        return new MoveToCommand(x, y, absolute);
    }

    public int[] toIntArray() {
        return new int[] { LENGTH, ID, x, y, absolute ? 1 : 0};
    }
    
}
