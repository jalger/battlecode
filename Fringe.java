package teamJA_ND;


/**
* A fringe represents newly explored territory that a robot has just
* discovered at the edge of its sensor range and wishes to transfer to
* other robots
*/


import teamJA_ND.comm.*;
import teamJA_ND.state.*;
import teamJA_ND.util.*;

public class Fringe implements Transferable<Fringe> {


    private Point[] points;
    private boolean[] groundTraversable;
    private int[] heights;

    public static final Fringe PARSER = new Fringe(null,null,null);

    /** The points array takes two ints per Point, booleans take 1, and
    * heights take 1.  Therefore it takes 4 times as long an array as the
    * length of any of our instance fields.
    */
    private static final int SIZE_SCALE = 4;

    /**
    * @prerequisite all three arrays are of the same length
    */
    public Fringe(Point[] points, boolean[] groundTraversable, int[] heights) {
        /*Assert.Assert(  groundTraversable.length == points.length &&
                        points.length == heights.length, "Lengths of arrays must"+
                        "match");
        */


        this.points = points;
        this.groundTraversable = groundTraversable;
        this.heights = heights;
    }


    public Point[] getPoints() { return points; }
    public boolean[] getGroundTraversable() { return groundTraversable; }
    public int[] getHeights() { return heights; }


    public int getLength() {
        return SIZE_SCALE * points.length + 1;
    }


    /**
    * TODO : Fix the .equals
    */
    public boolean equals(Fringe other) {
        return  java.util.Arrays.equals(points, other.points) &&
                java.util.Arrays.equals(heights, other.heights) &&
                java.util.Arrays.equals(groundTraversable, other.groundTraversable);
    }

    public String toString() {
        return java.util.Arrays.toString(points) +
                java.util.Arrays.toString(heights) +
                java.util.Arrays.toString(groundTraversable);

    }

    /**
    * Packs the Fringe into an array of integers, in the format
    * SIZE points[0].x points[0].y ... points[n].x points[n].y
    * groundTraversable[0] ... groundTraversable[n] heights[0] ... heights[n]
    */
    public int[] toIntArray() {
        // Need an extra space to hold this SIZE entry.

        int SIZE = SIZE_SCALE * points.length + 1;
        int [] packedMessage = new int[SIZE];
        packedMessage[0] = SIZE;


        int counter = 1;
        // Encode all of the points
        for (Point p : points) {
            packedMessage[counter++] = p.x;
            packedMessage[counter++] = p.y;
        }

        // Encode the booleans
        for (boolean b : groundTraversable) {
            packedMessage[counter++] = b ? 1 : 0;
        }

        // Encode the heights
        for (int h : heights) {
            packedMessage[counter++] = h;
        }

        return packedMessage;


    }

    /**
    *
    */
    public Fringe fromIntArray(int[] array, int offset) {
        int size = array[offset];
        // The points array takes two ints per Point, booleans take 1, and
        // heights take 1.
        final int numPoints = (size - 1) / SIZE_SCALE;

        Point[] points = new Point[numPoints];
        boolean[] bools = new boolean[numPoints];
        int[] ints = new int[numPoints];

        final int pointOffset = offset + 1;
        final int boolOffset = offset + 1 +  (2 * numPoints);
        final int intOffset = offset + 1 +  (3 * numPoints);

        // Make the points, bools, and ints in one pass
        for (int i = 0; i < numPoints; i++) {

            points[i] = new Point(array[pointOffset + (2 * i)], array[pointOffset + (2 * i) + 1]);

            bools[i] = array[boolOffset + i ] == 1;

            ints[i] = array[intOffset + i];
        }

        return new Fringe(points, bools, ints);
    }



    /**
    *
    */
    public static void main (String[] args) {
        Point [] points1 = new Point[]
        {
            new Point(100, 200),
            new Point(0,0),
            new Point(-120581204,1),
            new Point(105,105),
            new Point (100, 20+31),
            new Point(1500, 200),
            new Point(0,20),
            new Point(-125204,152),
            new Point(105,1052),
            new Point (100, 206)
        };
        boolean [] traversable = new boolean[]
        {
            true,
            true,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            true
        };

        int [] heights = new int[]
        {
            1,
            3,
            5,
            -1,
            0,
            26,
            6,
            1,
            2,
            3
        };

        Fringe f = new Fringe(points1, traversable, heights);
        int[] h =  f.toIntArray();

        int[] array = new int[heights.length + 236];
        System.arraycopy(h, 0, array, 135, h.length);

        Fringe g = PARSER.fromIntArray(array, 135);

        //arraycopy(Object src, int srcPos, Object dest, int destPos, int length)

        System.out.println("f" + f);
        System.out.println("g" + g);
        System.out.println(f.equals(g));

        /*
        Assert.Assert(f.equals(g));
        */

    }



}