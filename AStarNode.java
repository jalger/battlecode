/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package teamJA_ND;

/**
 *
 * @author Jeff
 */
public class AStarNode implements Comparable<AStarNode>{

    protected int x,y;
    protected double d, score;
    protected AStarNode parent;
    protected AStarNode successor;


    public AStarNode(int xIn, int yIn) {
        x = xIn;
        y = yIn;
    }

    public int compareTo(AStarNode other) {
        return (int)(score - other.score);
    }

}
