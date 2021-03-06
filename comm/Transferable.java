package teamJA_ND.comm;

/**
* @author Nicholas Dunn
* 
*/
public interface Transferable<T> {
    
    /**
    * Every transferable object must be able to be encoded in an int array
    */
    public void toIntArray(int[] array, int offset);
    
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