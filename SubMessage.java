package jj_nick;

/**
* A submessage represents meta information that tells a robot whether
* the body of the message is intended for it, as well as the body
* of the message itself.
*

*
* @author Nicholas Dunn
* @date   April 4, 2009
*/
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
        SubMessageHeader header = SubMessageHeader.PARSER.fromIntArray(ints, offset);
        
        SubMessageBody body = null;
        
        int newOffset = offset + header.getLength();
        switch (ints[newOffset]) {
            case Command.IDENTIFIER:
                //body = Command.PARSER.fromIntArray(ints, newOffset);
                break;
            case Information.IDENTIFIER:
                //body = Information.PARSER.fromIntArray(ints, newOffset);
                break;
            default:
                Assert.Assert(false, "Error, unrecognized SubMessageBody id: " 
                            + ints[newOffset]);
        }
        return new SubMessage(header, body);
    }
    
    public SubMessage fromIntArray(int[] ints, int offset) {
        return parse(ints, offset);
    }
    
    public int[] toIntArray() {
        int[] header = this.header.toIntArray();
        int[] body = this.body.toIntArray();
        
        int[] result = new int[header.length + body.length];
        System.arraycopy(header,0,result,0,header.length);
        System.arraycopy(body,0,result,header.length,body.length);
        return result;
    }
}