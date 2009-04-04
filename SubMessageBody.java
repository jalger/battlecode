package jj_nick;

public abstract class SubMessageBody implements Transferable<SubMessageBody>
{
    public abstract int getLength();

    public abstract SubMessageBody fromIntArray(int[] array, int offset);

    public abstract int[] toIntArray();

}