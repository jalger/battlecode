package jj_nick;

import java.lang.StringBuilder;
import java.util.List;
import java.util.ArrayList;

/**
* A Path simply consists of a list of points that can be dead reckoned
* between to get from one place to another.
* @author Nicholas Dunn
* @date   March 31, 2009
*/

public class Path implements Transferable<Path> {
    
    // A path is represented as {{x0,y0}#{x1,y1}#...}
    
    private static final String OPEN_BRACKET = "{";
    private static final String CLOSE_BRACKET = "}";
    
    private static final String FIELD_SEPARATOR = "#";
    private static final String COORD_SEPARATOR = ",";
    
    private List<Point> waypoints;
    
    public Path(String waypoints) {
        this.waypoints = Path.fromString(waypoints);
    }
    
    public Path(List<Point> waypoints) {
        this.waypoints = waypoints;
    }
    
    public static final Path PARSER = new Path(new ArrayList<Point>());
    
    public int[] toIntArray() {
        int[] points = new int[2 * waypoints.size() + 1];
        points[0] = points.length;

        int counter = 1;
        for (Point p : waypoints) {
            points[counter++] = p.x;
            points[counter++] = p.y;
        }
        return points;
    }
    
    public Path fromIntArray(int[] points, int offset) {
        int nInts = points[offset];
        int nPoints = (nInts - 1) / 2;
                
        List <Point> waypoints = new ArrayList<Point>();
        for (int i = 0; i < nPoints; i++) {
            int index = (2 * i) + 1;
            waypoints.add(new Point(points[index], points[index+1]));
        }
        
        return new Path(waypoints);
    }
    
    
    public static List<Point> fromString(String s) {
        // Trip off opening and closing brackets
        s = s.substring(1, s.length() - 1);
        String[] array = s.split(FIELD_SEPARATOR);
        
        List<Point> points = new ArrayList<Point>();
        for (String orderedPair : array) {
            points.add(Point.fromString(orderedPair));
        }
        return points;
    }
    
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(OPEN_BRACKET);
        // Iterate through all but the last element in list
        for (int i = 0; i < waypoints.size() - 1; i++) {
            b.append(waypoints.get(i).toString());
            b.append(FIELD_SEPARATOR);
        }
        // Add the last element, and then close off the expression
        b.append(waypoints.get(waypoints.size() - 1));
        b.append(CLOSE_BRACKET);
        
        return b.toString();
    }
    
    // TODO: This is not a *real* equals method because it does not
    // take Object as an argument.
    public boolean equals(Path other) {
        return this.waypoints.equals(other.waypoints);
    }
    
    
    public static void main (String [] args)
    {
        Point [] points1 = new Point[] 
        {                               
            new Point(100, 200),
            new Point(0,0),
            new Point(-120581204,1),
            new Point(105,105),
            new Point (100, 20+31)  
        };
        Point [] points2 = new Point[] 
        {                               
            new Point(1010, 200),
            new Point(0,-1250),
            new Point(-1205125,1315),
            new Point(105,15),
            new Point (100, 1000)  
        };
        
        Path[] paths = new Path[] 
        {
            new Path(java.util.Arrays.asList(points1)),
            new Path(java.util.Arrays.asList(points2))
        };
        
        for (Path p : paths) {
            p.equals(Path.fromString(p.toString()));
        }
    }
    
    public static void main2(String [] args) {
        
        Point [] points1 = new Point[] 
        {                               
            new Point(100, 200),
            new Point(0,0),
            new Point(-120581204,1),
            new Point(105,105),
            new Point (100, 20+31)  
        };
        Point [] points2 = new Point[] 
        {                               
            new Point(1010, 200),
            new Point(0,-1250),
            new Point(-1205125,1315),
            new Point(105,15),
            new Point (100, 1000)  
        };
        
        Path[] paths = new Path[] 
        {
            new Path(java.util.Arrays.asList(points1)),
            new Path(java.util.Arrays.asList(points2))
        };
        
        for (Path p : paths) {
            p.equals(PARSER.fromIntArray(p.toIntArray(), 0));
            
            /*System.out.println(p);
            System.out.println(PARSER.fromIntArray(p.toIntArray(), 0));*/
        }
        
        
        
        
    }
    
    
    public static void testToIntArray() {
        Point [] points1 = new Point[] 
        {                               
            new Point(100, 200),
            new Point(0,0),
            new Point(-120581204,1),
            new Point(105,105),
            new Point (100, 20+31)  
        };
        Point [] points2 = new Point[] 
        {                               
            new Point(1010, 200),
            new Point(0,-1250),
            new Point(-1205125,1315),
            new Point(105,15),
            new Point (100, 1000)  
        };
        
        Path[] paths = new Path[] 
        {
            new Path(java.util.Arrays.asList(points1)),
            new Path(java.util.Arrays.asList(points2))
        };
        
        for (Path p : paths) {
            p.toIntArray();
        }
        
    }
    
    public static void testFromIntArray() {
        int points1[] = {7, 1, 2, 5, 3, 7, 3};
        int points2[] = {9, 4, -1, -25, 0, 3, 51, 135, 52};
        
        Path p1 = PARSER.fromIntArray(points1, 0);
        Path p2 = PARSER.fromIntArray(points2, 0);
        
    }
    
}