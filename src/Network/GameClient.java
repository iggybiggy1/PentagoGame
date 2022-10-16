package Network;

import java.net.InetAddress;

/**
 * Interface containing all necessary methods that are to be used by the client.
 */
public interface GameClient {

    /**
     * Starts up the connection.
     * @param address IP address
     * @param port port number
     * @requires address instanceOf InetAddress
     * @return true if connection established, false if not established
     */
    boolean connection(InetAddress address, int port);

    /**
     * Closes the connection between the client and server.
     * @return true if connection terminated, false if not terminated.
     */
    boolean close();

    /**
     * Sends a MOVE message to the server.
     * @param input1 first input
     * @param input2 second input
     * @requires input1 >= 0 && input1 < 36
     * @requires input2 >= 0 && input2 < 8
     * @return true if move was sent, false if it wasn't
     */
    boolean sendMove(int input1, int input2);

    /**
     * Sends a LOGIN message to the server.
     * @param username username of the player
     * @return true if the login was sent, false if it wasn't
     */
    boolean sendLogin(String username);

    /**
     * Sends the HELLO message to the server.
     * @param name username of the player
     * @return true if HELLO was sent, false if it wasn't
     */
    boolean sendHello(String name);

    /**
     * Sends a LIST message to the server.
     * @return true if LIST was sent, false if it wasn't
     */
    boolean sendList();

    /**
     * Sends a QUIT message to the server.
     * @return true if QUIT was sent, false if it wasn't
     */
    boolean sendQuit();

    /**
     * Sends a QUEUE message to the server.
     * @return true if QUEUE was sent, false if it wasn't
     */
    boolean sendQueue();

    /**
     * Sends a PING message to the server.
     * @return true if PING was sent, false if it wasn't
     */
    boolean sendPing();

    /**
     * Sends a PONG message to the server.
     * @return true if PONG was sent, false if it wasn't
     */
    boolean returnPong();
}
