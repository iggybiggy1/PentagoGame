package Game.Move;

import Game.Player.Player;

/**
 * Class responsible for getting and setting parameters required to perform a move on the board.
 */
public class Move {

    private Rotation rotation;
    private Coordinates coordinates;
    private Player player;
    private int square;

    /**
     * Constructor that constructs the actual move.
     * It contains four parameters, each very important for the game flow.
     * @param player player performing a move
     * @param coordinates x,y pair
     * @param square square number between 0-3
     * @param rotation rotation that can either be clockwise or counterclockwise
     * @requires player != null
     * @requires coordinates != null
     * @requires square >= 0 && square < 4
     * @requires rotation.equals(Rotation.COUNTERCLOCKWISE) || rotation.equals(Rotation.CLOCKWISE)
     */
    public Move(Player player, Coordinates coordinates, int square, Rotation rotation) {
        this.player = player;
        this.coordinates = coordinates;
        this.rotation = rotation;
        this.square = square;
    }

    /**
     * Method for returning the current rotation.
     * @ensures rotation != null
     * ensures rotation.equals(Rotation.CLOCKWISE) || rotation.equals(Rotation.COUNTERCLOCKWISE)
     * @return this.rotation performed on the square
     */
    public Rotation getRotation() {
        return this.rotation;
    }

    /**
     * Returns the square numbers.
     * @return this.square number between 0-3
     */
    public int getSquare() {
        return this.square;
    }

    /**
     * Sets the integer value for the square.
     * @param square square number in the board, can be between 0-3
     */
    public void setSquare(int square) {
        this.square = square;
    }

    /**
     * Method for setting the rotation.
     * @ensures this.rotation.equals(rotation)
     * @param rotation CLOCKWISE or COUNTERCLOCKWISE
     */
    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    /**
     * Method for getting the coordinates.
     * @ensures coordinates != null
     * @return this.coordinates value to be used with the move
     */
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    /**
     * Method for setting the coordinates.
     * @requires coordinates != null
     * @param coordinates x,y pair
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Method that gets current player.
     * @ensures this.player != null
     * @ensures this.player instanceof DummyBot || this.player instanceof RealPlayer
     * @return this.player current object of type player.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Method that sets the value of the player.
     * @requires player != null
     * @param player current player, can be either DummyBot or RealPlayer
     */
    public void setPlayer(Player player) {
        this.player = player;
    }


}
