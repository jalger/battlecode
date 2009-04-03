package jj_nick;

public class MapArea {


    public static final String SEPARATOR = "#";

    private static class Holder {
        boolean[][] map1;
        boolean[][] map2;

        int left;
        int top;
        int right;
        int bottom;

        public Holder(boolean[][] map1, boolean[][] map2,
                      int left, int top, int right, int bottom) {
            this.map1 = map1;
            this.map2 = map2;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
        public String toString() {
            return Map.toString(map1, map2, left, top, right, bottom);

        }
    }

    public static void main(String[] args) {
        int left = 125124;
        int right = 125127;
        int top = 135;
        int bottom = 138;
        boolean[][] map1 = { {true, true, false, true},
                             {false, false, true, true},
                             {false, false, false, false},
                             {true, true, true, true} };

        boolean[][] map2 = { {true, false, false, true},
                             {false, true, true, true},
                             {false, false, true, false},
                             {true, true, true, false} };

        String repr = toString(map1, map2, left, top, right, bottom);
        System.out.println(repr);

        Holder h = fromString(repr);
        System.out.println(h);

        assert(repr.equals(h.toString()));
    }



    public static Holder fromString(String s) {

        String[] values = s.split(SEPARATOR);
        int left = Integer.parseInt(values[0]);
        int top = Integer.parseInt(values[1]);
        int right = Integer.parseInt(values[2]);
        int bottom = Integer.parseInt(values[3]);

        int width = right - left + 1;
        int height = bottom - top + 1;

        boolean[][] map1 = parseMapString(height, width, values[4]);
        boolean[][] map2 = parseMapString(height, width, values[5]);

        return new Holder(map1, map2, left, top, right, bottom);
    }

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
    public static String toString(boolean[][] map1, boolean[][] map2,
                                  int left, int top, int right, int bottom) {

        int width = right - left;
        int height = bottom - top;

        assert(width > 0);
        assert(height > 0);

        assert (map1.length == map2.length);
        assert (map1[0].length == map2[0].length);
        assert (map1.length == height);
        assert (map1[0].length == width);

        // Format for String will be
        // left # top # right # bottom # map1 # map2

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
        // Encode the map1 information
        for (int row = 0; row < map1.length; row++) {
            for (int col = 0; col < map1[0].length; col++) {
                // Encode true as 1, false as 0.
                b.append(map1[row][col] ?
                         '1' :
                         '0');
            }
        }
        b.append(SEPARATOR);
        // Encode map2 information
        for (int row = 0; row < map2.length; row++) {
            for (int col = 0; col < map2[0].length; col++) {
                // Encode true as 1, false as 0.
                b.append(map2[row][col] ?
                         '1' :
                         '0');

            }
        }

        return b.toString();
    }




}