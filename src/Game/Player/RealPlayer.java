package Game.Player;

import Game.Board.Board;
import Game.Board.Spot;
import Game.Game;
import Game.Move.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains method to implement RealPlayer using interface Player.
 */
public class RealPlayer extends Game implements Player {

    private String name;

    /**
     * Constructor to set the name of the player.
     * @param name is the name of the Real player
     * @requires name != null
     * @requires name instanceof String
     */
    public RealPlayer(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the player.
     * @ensures name != null
     * @ensures name instanceof String
     * @return name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Method that accesses the field of the super class game called players.
     * @param player currently checked player
     * @return boolean true if players color is white, false if the players color is not white
     */
    @Override
    public boolean checkPlayerColor(Player player) {
        if (super.players.get(player).equals(Spot.WHITE)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the move is valid or not. Returns true if Spot on the provided Coordinates is Spot.EMPTY
     * Returns false if Spot on the provided Coordinates is not Spot.EMPTY
     *
     * @param board 6x6 board composed of 4 squares each 3x3
     * @param coordinates x,y pair
     * @return true if move is valid or false
     * @requires board != null
     * @requires coordinates != null
     * @ensures boolean == true || boolean == false
     */
    @Override
    public boolean checkMove(Board board, Coordinates coordinates) {
        if (board.getSpot(coordinates).equals(Spot.EMPTY)) {
            return true;
        }
        return false;
    }

    /**
     * Redundant method, couldn't be used for anything useful.
     * @param validCoordinates is a list of all valid possible moves as coordinates
     * @return null
     */
    @Override
    public Coordinates chooseMove(ArrayList<Coordinates> validCoordinates) {

        return null;
    }

    /**
     * Method to get all the possible coordinates for a valid move, and store them in a list.
     * @param coordinates list of all coordinates on 6x6 board that contain empty spot
     * @ensures list.isEmpty() == false
     * @ensures list != null
     * @return coordinates a list of all possible coordinates for a valid move
     */
    public List<Coordinates> getHints(ArrayList<Coordinates> coordinates) {
        ArrayList<Coordinates> possibleMoves = new ArrayList<>();

        for (Coordinates c : possibleMoves) {
            possibleMoves.add(c);
            System.out.println("X coordinate: " + c.getX() + "\nY coordinate: " + c.getY() + "\n");

            if(possibleMoves.size() == 3){
                break;
            }
        }

        return possibleMoves;
    }

}
