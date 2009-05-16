package teamJA_ND.comm;
/**
* A submessage represents meta information that tells a robot whether
* the body of the message is intended for it, as well as the body
* of the message itself.
*
* SubMessageHeaders and SubMessageBodies are similar in that both must
* be able to be converted to and from an int array.  Since these objects
* are serialized into a series of ints, there are two important things they
* must do: indicate how big they are (how many elements within the int array)
* as well as what type of object they are.
*
* @author Nicholas Dunn
* @date   April 4, 2009
**/
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
        int newOffset = offset + header.getLength();
        SubMessageBody body = SubMessageBody.parse(ints, newOffset);
        return new SubMessage(header, body);
    }
    
    public SubMessage fromIntArray(int[] ints, int offset) {
        return parse(ints, offset);
    }
    
    public void toIntArray(int[] array, int offset) {
        header.toIntArray(array, offset);
        body.toIntArray(array, offset + header.getLength());
    }
    
    public String toString() {
        return "Header: " + header.toString() + "\n" + "Body: " + body.toString();
    }

    public SubMessageBody getBody() {
        return body;
    }
}