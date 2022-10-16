package Game;

import Game.Board.Board;
import Game.Board.Spot;
import Game.Move.Coordinates;
import Game.Move.Move;
import Game.Move.Rotation;
import Game.Player.DummyBot;
import Game.Player.Player;
import Game.Player.RealPlayer;
import Network.Client;
import Network.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Starts the actual game with two players.
 * Creates a new 6x6 board, filled with 4 3x3 squares
 * The board is initially filled with Spot.EMPTY
 * Contains methods that control the entire game flow of the system.
 */
public class Game extends Board {

    private Board board;    public static HashMap<Player, Spot> players = new HashMap<Player, Spot>();
    private static String startingName;
    private static String otherName;
    public static Game game;
    private TUI tui = new TUI();
    private Scanner scanner = new Scanner(System.in);
    private Move move;
    private String p1;
    private String p2;
    private static Player player1;
    private static Player player2;
    private Rotation rotation = null;
    private int squareNumber;
    private String[] messages = new String[4];
    private boolean smallerWhile = true;
    private boolean biggerWhile = true;
    private Random rand = new Random();
    private BufferedReader buff;
    private int randomSquare;
    private int randomRotation;
    private String rotationString;
    private Utilities utilities = new Utilities();
    private int x;
    private int y;
    public static Move currentMove;
    private static Client client = new Client();

    /**
     * Creates a new object of class Game, setting all the parameters for the board to start.
     */
    public Game() {
        this.board = new Board();
    }

    /**
     * Determines who will start the game with Spot.WHITE.
     * @return random 0 or 1
     */
    public boolean determineWhoStarts() {
        rand = new Random();
        int random = rand.nextInt(2);
        return random == 0;
    }

    /**
     * Method that randomly determines if player1 starts or player2 starts.
     * Appends both players to players hashmap that contains names of the players, and their colors
     * @param firstPlayer player that is to be matched with certain Spot
     * @param secondPlayer player that is to be matched with the other Spot as player1
     * @requires player1 != null && player1 instanceof Player
     * @requires player2 != null && player2 instanceof Player
     */
    public void setPlayerRandomly(Player firstPlayer, Player secondPlayer) {
        if (determineWhoStarts()) {
            players.put(firstPlayer, Spot.WHITE);
            players.put(secondPlayer, Spot.BLACK);
            startingName = firstPlayer.getName();
            otherName = secondPlayer.getName();
        } else {
            players.put(secondPlayer, Spot.WHITE);
            players.put(firstPlayer, Spot.BLACK);
            startingName = secondPlayer.getName();
            otherName = firstPlayer.getName();
        }
    }

    /**
     * Method that assigns colors to the given players, in the order the names are provided (used for client).
     * @param firstPlayer player that is to be matched with WHITE Spot
     * @param secondPlayer player that is to be matched with the BLACK Spot
     * @requires player1 != null && player1 instanceof Player
     * @requires player2 != null && player2 instanceof Player
     */
    public void setPlayer(Player firstPlayer, Player secondPlayer) {
        if (firstPlayer.getName().equals(client.getUsernames()[0])) {
            players.put(firstPlayer, Spot.WHITE);
            players.put(secondPlayer, Spot.BLACK);
            startingName = firstPlayer.getName();
            otherName = secondPlayer.getName();
        } else if (firstPlayer.getName().equals(client.getUsernames()[1])) {
            players.put(firstPlayer, Spot.WHITE);
            players.put(secondPlayer, Spot.BLACK);
            startingName = secondPlayer.getName();
            otherName = firstPlayer.getName();
        }
    }

    /**
     * Method that translates server syntax, into local game syntax using class Utilities.
     * @param firstInput value between 0 and 35. Associated with x,y coordinates
     * @param secondInput value between 0 and 7. Associated with square number and it's rotation
     */
    public void setMove(int firstInput, int secondInput) {

        if (firstInput == 999 && secondInput == 999) {
            currentMove = null;
            return;
        }
        if (firstInput < 0 || firstInput > 35) {
            System.out.println("Invalid input provided in method setMove class Game");
            currentMove = null;
        } else if (secondInput < 0 || secondInput > 8) {
            System.out.println("Invalid input provided in method setMove class Game");
            currentMove = null;
        } else if (startingName.equals(player1.getName())) {
            currentMove = new Move(player1, utilities.translateCoords(firstInput), utilities.translateSquare(secondInput), utilities.translateRotation(secondInput));
        } else {
            currentMove = new Move(player2, utilities.translateCoords(firstInput), utilities.translateSquare(secondInput), utilities.translateRotation(secondInput));
        }

    }

    /**
     * Method for returning the current move in the game (used by the client).
     * @return this.currentMove
     */
    public Move getMove() {
        return this.currentMove;
    }

    /**
     * Adds player to the hashmap where Player name, Spot color pairs are stored.
     * @param player player to be set in the hashmap
     * @param spot spot to be set in the hashmap
     * @requires player != null
     * @requires spot != null
     * @requires player instanceof RealPlayer || player instanceof DummyBot
     * @requires spot.equals(Spot.WHITE) || spot.equals(Spot.BLACK)
     */
    public void addPlayer(Player player, Spot spot) {
        startingName = player.getName();
        player1 = player;
        players.put(player, spot);
    }

    /**
     * Switches the turn of the player.
     * If player1 made a move, then player2 will get the turn and vice versa
     * @param firstPlayer player that is to be matched with certain Spot
     * @param secondPlayer player that is to be matched with the other Spot as player1
     * @requires player1 != null && player1 instanceof Player
     * @requires player2 != null && player2 instanceof Player
     */
    public void switchPlayerTurn(Player firstPlayer, Player secondPlayer) {
        if (startingName.equals(firstPlayer.getName())) {
            startingName = secondPlayer.getName();
            otherName = firstPlayer.getName();
        } else {
            startingName = firstPlayer.getName();
            otherName = secondPlayer.getName();
        }
    }

    /**
     * Entire code for the game between a real players and a DummyBot.
     * Code is very extensive, robust and poorly optimized.
     * Creates an offline game between the RealPlayer and a DummyBot
     */
    public void bot() {

        game = new Game();                                                  // create new game
        System.out.println("Playing with the dummyBot Garry\n");
        System.out.println("Provide your name: ");
        p1 = scanner.nextLine();                                            // asks for the name of the player
        player1 = new RealPlayer(p1);                                       // sets player1 to realPlayer
        player2 = new DummyBot("Garry");                              // sets dummyBot aka. player2 to Garry
        game.setPlayerRandomly(player1, player2);                                   // sets the colors for the players
        System.out.println("Write just -h for help, or -q to quit the system, or -hint for remaining possible coordinates\n");
        System.out.println("Starting the game ...");
        System.out.println(startingName + " Starts ! ");
        smallerWhile = true;

        while (smallerWhile) {                                          // while loop that terminates if: the game is won, or is a tie, or -q is written
            randomSquare = rand.nextInt(4);                      // dummyBot randomly chooses between four squares on the board to rotate
            try {

                if (game.detectIfTie()) {                               // detects if the game is a tie
                    System.out.println("\n" + "The game is a tie !!!" + "\n");
                    smallerWhile = false;
                    break;
                }

                if (game.detectIfWin(game) != null) {                                                                  // checks if there are any winning positions
                                                                                                                    // if there are, terminate the gam
                    if (game.detectIfWin(game).equals(players.get(player1))) {
                        System.out.println("Player: " + player1.getName() + " won!\n");
                    } else {
                        System.out.println("Player: " + player2.getName() + " won!\n");
                    }
                    smallerWhile = false;
                    break;
                }

                if (startingName.equals(player1.getName())) {             // writes additional information if it is the turn of player1
                    System.out.println(game.printBoard());
                    System.out.print("Write -h to get help menu OR ");
                    System.out.print("Write -q to quit OR -hint for all remaining free coordinates\n");
                    System.out.println("Provide input:\n");
                }

                randomRotation = rand.nextInt(2);                   // dummyBot randomly chooses if the rotation on the square is clockwise or counterclockwise

                if (randomRotation == 0) {
                    rotation = Rotation.CLOCKWISE;
                } else {
                    rotation = Rotation.COUNTERCLOCKWISE;
                }

                if (startingName.equals("Garry")) {                                     // dummyBot turn
                    ArrayList<Coordinates> coords = game.getFreeCoordinates();          // calls for list of all free coordinates on the board
                    Coordinates currentCoordinates = player2.chooseMove(coords);        // chooses a random free coordinate on the board
                    while (true) {                                                        // checks if the currently chosen spot is indeed empty
                        if (game.checkIfEmpty(currentCoordinates)) {
                            move = new Move(player2, currentCoordinates, randomSquare, rotation);       // creates the move for the dummyBot
                            move.setSquare(randomSquare);                                               // sets the squareNumber for method move in class Board
                            game.move(move);                                                            // performs the move
                            break;
                        } else {
                            coords = game.getFreeCoordinates();
                            currentCoordinates = player2.chooseMove(coords);
                        }
                    }
                    game.switchPlayerTurn(player1, player2);                                    // switches the turn of the players
                    System.out.println("Board after performing the rotation");
                    System.out.println(game.printBoard());

                    if (game.detectIfWin(game) != null) {                                            // doesn't print the name of player when it's his turn, because
                        break;                                                                     // there is a winning move
                    } else {
                        System.out.println("Turn of player " + startingName);
                    }

                } else if (startingName.equals(player1.getName())) {                            // realPlayer turn
                    buff = new BufferedReader(new InputStreamReader(System.in));                // BufferedReader to read the input from the console
                    String line = buff.readLine();
                    while (line != null) {

                        messages = line.split(" ");                                       // Splits the input into array of 4 different parameters

                        if (messages[0].equals("-q")) {                                         // if first parameter is -q, terminate the game
                            smallerWhile = false;
                            System.out.println("Exiting the game...");
                            break;
                        }

                        if (messages[0].equals("-h")) {                                         // if first parameter is -h, print additional help menu
                            tui.printHelpMenu();
                        }

                        if (messages[0].equals("-hint")) {                                      // if first parameter is -hint, print all free coordinates on the board
                            printFreeCoordinates(game);
                        }

                        if (messages.length != 2) {
                            System.out.print("\n" + "Provide correct number of parameters!" + "\n");
                            break;
                        }

                        if (Integer.parseInt(messages[0]) > 35 || Integer.parseInt(messages[0]) < 0) {
                            System.out.print("\n" + "Provide correct parameters between 0-35!" + "\n");
                            break;
                        }

                        if (Integer.parseInt(messages[1]) > 7 || Integer.parseInt(messages[1]) < 0) {
                            System.out.print("\n" + "Provide correct parameters between 0-8!" + "\n");
                            break;
                        }
                        x = Integer.parseInt(messages[0]);
                        y = Integer.parseInt(messages[1]);

                        move = new Move(player1, utilities.translateCoords(x), utilities.translateSquare(y), utilities.translateRotation(y));       // create the move for the realPlayer

                        while (!game.move(move)) {                                                        // check if the move is indeed valid
                            System.out.println("Please provide another field that is empty!");
                            line = buff.readLine();
                            messages = line.split(" ");

                            if (messages.length != 2) {
                                System.out.print("\n" + "Provide correct number of parameters!" + "\n");
                                break;
                            }

                            if (Integer.parseInt(messages[0]) > 35 || Integer.parseInt(messages[0]) < 0) {
                                System.out.print("\n" + "Provide correct parameters between 0-35!" + "\n");
                                break;
                            }

                            if (Integer.parseInt(messages[1]) > 7 || Integer.parseInt(messages[1]) < 0) {
                                System.out.print("\n" + "Provide correct parameters between 0-8!" + "\n");
                                break;
                            }
                            x = Integer.parseInt(messages[0]);
                            y = Integer.parseInt(messages[1]);

                            move = new Move(player1, utilities.translateCoords(x), utilities.translateSquare(y), utilities.translateRotation(y));
                            move.setSquare(squareNumber);                                                   // set the squareNumber for method move in class Board
                        }
                        game.switchPlayerTurn(player1, player2);                                            // switches the turn for the players
                        System.out.println("Board after performing the rotation");
                        System.out.println(game.printBoard());                                       // print the board after the rotation

                        if (game.detectIfWin(game) != null) {
                            break;
                        } else {
                            System.out.println("Turn of player " + startingName);
                        }
                        line = null;
                    }
                }
            } catch (IOException e) {
                System.out.println("IO exception in class game method play");
            } catch (NumberFormatException e) {
                switch (messages[0]) {
                    case "-q":                                                                   // if -q, terminate the big while loop
                        smallerWhile = false;
                        break;
                    case "-h":                                                                   // if -h, don't throw any exception
                        break;
                    case "-hint":                                                                // if -hint, don't throw any exception
                        break;
                    default:                                                                     // wrong input in general, repeat the move
                        System.out.println("Wrong input, try again!");
                        System.out.println("To perform a move, provide x & y coordinate, square to rotate (between 0-3) and rotation:");
                        break;
                }
            }
        }
    }

    /**
     * Entire code for the game between two real players.
     * Code is very extensive, robust and poorly optimized.
     * Creates an offline game two instances of RealPlayer
     */
    public void realPlayer() {

        game = new Game();                                              // starts a new game
        System.out.println("Playing with another player\n");
        System.out.println("Provide name of the first player: ");

        p1 = scanner.nextLine();                                        // takes the name of the first player
        player1 = new RealPlayer(p1);                                   // assigns the name to the player1

        System.out.println("Provide name of the second player: ");

        p2 = scanner.nextLine();                                        // takes the name of the second player
        player2 = new RealPlayer(p2);                                   // assigns the name to the player2
        game.setPlayerRandomly(player1, player2);                               // sets the colors for the players (randomly)

        System.out.println("Starting the game ...");
        System.out.println(startingName + " Starts ! ");
        smallerWhile = true;

        while (smallerWhile) {                                                  // while loop that terminates if: the game is won, or is a tie, or -q is written
            buff = new BufferedReader(new InputStreamReader(System.in));        // BufferedReader for reading the values from the console
            try {
                System.out.println(game.printBoard());
                String line;

                if (game.detectIfWin(game) != null) {                                          // checks if there are any winning positions and if there are, terminate the gam
                    if (game.detectIfWin(game).equals(players.get(player1))) {
                        System.out.println("Player: " + player1.getName() + " won!\n");
                    } else {
                        System.out.println("Player: " + player2.getName() + " won!\n");
                    }
                    smallerWhile = false;
                    break;
                }

                if (game.detectIfTie()) {                           // performs a check if there are no more free coordinates on the board
                    System.out.println("\n" + "The game is a tie !!!" + "\n");
                    smallerWhile = false;
                    break;
                }

                System.out.print("Write -h to get help menu OR ");
                System.out.print("Write -q to quit OR -hint for all remaining free coordinates\n");
                System.out.println("Provide the input in form: int int \n");
                while ((line = buff.readLine()) != null) {

                    messages = line.split(" ");                     // splits the message into four different parts
                                                                          // x value, y value, square number, rotation

                    if (messages[0].equals("-q")) {                       // terminate the game if -q is written
                        smallerWhile = false;
                        System.out.println("Exiting the game...");
                        break;
                    }

                    if (messages[0].equals("-h")) {                     // print out help menu if -h is written
                        tui.printHelpMenu();
                    }

                    if (messages[0].equals("-hint")) {                  // print out all remaining free coordinates if -hint is written
                        printFreeCoordinates(game);
                    }

                    if (messages.length != 2) {
                        System.out.print("\n" + "Provide correct number of parameters!" + "\n");
                        break;
                    }

                    if (Integer.parseInt(messages[0]) > 35 || Integer.parseInt(messages[0]) < 0) {
                        System.out.print("\n" + "Provide correct parameters between 0-35!" + "\n");
                        break;
                    }

                    if (Integer.parseInt(messages[1]) > 7 || Integer.parseInt(messages[1]) < 0) {
                        System.out.print("\n" + "Provide correct parameters between 0-8!" + "\n");
                        break;
                    }

                    x = Integer.parseInt(messages[0]);
                    y = Integer.parseInt(messages[1]);

                    if (startingName.equals(player1.getName())) {                                                             // checks if the startingName (white color) belongs to player 1
                        move = new Move(player1, utilities.translateCoords(x), utilities.translateSquare(y), utilities.translateRotation(y));    // creates a new move to perform on the board
                        while (!game.move(move)) {                                                                          // checks whether the move can be performed on the board
                            System.out.println("Please provide another field that is empty!");
                            line = buff.readLine();
                            messages = line.split(" ");

                            if (messages.length != 2) {
                                System.out.print("\n" + "Provide correct number of parameters!" + "\n");
                                break;
                            }

                            if (Integer.parseInt(messages[0]) > 35 || Integer.parseInt(messages[0]) < 0) {
                                System.out.print("\n" + "Provide correct parameters between 0-35!" + "\n");
                                break;
                            }

                            if (Integer.parseInt(messages[1]) > 7 || Integer.parseInt(messages[1]) < 0) {
                                System.out.print("\n" + "Provide correct parameters between 0-8!" + "\n");
                                break;
                            }
                            x = Integer.parseInt(messages[0]);
                            y = Integer.parseInt(messages[1]);

                            move = new Move(player1, utilities.translateCoords(x), utilities.translateSquare(y), utilities.translateRotation(y));
                                                                                                            // recreates the move with coordinates that lead to an empty slot
                            move.setSquare(squareNumber);                                                   // sets the square number for method move in class Board
                        }
                        game.switchPlayerTurn(player1, player2);                                            // switches the turn of the players after the move has
                        System.out.println("Board after performing the rotation");                          // been performed
                        System.out.println(game.printBoard());

                        if (game.detectIfWin(game) != null) {                                               // doesn't print the name of player when it's his turn, because
                            break;                                                                          // there is a winning move
                        } else {
                            System.out.println("Turn of player " + startingName);
                        }
                        System.out.print("Write -h to get help menu OR");
                        System.out.print("Write -q to quit and -hint for all remaining free coordinates\n");
                        System.out.println("Provide input:\n");

                    } else if (startingName.equals(player2.getName())) {                                    // checks if starting name (white color)
                        move = new Move(player2, utilities.translateCoords(x), utilities.translateSquare(y), utilities.translateRotation(y));
                        while (!game.move(move)) {                                                            // check if the move is valid and leads to empty spot on the board
                            System.out.println("Please provide another field that is empty!");
                            line = buff.readLine();
                            messages = line.split(" ");                                                // splits the input into four different parameters

                            if (messages.length != 2) {
                                System.out.print("\n" + "Provide correct number of parameters!" + "\n");
                                break;
                            }

                            if (Integer.parseInt(messages[0]) > 35 || Integer.parseInt(messages[0]) < 0) {
                                System.out.print("\n" + "Provide correct parameters between 0-35!" + "\n");
                                break;
                            }

                            if (Integer.parseInt(messages[1]) > 7 || Integer.parseInt(messages[1]) < 0) {
                                System.out.print("\n" + "Provide correct parameters between 0-8!" + "\n");
                                break;
                            }

                            x = Integer.parseInt(messages[0]);
                            y = Integer.parseInt(messages[1]);

                            move = new Move(player2, utilities.translateCoords(x), utilities.translateSquare(y), utilities.translateRotation(y));
                            move.setSquare(squareNumber);
                        }
                        game.switchPlayerTurn(player1, player2);                                        // switches the turn of the players
                        System.out.println("Board after performing the rotation");
                        System.out.println(game.printBoard());

                        if (game.detectIfWin(game) != null) {                                            // doesn't print the name of player when it's his turn, because
                            break;                                                                       // there is a winning move
                        } else {
                            System.out.println("Turn of player " + startingName);
                        }
                        System.out.print("Write -h to get help menu OR");
                        System.out.print(" Write -q to quit\n");
                        System.out.println("Provide input:\n");
                    }
                }
            } catch (IOException e) {
                System.out.println("IO exception in class game method play");
            } catch (NumberFormatException e) {
                switch (messages[0]) {
                    case "-q":                                                                  // if input is -q, terminate the program and exit the while loop
                        smallerWhile = false;
                        break;
                    case "-h":                                                                   // if input is -h, don't throw any exception
                        break;
                    case "-hint":                                                                // if input is -hint, don't throw any exception
                        break;
                    default:                                                                     // if input generally incorrect, repeat the input
                        System.out.println("Wrong input, try again!!\n");
                        System.out.println("To perform a move, provide x & y coordinate, square to rotate (between 0-3) and rotation:");
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Please provide all of the necessary parameters: x & coordinate, and rotation");
            }
        }
    }

    /**
     * Method that initializes the connection with the server.
     * Checks if the input contains proper syntax, and if values are valid
     * Finishes only if there is a connection established between the client and a server
     * Input is to be: IPaddress port
     * If input is only 999, the connection sequence is terminated
     * @return true if client setup has finished successfully, false if there were issues on the way
     */
    public boolean clientSetup() {
        boolean state = false;
        smallerWhile = true;
        while (biggerWhile) {
            while (smallerWhile) {
                System.out.println("Test server IP and Port are: 130.89.253.64 55555");
                System.out.println("Provide valid IP address and port of the server:");
                System.out.println("Or provide the first parameter as 999 to exit the connection");
                String scannerInput = scanner.nextLine();
                String[] line = scannerInput.split(" ");

                if (line[0].equals("999")) {
                    System.out.println("Aborting connection...");
                    smallerWhile = false;
                    biggerWhile = false;
                    return false;
                } else if (line.length > 2) {
                    System.out.println("Provide only 2 parameters: ip address and port\n");
                    break;
                } else if (line.length < 2) {
                    System.out.println("Provide only 2 parameters: ip address and port\n");
                    break;
                } else if (!line[1].matches("[0-9]+")) {
                    System.out.println("Port can only be an integer!");
                    break;
                } else if (Integer.parseInt(line[1]) < 0 || Integer.parseInt(line[1]) > 65535) {
                    System.out.println("Port value can be between 0 - 65535!");
                    break;
                }

                try {
                    InetAddress address = InetAddress.getByName(line[0]);
                    int port = Integer.parseInt(line[1]);
                    if (!client.connection(address, port)) {            // enter if the connecting process doesn't result in a successful connection
                        System.out.println("Cannot connect to the server, please provide correct input");
                    } else {
                        biggerWhile = false;
                        state = true;
                    }
                    break;
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    smallerWhile = false;
                }

                if (client.returnConnectionStatus()) {                    // check if the connection status node is true, otherwise enter the if
                    System.out.println("Session wasn't initialized");
                    break;
                }
            }
        }
        return state;
    }

    public Game getGame() {
        return this.game;
    }

    /**
     * Entire code for the game between a RealPlayer and another client.
     * Code is very extensive, robust and poorly optimized.
     * Creates an offline game two instances of RealPlayer
     * @throws IOException in case the BufferedReader within is suddenly closed.
     */
    public void realClientPlayer() throws IOException {                                                     // Test server:  130.89.253.64 55555.
        client = new Client();                                                                              // object that initializes the client
        boolean clientSetup = false;
        if (clientSetup() == false) {                                                                         // method for connection initialization
            return;
        }
        System.out.println("Please provide a username to use while connecting to the server");
        Scanner scanner1 = new Scanner(System.in);                                                           // system ask the user for a username
        String name = scanner1.nextLine();
        client.sendHello(name);                                                                             // system sends
        client.sendLogin(name);
        synchronized (client.getGame()) {
            while (!client.pinged) {
                try {
                    System.out.println("Suspending the thread, until the ping is received");
                    Thread.sleep(2000);
                    client.sendPing();
                    client.getGame().wait();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted exception");
                }
            }
            System.out.println("Ping received, thread released");
        }
        System.out.println("Waiting for new game");
        client.sendQueue();                                                                                 // places the user in the game queue
        int counter = 0;
        while (client.getUsernames()[0] == null) {                                                            // checks if the server sent a newgame request containing name of two players
            try {
                Thread.sleep(3000);                                                                    // performs a check every two seconds to see if new game has started
                counter++;                                                                                  // after 20 tries terminates the game
                if (counter == 20) {
                    client.sendQuit();
                    biggerWhile = true;
                    return;
                } else if (counter == 5 || counter == 10 || counter == 15) {
                    System.out.println("No game has still been initialized\n");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (client.getUsernames()[0] != null && client.getUsernames()[1] != null) {                       // if the returned names are not empty, create two players with that names

            if (client.getUsernames()[0].equals(name)) {                                                  // checks who is the first person in the returned NEWGAME message
                player1 = new RealPlayer(name);                                                         // if I am, then I start the game
                player2 = new RealPlayer(client.getUsernames()[1]);
            } else {
                player2 = new RealPlayer(client.getUsernames()[0]);                                     // if I am not then the other client starts the game
                player1 = new RealPlayer(name);
            }
        }
        client.getGame().setPlayer(player1, player2);                                                               // used to set the colors for the players
        System.out.println("Player " + startingName + " starts!!");
        System.out.println(client.getGame().printBoard());                                          // prints out the current representation of the board
        while (!client.returnConnectionStatus()) {                                                          // will be ongoing until the game is not closed

            if (client.getGame().detectIfWin(client.getGame()) != null) {                 // checks if there are any winning positions and if there are, terminate the game
                if (client.getGame().detectIfWin(client.getGame()).equals(players.get(player1))) {
                    System.out.println("Player: " + player1.getName() + " won!\n");
                } else {
                    System.out.println("Player: " + player2.getName() + " won!\n");
                }
                client.sendQuit();
                break;
            }

            if (client.getGame().detectIfTie()) {                                                                        // performs a check if there are no more free coordinates on the board
                System.out.println("\n" + "The game is a tie !!!" + "\n");
                client.sendQuit();
                break;
            }

            if (startingName.equals(player1.getName())) {                                               // if player1 has the startingName

                System.out.println("Turn of player " + player1.getName() + " (1)" + "\n");
                System.out.print("Write -h to get help menu OR ");
                System.out.print("Write -q to quit OR -hint for all remaining free coordinates\n");
                System.out.println("Provide the input in form: int int \n");

                var buffer = new BufferedReader(new InputStreamReader(System.in));                    // buffered reader for reading the user input
                String line;
                smallerWhile = true;
                while (smallerWhile) {

                    while ((line = buffer.readLine()) != null) {

                        String[] currentInput = line.split(" ");

                        if (currentInput[0].equals("-q")) {                       // terminate the game if -q is written
                            System.out.println("Exiting the game...");
                            smallerWhile = true;                                  // exit all loops to main menu
                            biggerWhile = true;
                            client.sendQuit();
                            return;
                        }

                        if (currentInput[0].equals("-h")) {                     // print out help menu if -h is written
                            tui.printHelpMenu();
                            System.out.println("Please provide the move: " + player1.getName() + "\n");
                            break;
                        }

                        if (currentInput[0].equals("-hint")) {                  // print out all remaining free coordinates if -hint is written
                            printFreeCoordinates(client.getGame());
                            System.out.println("Please provide the move: " + player1.getName() + "\n");
                            break;
                        }


                        if (currentInput.length != 2) {                         // checks if the input contains exactly 2 parameters
                            System.out.print("\n" + "Provide correct number of parameters!" + "\n");
                            break;
                        }
                        try {
                            if (Integer.parseInt(currentInput[0]) > 35 || Integer.parseInt(currentInput[0]) < 0) {      // checks if the first input is between 0-35
                                System.out.print("\n" + "Provide correct parameters between 0-35!" + "\n");
                            }

                            if (Integer.parseInt(currentInput[1]) > 7 || Integer.parseInt(currentInput[1]) < 0) {       // checks if the second input is between 0-7
                                System.out.print("\n" + "Provide correct parameters between 0-8!" + "\n");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please provide correct format for the parameters, only integers are allowed");
                            break;
                        }


                        int firstInput = Integer.parseInt(currentInput[0]);
                        int secondInput = Integer.parseInt(currentInput[1]);
                        client.getGame().setMove(firstInput, secondInput);                                          // game sets the move on the local machine

                        while (!client.getGame().move(getMove())) {                               // check if the provided input is indeed correct and the spot there is EMPTY
                            System.out.println("Field taken, choose another one");
                            line = buffer.readLine();
                            currentInput = line.split(" ");

                            if (currentInput[0] instanceof String || currentInput[1] instanceof String) {
                                System.out.println("Provide the input in form of integers");
                                return;
                            }

                            if (currentInput.length != 2) {                                                             // checks if the input contains exactly 2 parameters
                                System.out.print("\n" + "Provide correct number of parameters!" + "\n");
                                break;
                            }

                            try {
                                if (Integer.parseInt(currentInput[0]) > 35 || Integer.parseInt(currentInput[0]) < 0) {      // checks if the first input is between 0-35
                                    System.out.print("\n" + "Provide correct parameters between 0-35!" + "\n");
                                }

                                if (Integer.parseInt(currentInput[1]) > 7 || Integer.parseInt(currentInput[1]) < 0) {       // checks if the second input is between 0-7
                                    System.out.print("\n" + "Provide correct parameters between 0-8!" + "\n");
                                }
                            } catch (NumberFormatException e) {
                                break;
                            }

                            if (currentInput[0].equals("-q")) {                       // terminate the game if -q is written
                                System.out.println("Exiting the game...");
                                smallerWhile = true;
                                biggerWhile = true;
                                client.sendQuit();
                                return;
                            }

                            if (currentInput[0].equals("-h")) {                     // print out help menu if -h is written
                                tui.printHelpMenu();
                                System.out.println("Please provide the move: " + player1.getName() + "\n");
                                return;
                            }

                            if (currentInput[0].equals("-hint")) {                  // print out all remaining free coordinates if -hint is written
                                printFreeCoordinates(client.getGame());
                                System.out.println("Please provide the move: " + player1.getName() + "\n");
                            }

                            try {
                                firstInput = Integer.parseInt(currentInput[0]);
                                secondInput = Integer.parseInt(currentInput[1]);
                                client.getGame().setMove(firstInput, secondInput);              // if the input was incorrect, correct it and check again if the new move is correct
                            } catch (NumberFormatException e) {
                                System.out.println("Provide only integers");
                            }
                        }

                        client.sendMove(firstInput, secondInput);                                       // client sends the move to the server
                        client.getGame().getMove().setPlayer(player1);                                              // game sets the player on the local board (methods in client class may need it)
                        currentMove.setCoordinates(utilities.translateCoords(firstInput));              // client sets the coordinates on the local machine
                        currentMove.setSquare(utilities.translateSquare(secondInput));                  // client sets the square on which the rotation must be performed
                        currentMove.setRotation(utilities.translateRotation(secondInput));              // client sets the rotation on the local board
                        client.getGame().switchPlayerTurn(player1, player2);                                        // the turn is switched for other player
                        client.getGame().setMove(999, 999);                                                         // method that ensures that the current move is null
                        System.out.println("Printing the board after rotation");
                        System.out.println(client.getGame().printBoard());
                        smallerWhile = false;                                                            // boolean to exit the smallerWhile loop
                        break;                                                                          // exit buffered reader loop
                    }
                }
            } else {
                System.out.println("Turn of player " + player2.getName() + " (2)" + "\n");              // if player2 has the startingName
                counter = 0;
                while (client.getGame().getMove() == null) {                                          // checks if the move has been performed by the other player
                    try {
                        Thread.sleep(2000);
                        counter++;                                                                          // conditions that checks if the game has started
                        if (counter == 15) {                                                      // if no move is performed in 30 seconds, the game is terminated
                            System.out.println("You have waited 30 seconds, the client probably timed out\n!");
                            System.out.println("Exiting the game\n");
                            client.sendQuit();
                            biggerWhile = true;
                            return;
                        } else if (counter == 5 || counter == 10) {
                            System.out.println("Other client might have timed out\n");
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted exception in method realClientPlayer");
                        client.sendQuit();
                    }
                }
                client.getGame().move(getMove());                                               // retrieves the move performed by the other client and performs it on the local board
                client.getGame().switchPlayerTurn(player1, player2);                            // switches the turn of the one player to another
                client.getGame().setMove(999, 999);                                             // resets the move
                System.out.println("Printing the board after rotation");                      // prints the board after the rotation
                System.out.println(client.getGame().printBoard());
            }
        }
        System.out.println("Connection terminated, see ya later!");                     // if -q is written as an input, the game is terminated
        biggerWhile = true;
    }

    /**
     * Entire code for the game between a DummyBot client and another client.
     * Code is very extensive, robust and poorly optimized.
     * Creates an offline game two instances of RealPlayer
     */
    public void botClientPlayer() {
        game = new Game();                                                                  // initializes a new game object
        client = new Client();                                                              // initializes a new client object
        if (clientSetup() == false) {                                                         // method for connection initialization
            return;
        }
        System.out.println("Please provide a username to use while connecting to the server");
        Scanner scanner1 = new Scanner(System.in);                                                           // system ask the user for a username
        String name = scanner1.nextLine();
        client.sendHello(name);                                                                             // system sends
        client.sendLogin(name);
        synchronized (client.getGame()) {                                                 // used for multithreading between this thread and thread of class Client
            while (!client.pinged) {                                                       // if the ping hasn't been received
                try {
                    System.out.println("Suspending the thread, until the ping is received");
                    Thread.sleep(2000);
                    client.sendPing();
                    client.getGame().wait();                                            // put the system in wait until ping is received
                } catch (InterruptedException e) {
                    System.out.println("Interrupted exception");
                }
            }
            System.out.println("Ping received, thread released");
        }
        System.out.println("Waiting for new game");
        client.sendQueue();                                                           // places the user in the game queue
        int counter = 0;
        while (client.getUsernames()[0] == null) {                                      // checks if the server sent a newgame request containing name of two players
            try {
                Thread.sleep(3000);
                counter++;                                                 // every two seconds performs a check to see if the game has started, after 20 tries
                if (counter == 20) {                                         // terminates the game
                    client.sendQuit();
                    biggerWhile = true;
                    return;
                } else if (counter == 5 || counter == 10 || counter == 15) {
                    System.out.println("No game has still been initialized\n");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (client.getUsernames()[0] != null && client.getUsernames()[1] != null) {             // if the returned names are not empty, create two players with that names

            if (client.getUsernames()[0].equals(name)) {                                       // checks who are the first person in the returned NEWGAME message
                player1 = new DummyBot(name);                                                // if I am, then I start the game
                player2 = new DummyBot(client.getUsernames()[1]);
            } else {
                player2 = new DummyBot(client.getUsernames()[0]);                                     // if I am not then the other client starts the game
                player1 = new DummyBot(name);
            }
        }
        client.getGame().setPlayer(player1, player2);                                            // sets the players and their colors
        System.out.println("Player " + startingName + " starts!!");
        System.out.println(client.getGame().printBoard());
        while (!client.returnConnectionStatus()) {

            if (client.getGame().detectIfWin(client.getGame()) != null) {      // checks if there are any winning positions and if there are, terminate the gam
                if (client.getGame().detectIfWin(client.getGame()).equals(players.get(player1))) {
                    System.out.println("Player: " + player1.getName() + " won!\n");
                } else {
                    System.out.println("Player: " + player2.getName() + " won!\n");
                }
                client.sendQuit();

                break;
            }

            if (client.getGame().detectIfTie()) {                             // performs a check if there are no more free coordinates on the board
                System.out.println("\n" + "The game is a tie !!!" + "\n");
                client.sendQuit();
                break;
            }

            if (startingName.equals(player1.getName())) {                                                         // if the turn is of the local bot player
                System.out.println("Turn of player " + player1.getName() + " (1)" + "\n");
                Random random = new Random();
                ArrayList<Coordinates> freeCoordinates = client.getGame().getFreeCoordinates();          // get all remaining free coordinates from the current board
                Coordinates currentCoordinates = player1.chooseMove(freeCoordinates);                    // choose a coordinate
                while (true) {
                    if (client.getGame().checkIfEmpty(currentCoordinates)) {                                  // check if the chosen coordinate is indeed empty
                        int coordinateValue = currentCoordinates.getX() + 6 * currentCoordinates.getY();       // get the board index
                        int squareRotationValue = random.nextInt(8);                                      // determine randomly which square to rotate and in which way

                        client.sendMove(coordinateValue, squareRotationValue);                                // client sends the move to the server
                        client.getGame().setMove(coordinateValue, squareRotationValue);                                   // game sets the move on the local machine
                        client.getGame().getMove().setPlayer(player1);                                                    // game sets the player on the local board (methods in client class may need it)
                        currentMove.setCoordinates(utilities.translateCoords(coordinateValue));               // client sets the coordinates on the local machine
                        currentMove.setSquare(utilities.translateSquare(squareRotationValue));                // client sets the square on which the rotation must be performed
                        currentMove.setRotation(utilities.translateRotation(squareRotationValue));            // client sets the rotation on the local board
                        client.getGame().move(getMove());                                                     // client performs the move on the board
                        client.getGame().switchPlayerTurn(player1, player2);                                  // the turn is switched for other player
                        client.getGame().setMove(999, 999);                                                   // method that ensures that the current move is null
                        System.out.println("Board after the rotation\n");
                        System.out.println(client.getGame().printBoard());
                        break;                                                                                // exit the while loop
                    } else {
                        freeCoordinates = client.getGame().getFreeCoordinates();                              // if the chosen coordinate is not empty, choose another one and perform
                        currentCoordinates = player1.chooseMove(freeCoordinates);                             // the check again
                    }
                }
            } else {                                                                                          // if the turn is of the other client player
                System.out.println("Turn of player " + player2.getName() + " (2)" + "\n");
                counter = 0;
                while (client.getGame().getMove() == null) {                                                  // checks if the move has been performed by the other player
                    try {
                        Thread.sleep(2000);
                        counter++;                                                                          // conditions that checks if the game has started
                        if (counter == 15) {                                                      // if no move is performed in 30 seconds, the game is terminated
                            System.out.println("You have waited 30 seconds, the client probably timed out!\n");
                            System.out.println("Exiting the game\n");
                            client.sendQuit();
                            biggerWhile = true;
                            return;
                        } else if (counter == 5 || counter == 10) {
                            System.out.println("Other client might have timed out\n");
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted exception in method realClientPlayer");
                        client.sendQuit();
                    }
                }
                client.getGame().move(getMove());                                       // get the move performed by the client and set the move on the local board
                client.getGame().switchPlayerTurn(player1, player2);                    // switch the turn of one player to another
                client.getGame().setMove(999, 999);                                     // reset the move
                System.out.println("Board after the rotation\n");
                System.out.println(client.getGame().printBoard());
            }
        }
        biggerWhile = true;
        System.out.println("Connection terminated, see ya later!");
    }

    /**
     * Switch cases that is used in the main menu, and allows the user to navigate through different game scenarios.
     */
    public void play() {

        while (biggerWhile) {

            tui.printMenu();                    // prints out the start menu for the game
            System.out.println("Please provide a parameter to start a game:");
            String choice = scanner.nextLine();

            switch (choice.toLowerCase()) {
                case "-b":                       // game sequence for dummyBot
                    bot();
                    break;
                case "-r":                      // game sequence for two real players
                    realPlayer();
                    break;
                case "-cr":                      // game sequence for a real player connecting to a server
                    try {
                        realClientPlayer();
                    } catch (IOException e) {
                        System.out.println("IO Exception in method realClientPlayer class Game");
                    }
                    break;
                case "-cb":                     // game sequence for an AI connecting to a server
                    botClientPlayer();
                    break;
                case "-h":                      // prints out the help menu, useful when oblivious of the move
                    tui.printHelpMenu();
                    break;
                case "-q":                      // gracefully exits the system
                    System.out.println("Exiting the system");
                    biggerWhile = false;
                    System.exit(0);
                    break;
                default:                        // if none of the parameters are correct
                    System.out.println("Invalid parameter, provide a valid one!\n");
                    break;
            }
        }
    }

    /**
     * Returns the current state of the game in textual representation from the superclass Board.
     * @ensures message != null
     * @return message
     */
    public String printBoard() {
        return super.toString();
    }

    /**
     * Prints out three random free coordinates on the current board, in a user-friendly way.
     * @param currentGame current game
     */
    public void printFreeCoordinates(Game currentGame) {
        ArrayList<Integer> randomThree = new ArrayList<>();
        Random randomize = new Random();
        int randomInteger;
        System.out.println("Those are three free coordinates on current board: ");

        for (Coordinates c : currentGame.getFreeCoordinates()) {
            int currentValid = c.getX() + 6 * c.getY();
            randomThree.add(currentValid);
        }

        for (int i = 0; i < 3; i++) {
            randomInteger = randomize.nextInt(randomThree.size());
            System.out.println(randomThree.get(randomInteger));
        }
    }

}
