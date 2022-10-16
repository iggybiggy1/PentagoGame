package Network;

import Game.Game;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class responsible for handling the connection between the client and the server.
 */
public class Client extends Game implements GameClient, Runnable {
    private Socket socket = null;
    public boolean quit = false;
    private static String message;
    private Thread thread;
    private Runnable pingThread;
    private ReentrantLock lock = new ReentrantLock();
    public String player1 = null;
    public String player2 = null;
    private Game game;
    public boolean pinged;

    /**
     * Establishes a connection with a server, with a given IP address and port.
     * @param address valid IP address of the server
     * @param port valid open port of the server
     * @ensures address != null
     * @ensures address instanceOf InetAddress
     * @ensures port > 0
     * ensures port < 65535
     * @return true if connection could be established, return false if any error appeared
     */
    @Override
    public boolean connection(InetAddress address, int port) {      // used to establish the connection with the server
        try {
            socket = new Socket(address, port);
            thread = new Thread(this);
            game = new Game();
            thread.start();
            return socket.isConnected();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Returns current socket used by the class.
     * @return this.socket that has the connection with the server
     */
    public Socket getSocket() {
        return this.socket;
    }

    /**
     * Returns the boolean of connection status. If there is a connection, will return true, otherwise false.
     * @return quit
     */
    public boolean returnConnectionStatus() {
        return quit;
    }

    /**
     * Terminates the connection with the server.
     */
    @Override
    public boolean close() {
        try {
            socket.close();
            quit = true;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Client sends an initial message Hello to the server with their username.
     * @param name username of the player
     * @requires name != null
     * @requires name instanceOf String
     * @return true if everything went correctly, return false if any error appeared
     */
    @Override
    public boolean sendHello(String name) {
        try {
            var pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            pw.println(String.format("HELLO~%s", name));
            pw.flush();
            return true;
        } catch (IOException e) {
            close();
            return false;
        }
    }

    /**
     * Client sends a move that contains two parameters for square coordinates, its number and rotation.
     * @param input1 parameter for x,y coordinates
     * @ensures input1 >= 0
     * @ensures input 1 < 36
     * @param input2 parameter for number of the square to rotate, and the value of rotation
     * @ensures input2 >= 0
     * @ensures input2 < 9
     * @return true if move was sent to the server, false if there was an error
     */
    @Override
    public boolean sendMove(int input1, int input2) {
        try {
            var pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            pw.println(String.format("MOVE~%d~%d", input1, input2));
            pw.flush();
            return true;
        } catch (IOException e) {
            close();
            return false;
        }
    }

    /**
     * Client sends the login message, in order to establish presence at the server.
     * @param username player's name
     * @requires username != null
     * @requires username instanceOf String
     * @return true if everything went correctly, return false if any error appeared
     */
    @Override
    public boolean sendLogin(String username) {                     // used to log in to the server
        try {
            var pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            pw.println(String.format("LOGIN~%s", username));
            pw.flush();
            return true;
        } catch (IOException e) {
            close();
            return false;
        }
    }

    /**
     * Client sends the request for a list of all possible users currently logged in.
     * @return true if message was sent, false if any error appeared
     */
    @Override
    public boolean sendList() {                           // used to return current user list from the server
        try {
            var pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            pw.println("LIST");
            pw.flush();
            return true;
        } catch (IOException e) {
            close();
            return false;
        }
    }

    /**
     * Client sends the quit message, if he wishes to exit the game.
     * @return true if message was sent, false if any error appeared
     */
    @Override
    public boolean sendQuit() {
        try {
            var pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            pw.println("QUIT");
            pw.flush();
            quit = true;
            return true;
        } catch (IOException e) {
            close();
            return false;
        }
    }

    /**
     * Clients sends the queue message, if he wishes to participate in a game.
     * @return true if message was sent, false if any error appeared
     */
    @Override
    public boolean sendQueue() {                              // used to queue the user at the server
        try {
            var pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            pw.println("QUEUE");
            pw.flush();
            return true;
        } catch (IOException e) {
            close();
            return false;
        }
    }

    /**
     * PING message sent by client to the server.
     * @return true if message was sent, false if any error appeared
     */
    @Override
    public boolean sendPing() {                                 // send ping request to the server
        try {
            var pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            pw.println("PING");
            pw.flush();
            return true;
        } catch (IOException e) {
            close();
            return false;
        }
    }

    /**
     * Send PONG message if PING message was received.
     * @return message PONG
     */
    @Override
    public synchronized boolean returnPong() {                                // return pong if ping request was received
        try {
            var pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            pw.println("PONG");
            pw.flush();
            return true;
        } catch (IOException e) {
            close();
            return false;
        }
    }

    /**
     * Returns the current instance of the game being used by the client.
     * @return this.game
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Returns the boolean value of variable pinged. Returns false if ping hasn't been received from the server, returns true if the ping has been received.
     * @return this.ping
     */
    public boolean getPinged() {
        return this.pinged;
    }

    /**
     * Returns the array of usernames retrieved from the NEWGAME message i.e. name of two players.
     * @return usernames of two players when new game is created
     */
    public String[] getUsernames() {
        String[] usernames = new String[2];
        usernames[0] = player1;
        usernames[1] = player2;
        return usernames;
    }

    /**
     * Returns current message received by the BufferedReader from the server.
     * @ensures this.message != null
     * @ensures this.message instanceof String
     * @return this.message from the server
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets message sent from the server, as the current message to be retrieved by other classes.
     * @param message sets message to the one retrieved from the server
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the name of two usernames retrieved from the message NEWGAME from the server.
     * @param firstPlayer first player in the message
     * @param secondPlayer second player in the message
     * @requires player1 != null
     * @requires player2 != null
     */
    public void setUsernames(String firstPlayer, String secondPlayer) {
        this.player1 = firstPlayer;
        this.player2 = secondPlayer;
    }


    /**
     * Thread method for checking and responding to different kinds of messages sent by the server.
     */
    @Override
    public void run() {
        try {
            BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));            // BufferedReader to read the input from the server
            while ((message = buff.readLine()) != null) {

                if (message.contains("HELLO") || message.contains("LOGIN") || message.contains("LIST")) {       // if input contains those parameters, just print them
                    System.out.println("Message from the server is " + message);
                }

                if (message.contains("NEWGAME")) {
                    String[] messages = message.split("~");
                    String firstPlayer = messages[1];
                    String secondPlayer = messages[2];
                    setUsernames(firstPlayer, secondPlayer);
                }

                if (message.contains("QUEUE")) {                                                       // if input contains QUEUE, print the QUEUE
                    System.out.println("Player was put in a queue " + message);
                }

                if (message.contains("QUIT")) {                                                   // if input contains QUIT, quit the game
                    System.out.println("Client quit!");
                    buff.close();
                    break;
                }

                if (message.contains("MOVE")) {                                                   // if input contains MOVE, perform the move on the local board
                    String[] currentMove = new String[3];
                    currentMove = message.split("~");
                    game.setMove(Integer.parseInt(currentMove[1]), Integer.parseInt(currentMove[2]));
                    System.out.println("Move performed " + currentMove[1] + " " + currentMove[2]);
                }

                synchronized (game) {                                                            // used for multithreading between this thread and thread of class Game
                    if (message.contains("PING")) {                                               // if input contains PING, notify the waiting thread in Game class
                        returnPong();
                        pinged = true;
                        game.notifyAll();
                    }
                }

                synchronized (game) {
                    if (message.contains("PONG")) {
                        setMessage(message);
                        pinged = true;
                        game.notifyAll();
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}