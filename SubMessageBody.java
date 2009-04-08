package teamJA_ND;

public abstract class SubMessageBody implements Transferable<SubMessageBody>
{
    public abstract int getLength();

    public abstract SubMessageBody fromIntArray(int[] array, int offset);

    public abstract int[] toIntArray();

}