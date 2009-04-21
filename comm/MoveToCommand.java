package teamJA_ND.comm;


import battlecode.common.Clock;

public class MoveToCommand extends SubMessageBody{
	private int x;
	private int y;
	private boolean absolute;
	public static final int ID = SubMessageBody.MOVE_TO_ID;
	public static final MoveToCommand PARSER = new MoveToCommand(0, 0, false);

    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    

	public MoveToCommand (int x, int y, boolean absolute) {
		this.x = x;
		this.y = y;
		this.absolute = absolute;
	}

	public int getLength() {
		return 5;
	}
	public int getID() { return ID; }

	public void toIntArray(int[] array, int offset) {
		array[offset] = getLength();
		array[offset + 1] = ID;
		array[offset +2] = x;
		array[offset +3] = y;
		array[offset +4] = absolute ? 1 : 0;
	}
	public MoveToCommand fromIntArray(int[] array, int offset) {
		int counter = 2 + offset;
		int x = array[counter+0];
		int y = array[counter +1];
		boolean absolute = (array[counter +2] == 1);
		return new MoveToCommand(x, y, absolute);
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean getAbsolute() {
		return absolute;
	}

    public void debug_tick() {
        clockTurnNum = Clock.getRoundNum();
        clockByteNum = Clock.getBytecodeNum();
    }
    
    public void debug_tock() {
        int turnFinal = Clock.getRoundNum();
        int bytesFinal = Clock.getBytecodeNum() - 1; //The -1 accounts for the cost of calling debug_tock().
        int delta = bytesFinal - clockByteNum + BYTES_PER_ROUND*(turnFinal - clockTurnNum);
        System.out.println(delta + " bytecodes used since calling debug_tick().");
    }
    }