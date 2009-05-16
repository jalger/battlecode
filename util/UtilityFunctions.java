package teamJA_ND.util;

import battlecode.common.*;
import java.util.Comparator;

/**
* @author Nicholas Dunn
* @date   May 13, 2009
**/
public class UtilityFunctions {
    
    /**
    * @return how many units are on your team that are not archons
    **/
    public static int getNumNonArchons(RobotController rc) {
        return rc.getTeamUnitCount() - rc.getUnitCount(RobotType.ARCHON);
    }
    
    
    
    /**
    * Iterates over the array and finds the min element, as specified by the 
    * comparator c.
    * Modified from the sort prototype at
    *  http://java.sun.com/j2se/1.5.0/docs/api/java/util/Arrays.html#sort(T[],%20int,%20int,%20java.util.Comparator)
    * @param a - the array in which to find minimum
    * @param fromIndex - the index of the first element (inclusive) to be searched.
    * @param toIndex - the index of the last element (exclusive) to be searched.
    * @c - the comparator to determine the order of the array. 
    **/
    public static <T> T min(T[] a, int fromIndex, int toIndex, Comparator<? super T> c)
    {
        T minElement = a[fromIndex];
        for (int i = fromIndex; i < toIndex; i++) {
            T curElement = a[i];
            // A negative result means first arg is less than second
            if (c.compare(curElement, minElement) < 0) {
                minElement = curElement;
            }
        }
        return minElement;
    }

    /**
    * Iterates over the array and finds the max element, as specified by the 
    * comparator c.
    * Modified from the sort prototype at
    *  http://java.sun.com/j2se/1.5.0/docs/api/java/util/Arrays.html#sort(T[],%20int,%20int,%20java.util.Comparator)
    * @param a - the array in which to find minimum
    * @param fromIndex - the index of the first element (inclusive) to be searched.
    * @param toIndex - the index of the last element (exclusive) to be searched.
    * @c - the comparator to determine the order of the array. 
    **/
    
    public static <T> T max(T[] a, int fromIndex, int toIndex, Comparator<? super T> c)
    {
        T maxElement = a[fromIndex];
        for (int i = fromIndex; i < toIndex; i++) {
            T curElement = a[i];
            // A negative result means first arg is less than second
            if (c.compare(curElement, maxElement) > 0) {
                maxElement = curElement;
            }
        }
        return maxElement;
    }
    
    
    public static void testMinAndMax() {
        
        Comparator<Integer> intComparator = new Comparator<Integer>() {
            public int 	compare(Integer i1, Integer i2) {
                
                return i1.intValue() - i2.intValue();
            }

            public boolean	equals(Object obj) { return false; }
        } ;
        
        
        Integer[] array  = new Integer[] {
            1,
            3,
            -251,
            4,
            0,
            -15151616,
            23062
        };
        
                
        Integer minValue = min(array, 0, array.length, intComparator);
        Integer maxValue = max(array, 0, array.length, intComparator);
        Assert.Assert(minValue.intValue() == -15151616);
        Assert.Assert(maxValue.intValue() == 23062);
    }
}