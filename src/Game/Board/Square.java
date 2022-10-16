package Game.Board;

import Game.Move.Coordinates;
import Game.Move.Rotation;

import java.util.Arrays;

/**
 * Class responsible for creating the squares, four of which are required to create a board.
 */
public class Square {

    private Spot[] spots = new Spot[9];

    /**
     * Creates a square of grid 3x3 (array of length 9) and fills it with EMPTY() spots.
     */
    public Square() {
        Arrays.fill(spots, Spot.EMPTY);
    }

    /**
     * Method that retrieves a Spot with the use of x,y coordinates.
     * @param coordinates x,y pair
     * @requires coordinates != null
     * @ensures position == Spot.EMPTY || position == Spot.WHITE || position == Spot.BLACK
     * @ensures position != null
     * @return position returned from the board
     */
    public Spot getSquareSpot(Coordinates coordinates) {
        Spot position = spots[getIndex(coordinates)];
        return position;
    }

    /**
     * Setter method for position with the use of x,y coordinates.
     * Sets the specified spot on specified coordinates
     * @param coordinates x,y pair
     * @param spot value on the board, can be: EMPTY(), WHITE(W), BLACK(B)
     * @requires coordinates != null
     * @requires spot != null
     */
    public void setSquareSpot(Spot spot, Coordinates coordinates) {
        spots[getIndex(coordinates)] = spot;
    }

    /**
     * Method for rotating (transposing) the 3x3 square.
     * If a rotation is CLOCKWISE, the grid is going to get transposed to the right
     * If a rotation is COUNTERCLOCKWISE, the grid is going the get transposed to te left
     * @requires rotation != null
     * @requires rotation.equals(Rotation.COUNTERCLOCKWISE) || rotation.equals(Rotation.CLOCKWISE)
     * @param rotation COUNTERCLOCKWISE or CLOCKWISE
     */
    public void rotateSquare(Rotation rotation) {
        Spot[] values = new Spot[spots.length];         // Generates a temporary copy of the square

        for (int i = 0; i < spots.length; i++) {          // Populates the values with positions in original square
            values[i] = spots[i];
        }

        if (rotation == Rotation.COUNTERCLOCKWISE) {      // checks if the chosen rotation is COUNTERCLOCKWISE
            spots[0] = values[2];
            spots[1] = values[5];
            spots[2] = values[8];
            spots[3] = values[1];
            spots[4] = values[4];
            spots[5] = values[7];
            spots[6] = values[0];
            spots[7] = values[3];
            spots[8] = values[6];

        } else if (rotation == Rotation.CLOCKWISE) {       // checks if the chosen rotation is CLOCKWISE
            spots[0] = values[6];
            spots[1] = values[3];
            spots[2] = values[0];
            spots[3] = values[7];
            spots[4] = values[4];
            spots[5] = values[1];
            spots[6] = values[8];
            spots[7] = values[5];
            spots[8] = values[2];
        }
    }

    /**
     * Creates a copy of a square that can be used for potential manipulations.
     * E.g. translation during rotation
     * @ensures Square != null
     * @ensures copy[] == Spot.EMPTY || Spot.WHITE || Spot.BLACK
     * @return copy of the square
     */
    public Square squareCopy() {
        Square copy = new Square();
        for (int i = 0; i < spots.length; i++) {
            copy.spots[i] = spots[i];
        }
        return copy;
    }

    /**
     * Method to determine the index of the current square, with the use of X,Y coordinates.
     * @param coordinates x,y pair
     * @requires coordinates != null
     * @ensures index >= 0
     * @ensures index <= 8
     * @return index of the square derived from provided coordinates
     */
    public int getIndex(Coordinates coordinates) {
        int index = 0;
        int coordinateX = coordinates.getX();
        int coordinateY = coordinates.getY();

        if (coordinateX == 0 && coordinateY == 0) {
            index = 0;
        } else if (coordinateX == 1 && coordinateY == 0) {
            index = 1;
        } else if (coordinateX == 2 && coordinateY == 0) {
            index = 2;
        } else if (coordinateX == 0 && coordinateY == 1) {
            index = 3;
        } else if (coordinateX == 1 && coordinateY == 1) {
            index = 4;
        } else if (coordinateX == 2 && coordinateY == 1) {
            index = 5;
        } else if (coordinateX == 0 && coordinateY == 2) {
            index = 6;
        } else if (coordinateX == 1 && coordinateY == 2) {
            index = 7;
        } else if (coordinateX == 2 && coordinateY == 2) {
            index = 8;
        }

        return index;
    }

}
