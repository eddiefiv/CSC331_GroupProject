/**
 * Subclass of Player, represents a Player that the user can control, unlike an NPC that is handled automatically
 */

public class ControllablePlayer extends Player {
    /**
     * ControllablePlayer constructor, taking in a playerName String to satisfy Player(String playerName)
     * @param playerName the player's name
     */
    public ControllablePlayer(String playerName) {
        super(playerName);
    }

    /**
     * ControllablePlayer constructor, taking in a playerName String and balance integer to satisfy Player(String playerName, int balance)
     * @param playerName the player's name
     * @param balance the player's starting balance
     */
    public ControllablePlayer(String playerName, int balance) {
        super(playerName, balance);
    }
}
