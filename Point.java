package jj_nick;

/**
 *
 * @author Jeff Alger, Nick Dunn
 * @date   April 1, 2009
 */
public class Point {

    public static final Point WEST = new Point(-1,0);
    public static final Point NORTH_WEST = new Point(-1,-1);
    public static final Point NORTH = new Point(0,-1);
    public static final Point NORTH_EAST = new Point(1,-1);
    public static final Point EAST = new Point(1,0);
    public static final Point SOUTH_EAST = new Point(1,1);
    public static final Point SOUTH = new Point(0,1);
    public static final Point SOUTH_WEST = new Point(-1,1);

    public int x,y;

    public Point(int xIn, int yIn) {
        x = xIn;
        y = yIn;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Point)) { return false; }
        Point p = (Point) other;
        return p.x == x && p.y == y;
    }


    public boolean equals(Point other) {
        return (other.x == x && other.y == y);
    }
    

    public String toString() {
        java.lang.StringBuilder b = new java.lang.StringBuilder();
        b.append('(');
        b.append(x);
        b.append(',');
        b.append(y);
        b.append(')');

        return b.toString();
    }
}