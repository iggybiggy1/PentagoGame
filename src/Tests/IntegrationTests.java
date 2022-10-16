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
import Network.Utilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTests {

    private Board board;
    private Game game;
    private Square square;
    private Coordinates coords;
    private Rotation rotation;
    private Player player1;
    private RealPlayer player2;
    private Client client;
    private Move move;

    @BeforeEach // Initialize objects for Unit Testing
    public void init() {

        board = new Board();    // creates new Board 6x6 compose of 4 squares
        square = new Square();  // creates new Square 3x3
        game = new Game();      // creates an object for the game
        square = new Square();
    }

    @Test
    public void checkIfTie() {                       // detects if there are no other freeCoordinates on the board
        // terminates the game and results in a tie
        Player playerOne = new DummyBot("Test");
        game.addPlayer(playerOne, Spot.BLACK);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                move = new Move(playerOne, new Coordinates(j, i), 1, Rotation.CLOCKWISE);
                game.move(move);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 3; j < 6; j++) {
                move = new Move(playerOne, new Coordinates(j, i), 2, Rotation.CLOCKWISE);
                game.move(move);
            }
        }
        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                move = new Move(playerOne, new Coordinates(j, i), 3, Rotation.CLOCKWISE);
                game.move(move);
            }
        }
        for (int i = 3; i < 6; i++) {
            for (int j = 3; j < 6; j++) {
                move = new Move(playerOne, new Coordinates(j, i), 0, Rotation.CLOCKWISE);
                game.move(move);
            }
        }

        assertTrue(game.detectIfTie());
    }

    @Test
    public void checkIfWinDiagonallyLeftRight() {            // check if there is a winning diagonal
        Player playerOne = new DummyBot("Test");         // from left to right color sequence
        game.addPlayer(playerOne, Spot.BLACK);
        Coordinates coords1 = new Coordinates(0, 0);
        Coordinates coords2 = new Coordinates(1, 1);
        Coordinates coords3 = new Coordinates(2, 2);
        Coordinates coords4 = new Coordinates(3, 3);
        Coordinates coords5 = new Coordinates(4, 4);

        Move move1 = new Move(playerOne, coords1, 2, Rotation.CLOCKWISE);
        Move move2 = new Move(playerOne, coords2, 2, Rotation.CLOCKWISE);
        Move move3 = new Move(playerOne, coords3, 2, Rotation.CLOCKWISE);
        Move move4 = new Move(playerOne, coords4, 2, Rotation.CLOCKWISE);
        Move move5 = new Move(playerOne, coords5, 2, Rotation.CLOCKWISE);

        game.move(move1);
        game.move(move2);
        game.move(move3);
        game.move(move4);
        game.move(move5);

        assertNotEquals(null,  game.detectIfWin(game));

    }

    @Test
    public void checkIfWinDiagonallyRightLeft() {            // check if there is a winning diagonal
        Player playerOne = new DummyBot("Test");        // from right to left color sequence
        game.addPlayer(playerOne, Spot.BLACK);
        Coordinates coords1 = new Coordinates(5, 0);
        Coordinates coords2 = new Coordinates(4, 1);
        Coordinates coords3 = new Coordinates(3, 2);
        Coordinates coords4 = new Coordinates(2, 3);
        Coordinates coords5 = new Coordinates(1, 4);

        Move move1 = new Move(playerOne, coords1, 3, Rotation.CLOCKWISE);
        Move move2 = new Move(playerOne, coords2, 3, Rotation.CLOCKWISE);
        Move move3 = new Move(playerOne, coords3, 3, Rotation.CLOCKWISE);
        Move move4 = new Move(playerOne, coords4, 3, Rotation.CLOCKWISE);
        Move move5 = new Move(playerOne, coords5, 3, Rotation.CLOCKWISE);

        game.move(move1);
        game.move(move2);
        game.move(move3);
        game.move(move4);
        game.move(move5);

        assertNotEquals(null, game.detectIfWin(game));

    }

    @Test
    public void checkIfWinVertically() {                 // check if there is a winning vertical
        Player playerOne = new DummyBot("Test");    // color sequence
        game.addPlayer(playerOne, Spot.BLACK);
        Coordinates coords1 = new Coordinates(0, 0);
        Coordinates coords2 = new Coordinates(0, 1);
        Coordinates coords3 = new Coordinates(0, 2);
        Coordinates coords4 = new Coordinates(0, 3);
        Coordinates coords5 = new Coordinates(0, 4);

        Move move1 = new Move(playerOne, coords1, 3, Rotation.CLOCKWISE);
        Move move2 = new Move(playerOne, coords2, 3, Rotation.CLOCKWISE);
        Move move3 = new Move(playerOne, coords3, 3, Rotation.CLOCKWISE);
        Move move4 = new Move(playerOne, coords4, 3, Rotation.CLOCKWISE);
        Move move5 = new Move(playerOne, coords5, 3, Rotation.CLOCKWISE);

        game.move(move1);
        game.move(move2);
        game.move(move3);
        game.move(move4);
        game.move(move5);

        assertNotEquals(null, game.detectIfWin(game));

    }

    @Test
    public void checkIfWinHorizontally() {                // check if there is a winning horizontal
        Player playerOne = new DummyBot("Test");    // color sequence
        game.addPlayer(playerOne, Spot.BLACK);
        Coordinates coords1 = new Coordinates(0, 0);
        Coordinates coords2 = new Coordinates(1, 0);
        Coordinates coords3 = new Coordinates(2, 0);
        Coordinates coords4 = new Coordinates(3, 0);
        Coordinates coords5 = new Coordinates(4, 0);

        Move move1 = new Move(playerOne, coords1, 3, Rotation.CLOCKWISE);
        Move move2 = new Move(playerOne, coords2, 3, Rotation.CLOCKWISE);
        Move move3 = new Move(playerOne, coords3, 3, Rotation.CLOCKWISE);
        Move move4 = new Move(playerOne, coords4, 3, Rotation.CLOCKWISE);
        Move move5 = new Move(playerOne, coords5, 3, Rotation.CLOCKWISE);

        game.move(move1);
        game.move(move2);
        game.move(move3);
        game.move(move4);
        game.move(move5);

        assertNotEquals(null, game.detectIfWin(game));
    }

    @Test
    public void sendAndReceiveMessages() {           // when no message was received from the server

        try {
            client = new Client();
            InetAddress address = InetAddress.getByName("130.89.253.64");
            client.connection(address, 55555);
            assertEquals(null, client.getMessage());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getPlayerHint() {                   // prints out three random free coordinates from current board
        player2 = new RealPlayer("Test");
        game.addPlayer(player2, Spot.BLACK);
        ArrayList<Coordinates> freeCoordinates = game.getFreeCoordinates();
        player2.getHints(freeCoordinates);
        game.printFreeCoordinates(game);
    }

    @Test
    public void offlineGame(){                          // a test that checks whether the system sees a winning alignment
        Utilities utilities = new Utilities();          // on the current board
        player2 = new RealPlayer("Test1");
        player1 = new DummyBot("Test2");
        game.addPlayer(player1, Spot.BLACK);
        game.addPlayer(player2, Spot.WHITE);
        Random rand = new Random();

        while(game.detectIfWin(game) == null){
            int randomMove = rand.nextInt(game.getFreeCoordinates().size());
            int randomRotation = rand.nextInt(8);
            game.setMove(randomMove, randomRotation);
            game.getMove().setPlayer(player2);
            game.move(game.getMove());
            game.switchPlayerTurn(player1, player2);
            Coordinates coordinates = (player1.chooseMove(game.getFreeCoordinates()));
            int botMove = coordinates.getX() + 6 * coordinates.getY();
            game.setMove(botMove, randomRotation);
            game.getMove().setPlayer(player1);
            game.move(game.getMove());
            System.out.println(game.printBoard());
        }
        Spot winningSpot = game.detectIfWin(game);
        System.out.println(winningSpot + " wins!");
    }

    @Test
    public void checkMoveServerSyntax(){                // check if the valid move is registered in the game
        Utilities utilities = new Utilities();          // using server syntax
        RealPlayer player1 = new RealPlayer("Test");
        int input1 = 5;
        int input2 = 6;
        game.addPlayer(player1, Spot.BLACK);
        game.setMove(input1, input2);
        assertTrue(game.checkIfEmpty(utilities.translateCoords(input1)));

    }

    @Test
    public void checkInvalidMoveServerSyntax(){             // checks that an invalid move is not registered as a move on the board
        RealPlayer player1 = new RealPlayer("Test");  // using server syntax
        game.addPlayer(player1, Spot.BLACK);
        game.setMove(10, 10);
        ArrayList<Coordinates> freeCoordinates = game.getFreeCoordinates();
        assertTrue(freeCoordinates.size() == 36);
    }

    @Test
    public void checkMoveLocalSyntax() {          // perform a move for the real player with the use of system's syntax
        player1 = new RealPlayer("Test");
        game.addPlayer(player1,  Spot.WHITE);
        coords = new Coordinates(1, 1);
        rotation = Rotation.CLOCKWISE;
        int squareNumber = 2;
        move = new Move(player1, coords, squareNumber, rotation);
        move.setPlayer(player1);
        game.move(move);
        assertTrue(player1.checkMove(board, coords));

    }

    @Test
    public void checkDummyBotMoveLocalSyntax() {            // perform the move for the bot with the use of system's syntax
        ArrayList<Coordinates> listOfCoordinates = game.getFreeCoordinates();
        player1 = new DummyBot("Test");
        game.addPlayer(player1,  Spot.BLACK);
        coords = player1.chooseMove(listOfCoordinates);
        rotation = Rotation.CLOCKWISE;
        int squareNumber = 2;
        move = new Move(player1, coords, squareNumber, rotation);
        move.setPlayer(player1);
        game.move(move);
        assertTrue(player1.checkMove(board, coords));

    }

}
