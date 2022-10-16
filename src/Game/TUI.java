package Game;

/**
 * Class that prints the main menu and the help menu and the during the game.
 */
public class TUI {

    /**
     * Prints out the menu: when the system is started, or after every game.
     */
    public void printMenu() {

        System.out.println("Welcome to Pentago game made by Ignacy Kepka, please provide your input: \n");
        System.out.println("-b : bot player - creates a local game with the bot\n" +
                            "-r : real player - creates a local game with another player\n" +
                            "-h: prints the rules of the game\n" +
                            "-cr : connect to a server - connects the real client to a server on a given ip address and port\n" +
                            "-cb : connect to a server as an AI- connects the bot client to a server on a given ip address and port\n" +
                            "-q : quit - quits the game (potentially terminating connection with the server)\n");

    }

    /**
     * Prints out the help menu during the game when -h is provided as an input.
     */
    public void printHelpMenu() {
        System.out.println("You pressed -h, here is the quick manual how to use the game\n");
        System.out.println("You are to provide 2 different parameters in total");
        System.out.println("First you look at the generated board, and see if it is your turn");
        System.out.println("Board contains four squares in form:");
        System.out.println("[0-1]");
        System.out.println("[2-3]");
        System.out.println(" ");
        System.out.println("Syntax for game on a server looks like this: int coordinate (between 0-35) int rotation (between 0-7)");
        System.out.println("The game prints the board before and after the rotation was performed\n");
        System.out.println("Example move: 6 3, sets player's color on: coordinates x = 0, y = 1 and rotates square 1 clockwise");
        System.out.println("Example move: 10 4, sets player's color on: coordinates x = 4, y = 1 and rotates square 2 counterclockwise");
        System.out.println(" ");
        System.out.println("The first move below the board is the one you performed");
        System.out.println("The second move below the board is the move performed by the other client\n");
    }

}

