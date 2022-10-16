package Game.Player;

import Game.Board.Board;
import Game.Board.Spot;
import Game.Game;
import Game.Move.Coordinates;

import java.util.ArrayList;
import java.util.Random;

/**
 * Contains method used by DummyBot player, which implements the interface Player.
 */
public class DummyBot extends Game implements Player {

    private String name;

    /**
     * Constructor to set the name of the DummyBot.
     * @param name is the name of the DummyBot
     * @requires name != null
     * @requires name instanceof String
     */
    public DummyBot(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the player.
     * @ensures name != null
     * @return name of the current DummyBot player
     */
    public String getName() {
        return name;
    }

    /**
     * Method that accesses the field of the super class game called players.
     * Checks the current color used by the provided player
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
     * Checks if the move is valid or not. Returns true if Spot from the provided Coordinates is Spot.EMPTY
     * Returns false if Spot on the provided Coordinates is not Spot.EMPTY
     * @param board 6x6 board composed of 4 squares each 3x3
     * @param coordinates x,y pair
     * @requires board != null
     * @requires coordinates != null
     * @return true if move is valid or false if move is not valid
     */
    @Override
    public boolean checkMove(Board board, Coordinates coordinates) {
        if (board.checkIfEmpty(coordinates) == true) {
            return true;
        }
        return false;
    }

    /**
     * Method that takes the list of validCoordinates and returns a random possible move.
     * @param validCoordinates is a list of validCoordinates where a move can be made
     * @requires validCoordinate != null
     * @ensures Coordinates != null
     * @ensures validCoordinates instanceof Coordinates
     */
    public Coordinates chooseMove(ArrayList<Coordinates> validCoordinates) {
        Random rand = new Random();
        int random = rand.nextInt(validCoordinates.size());
        return validCoordinates.get(random);
    }


}
