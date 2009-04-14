package teamJA_ND.comm;
import teamJA_ND.util.Assert;

/**
* @author Nicholas Dunn
* 
*/
public interface Transferable<T> {
    
    /**
    * Every transferable object must be able to be encoded in an int array
    */
    public int[] toIntArray();
    
    /**
    * Every transferable object must be able to be decoded from an int array,
    * and in particular a subset of the int array
    * @return true if successful, else false
    */
    public T fromIntArray(int[] array, int offset);
    
    /**
     * 
     * @return the number of ints it takes to represent this transferable
     * object
     */
    public int getLength();

    /**
    * Each transferable object must implement a unique ID that identifies it
    * so that we can ensure the correct parsers are called
    */
        //public int getUniqueID();
}