/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package teamJA_ND;

/**
 *
 * @author J.J. Alger
 */
public class PathNode {

    protected int x,y,direcIndex;
    protected PathNode successor;

    public PathNode(int xIn, int yIn, int n_direc) {
        x = xIn;
        y = yIn;
        direcIndex = n_direc;
    }

    public void setSuccessor(PathNode successorIn) {
        successor = successorIn;
    }

    //Not a very good equals method - be careful in using.
    //Needed simple implementation for computational speed.
    public boolean equals(PathNode other) {
        return (other.x == x && other.y == y);
    }

}
