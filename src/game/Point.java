/*
 * Point class that represents a point on the board
 */
package game;

/**
 *
 * @author victorgao
 */
public class Point {

    int xpos; //Class variables xpos and ypos
    int ypos;

    //Constructor
    public Point(int x, int y) {
        xpos = x;
        ypos = y;
    }

    //Accessor methods
    public int getX() {
        return xpos;
    }

    public int getY() {
        return ypos;
    }

    //Modifier methods
    public void setX(int x) {
        xpos = x;
    }

    public void setY(int y) {
        ypos = y;
    }

    @Override
    public String toString() {
        return (xpos + ", " + ypos);
    }

}
