package apryraz.eworld;

/**
 * Manages the x and y coordinate of the position
 **/
public class Position {
    /**
     * x and y coordinate of the position
     **/
    public int x, y;

    /**
     * Constructor for the Position class
     *
     * @param row the x coordinate
     * @param col the y coordinate
     */
    public Position(int row, int col) {
        x = row;
        y = col;
    }
}
