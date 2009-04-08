package teamJA_ND;

/**
* A MapArea represents a rectangular region of the map.
*/
public class MapArea implements Transferable<MapArea> {

    private boolean[][] groundTraversable;
    private boolean[][] visited;

    private int left;
    private int top;
    private int right;
    private int bottom;
    

    public MapArea(boolean[][] groundTraversable, boolean[][] visited, 
                    int left, int top, int right, int bottom) {
            this.groundTraversable = groundTraversable;
            this.visited = visited;
            this.left = left;
            this.right = right;
            this.bottom = bottom;
            this.top = top;
    }

    public boolean[][] getGroundTraversable() {
        return groundTraversable;
    }

    public boolean[][] getVisited() {
        return visited;
    }
    
    public int getLeft() {
        return left;
    }

    public int getRight() {
        return left;
    }

    public int getTop() {
        return top;
    }
    
    public int getBottom() {
        return bottom;
    }

    
    public static void main(String[] args) {
        int left = 125124;
        int right = 125127;
        int top = 135;
        int bottom = 138;
        boolean[][] groundTraversable = { {true, true, false, true},
                             {false, false, true, true},
                             {false, false, false, false},
                             {true, true, true, true} };

        boolean[][] visited = { {true, false, false, true},
                             {false, true, true, true},
                             {false, false, true, false},
                             {true, true, true, false} };
                         /*
        String repr = toString(groundTraversable, visited, left, top, right, bottom);
        System.out.println(repr);

        Holder h = fromString(repr);
        System.out.println(h);

        assert(repr.equals(h.toString()));*/
    }


    /**
    *
    */
    public int[] toIntArray() {
        int width = right - left + 1;
        int height = bottom - top + 1;

        Assert.Assert(width > 0);
        Assert.Assert(height > 0);

        Assert.Assert (groundTraversable.length == visited.length);
        Assert.Assert (groundTraversable[0].length == visited[0].length);
        Assert.Assert (groundTraversable.length == height);
        Assert.Assert (groundTraversable[0].length == width);

        int size = 5 + 2 * (width * height);

        int[] packed = new int[size];
        // Start off with the size
        packed[0] = size;

        packed[1] = left;
        packed[2] = top;
        packed[3] = right;
        packed[4] = bottom;
        
        int counter = 5;
        // Now we need to encode the map.  Start with the traversable info
        for (int row = 0; row < groundTraversable.length; row++) {
            for (int col = 0; col < groundTraversable[0].length; col++) {
                // Encode true as 1, false as 0.
                packed[counter++] = groundTraversable[row][col] ? 1 : 0;
            }
        }

        // Encode visited information
        for (int row = 0; row < visited.length; row++) {
            for (int col = 0; col < visited[0].length; col++) {
                // Encode true as 1, false as 0.
                packed[counter++] = visited[row][col] ? 1 : 0;

            }
        }

        return packed;
        
    }
    
    public MapArea fromIntArray(int[] array, int offset) {
        return null;
    }

    public int getLength() {
        return 0;
    }

    /*
    public static Holder fromString(String s) {

        String[] values = s.split(SEPARATOR);
        int left = Integer.parseInt(values[0]);
        int top = Integer.parseInt(values[1]);
        int right = Integer.parseInt(values[2]);
        int bottom = Integer.parseInt(values[3]);

        int width = right - left + 1;
        int height = bottom - top + 1;

        boolean[][] groundTraversable = parseMapString(height, width, values[4]);
        boolean[][] visited = parseMapString(height, width, values[5]);

        return new Holder(groundTraversable, visited, left, top, right, bottom);
    }*/

    public static boolean[][] parseMapString(int rows, int cols, String mapString) {
        assert(mapString.length() == rows * cols);

        boolean[][] map = new boolean[rows][cols];
        char[] chars = mapString.toCharArray();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                boolean value = chars[(row * cols) + col] == '1' ?
                    true :
                    false;
                map[row][col] = value;
            }
        }

        return map;
    }


    /**
     * 
     */
     /*
    public static String toString(boolean[][] groundTraversable, boolean[][] visited,
                                  int left, int top, int right, int bottom) {

        int width = right - left;
        int height = bottom - top;

        assert(width > 0);
        assert(height > 0);

        assert (groundTraversable.length == visited.length);
        assert (groundTraversable[0].length == visited[0].length);
        assert (groundTraversable.length == height);
        assert (groundTraversable[0].length == width);

        // Format for String will be
        // left # top # right # bottom # groundTraversable # visited

        StringBuilder b = new StringBuilder();

        // Encode the bounding box information
        b.append(left);
        b.append(SEPARATOR);
        b.append(top);
        b.append(SEPARATOR);
        b.append(right);
        b.append(SEPARATOR);
        b.append(bottom);


        b.append(SEPARATOR);
        // Encode the groundTraversable information
        for (int row = 0; row < groundTraversable.length; row++) {
            for (int col = 0; col < groundTraversable[0].length; col++) {
                // Encode true as 1, false as 0.
                b.append(groundTraversable[row][col] ?
                         '1' :
                         '0');
            }
        }
        b.append(SEPARATOR);
        // Encode visited information
        for (int row = 0; row < visited.length; row++) {
            for (int col = 0; col < visited[0].length; col++) {
                // Encode true as 1, false as 0.
                b.append(visited[row][col] ?
                         '1' :
                         '0');

            }
        }

        return b.toString();
    }*/




}