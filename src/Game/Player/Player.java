package Game.Player;

import Game.Board.Board;
import Game.Move.Coordinates;
import java.util.ArrayList;

/**
 * Contains stub methods used by all players of type Player: RealPlayer, DummyBot.
 */
public interface Player {

    /**
     * Method that determines the color used by the player.
     * @param player currently checked player
     * @return boolean if the color is white
     */
    public boolean checkPlayerColor(Player player);

    /**
     * Returns the name of the player.
     * @return String name
     */
    public String getName();

    /**
     * Checks if the move is valid or not.
     * @requires board != null
     * @requires coordinates != null
     * @ensures boolean == true || boolean == false
     * @param board 6x6 board containing 4 3x3 squares
     * @param coordinates x,y pair
     * @return boolean true if move can be placed, or false if the move cannot be placed
     */
    public boolean checkMove(Board board, Coordinates coordinates);

    /**
     * Chooses the move for the DummyBot.
     * @requires validCoordinates != null
     * @requires validCoordinates.isEmpty() != false
     * @ensures Coordinates != null
     * @param validCoordinates is a list of all valid possible moves as coordinates
     * @return coordinates currently chosen coordinates
     */
    public Coordinates chooseMove(ArrayList<Coordinates> validCoordinates);



}
