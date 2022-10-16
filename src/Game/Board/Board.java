package Game.Board;

import Game.Game;
import Game.Move.Coordinates;
import Game.Move.Move;

import java.util.ArrayList;

/**
 * Creates a 6x6 board that contains four 3x3 squares.
 * Contains methods for board manipulation.
 */
public class Board {
    private Square[] squares = new Square[4];
    public static ArrayList<Coordinates> freeCoordinates = new ArrayList<Coordinates>();

    /**
     * Creates a new board dnd stores four squares in a squares[] array.
     * Each square represents 1/4 of the board, and contains 3x3 grid.
     * The board contains 4 squares, making the grid 6x6.
     */
    public Board() {
        for (int i = 0; i < squares.length; i++) {
            squares[i] = new Square();
        }
    }

    /**
     * Public getter method to return a particular square.
     * @param squareNr a square number between 0-3
     * @requires squareNr >= 0
     * @requires squareNr <= 3
     * @ensures Square != null
     * @return squares[squareNr]
     */
    public Square getSquare(int squareNr) {

        return squares[squareNr];
    }

    /**
     * Method that returns the array with all the squares in it.
     * @return squares[]
     */
    public Square[] getSquares() {
        return squares;
    }

    /**
     * Performs the move on the board, and rotates the tile.
     * @param move actual move to be performed on the board
     * @requires move != null
     * @return boolean return true if the method uses valid coordinates,
     * returns false if the spot is occupied by a BLACK or WHITE spot
     */
    public boolean move(Move move) {
        Coordinates playerCoordinates = move.getCoordinates(); // gets the coordinates from class Move
        boolean run = false;
        int counter = 0;

        if (checkIfEmpty(playerCoordinates) == true) {                             // checks if the current spot is empty
            Square temporarySquare = getSquareWithCoordinates(playerCoordinates);  // gets the square with appropriate coordinates
            Coordinates squareCords = shift(temporarySquare, playerCoordinates);   // shifts the coordinates depending on the value of square in the squares array
            if (counter >= 1) {
                run = true;
            } else {
                if (move.getPlayer().checkPlayerColor(move.getPlayer()) == true) {          // if the player is white
                    temporarySquare.setSquareSpot(Spot.WHITE, squareCords);
                } else {                                                                    // if the player is black
                    temporarySquare.setSquareSpot(Spot.BLACK, squareCords);
                }
                System.out.println("\n" + "Board before performing the rotation");
                run = true;
                System.out.println(toString());                                  // print the board before the rotation
                squares[move.getSquare()].rotateSquare(move.getRotation());             // rotates the square
            }
        } else {
            counter++;
        }
        return run;
    }

    /**
     * Performs a check on the board searching for all potential winning combinations.
     * Will return the color that has the winning position
     * WIll return null if no winning positions
     * @param game currently played game
     * @ensures \result.equals(Spot.BLACK) || \result.equals(Spot.WHITE)
     * @return true if any winning alignment on the board is present, else return false
     */
    public Spot detectIfWin(Game game) {

        Spot winningSpot = null;

        for (Coordinates coordinates : game.getBoardCoordinates()) {
            Spot spot = getSpot(coordinates);
            if (spot.checkIfEmpty() != true) {
                                                                                                                            // HORIZONTAL CHECKS
                if (coordinates.shift(0, 0) != null && getSpot(coordinates.shift(0, 0)).equals(spot) &&
                    coordinates.shift(-1, 0) != null && getSpot(coordinates.shift(-1, 0)).equals(spot) &&
                    coordinates.shift(-2, 0) != null && getSpot(coordinates.shift(-2, 0)).equals(spot) &&
                    coordinates.shift(-3, 0) != null && getSpot(coordinates.shift(-3, 0)).equals(spot) &&
                    coordinates.shift(-4, 0) != null && getSpot(coordinates.shift(-4, 0)).equals(spot)) {        // check horizontal win
                    winningSpot = spot;                                                                                         // from right to left
                    break;

                } else if (coordinates.shift(0, 0) != null && getSpot(coordinates.shift(0, 0)).equals(spot) &&
                    coordinates.shift(1, 0) != null && getSpot(coordinates.shift(1, 0)).equals(spot) &&
                    coordinates.shift(2, 0) != null && getSpot(coordinates.shift(2, 0)).equals(spot) &&
                    coordinates.shift(3, 0) != null && getSpot(coordinates.shift(3, 0)).equals(spot) &&
                    coordinates.shift(4, 0) != null && getSpot(coordinates.shift(4, 0)).equals(spot)) {          // check horizontal win
                    winningSpot = spot;                                                                                         // from left to right
                    break;

                } else if (coordinates.shift(0, 0) != null && getSpot(coordinates.shift(0, 0)).equals(spot) &&      // VERTICAL CHECKS
                      coordinates.shift(0, -1) != null && getSpot(coordinates.shift(0, -1)).equals(spot) &&
                      coordinates.shift(0, -2) != null && getSpot(coordinates.shift(0, -2)).equals(spot) &&
                      coordinates.shift(0, -3) != null && getSpot(coordinates.shift(0, -3)).equals(spot) &&
                      coordinates.shift(0, -4) != null && getSpot(coordinates.shift(0, -4)).equals(spot)) {         // check vertical win
                    winningSpot = spot;                                                                                          // from down to up
                    break;

                } else if (coordinates.shift(0, 0) != null && getSpot(coordinates.shift(0, 0)).equals(spot) &&
                    coordinates.shift(0, 1) != null && getSpot(coordinates.shift(0, 1)).equals(spot) &&
                    coordinates.shift(0, 2) != null && getSpot(coordinates.shift(0, 2)).equals(spot) &&
                    coordinates.shift(0, 3) != null && getSpot(coordinates.shift(0, 3)).equals(spot) &&
                    coordinates.shift(0, 4) != null && getSpot(coordinates.shift(0, 4)).equals(spot)) {           // check vertical win
                    winningSpot = spot;                                                                                          // from up to down
                    break;

                } else if (coordinates.shift(0, 0) != null && getSpot(coordinates.shift(0, 0)).equals(spot) &&       // DIAGONAL CHECKS
                    coordinates.shift(1, 1) != null && getSpot(coordinates.shift(1, 1)).equals(spot) &&
                    coordinates.shift(2, 2) != null && getSpot(coordinates.shift(2, 2)).equals(spot) &&
                    coordinates.shift(3, 3) != null && getSpot(coordinates.shift(3, 3)).equals(spot) &&
                    coordinates.shift(4, 4) != null && getSpot(coordinates.shift(4, 4)).equals(spot)) {           // check diagonal win
                    winningSpot = spot;                                                                                          // from left to right
                    break;                                                                                                       // up to down

                } else if (coordinates.shift(0, 0) != null && getSpot(coordinates.shift(0, 0)).equals(spot) &&
                    coordinates.shift(-1, -1) != null && getSpot(coordinates.shift(-1, -1)).equals(spot) &&
                    coordinates.shift(-2, -2) != null && getSpot(coordinates.shift(-2, -2)).equals(spot) &&
                    coordinates.shift(-3, -3) != null && getSpot(coordinates.shift(-3, -3)).equals(spot) &&
                    coordinates.shift(-4, -4) != null && getSpot(coordinates.shift(-4, -4)).equals(spot)) {         // check diagonal win
                    winningSpot = spot;                                                                                            // from left to right
                    break;                                                                                                         // down to up

                }  else if (coordinates.shift(0, 0) != null && getSpot(coordinates.shift(0, 0)).equals(spot) &&
                    coordinates.shift(-1, 1) != null && getSpot(coordinates.shift(-1, 1)).equals(spot) &&
                    coordinates.shift(-2, 2) != null && getSpot(coordinates.shift(-2, 2)).equals(spot) &&
                    coordinates.shift(-3, 3) != null && getSpot(coordinates.shift(-3, 3)).equals(spot) &&
                    coordinates.shift(-4, 4) != null && getSpot(coordinates.shift(-4, 4)).equals(spot)) {            // check diagonal win
                    winningSpot = spot;                                                                                             // from right to left
                    break;                                                                                                          // from up to down

                } else if (coordinates.shift(0, 0) != null && getSpot(coordinates.shift(0, 0)).equals(spot) &&
                    coordinates.shift(1, -1) != null && getSpot(coordinates.shift(1, -1)).equals(spot) &&
                    coordinates.shift(2, -2) != null && getSpot(coordinates.shift(2, -2)).equals(spot) &&
                    coordinates.shift(3, -3) != null && getSpot(coordinates.shift(3, -3)).equals(spot) &&
                    coordinates.shift(4, -4) != null && getSpot(coordinates.shift(4, -4)).equals(spot)) {            // check diagonal win
                    winningSpot = spot;                                                                                             // from right to left
                    break;                                                                                                          // from down to up
                }
            }
        }
        return winningSpot;
    }

    /**
     * Performs a check on the board and checks if there is a tie.
     * Will return true if there are no free coordinates
     * Will return false if there are still free coordinates
     * @return true if board is full, false is board is not full
     */
    public boolean detectIfTie() {

        if (getFreeCoordinates().size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Performs an appropriate coordinate shift depending on the square number between 0-3.
     * E.g. if the square is 1, then shift the x coordinates by -3, since it is the top-right one
     * @ensures square != null
     * @ensures coordinates != null
     * @param square currently used square
     * @param coordinates x,y pair
     * @return translated coordinates
     */
    public Coordinates shift(Square square, Coordinates coordinates) {
        Coordinates coords = null;

        if (square == getSquare(0)) {
            coords = coordinates;
        } else if (square == getSquare(1)) {
            coords = coordinates.shift(-3, 0);
        } else if (square == getSquare(2)) {
            coords = coordinates.shift(0, -3);
        } else if (square == getSquare(3)) {
            coords = coordinates.shift(-3, -3);
        }
        return coords;
    }

    /**
     * Method that returns a list of all the board coordinates.
     * Returns all 36 coordinates
     * @ensures coordinates != null
     * @return coordinates - a list with all board coordinates
     */
    public ArrayList<Coordinates> getBoardCoordinates() {
        ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                coordinates.add(new Coordinates(j, i));
            }
        }

        return coordinates;
    }

    /**
     * Checks if the given coordinate contains a Spot.EMPTY.
     * Return true if the coordinate on the board contains Spot.EMPTY
     * Return false if the coordinate on the board doesn't contain Spot.EMPTY
     * @param coordinates x,y pair
     * @requires coordinates != null
     * @ensures isEmpty == true || isEmpty == false
     * @return isEmpty boolean value
     */
    public boolean checkIfEmpty(Coordinates coordinates) {
        boolean isEmpty = false;

        if (getSpot(coordinates).equals(Spot.EMPTY)) {
            isEmpty = true;
        }

        return isEmpty;
    }

    /**
     * Method that returns the appropriate square with the current coordinates.
     * The method can return either Square[0] top-left square or Square[1] top-right square or Square[2]
     * bottom-left square or Square[3] bottom-right square
     * @param coordinates x,y pair
     * @requires coordinates != null
     * @ensures square != null
     * @return square from the squares[] array
     */
    public Square getSquareWithCoordinates(Coordinates coordinates) {
        Square square = null;

        if (coordinates.getX() <= 2) {

            if (coordinates.getY() <= 2) {
                square = getSquare(0);
            } else if (coordinates.getY() > 2) {
                square = getSquare(2);
            }
        } else if (coordinates.getX() > 2) {

            if (coordinates.getY() <= 2) {
                square = getSquare(1);
            } else if (coordinates.getY() > 2) {
                square = getSquare(3);
            }
        }

        return square;
    }

    /**
     * Method that returns the correct square number with the current coordinates.
     * The method can return either 0,1,2,3
     * @param coordinates x,y pair
     * @requires coordinates != null
     * @ensures squareNumber >= 0
     * @ensures squareNumber <= 3
     * @return squareNumber of the square on the board
     */
    public int getSquareNumber(Coordinates coordinates) {
        int squareNumber = 0;

        if (coordinates.getX() <= 2) {

            if (coordinates.getY() <= 2) {
                squareNumber = 0;
            } else if (coordinates.getY() > 2) {
                squareNumber = 2;
            }
        } else if (coordinates.getX() > 2) {

            if (coordinates.getY() <= 2) {
                squareNumber = 1;
            } else if (coordinates.getY() > 2) {
                squareNumber = 3;
            }
        }

        return squareNumber;
    }


    /**
     * Method that returns the current spot on the board from provided coordinates.
     * The method is used to determine whether the spot is either WHITE or BLACK
     * @param coordinates x,y pair
     * @requires coordinates != null
     * @ensures spot == Spot.WHITE || spot == Spot.BLACK
     * @return the spot set on provided cooridnates
     */
    public Spot getSpot(Coordinates coordinates) {
        Square temporarySquare = new Square();
        Coordinates coords = coordinates;

        if (coordinates.getX() <= 2) {                                                        // checks if the x coordinate lays in the left half
            if (coordinates.getY() <= 2) {                                                    // checks if the y coordinate lays in the left upper part
                temporarySquare = getSquare(0);
            } else if (coordinates.getY() > 2) {                                              // checks if the y coordinate lays in the left lower part
                temporarySquare = getSquare(2);
                coords = new Coordinates(coordinates.getX(), coordinates.getY() - 3);
            }

        } else if (coordinates.getX() > 2) {                                                    // checks if the x coordinate lays in the right half
            if (coordinates.getY() <= 2) {                                                     // checks if the y coordinate lays in the left upper part
                temporarySquare = getSquare(1);
                coords = new Coordinates(coordinates.getX() - 3, coordinates.getY());
            } else if (coordinates.getY() > 2) {                                               // checks if the y coordinate lays in the left lower part
                temporarySquare = getSquare(3);
                coords = new Coordinates(coordinates.getX() - 3, coordinates.getY() - 3);
            }
        }
        
        return temporarySquare.getSquareSpot(coords);
    }

    /**
     * Returns a list with all remaining coordinates that contain Spot.EMPTY.
     * @ensures freeCoordinates != null
     * @return freeCoordinates a list of all free coordinates from current board
     */
    public ArrayList<Coordinates> getFreeCoordinates() {
        freeCoordinates = new ArrayList<Coordinates>();
        Square temporarySquare = null;


        for (int i = 0; i < squares.length; i++) {          // iterates through each square
            if (i == 0) {
                temporarySquare = getSquare(0);
            } else if (i == 1) {
                temporarySquare = getSquare(1);
            } else if (i == 2) {
                temporarySquare = getSquare(2);
            } else if (i == 3) {
                temporarySquare = getSquare(3);
            }

            for (int j = 0; j < squares.length - 1; j++) {        // iterates through all x coordinates
                for (int k = 0; k < squares.length - 1; k++) {    // iterates through all y coordinates

                    if (temporarySquare.getSquareSpot(new Coordinates(k, j)).equals(Spot.EMPTY)) {
                        Coordinates freeCoordinate = null;
                        if (temporarySquare.equals(getSquare(0))) {
                            freeCoordinate = new Coordinates(k, j);
                            freeCoordinates.add(freeCoordinate);
                        } else if (temporarySquare.equals(getSquare(1))) {
                            freeCoordinate = new Coordinates(k + 3, j);        // performs coordinate shift on x values so that the second square could be reached
                            freeCoordinates.add(freeCoordinate);
                        } else if (temporarySquare.equals(getSquare(2))) {
                            freeCoordinate = new Coordinates(k, j + 3);         // performs coordinate shift on y values so that the third square could be reached
                            freeCoordinates.add(freeCoordinate);
                        } else if (temporarySquare.equals(getSquare(3))) {
                            freeCoordinate = new Coordinates(k + 3, j + 3);    // performs coordinate shift on x and y values so that the third square could be reached
                            freeCoordinates.add(freeCoordinate);
                        }
                    }
                }
            }
        }
        return freeCoordinates;
    }

    /**
     * Method that prints the current representation of the 6x6 board with all the Spots on it.
     * @ensures board != null
     * @ensures board.contains("|") == true
     * @ensures board.contains("-") == true
     * @return board with all the already set values on it
     */
    public String toString() {
        String finalBoard = " ";

        for (int i = 0; i < 6; i++) {
            if (i == 3) {
                finalBoard += "\n" + "----------------";

            }
            finalBoard += "\n" + i;

            if (i < 3) {
                for (int j = 0; j < 3; j++) {
                    finalBoard += " ";
                    finalBoard += getSquare(0).getSquareSpot(new Coordinates(j, i)).getSpot();
                }
                finalBoard += " |";
                for (int j = 0; j < 3; j++) {
                    finalBoard += " ";
                    finalBoard += getSquare(1).getSquareSpot(new Coordinates(j, i)).getSpot();
                }

            } else if (i >= 3) {
                for (int j = 0; j < 3; j++) {
                    finalBoard += " ";
                    finalBoard += getSquare(2).getSquareSpot(new Coordinates(j, i - 3)).getSpot();
                }
                finalBoard += " |";
                for (int j = 0; j < 3; j++) {
                    finalBoard += " ";
                    finalBoard += getSquare(3).getSquareSpot(new Coordinates(j, i - 3)).getSpot();
                }
            }
        }
        finalBoard += "\n";
        finalBoard += " ";

        for (int i = 0; i < 7; i++) {

            if (i == 0) {
                finalBoard += " ";
            } else if (i == 6) {
                finalBoard += i - 1;
            } else if (i == 3) {
                finalBoard += (i - 1) + " | ";
            } else {
                finalBoard += (i - 1) + " ";
            }
        }
        finalBoard += "\n";

        return finalBoard;

    }

}
