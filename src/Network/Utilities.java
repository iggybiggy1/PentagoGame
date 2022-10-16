package Network;

import Game.Move.Coordinates;
import Game.Move.Rotation;

/**
 * Class containing useful methods for translating the client syntax into game syntax.
 */
public class Utilities {

    /**
     * Method that translates the move of the client syntax into game syntax.
     * Returns null if the input is below 0 or above 35
     * E.g. 10 = 4 1 where
     * 4 = x coordinate
     * 1 = y coordinate
     * @requires  move >= 0
     * @requires move < 36
     * @param move possible move between 0-35
     * @return coordinates after translation. If move doesn't respect preconditions, return null.
     */
    public Coordinates translateCoords(int move) {

        if (move < 0 || move > 36) {
            System.out.println("Error in method translateCoords, Class: Utilities");
            return null;
        }

        return new Coordinates(move % 6, move / 6);

    }

    /**
     * Method that translates the rotation of the client syntax into game syntax.
     * Returns null if input is below 0 or above 8.
     * @param input possible rotation between 0-8, second parameter provided by the user
     * @requires input >= 0
     * @requires input < 9
     * @return rotation after the translation
     */
    public Rotation translateRotation(int input) {
        Rotation changedRotation;

        if (input < 0 || input > 8) {
            System.out.println("Error in method translateRotation, Class: Utilities");
            return null;
        }

        if (input % 2 == 0) {
            changedRotation = Rotation.COUNTERCLOCKWISE;
        } else {
            changedRotation = Rotation.CLOCKWISE;
        }

        return changedRotation;
    }

    /**
     * Method that translates the client syntax for accessing the square on the board, into game syntax.
     * E.g. 7 = 3 c where:
     * 3 is the number of square between [0-3]
     * c means that the rotation is Clockwise
     * @param input possible between 0-7 second parameter provided by the user
     * @requires input >= 0
     * @requires input < 9
     * @return changedSquareNumber
     * Returns 999 if the input was below 0 or above 8
     */
    public int translateSquare(int input) {

        if (input < 0 || input > 8) {
            System.out.println("Error in method translateSquare, Class: Utilities");
            return 999;
        } else {
            return input / 2;
        }
    }


}
