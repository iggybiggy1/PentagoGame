package Game.Move;

/**
 * Class responsible for creating a Coordinates object, used with all board manipulation moves.
 */
public class Coordinates {

    private int x;
    private int y;

    /**
     * Constructor that creates new object with x,y coordinates in it.
     * @requires x > 0
     * @requires y > 0
     * @requires x < 6
     * @requires y < 6
     * @ensures this.x == getX()
     * @ensures this.y == getY()
     * @param x horizontal coordinate
     * @param y vertical coordinate
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x value of the coordinate.
     * @ensures x >= 0
     * @ensures x <= 5
     * @return this.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the y value of the coordinate.
     * @ensures y >= 0
     * @ensures y <= 5
     * @return this.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Performs a shift on the coordinates, and checks if
     * the provided coordinates are within the preconditions.
     * @param nx shift over x value
     * @param ny shift over y value
     * @return coordinates shifted over x and y value
     */
    public Coordinates shift(int nx, int ny) {
        Coordinates coordinates = null;

        if (!(this.x + nx < 0) && !(this.x + nx > 5) && !(this.y + ny < 0) && !(this.y + ny > 5)) {
            coordinates = new Coordinates(this.x + nx, this.y + ny);
        }
        return coordinates;
    }

    /**
     * Returns the integer representation of the coordinates, between 0-8.
     * @param coordinates x,y pair
     * @requires coordinates != null
     * @return integer with the translated coordinates
     */
    public int getIndex(Coordinates coordinates) {
        return coordinates.getX() + 3 * coordinates.getY();
    }



}
