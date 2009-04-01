package jj_nick;

/**
* @author Nicholas Dunn
* 
*/
public interface Transferable {
    
    /**
    * Every transferable object must be able to be encoded in an int array
    */
    public int[] toIntArray();
    
    /**
    * Every transferable object must be able to be decoded from an int array,
    * and in particular a subset of the int array
    * @return true if successful, else false
    */
    public boolean fromIntArray(int[] array, int offset);
}