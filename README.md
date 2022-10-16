# Pentago Project 


## Description
Pentago is an abstract turn based strategy game with four 3×3 grids arranged into a larger 6×6 grid. 
The game is played by two players, whose goal is to put SPOTS of different color (either white or black) on the 6x6 grid.
During each player's turn, it is mandatory to rotate one of the four 3x3 squares, either clockwise or counterclockwise.
The game ends, when one of the players creates a winning position on the board. In order to win, a player must insert five spots of his color in a certain
alignment: either horizontal, vertical or diagonal. If the board is full and cannot take another spot, a tie is drawn. 

## Usage of the system
This system is an implementation of the aforementioned game and its consecutive rules, and allows the user to: play an offline game with an AI, play an offline game with another user,
play an online game as a real client against another online client, or play as an AI against another online client. Before being
able to play a game however, it is mandatory to provide a username, and additionally in case of an online game, a valid IP address and open port.


In order to start the system, the user must start the Tournament class, contained in the Pentago/src/Game package. After this
a menu is prompted, asking user to provide an input. If the input is invalid, the system detects that and handles the errors.
The input options are:
```
-b : bot player - creates a local game with the bot
-r : real player - creates a local game with another player
-h: prints the rules of the game
-cr : connect to a server - connects the real client to a server on a given ip address and port
-cb : connect to a server as an AI- connects the bot client to a server on a given ip address and port
-q : quit - quits the game (potentially terminating connection with the server)
```
If an offline game is started, and username is provided, it is randomly chose, which player will start. The starting player
always has the white spot, and the other has the black one. A real player is given a possibility to use three different in-game commands:
```
-q : terminates the game, and a connection with the server if there was one
-h : displays the in-game help menu, which contains a series of useful informations about how to 
play the game
-hint: displays three random coordinates that are still not occupied by any of the spots
```
Before the initial move, and after every consecutive move, the board is printed, showing its current state:
before and after the rotation on one of its squares was performed. To perform a move, a player must provide two parameters:
one between 0-35, and the other one between 0-7. The syntax looks like: 
```
Write -h to get help menu OR Write -q to quit OR -hint for all remaining free coordinates
Provide input:

<first input> <second input>

19 5

Board before performing the rotation
 
0 - - - | - - -
1 - - - | - - -
2 - - - | - - -
----------------
3 - B - | - - -
4 W - - | - - -
5 - - - | - - -
  0 1 2 | 3 4 5

Board after performing the rotation
 
0 - - - | - - -
1 - - - | - - -
2 - - - | - - -
----------------
3 - W - | - - -
4 - - B | - - -
5 - - - | - - -
  0 1 2 | 3 4 5
```
If the input is invalid, the system detects it, and asks the user to correct the input. Input can be incorrect when: input type is not an Integer,
input contains more or less than two parameters, or if the provided coordinates already contain a spot other than EMPTY. The system makes sure that whenever an invalid move is provided 
the move is not actually performed on the board, until a valid one is provided, and prints out this message whenever it occurs. 

```
Please provide another field that is empty!
```


In case of an online game, before the user can provide his username, and be put in the queue for the new game, he must provide 
valid IP address and port. The user menu looks like this:
```
Test server IP and Port are: 130.89.253.64 55555
Provide valid IP address and port of the server:
Or provide the first parameter as 999 to exit the connection
<IP_ADDRESS> <PORT>
```

The game is played until there is a winning position detected, or there is a tie. In case of a win, winning player's name is printed. 
After this, the user is prompted back to the main menu, where he can play another offline or online game. Whenever a user wants to quit, they just have to provide input "-q" in the main menu, which
will terminate the system. 

This concludes the section concerned about the usage of the system.

## How does the system work 

For a future maintainer, or a curious cookie, the system works as follows. Class tournament instantiates an object of class Game. 
Game contains all code for every game scenario mentioned in section usage. Whenever a new game is started, and parameters are provided the game may create an object of 
class Client (responsible for handling a connection between the server and our client). Method DetermineWhoStarts() in class Game is used 
to determine which player starts first and with white spots, randomly when playing an offline game. When playing an online game, the first player to start 
is the one that was first in the queue. Information about who starts in an online game is taken from the message received from the server (NEWGAME~player1~player2).
Class Client follows the practice server protocol, and allows the system to send, and receive messages with following flag:
```
HELLO - The initial message sent by the client once a connection has been established.
LOGIN - Sent by the client to claim a username on the server.
LIST - Sent by a client to request a list of clients who are currently logged into the server. 
QUEUE - Sent by the client to indicate that it wants to participate in a game.
MOVE - Sent by the client to indicate which row(s) or column(s) the player wants to push.
PING - Sent by client or server. The other party must immediately return a PONG.
PONG - Sent by client or server in response to PING.
QUIT - Sent by client or server to ask the other party to disconnect.
```
Whenever a move is to be performed, the input is read from the user, and a new object from class 
Move is created. Move contains four parameters: 

```java
Move move = new Move(Object Player, Object Coordinates, Integer squareNumber, Enum Rotation)
Where:
Object Player - is any object that implements the interface Player
Object Coordinates - is an object of class Coordinates that contains (x,y) values 
Integer squareNumber - is the number of the square from the board, between 0-3
Enum Rotation - is either: CLOCKWISE or COUNTERCLOCKWISE
```

Class Move contains getters and setters for all aforementioned objects/fields, which allows for easier manipulation of data 
between each component of the system. Once a move is created, it is performed by a method move() in class Board.
Game class controls the flow of the game, while class Client ensures that the messages are sent to the server or received from the server. A thread in class Client 
runs all the time, waiting for new message to appear. When that happens, protocol flag is checked and an appropriate method from class 
Client is called. Whenever the user wants to terminate a connection, a sendQuit() method from class Client is invoked 
which sends the message QUIT to the server, and then the method close() is called, which closes the BufferedReader, and the thread waiting 
for new messages from the server is closed.

Lastly, there are two different kinds of test included in this project, those are Unit Tests and Integration Tests. UnitTest were more focused on testing all the core functions of the system: generating a board, 
manipulating a board, and a square on that board. Creating new coordinates, creating new moves, copying both board and squares, as well as, check the functioning of object DummyBot and RealPlayer. Those showed me that the fundaments of the system
work as intended. Then there are integration tests, which mainly contain tests to check if there is a winning move on the board, or a tie. There is also 
one test included that checks the game sequence between player selecting random spots, and a bot. The test suite composed of those tests
may help someone, who wishes to learn more about the critical parts of the system.

##That's it, have fun!
