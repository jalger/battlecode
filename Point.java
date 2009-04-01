package jj_nick;

/**
 *
 * @author Jeff Alger, Nick Dunn
 * @date   April 1, 2009
 */
public class Point {

    private static final char OPEN_BRACKET = '(';
    private static final char CLOSE_BRACKET = ')';
    private static final String SEPARATOR = ",";


    

    private static final int OPEN_BRACKET_SIZE = 1;
    private static final int CLOSE_BRACKET_SIZE = 1;

    private static final int MAX_CHARS_IN_INT = 11;
    // 2 extra spaces for open bracket, close bracket, 1 for separator
    private static final int MAX_CHARS_IN_POINT = 2 * MAX_CHARS_IN_INT + 2 + 1;
    
    

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

    public boolean equals(Point other) {
        return (other.x == x && other.y == y);
    }
    

    public static Point fromString(String s) {
        String[] vals = s.split(SEPARATOR);
        
        // Ignore the open bracket
        int x = Integer.parseInt(vals[0].substring(OPEN_BRACKET_SIZE));
        // Ignore the close bracket
        int y = Integer.parseInt(vals[1].substring(0, vals[1].length() - CLOSE_BRACKET_SIZE));
        return new Point(x, y);
    }
    
    public String toString() {
        
        /*
        char [] result = new char[MAX_CHARS_IN_POINT];
        result[0] = 
        */
        
        java.lang.StringBuilder b = new java.lang.StringBuilder();
        b.append(OPEN_BRACKET);
        b.append(x);
        b.append(SEPARATOR);
        b.append(y);
        b.append(CLOSE_BRACKET);

        return b.toString();
    }
    
    
    
    /**
    * Tests the functionality of to/from string
    */
    public static void main (String [] args) throws Exception
    {
        /* code */
        
        Point [] points = new Point[] { new Point(100, 200),
                                        new Point(0,0),
                                        new Point(-120581204,1),
                                        new Point(105,105),
                                        new Point (100, 20+31)  };
        for (Point p : points) {
            System.out.println(p +  " " + Point.fromString(p.toString()));
            
            
            if (!p.equals(Point.fromString(p.toString()))) {
                throw new Exception();
            }
        }
                

        
        
    }
    
    public static void testToString() {
        Point [] points = new Point[] { new Point(100, 200),
                                        new Point(0,0),
                                        new Point(-120581204,1),
                                        new Point(105,105),
                                        new Point (100, 20+31)  };
        for (Point p : points) {
            p.toString();
        }                                
                                        
    }
    
    
    public static void testFromString() {
        String[] stringReprs = new String[] {
        "(100,200)",
        "(0,0)",
        "(-120581204,1)",
        "(105,105)",
        "(100,51)"
        };
        
        for (String s : stringReprs) {
            Point.fromString(s);
        }        
    }

}
