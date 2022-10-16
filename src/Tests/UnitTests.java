package Tests;

import Game.Board.Board;
import Game.Board.Spot;
import Game.Board.Square;
import Game.Game;
import Game.Move.Coordinates;
import Game.Move.Move;
import Game.Move.Rotation;
import Game.Player.DummyBot;
import Game.Player.Player;
import Game.Player.RealPlayer;
import Network.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {

    private Board board;
    private Game game;
    private Square square;
    private Coordinates coords;
    private Rotation rotation;
    private Player player1;
    private Client client;
    private Move move;

    @BeforeEach // Initialize objects for Unit Testing
    public void init() {
        board = new Board();    // creates new Board 6x6 compose of 4 squares
        square = new Square();  // creates new Square 3x3
        game = new Game();      // creates an object for the game
        client = new Client();
    }

    @Test // checks whether all the spots on the squares are filled with Spot.EMPTY
    public void checkIfSquareInitialEmpty() {
        for (int i = 0; i < 2; i++) {       // generates x values for the coordinates
            for (int j = 0; j < 2; j++) { // generates y values for the coordinates
                assertEquals(Spot.EMPTY,  square.getSquareSpot(new Coordinates(i,  j)));
            }
        }
    }

    @Test // checks whether spot on that coordinates is empty
    public void checkIncorrectSquareSpot() {
        coords = new Coordinates(1, 1);
        square.setSquareSpot(Spot.WHITE,  coords);       // sets the Spot.WHITE on coordinates 1,1
        assertNotEquals(Spot.EMPTY,  square.getSquareSpot(coords));
    }


    @Test // checks whether spot on that coordinates is black
    public void checkCorrectSquareSpot() {
        coords = new Coordinates(1, 1);
        square.setSquareSpot(Spot.BLACK,  coords); // sets another spot Spot.BLACK on the same coordinates
        assertEquals(Spot.BLACK, square.getSquareSpot(coords));
    }


    @Test // checks if the fields in the copied square equal to fields of initial square
    public void testSquareCopy() {
        coords = new Coordinates(1, 1);
        square.setSquareSpot(Spot.BLACK, coords);                     // sets a spot at coordinates 1,1
        square.setSquareSpot(Spot.WHITE, new Coordinates(2, 2)); // sets another spot at coordinates 2,2
        Square test = square.squareCopy();                                // copies the square and stores it in test square

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(square.getSquareSpot(new Coordinates(i, j)),  test.getSquareSpot(new Coordinates(i, j)));
            }
        }
    }

    @Test // checks if the rotated square is indeed rotated counterclockwise
    public void checkCounterClockwiseRotation() {
        square.setSquareSpot(Spot.BLACK, new Coordinates(0, 0)); // sets a spot at coordinates 0,0
        square.setSquareSpot(Spot.BLACK, new Coordinates(1, 0)); // sets a spot at coordinates 1,0
        square.setSquareSpot(Spot.WHITE, new Coordinates(2, 0)); // sets a spot at coordinates 1,0
        square.setSquareSpot(Spot.WHITE, new Coordinates(2, 1)); // sets a spot at coordinates 2,1
        square.setSquareSpot(Spot.WHITE, new Coordinates(2, 2)); // sets a spot at coordinates 2,2
        Square test = square.squareCopy();

        test.rotateSquare(Rotation.COUNTERCLOCKWISE);       // rotates the test square counterclockwise

        for (int i = 0; i < 2; i++) {
            assertNotEquals(square.getSquareSpot(new Coordinates(i, 0)), test.getSquareSpot(new Coordinates(i, 0)));
        }


    }

    @Test // checks if the rotated square is indeed rotated clockwise
    public void checkClockwiseRotation() {
        square.setSquareSpot(Spot.BLACK, new Coordinates(0, 0)); // sets a spot at coordinates 0,0
        square.setSquareSpot(Spot.BLACK, new Coordinates(1, 0)); // sets a spot at coordinates 1,0
        square.setSquareSpot(Spot.WHITE, new Coordinates(2, 0)); // sets a spot at coordinates 2,0
        square.setSquareSpot(Spot.BLACK, new Coordinates(2, 1)); // sets a spot at coordinates 2,1
        square.setSquareSpot(Spot.BLACK, new Coordinates(2, 2)); // sets a spot at coordinates 2,2
        Square test = square.squareCopy();

        test.rotateSquare(Rotation.CLOCKWISE);      // rotates the test square clockwise

        for (int i = 0; i < 2; i++) {
            assertNotEquals(square.getSquareSpot(new Coordinates(i, 0)),  test.getSquareSpot(new Coordinates(i, 0)));
        }
    }

    @Test // checks if the spot is indeed empty
    public void checkIfSpotEmpty() {
        Spot spot = Spot.EMPTY; // set spot to empty
        assertEquals("-",  spot.getSpot());
    }

    @Test   // checks if the spot is indeed empty (Second option with boolean)
    public void checkIfSpotEmpty2() {
        Spot spot = Spot.EMPTY; // set spot to empty
        assertEquals(spot.checkIfEmpty(), true);
    }

    @Test // checks if the spot is not empty
    public void checkIfSpotNotEmpty() {
        Spot spot = Spot.WHITE; // set spot to white
        assertNotEquals(" ",  spot.getSpot());
    }

    @Test // checks if the spot is not empty (Second option with boolean)
    public void checkIfSpotNotEmpty2() {
        Spot spot = Spot.WHITE; // set spot to white
        assertNotEquals(spot.checkIfEmpty(),  true);
    }

    @Test
    public void testCoordinate() {                   // checks if the set coordinate has desired parameters
        coords = new Coordinates(1, 1);
        assertEquals(1, coords.getX());
        assertEquals(1, coords.getY());

    }

    @Test
    public void testWrongCoordinate() {              // checks if the set coordinate is wrong
        coords = new Coordinates(1, 1);
        coords = new Coordinates(2, 2);
        assertNotEquals(1,  coords.getX());
        assertNotEquals(1,  coords.getY());
    }

    @Test
    public void testCoordinateShift() {              // checks the shift of the coordinate over 2 right and 1 down
        coords = new Coordinates(1, 1);
        assertEquals(3, coords.shift(2, 1).getX());
        assertEquals(2, coords.shift(2, 1).getY());

    }

    @Test
    public void checkIndexOfCoordinate() {           // check the index of the coordinate on the square
        coords = new Coordinates(1, 1);
        assertEquals(4, coords.getIndex(coords));
    }

    @Test
    public void testMove() {                         // checks if the parameters in the object move
        player1 = new RealPlayer("Test");     // are correct and can be accessible
        coords = new Coordinates(1, 1);
        rotation = Rotation.CLOCKWISE;
        move = new Move(player1, coords, 1, rotation);
        assertEquals("Test", move.getPlayer().getName());
        assertEquals(coords, move.getCoordinates());
        assertEquals(1, move.getSquare());
        assertEquals(Rotation.CLOCKWISE, move.getRotation());

    }

    @Test
    public void testBotName() {                       // tests the method for getting bots string name
        player1 = new DummyBot("Harry");
        assertEquals("Harry",  player1.getName());
    }

    @Test
    public void testPlayerColorBlack() {             // tests if the player has a black color
        player1 = new RealPlayer("Test");
        game.addPlayer(player1,  Spot.BLACK);
        assertFalse(player1.checkPlayerColor(player1));
    }

    @Test
    public void testPlayerColorWhite() {                 // tests if the player has a white color
        player1 = new RealPlayer("Test");
        game.addPlayer(player1,  Spot.WHITE);
        assertTrue(player1.checkPlayerColor(player1));
    }

    @Test
    public void testIfMoveIsNotFree() {                 // tests if the checked coordinate is not empty
        player1 = new DummyBot("Test");
        coords = new Coordinates(1, 1);
        game.addPlayer(player1,  Spot.BLACK);
        move = new Move(player1, coords, 1, Rotation.CLOCKWISE);
        game.move(move);
        assertFalse(player1.checkMove(game, coords));
    }

    @Test
    public void testIfMoveIsFree() {                // tests if the checked coordinate is empty
        player1 = new DummyBot("Test");
        coords = new Coordinates(1, 1);
        game.addPlayer(player1,  Spot.BLACK);
        move = new Move(player1,  coords, 1,  Rotation.CLOCKWISE);
        game.move(move);
        coords = new Coordinates(2, 2);
        assertTrue(player1.checkMove(game, coords));
    }


    @Test
    public void testIfBoardCoordinateEmpty() {               // tests if all board coordinates
        player1 = new DummyBot("Test");               // are initially empty
        coords = new Coordinates(1, 1);
        game.addPlayer(player1,  Spot.BLACK);
        move = new Move(player1,  coords, 1,  Rotation.CLOCKWISE);
        game.move(move);
        assertFalse(game.checkIfEmpty(coords));
    }

    @Test
    public void testGetSquareNumberWithCoordinates() {           // tests if all squares are properly
        Coordinates coords1 = new Coordinates(1, 1);       // retrieved when given particular
        Coordinates coords2 = new Coordinates(4, 1);      // coordinates
        Coordinates coords3 = new Coordinates(1, 4);
        Coordinates coords4 = new Coordinates(4, 4);

        assertEquals(0,  game.getSquareNumber(coords1));
        assertEquals(1,  game.getSquareNumber(coords2));
        assertEquals(2,  game.getSquareNumber(coords3));
        assertEquals(3,  game.getSquareNumber(coords4));
    }

    @Test
    public void testGetSquare0WithCoordinates() {            // checks if the method retrieves first
        Coordinates coords1 = new Coordinates(1, 1);   // square from the board
        Square[] squares = game.getSquares();
        assertEquals(squares[0], game.getSquareWithCoordinates(coords1));
    }

    @Test
    public void testGetSquare1WithCoordinates() {            // checks if the method retrieves second
        coords = new Coordinates(4, 1);                // square from the board
        Square[] squares = game.getSquares();
        assertEquals(squares[1],  game.getSquareWithCoordinates(coords));
    }

    @Test
    public void testGetSquare2WithCoordinates() {            // check if the method retrieves third
        coords = new Coordinates(1, 4);               // square from the board
        Square[] squares = game.getSquares();
        assertEquals(squares[2], game.getSquareWithCoordinates(coords));
    }

    @Test
    public void testGetSquare3WithCoordinates() {            // check if the method retrieves forth
        coords = new Coordinates(4, 4);               // square from the board
        Square[] squares = game.getSquares();
        assertEquals(squares[3], game.getSquareWithCoordinates(coords));

    }

    @Test
    public void testShiftCoords() {                          // checks if all the coordinates are shifted
        Coordinates coords1 = new Coordinates(1, 1);   // to proper coordinates on proper square
        Coordinates coords2 = new Coordinates(4, 1);
        Coordinates coords3 = new Coordinates(1, 4);
        Coordinates coords4 = new Coordinates(4, 4);
        Square[] squares = game.getSquares();
        assertEquals(1,  game.shift(squares[0],  coords1).getX());    // check if coordinates match shifted coordinates on square 0
        assertEquals(1,  game.shift(squares[1],  coords2).getX());    // check if coordinates match shifted coordinates on square 1
        assertEquals(1,  game.shift(squares[2],  coords3).getX());    // check if coordinates match shifted coordinates on square 2
        assertEquals(1,  game.shift(squares[3],  coords4).getX());    // check if coordinates match shifted coordinates on square 3

    }

    @Test
    public void testFreeBoardCoordinates() {     // tests if all the free coordinates match the x value
        Coordinates[] boardCoordinates = game.getFreeCoordinates().toArray(new Coordinates[35]);

        for (int i = 0; i < 12; i++) {

            if (i < 3) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(j, boardCoordinates[j + 3 * i].getX());
                }
            } else if (i >= 3 && i < 5) {
                for (int j = 3; j < 6; j++) {
                    assertEquals(j, boardCoordinates[j + 3 * i].getX());
                }
            } else if (i >= 5 && i < 8) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(j, boardCoordinates[(j + 3 * i) + 3].getX());
                }
            } else if (i >= 8 && i < 11) {
                for (int j = 3; j < 6; j++) {
                    assertEquals(j, boardCoordinates[j + 3 * i].getX());
                }
            }

        }
    }


}
