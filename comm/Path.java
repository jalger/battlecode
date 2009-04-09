package comm.teamJA_ND;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import teamJA_ND.Point;

/**
* A Path simply consists of a list of points that can be dead reckoned
* between to get from one place to another.
* @author Nicholas Dunn
* @date   March 31, 2009
*/

public class Path implements Transferable<Path> {

    private List<Point> waypoints;

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

    public int getLength() {
        return 2 * waypoints.size() + 1;
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

    public String toString() {
        return waypoints.toString();
    }

    // TODO: This is not a *real* equals method because it does not
    // take Object as an argument.
    public boolean equals(Path other) {
        return this.waypoints.equals(other.waypoints);
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
            new Path(Arrays.asList(points1)),
            new Path(Arrays.asList(points2))
        };

        for (Path p : paths) {
            p.equals(PARSER.fromIntArray(p.toIntArray(), 0));

            /*System.out.println(p);
            System.out.println(PARSER.fromIntArray(p.toIntArray(), 0));*/
        }

    }




}