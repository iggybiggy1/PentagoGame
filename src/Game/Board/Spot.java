package Game.Board;

/**
 * Enum responsible for providing the spots that are used on the board.
 */
public enum Spot {

    WHITE("W"),
    EMPTY("-"),
    BLACK("B");

    private String spot;
    private boolean value;

    /**
     * Constructor that initializes the value of spot.
     * Can be either: empty(-), white(W) or black(B)
     * @requires spot != null
     * @ensures this.spot == spot
     * @ensures this.spot != null
     */
    Spot(String spot) {
        this.spot = spot;
    }

    /**
     * Checks if the current spot is empty, or occupied.
     * Returns true when empty, returns false when occupied
     * @ensures value == true || value == false
     * @return value
     */
    public boolean checkIfEmpty() {
        value = this == EMPTY;
        return value;
    }

    /**
     * Returns the current spot color, can be EMPTY, WHITE or BLACK.
     * @requires spot != null
     * @ensures spot != null
     * @return spot
     */
    public String getSpot() {
        try {
            return spot;
        } catch (NullPointerException e) {
            System.out.println("Color wasn't initialized");
            return null;
        }
    }

}

