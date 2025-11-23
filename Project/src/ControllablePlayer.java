/**
* @author James Frink, Eddie Falco
* CSC 331 Section 001 - 002 - 003
* Date: 11/23/2025
* Purpose: Create a builder for a controllable Player object
*/
public class ControllablePlayer extends Player {
    /**
    * ControllablePlayer Constructor
    * @param playerName player's name (String)
    */
    public ControllablePlayer(String playerName) {
        super(playerName);
    }
    /**
    * ControllablePlayer Constructor
    * @param playerName player's name (String)
    * @param balance starting money for the player (int)
    */
    public ControllablePlayer(String playerName, int balance) {
        super(playerName, balance);
    }
}
