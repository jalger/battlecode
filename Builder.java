package jj_nick;

/**
* @author Nicholas Dunn
* @date   March 31, 2009
* Builder pattern described in more depth in Joshua Bloch's
* Effective Java Second Edition, Item 2.
*/
// A builder for objects of type T
public interface Builder<T> {
    public T build();
    
    //public T build(int[] array, int offset);
}