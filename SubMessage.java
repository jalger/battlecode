package jj_nick;

public class SubMessage implements Transferable<SubMessage> {

    private SubMessageHeader header;
    private SubMessageBody body;

    public static final SubMessage PARSER = new SubMessage(null, null);

    public SubMessage(SubMessageHeader header, SubMessageBody body) {
        this.header = header;
        this.body = body;
        
    }

    public int getLength() {
        return header.getLength() + body.getLength();
    }

    public static SubMessage parse(int[] ints, int offset) {
        return null;
    }

    public SubMessage fromIntArray(int[] ints, int offset){
        return null;
    }
    
    public int[] toIntArray() {
        return null;
    }

    

}